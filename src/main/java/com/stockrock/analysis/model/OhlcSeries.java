package com.stockrock.analysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.List;

public class OhlcSeries implements Serializable {


    @BsonProperty("beginTime")
    @SerializedName("beginTime")
    @Expose
    private String beginTime;

    @BsonProperty("endTime")
    @SerializedName("endTime")
    @Expose
    private String endTime;

    @BsonProperty("interval")
    @SerializedName("interval")
    @Expose
    private String interval;

    @BsonProperty("open")
    @SerializedName("open")
    @Expose
    private List<Double> open;

    @BsonProperty("close")
    @SerializedName("close")
    @Expose
    private List<Double> close;

    @BsonProperty("low")
    @SerializedName("low")
    @Expose
    private List<Double> low;

    @BsonProperty("high")
    @SerializedName("high")
    @Expose
    private List<Double> high;

    @BsonProperty("volume")
    @SerializedName("volume")
    @Expose
    private List<Double> volume;

    @BsonProperty("duration")
    @SerializedName("duration")
    @Expose
    private List<Long> duration;

    @BsonProperty("beginTimeSeries")
    @SerializedName("beginTimeSeries")
    @Expose
    private List<String> beginTimeSeries;

    @BsonProperty("endTimeSeries")
    @SerializedName("endTimeSeries")
    @Expose
    private List<String> endTimeSeries;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public List<Double> getOpen() {
        return open;
    }

    public void setOpen(List<Double> open) {
        this.open = open;
    }

    public List<Double> getClose() {
        return close;
    }

    public void setClose(List<Double> close) {
        this.close = close;
    }

    public List<Double> getLow() {
        return low;
    }

    public void setLow(List<Double> low) {
        this.low = low;
    }

    public List<Double> getHigh() {
        return high;
    }

    public void setHigh(List<Double> high) {
        this.high = high;
    }

    public List<Double> getVolume() {
        return volume;
    }

    public void setVolume(List<Double> volume) {
        this.volume = volume;
    }

    public List<Long> getDuration() {
        return duration;
    }

    public void setDuration(List<Long> duration) {
        this.duration = duration;
    }

    public List<String> getBeginTimeSeries() {
        return beginTimeSeries;
    }

    public void setBeginTimeSeries(List<String> beginTimeSeries) {
        this.beginTimeSeries = beginTimeSeries;
    }

    public List<String> getEndTimeSeries() {
        return endTimeSeries;
    }

    public void setEndTimeSeries(List<String> endTimeSeries) {
        this.endTimeSeries = endTimeSeries;
    }
}
