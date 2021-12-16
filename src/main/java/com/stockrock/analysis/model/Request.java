package com.stockrock.analysis.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

    @JsonProperty("request")
    public String request;

    @JsonProperty("response")
    private String response;

}
