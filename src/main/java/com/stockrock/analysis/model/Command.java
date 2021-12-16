package com.stockrock.analysis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Command implements Serializable {

    @SerializedName("sessionId")
    @Expose
    private String sessionId = "";

    @SerializedName("operation")
    @Expose
    private String operation;

    @SerializedName("command")
    @Expose
    private String command;

    @SerializedName("data")
    @Expose
    private Object data;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
