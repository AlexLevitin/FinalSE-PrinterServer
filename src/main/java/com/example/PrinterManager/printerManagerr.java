package com.example.PrinterManager;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
class PrinterManager {

    private static PrinterManager singleInstance = null;
    private ArrayList<printer> printers;  // Create an ArrayList object}

    private PrinterManager() {
        this.printers = new ArrayList<printer>();
    }

    public static PrinterManager getInstance()
    {
        if(singleInstance == null)
            singleInstance = new PrinterManager();
        return singleInstance;
    }
    public  void KillSingleton()
    {
        this.printers = null;
        singleInstance = null;
    }

    public void addPrinter(int id,boolean liveness) {
        this.printers.add(new printer(id,liveness));
    }

    public ArrayList<printer> getPrinters() {
        return this.printers;
    }

    public JSONObject getPrinter(int id) {
        JSONObject tmp = new JSONObject();
        this.printers.forEach((printer) ->{
            if(printer.getId()==id) {
                tmp.put("printer"+Integer.toString(id),printer.getPrinter()) ;}
        });
        return  tmp;
    }

    public JSONObject getPrinterNoJobs(int id) {
        JSONObject tmp = new JSONObject();
        this.printers.forEach((printer) ->{
            if(printer.getId()==id) {
                tmp.put("printer"+Integer.toString(id),printer.getPrinterNoJobs()) ;}
        });
        return  tmp;
    }

    public ArrayList<job> getAllJobs() {
        ArrayList<job> allJobs = new ArrayList<job>(0);

        this.printers.forEach((printer) ->{
            printer.getJobs().forEach((job) ->{
                allJobs.add(job);
            });

        });
        return allJobs;
    }

    public void disconnect(int id) {
        this.printers.forEach((printer) ->{
            if(printer.getId()==id)
                printer.setLiveness(false);
        });
    }

    public boolean updateLiveness(int id) {
        for (printer i : this.printers) {
            if(i.getId()==id)
            {
                if(i.getLiveness())
                    return true;
                else {
                    i.setLiveness(true);
                    return true;
                }
            }
        }
        return false;
    }

    public int addJobToPrinter(int PrinterId,String content ) {
        for (printer i : this.printers) {
            if(i.getId()==PrinterId)
            {
                i.addJob(content);
                return i.getLatestJob().getId();
            }
        }
        return -1;
    }


    public ArrayList<job> getAllJobsPrinter(int printerId,boolean status){
        ArrayList<job> allJobs = new ArrayList<job>(0);

        this.printers.forEach((printer) ->{
            if(printer.getId()==printerId) {
                printer.getJobs().forEach((job) -> {
                    if (job.getStatus()==status)
                        allJobs.add(job);
                });
            }
        });
        return allJobs;
    }

    public ArrayList<job> getAllJobsPrinter(int printerId){
        ArrayList<job> allJobs = new ArrayList<job>(0);

        this.printers.forEach((printer) ->{
            if(printer.getId()==printerId) {
                printer.getJobs().forEach((job) -> {
                    allJobs.add(job);
                });
            }
        });
        return allJobs;
    }

    public ArrayList<job> getAllJobsPrinter(boolean status){
        ArrayList<job> allJobs = new ArrayList<job>(0);

        this.printers.forEach((printer) ->{
            printer.getJobs().forEach((job) -> {
                if (job.getStatus()==status)
                    allJobs.add(job);
            });
        });
        return allJobs;
    }

    public ArrayList<job> getAllJobs(boolean status,LocalDateTime date){
        ArrayList<job> allJobs = new ArrayList<job>(0);

        this.printers.forEach((printer) ->{
            printer.getJobs().forEach((job) -> {
                if (job.getStatus()==status && job.getDate().compareTo(date)>0)
                    allJobs.add(job);
            });
        });
        return allJobs;
    }

    public ArrayList<job> getAllJobs(boolean status){
        ArrayList<job> allJobs = new ArrayList<job>(0);

        this.printers.forEach((printer) ->{
            printer.getJobs().forEach((job) -> {
                if (job.getStatus()==status )
                    allJobs.add(job);
            });
        });
        return allJobs;
    }

    public ArrayList<job> getAllJobs(LocalDateTime date){
        ArrayList<job> allJobs = new ArrayList<job>(0);

        this.printers.forEach((printer) ->{
            printer.getJobs().forEach((job) -> {
                if (job.getDate().compareTo(date)>0)
                    allJobs.add(job);
            });
        });
        return allJobs;
    }

    public void updateStatusForAJob(int jobId){
        ArrayList<String> a = new ArrayList<String>();
        this.printers.forEach((printer) ->{
            printer.getJobs().forEach((job) -> {
                if (job.getId()==jobId) {
                    job.setStatus(!job.getStatus());
                    System.out.println("Status of job: "+jobId+" was Updated!");
                    a.add("a");
                }
            });
        });
        if(a.size()==0)
        System.out.println("No such job -->"+jobId);
    }

    public JSONObject Statistics(){
        JSONObject statistics = new JSONObject();
        int livePrinters=0;
        int notLivePrinters=0;
        int jobsTrue=0;
        int jobsFalse=0;
        float FalseJobsTime=0;
        float TrueJobsTime=0;

        for (printer i :  this.printers) {
            if(i.getLiveness())livePrinters++;
            else notLivePrinters++;
            for(job j : i.getJobs()){
                if(j.getStatus())
                {
                    jobsTrue++;
                    TrueJobsTime+=j.timeForTheJob();
                }

                else {
                    jobsFalse++;
                    FalseJobsTime+=j.timeForTheJob();
                }
            }
        }
        statistics.put("livePrinters", livePrinters);
        statistics.put("notLivePrinters", notLivePrinters);
        statistics.put("Complete Jobs", jobsTrue);
        statistics.put("Incomplete Jobs", jobsFalse);
        if(jobsTrue!=0){statistics.put("Complete Jobs Time", TrueJobsTime/jobsTrue);}
        else statistics.put("Complete Jobs Time", "0");
        if(jobsFalse!=0){statistics.put("InComplete Jobs Time",  FalseJobsTime/jobsFalse);}
        else statistics.put("InComplete Jobs Time", "0");
        return statistics;
    }




}


