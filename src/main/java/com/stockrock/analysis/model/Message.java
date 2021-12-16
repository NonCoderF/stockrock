package com.stockrock.analysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    @SerializedName("service")
    @Expose
    private String service;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("progress")
    @Expose
    private float progress;

    @SerializedName("stages")
    @Expose
    private int stages;

    @SerializedName("currentStage")
    @Expose
    private int currentStage;

    @SerializedName("data")
    @Expose
    private Object data;

    @SerializedName("live")
    @Expose
    private boolean live;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getStages() {
        return stages;
    }

    public void setStages(int stages) {
        this.stages = stages;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
