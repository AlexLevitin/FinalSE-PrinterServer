package com.example.PrinterManager;

import org.json.JSONObject;

import java.util.ArrayList;


public class printer {
    private int id;
    private boolean liveness;
    ArrayList<job> jobs; // Create an ArrayList object}

    printer(int id,boolean liveness) {
        this.id = id;
        this.liveness=liveness;
        this.jobs=new ArrayList<job>();
    }

    public int getId(){
        return this.id;
    }

    public boolean getLiveness(){
        return this.liveness;
    }


    public void setLiveness(boolean liveness) {
        this.liveness = liveness;
    }

    public ArrayList<job> getJobs(){
        return this.jobs;
    }

    public void addJob(String content){
        this.jobs.add(new job(content));
    }

    public void updateJobStatus(int jobId,boolean status){
        jobs.forEach((n) ->{
            if(n.getId()==jobId) {
                n.setStatus(status);}
        });

    }

    public job getLatestJob(){
        return jobs.get(jobs.size() - 1);
    }

    public JSONObject getPrinter(){
        JSONObject Printer = new JSONObject();
        JSONObject jobss = new JSONObject() ;
        Printer.put("liveness", this.liveness);
        this.jobs.forEach((elm)->{
            jobss.put("job"+elm.getId(),elm.getJob());
        });
        Printer.put("Jobs", jobss);
        Printer.put("id", this.id);
        return Printer;
    }

    public JSONObject getPrinterNoJobs(){
        JSONObject Printer = new JSONObject();
        Printer.put("liveness", this.liveness);
        Printer.put("id", this.id);
        return Printer;
    }
}