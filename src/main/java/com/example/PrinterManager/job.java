// file path: src/main/java/com/example/Printers/Job.java
package com.example.PrinterManager;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
class job {

    private static int jobCount=0;
    private int id;
    private boolean status;
    private String content;
    private LocalDateTime date;

    private DateTimeFormatter DTF;

    private double timeForTheJob;




    job(String _content ) {
        this.id = this.jobCount++;
        this.status = false;
        this.content = _content;
        this.DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.date = LocalDateTime.now();
        this.timeForTheJob = _content.length()*0.001;
    }

    public int getId() {
        return this.id;
    }

    public boolean getStatus() {
        return this.status;
    }

    public double timeForTheJob() {
        return this.timeForTheJob;
    }

    public String getContent() {
        return this.content;
    }
    public LocalDateTime getDate() {
        return this.date;
    }


    public void setStatus(boolean _status) {
        this.status = _status;
    }

    public void setContent(String _content) {
        this.content = _content;
    }

    public void setDate(LocalDateTime _date) {
        this.date = _date;
    }

    public JSONObject getJob(){
        JSONObject Job = new JSONObject();
        Job.put("id", this.id);
        Job.put("status", this.status);
        Job.put("timeForTheJob", this.timeForTheJob);
        Job.put("content", this.content);
        Job.put("date", this.date);
        return Job;
    }


}