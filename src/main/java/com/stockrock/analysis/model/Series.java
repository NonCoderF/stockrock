package com.stockrock.analysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Series implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;


}
