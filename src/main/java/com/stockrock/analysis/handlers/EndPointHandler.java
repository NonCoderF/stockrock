package com.stockrock.analysis.handlers;

import com.google.gson.Gson;
import com.stockrock.analysis.GlobalData;
import com.stockrock.analysis.constants.CommandConstants.*;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.handlers.chart.ChartCmdHandler;
import com.stockrock.analysis.handlers.chart.ChartServicesHandler;
import com.stockrock.analysis.model.*;
import com.stockrock.analysis.model.Annotations.Type;
import com.stockrock.analysis.service.TerminalService;
import com.stockrock.analysis.utils.Messenger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.InvocationTargetException;

import static com.stockrock.analysis.GlobalData.sessionMap;
import static com.stockrock.analysis.constants.CommandConstants.*;
import static com.stockrock.analysis.utils.ArrayUtils.EnumHasString;

@Component
public class EndPointHandler implements ServiceExecuteListener {

    private final ChartServicesHandler chartServicesHandler;
    private final TerminalService terminalService;

    @Autowired
    public EndPointHandler(ChartServicesHandler chartServicesHandler, TerminalService terminalService) {
        this.chartServicesHandler = chartServicesHandler;
        this.terminalService = terminalService;
    }

    @Type(name = chart)
    public void initChartServices(Command command) {
        try {
            JSONObject jsonObject = new JSONObject(GlobalData.message.getPayload());
            if (jsonObject.has("data"))
                command.setData(new Gson().fromJson(jsonObject.getJSONObject("data").toString(), Data.class));

            ChartCmdHandler chartCmdHandler = new ChartCmdHandler(command);
            String currentId = chartCmdHandler.handleCommand();

            GlobalData.dataMap.get(currentId).setSessionId(command.getSessionId());

            chartServicesHandler.setListener(this);
            chartServicesHandler.init(GlobalData.dataMap.get(currentId));

        } catch (Exception e) {
            e.printStackTrace();
            Messenger.sendErrorMessage(sessionMap.get(command.getSessionId()), "Error parsing data : " + e);
        }
    }

    @Type(name = info)
    public void initInfoServices(Command command) {
        String string = new Gson().toJson(GlobalData.dataMap);
        Messenger.sendOkMessageObj(GlobalData.sessionMap.get(command.getSessionId()), string);
    }

    @Type(name = terminal)
    public void initTerminalServices(Command command) {
        terminalService.setObserver((serviceName, id, data, message) -> {
            if (id.equals("OK"))
                Messenger.sendOkMessage(GlobalData.sessionMap.get(command.getSessionId()), (String) data);
            else Messenger.sendErrorMessage(GlobalData.sessionMap.get(command.getSessionId()), (String) data);
        });
        terminalService.executeTerminalCommand(command.getCommand());
    }

    @Override
    public void onDeliver(Message message) {
        String messageStr = new Gson().toJson(message);

        if (message.getStatus().equals(MessageStatus.FAILED)) {
            Messenger.sendErrorMessageObj(sessionMap.get(message.getSessionId()), messageStr);
        } else {
            Messenger.sendOkMessageObj(sessionMap.get(message.getSessionId()), messageStr);
        }
    }

    public void handleSessionMessage(WebSocketSession session, TextMessage message) {
        GlobalData.sessionMap.put(session.getId(), session);
        GlobalData.stagesMap.put(session.getId(), new Stages());
        GlobalData.message = message;

        try {
            handleMessage(session);
        } catch (Exception e) {
            Messenger.sendErrorMessage(session, e.getStackTrace()[0].toString() + ", " + e.getMessage());
        }
    }

    private void handleMessage(WebSocketSession session) throws InvocationTargetException, IllegalAccessException, NullPointerException {
        Command command = new Gson().fromJson(GlobalData.message.getPayload(), Command.class);
        command.setSessionId(session.getId());

        if (command == null) throw new NullPointerException("Command packet is null");
        if (command.getOperation() == null) throw new NullPointerException("Operation is null");
        if (command.getCommand() == null) throw new NullPointerException("Command is null");
        if (!EnumHasString(Commands.class, command.getCommand()))
            throw new NullPointerException("Invalid command!!!");

        Annotations.Processor.methodInject(this, command.getOperation(), command);
    }
}
