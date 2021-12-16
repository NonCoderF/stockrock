package com.stockrock.analysis.model;

public class Stages {
    private Integer totalStages = 0;
    private Integer currentStage = 0;

    public void addStages(Integer stages){
        totalStages += stages;
    }

    public void incCurrentStage(){
        if (currentStage.equals(totalStages)) return;
        currentStage++;
    }

    public Integer getTotalStages() {
        return totalStages;
    }

    public Integer getCurrentStage() {
        return currentStage;
    }

    public Float getProgress(){
        float progress = (float) currentStage / totalStages * 100;
        if (progress > 0) return progress; else return 0F;
    }
}
