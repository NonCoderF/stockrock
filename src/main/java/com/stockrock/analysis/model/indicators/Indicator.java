package com.stockrock.analysis.model.indicators;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Indicator implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("data")
    @Expose
    private String[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
