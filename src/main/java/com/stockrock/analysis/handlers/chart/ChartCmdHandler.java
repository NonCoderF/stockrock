package com.stockrock.analysis.handlers.chart;

import com.stockrock.analysis.GlobalData;
import com.stockrock.analysis.constants.CommandConstants.Commands;
import com.stockrock.analysis.constants.CommandConstants.Days;
import com.stockrock.analysis.exceptions.CommandException;
import com.stockrock.analysis.model.Command;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.indicators.Indicator;
import com.stockrock.analysis.utils.ArrayUtils;
import com.stockrock.analysis.utils.DataUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.stockrock.analysis.constants.CommandConstants.Commands.*;
import static com.stockrock.analysis.constants.MessageStatus.*;
import static com.stockrock.analysis.utils.ArrayUtils.hasString;
import static com.stockrock.analysis.utils.ArrayUtils.stringArrayFromEnum;


public class ChartCmdHandler {

    private final Command command;
    private String currentChartSetId;

    public ChartCmdHandler(Command command) {
        this.command = command;
    }

    public String handleCommand() throws CommandException {
        checkCommand();
        checkData();
        checkRequirements();
        return currentChartSetId;
    }

    private void checkCommand() throws CommandException {
        if (command == null || command.getCommand() == null) {
            throw new CommandException(NOCOMMAND);
        }
        if (!hasString(stringArrayFromEnum(Commands.class), command.getCommand())) {
            throw new CommandException(INVALIDCOMMAND);
        }
    }

    private void checkData() throws CommandException {
        if (command.getData() == null) {
            throw new CommandException(INVALIDPARAM);
        }
    }

    private void checkRequirements() throws CommandException {
        String com = command.getCommand();
        Data data = (Data) command.getData();

        if (com.equalsIgnoreCase(open.name()))
            validateOpenReqs(data);

        if (com.equalsIgnoreCase(add.name()))
            validateAddSetRemoveReqs(data, add.name());

        if (com.equalsIgnoreCase(set.name()))
            validateAddSetRemoveReqs(data, set.name());

        if (com.equalsIgnoreCase(remove.name()))
            validateAddSetRemoveReqs(data, remove.name());

        if (com.equalsIgnoreCase(close.name()))
            validateCloseReqs(data);
    }

    private void validateOpenReqs(Data data) throws CommandException {
        if (data.getSymbol() == null || data.getSymbol().isEmpty()) {
            throw new CommandException(NOSYMBOL);
        }
        DataUtils generator = new DataUtils();
        currentChartSetId = data.getId() != null ? data.getId() : generator.generateID(data);

        boolean fromtimegen = generator.generateFromTimes(data);
        if (!fromtimegen && data.getDay() == null) {
            throw new CommandException("Invalid from time format");
        }
        boolean totimegen = generator.generateToTimes(data);
        if (!totimegen && data.getDay() == null) {
            throw new CommandException("Invalid to time format");
        }

        boolean daygen = generator.generateDay(data);
        if (!daygen) {
            throw new CommandException("Invalid days format. Formats are " + Arrays.toString(ArrayUtils.stringArrayFromEnum(Days.class)));
        }

        boolean intervalgen = generator.generateInterval(data);
        if (!intervalgen) {
            throw new CommandException("Invalid interval format. Formats are " + Arrays.toString(data.getIntervals()));
        }

        boolean curvegen = generator.generateCurves(data);
        boolean indicatorGen = generator.generateIndicators(data);

        GlobalData.dataMap.put(data.getId(), data);

    }

    private void validateAddSetRemoveReqs(Data data, String action) throws CommandException {

        if (data.getId() == null || data.getId().isEmpty()) {
            throw new CommandException("No id specified");
        }

        if (!Arrays.asList(GlobalData.dataMap.keySet().toArray()).contains(data.getId())) {
            throw new CommandException("Invalid id");
        }

        currentChartSetId = data.getId();

        DataUtils generator = new DataUtils();

        boolean fromTimeGen = false;
        boolean toTimeGen = false;

        if (generator.generateFromTimes(data)) {
            GlobalData.dataMap.get(data.getId()).setFrom(data.getFrom());
            fromTimeGen = true;
        }

        if (generator.generateToTimes(data)) {
            GlobalData.dataMap.get(data.getId()).setTo(data.getTo());
            toTimeGen = true;
        }

        if (generator.generateDay(data)) {
            GlobalData.dataMap.get(data.getId()).setFrom(data.getFrom());
            GlobalData.dataMap.get(data.getId()).setTo(data.getTo());
            GlobalData.dataMap.get(data.getId()).setDay(data.getDay());
            fromTimeGen = true;
            toTimeGen = true;
        } else {
            if (!fromTimeGen || !toTimeGen) {
                throw new CommandException("Invalid days format. Formats are " + Arrays.toString(ArrayUtils.stringArrayFromEnum(Days.class)));
            }
        }

        if ((fromTimeGen && !toTimeGen) || (!fromTimeGen && toTimeGen)) {
            throw new CommandException("Invalid from to time format!!!");
        }

        if (generator.generateInterval(data)) {
            GlobalData.dataMap.get(data.getId()).setInterval(data.getInterval());
            GlobalData.dataMap.get(data.getId()).setIntervals(data.getIntervals());
        } else {
            throw new CommandException("Invalid interval format. Formats are " + Arrays.toString(data.getIntervals()));
        }

        if (generator.generateCurves(data)) {
            String[] prevCurves = GlobalData.dataMap.get(data.getId()).getCurves();
            if (action.equalsIgnoreCase(add.name())) {
                GlobalData.dataMap.get(data.getId()).setCurves(ArrayUtils.mergeArray(prevCurves, data.getCurves()));
            }

            if (action.equalsIgnoreCase(set.name())) {
                GlobalData.dataMap.get(data.getId()).setCurves(data.getCurves());
            }

            if (action.equalsIgnoreCase(remove.name())) {
                GlobalData.dataMap.get(data.getId()).setCurves(ArrayUtils.removeElements(prevCurves, data.getCurves()));
            }

        }

        if (generator.generateIndicators(data)) {
            List<Indicator> prevIndicators = GlobalData.dataMap.get(data.getId()).getIndicators();

            if (action.equalsIgnoreCase(add.name())) {
                List<Integer> commonIndicatorIndex = new ArrayList<>();
                for (int i = 0; i < prevIndicators.size(); i++) {
                    for (int j = 0; j < data.getIndicators().size(); j++) {
                        if (prevIndicators.get(i).getName().equals(data.getIndicators().get(j).getName())) {
                            commonIndicatorIndex.add(i);
                        }
                    }
                }
                for (Integer i : commonIndicatorIndex) {
                    prevIndicators.remove((int) i);
                }
                prevIndicators.addAll(data.getIndicators());
                GlobalData.dataMap.get(data.getId()).setIndicators(prevIndicators);
            }

            if (action.equalsIgnoreCase(set.name())) {
                GlobalData.dataMap.get(data.getId()).setIndicators(data.getIndicators());
            }

            if (action.equalsIgnoreCase(remove.name())) {
                List<Indicator> newIndicators = new ArrayList<>();
                for (int i = 0; i < prevIndicators.size(); i++) {
                    for (int j = 0; j < data.getIndicators().size(); j++) {
                        if (!prevIndicators.get(i).getName().contains(data.getIndicators().get(j).getName())) {
                            newIndicators.add(prevIndicators.get(i));
                        }
                    }
                }
                GlobalData.dataMap.get(data.getId()).setIndicators(newIndicators);
            }

        }
    }

    private void validateCloseReqs(Data data) throws CommandException {
        if (data.getId() == null || data.getId().isEmpty()) {
            throw new CommandException("No id specified");
        } else {
            currentChartSetId = data.getId();
            GlobalData.dataMap.remove(data.getId());
        }
    }

}
