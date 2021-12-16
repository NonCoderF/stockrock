package com.stockrock.analysis.model.indicators;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Sma implements Serializable {

    @SerializedName("barCount")
    @Expose
    private int barCount;

    @SerializedName("sma")
    @Expose
    private List<Double> sma;

    public int getBarCount() {
        return barCount;
    }

    public void setBarCount(int barCount) {
        this.barCount = barCount;
    }

    public List<Double> getSma() {
        return sma;
    }

    public void setSma(List<Double> sma) {
        this.sma = sma;
    }
}
