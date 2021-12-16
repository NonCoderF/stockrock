package com.stockrock.analysis.model.indicators;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Adx implements Serializable {

    @SerializedName("adxCount")
    @Expose
    private int adxCount;

    @SerializedName("adx")
    @Expose
    private List<Double> adx;

    @SerializedName("plusDi")
    @Expose
    private List<Double> plusDi;

    @SerializedName("minusDi")
    @Expose
    private List<Double> minusDi;

    public int getAdxCount() {
        return adxCount;
    }

    public void setAdxCount(int adxCount) {
        this.adxCount = adxCount;
    }

    public List<Double> getAdx() {
        return adx;
    }

    public void setAdx(List<Double> adx) {
        this.adx = adx;
    }

    public List<Double> getPlusDi() {
        return plusDi;
    }

    public void setPlusDi(List<Double> plusDi) {
        this.plusDi = plusDi;
    }

    public List<Double> getMinusDi() {
        return minusDi;
    }

    public void setMinusDi(List<Double> minusDi) {
        this.minusDi = minusDi;
    }
}
