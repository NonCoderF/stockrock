package com.stockrock.analysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stockrock.analysis.model.indicators.Indicator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("symbol")
    @Expose
    private String symbol;

    @SerializedName("day")
    @Expose
    private String day;

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("interval")
    @Expose
    private String interval;

    @SerializedName("intervals")
    @Expose
    private String[] intervals;

    @SerializedName("curves")
    @Expose
    private String[] curves;

    @SerializedName("availableCurves")
    @Expose
    private String[] availableCurves;

    @SerializedName("realtime")
    @Expose
    private Boolean realtime = false;

    @SerializedName("indicators")
    @Expose
    private List<Indicator> indicators;

    @SerializedName("availableIndicators")
    @Expose
    private String[] availableIndicators;


    @SerializedName("ohlcSeries")
    @Expose
    private OhlcSeries ohlcSeries;

    @SerializedName("indicatorSeries")
    @Expose
    private HashMap<String, List<Double>> indicatorSeries;

    @SerializedName("trendData")
    @Expose
    private HashMap<String, Object> trendData;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String[] getIntervals() {
        return intervals;
    }

    public void setIntervals(String[] intervals) {
        this.intervals = intervals;
    }

    public String[] getCurves() {
        return curves;
    }

    public void setCurves(String[] curves) {
        this.curves = curves;
    }

    public String[] getAvailableCurves() {
        return availableCurves;
    }

    public void setAvailableCurves(String[] availableCurves) {
        this.availableCurves = availableCurves;
    }

    public Boolean getRealtime() {
        return realtime;
    }

    public void setRealtime(Boolean realtime) {
        this.realtime = realtime;
    }


    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    public String[] getAvailableIndicators() {
        return availableIndicators;
    }

    public void setAvailableIndicators(String[] availableIndicators) {
        this.availableIndicators = availableIndicators;
    }

    public OhlcSeries getOhlcSeries() {
        return ohlcSeries;
    }

    public void setOhlcSeries(OhlcSeries ohlcSeries) {
        this.ohlcSeries = ohlcSeries;
    }

    public HashMap<String, List<Double>> getIndicatorSeries() {
        return indicatorSeries;
    }

    public void setIndicatorSeries(HashMap<String, List<Double>> indicatorSeries) {
        this.indicatorSeries = indicatorSeries;
    }

    public HashMap<String, Object> getTrendData() {
        return trendData;
    }

    public void setTrendData(HashMap<String, Object> trendData) {
        this.trendData = trendData;
    }
}
