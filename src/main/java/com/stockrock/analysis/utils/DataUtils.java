package com.stockrock.analysis.utils;

import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.indicators.Indicator;

import java.text.ParseException;
import java.util.*;

import static com.stockrock.analysis.utils.ArrayUtils.hasString;
import static com.stockrock.analysis.utils.TimeUtils.*;

public class DataUtils {

    public String generateID(Data data) {
        if (data.getId() == null) {
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 18) {
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            data.setId(salt.toString());
            return salt.toString();
        }
        return null;
    }

    public boolean generateDay(Data data) {
        if (data.getDay() != null && !data.getDay().isEmpty()) {
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last5mins.name())) {
                data.setFrom(lastNthMin(5));
                data.setTo(lastNthMin(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last10mins.name())) {
                data.setFrom(lastNthMin(10));
                data.setTo(lastNthMin(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last15mins.name())) {
                data.setFrom(lastNthMin(15));
                data.setTo(lastNthMin(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last30mins.name())) {
                data.setFrom(lastNthMin(30));
                data.setTo(lastNthMin(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.lasthour.name())) {
                data.setFrom(lastNthHour(1));
                data.setTo(lastNthHour(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last2hours.name())) {
                data.setFrom(lastNthHour(2));
                data.setTo(lastNthHour(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last3hours.name())) {
                data.setFrom(lastNthHour(3));
                data.setTo(lastNthHour(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last6hours.name())) {
                data.setFrom(lastNthHour(6));
                data.setTo(lastNthHour(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.today.name())) {
                data.setFrom(lastNthDay(1));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.yesterday.name())) {
                data.setFrom(lastNthDay(2));
                data.setTo(lastNthDay(1));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.daybeforeyesterday.name())) {
                data.setFrom(lastNthDay(3));
                data.setTo(lastNthDay(2));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last2days.name())) {
                data.setFrom(lastNthDay(2));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last3days.name())) {
                data.setFrom(lastNthDay(3));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.lastweek.name())) {
                data.setFrom(lastNthDay(7));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last15days.name())) {
                data.setFrom(lastNthDay(15));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last30days.name())) {
                data.setFrom(lastNthDay(30));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last2months.name())) {
                data.setFrom(lastNthDay(60));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last3months.name())) {
                data.setFrom(lastNthDay(90));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.last6months.name())) {
                data.setFrom(lastNthDay(180));
                data.setTo(lastNthDay(0));
                return true;
            }
            if (data.getDay().equalsIgnoreCase(CommandConstants.Days.lastyear.name())) {
                data.setFrom(lastNthDay(365));
                data.setTo(lastNthDay(0));
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean generateFromTimes(Data data) {
        if (data.getFrom() == null || data.getFrom().isEmpty()) {
            return false;
        }

        String from = parseSpecificTime(data.getFrom());
        if (from == null || from.isEmpty()) {
            return false;
        }
        data.setFrom(from);

        return true;

    }

    public boolean generateToTimes(Data data) {

        if (data.getTo() == null || data.getTo().isEmpty()) {
            String to = parseCurrentTime();
            data.setTo(to);
        } else {
            String to = parseSpecificTime(data.getTo());
            if (to == null || to.isEmpty()) {
                return false;
            }
            data.setTo(to);
        }

        return true;

    }

    public boolean generateInterval(Data data) {
        try {
            Date fromDate = DateUtil.parse(data.getFrom(), "EEE MMM dd HH:mm:ss z yyyy");
            Date toDate = DateUtil.parse(data.getTo(), "EEE MMM dd HH:mm:ss z yyyy");

            Calendar fromCal = Calendar.getInstance();
            fromCal.setTime(fromDate);
            Calendar toCal = Calendar.getInstance();
            toCal.setTime(toDate);

            int years = CalendarUtil.elapsedYears(fromCal, toCal);
            int months = CalendarUtil.elapsedMonths(fromCal, toCal);
            int days = CalendarUtil.elapsedDays(fromCal, toCal);
            int hours = CalendarUtil.elapsedHours(fromCal, toCal);
            int mins = CalendarUtil.elapsedMinutes(fromCal, toCal);
            int secs = CalendarUtil.elapsedSeconds(fromCal, toCal);

            String[] intervals;

            //For Yahoo Finance
//            if (days < 5) {
//                intervals = new String[]{"1m", "2m", "5m", "15m", "30m", "60m", "90m", "1h", "1d"};
//            } else if (days < 29) {
//                intervals = new String[]{"5m", "15m", "30m", "60m", "90m", "1h", "1d", "5d", "1wk", "1mo", "3mo"};
//            } else if (days < 80) {
//                intervals = new String[]{"60m", "90m", "1h", "1d", "5d", "1wk", "1mo", "3mo"};
//            } else {
//                intervals = new String[]{"1d", "5d", "1wk", "1mo", "3mo"};
//            }
//            data.setInterval(intervals[0]);

            //For TwelveData

            intervals = new String[]{"1min", "5min", "15min", "30min", "45min", "1h", "2h", "4h", "1day", "1week", "1month"};

            data.setIntervals(intervals);

            if (data.getInterval() != null){
                return hasString(intervals, data.getInterval());
            }else{
                if (days < 2){
                    data.setInterval(intervals[0]);
                }else if (days < 8){
                    data.setInterval(intervals[3]);
                }else if (days < 16){
                    data.setInterval(intervals[5]);
                }else if (days < 32){
                    data.setInterval(intervals[8]);
                }else if (days < 63){
                    data.setInterval(intervals[8]);
                }else {
                    data.setInterval(intervals[8]);
                }
            }
            return true;

        } catch (ParseException e) {
            SysOut.e(e.getMessage());
        }

        return false;

    }

    public boolean generateCurves(Data data) {
        if (data.getAvailableCurves() == null || data.getAvailableCurves().length == 0) {
            data.setAvailableCurves(ArrayUtils.stringArrayFromEnum(CommandConstants.Curves.class));
        }

        List<String> curves = new ArrayList<>();

        if (data.getCurves() != null) {
            for (int i = 0; i < data.getCurves().length; i++) {
                if (ArrayUtils.hasString(data.getAvailableCurves(), data.getCurves()[i])) {
                    curves.add(data.getCurves()[i]);
                }
            }
        }

        if (curves.size() == 0) {
            curves.add(data.getAvailableCurves()[0]);
        }

        String[] curveArr = new String[curves.size()];
        for (int i = 0; i < curves.size(); i++) {
            curveArr[i] = curves.get(i);
        }

        data.setCurves(curveArr);

        return true;
    }

    public boolean generateIndicators(Data data) {
        if (data.getAvailableIndicators() == null || data.getAvailableIndicators().length == 0) {
            data.setAvailableIndicators(ArrayUtils.stringArrayFromEnum(CommandConstants.Indicators.class));
        }

        List<Indicator> indicators = new ArrayList<>();

        if (data.getIndicators() != null) {
            for (int i = 0; i < data.getIndicators().size(); i++) {
                if (ArrayUtils.hasString(data.getAvailableIndicators(), data.getIndicators().get(i).getName())) {
                    indicators.add(data.getIndicators().get(i));
                }
            }
        }

        data.setIndicators(indicators);

        return true;
    }

    public long intervalToMilis(String interval){
        long millis = 0;
        switch (interval){
            case "1min" :
                millis = 60 * 1000;
                break;
            case "5min" :
                millis = 5 * 60 * 1000;
                break;
            case "15min" :
                millis = 15 * 60 * 1000;
                break;
            case "30min" :
                millis = 30 * 60 * 1000;
                break;
            case "45min" :
                millis = 45 * 60 * 1000;
                break;
            case "1h" :
                millis = 60 * 60 * 1000;
                break;
            case "2h" :
                millis = 2 * 60 * 60 * 1000;
                break;
            case "4h" :
                millis = 4 * 60 * 60 * 1000;
                break;
            case "1day" :
                millis = 24 * 60 * 60 * 1000;
                break;
            case "1week" :
                millis = 7 * 24 * 60 * 60 * 1000;
                break;
            case "1month" :
                millis = 30 * 24 * 60 * 60 * 1000;
                break;
        }

        return millis;
    }

    public boolean isUpdatedData(Data data) {
        boolean timesUpdated = false;
        if (data.getOhlcSeries() != null) {
            if (!data.getInterval().equals(data.getOhlcSeries().getInterval())){
                timesUpdated = true;
            }

            long interval = intervalToMilis(data.getInterval());
            long diffFrom = Math.abs(TimeUtils.parseStringTime(data.getFrom()).getTime() - TimeUtils.parseStringTime(data.getOhlcSeries().getBeginTime()).getTime());
            long diffTo = Math.abs(TimeUtils.parseStringTime(data.getTo()).getTime() - TimeUtils.parseStringTime(data.getOhlcSeries().getEndTime()).getTime());

            if (diffFrom > interval || diffTo > interval){
                timesUpdated = true;
            }

        } else {
            timesUpdated = true;
        }

        return timesUpdated;
    }

}
