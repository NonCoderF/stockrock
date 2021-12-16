package com.stockrock.analysis.service;

import com.stockrock.analysis.constants.MessageStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class TerminalService extends ServiceInterface {

    @Override
//    @Async(CONCURRENT_THREAD)
    public void executeTerminalCommand(String command) {
        String s = null;

        try {

            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                getObserver().observe("", "OK", s, MessageStatus.OKAY);
            }

            while ((s = stdError.readLine()) != null) {
                getObserver().observe("", "ERROR", s, MessageStatus.OKAY);
            }

            stdInput.close();
            stdError.close();
        }
        catch (IOException e) {
            getObserver().observe("", "ERROR", e.getMessage(), MessageStatus.OKAY);
        }
    }
}
