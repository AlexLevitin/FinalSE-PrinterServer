package com.example.PrinterManager;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

@RestController
public class ControllerManager {
    private PrinterManager Pmanager;

    ControllerManager() {
        this.Pmanager = new PrinterManager();
        /*Pmanager.addPrinter(1,true);
        Pmanager.addPrinter(2,true);
        Pmanager.addJobToPrinter(1,"alex Hey Bro");
        Pmanager.addJobToPrinter(1,"David ");*/

    }


    @GetMapping("/printers")
    String GetPrinterss() {
        ArrayList<String> data= new ArrayList<String>();

        Pmanager.getPrinters().forEach((printer) ->{
            data.add(Pmanager.getPrinter(printer.getId()).toString());
        });
            return data.toString();
    }

    @PutMapping("/printers")
    void addPrinter(@RequestBody String id){
        JSONObject idJson = new JSONObject();
        idJson.put("id",id);
        Pmanager.addPrinter(Integer.parseInt(idJson.get("id").toString()),true);

        System.out.println("Printer Added, The id is :"+ Integer.parseInt(idJson.get("id").toString()));
    }

    @GetMapping("/printers/{id}")
    String getPrinterByID( @PathVariable int id) {
        return Pmanager.getPrinterNoJobs(id).toString();
    }
    @DeleteMapping("/printers/{id}")
    void deletePrinterByID( @PathVariable int id) {
        Pmanager.disconnect(id);
    }

    @GetMapping("/printers/{id}/full")
    String getPrinterByIDAndAllJobs( @PathVariable int id) {
        return Pmanager.getPrinter(id).toString();
    }

    @PostMapping("/printers/{id}/liveness")
    void UpdateLivness(@PathVariable int id) {
        Boolean Ans=false;
        Ans = Pmanager.updateLiveness(id);
        if(Ans)
            System.out.println("Printer"+id+" Liveness now is:"+ Ans.toString());
        else
            System.out.println("No Printer with such id ->"+id);
    }

    @PutMapping ("/printers/{id}/printjobs")
    int AddPrintingJobAndReturnId( @RequestBody String data,@PathVariable int id) {
        JSONObject dataJson = new JSONObject(data);
        return Pmanager.addJobToPrinter(id,dataJson.get("data").toString());
    }

    @PostMapping("/printjobs/{jobid}")
    void updateJobStatus(@PathVariable int jobid) {
        Pmanager.updateStatusForAJob(jobid);
    }

    @GetMapping("/printjobs/{jobid}")
    String GetJobDetails(@PathVariable int jobid) {
        String s ="{}";
        for(job jobz : Pmanager.getAllJobs())
        {
            if(jobz.getId() == jobid)
                s = jobz.getJob().toString();
        }
        return s;
    }

    @GetMapping("/statistics")
    String GetStatistics() {
        return Pmanager.Statistics().toString();
    }



    @GetMapping("/printers/{id}/printjobs")
     String GetPrinterJobsFiltered(@PathVariable("id") int id, @RequestParam(value = "status", required = false) String status) {
        // get all print jobs for the printer with the given ID
        // if the "status" parameter is present, filter the print jobs by status
        ArrayList<String> data= new ArrayList<String>();

        if (status!= null) {
            boolean flag= false;
            if (status.toLowerCase(Locale.ROOT).contains("true") || status.toLowerCase(Locale.ROOT).contains("1")){
                flag=true;
            }
            else if (status.toLowerCase(Locale.ROOT).contains("false") || status.toLowerCase(Locale.ROOT).contains("0")){
                flag=false;
            }
            // filter print jobs by status
            Pmanager.getAllJobsPrinter(id,flag).forEach((e)-> {
                data.add(e.getJob().toString());
            });
            return data.toString();
        }
        else{
            Pmanager.getAllJobsPrinter(id).forEach((e)-> {
            data.add(e.getJob().toString());
        });
            return data.toString();
        }

    }

    @GetMapping("/printjobs")
    String GetPrinterJobsFilteredSuper(@RequestParam(value = "status", required = false) String status,@RequestParam(value = "since", required = false) String dateString) {
        // get all print jobs for the printer with the given ID
        // if the "status" parameter is present, filter the print jobs by status
        ArrayList<String> data = new ArrayList<String>();
        if(dateString==null && status==null){
            Pmanager.getAllJobs().forEach((e)-> {
                data.add(e.getJob().toString());
            });
            return data.toString();
        }

        if(dateString==null && status!=null && !status.contains("?")){
            boolean flag= false;
            if (status.toLowerCase(Locale.ROOT).contains("true") || status.toLowerCase(Locale.ROOT).contains("1")){
                flag=true;
            }
            else if (status.toLowerCase(Locale.ROOT).contains("false") || status.toLowerCase(Locale.ROOT).contains("0")){
                flag=false;
            }
            Pmanager.getAllJobs(flag).forEach((e)-> {
                data.add(e.getJob().toString());
            });
            return data.toString();
        }

        if(dateString!=null && status==null){
            try {
                String dateStringString= dateString.toString();
                LocalDateTime date = LocalDateTime.parse(dateStringString);
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    Pmanager.getAllJobs(date).forEach((e)-> {
                    data.add(e.getJob().toString());
                });
            }
            catch (Exception e) {
                System.out.println("Exception: " + e);
                return "This date Is not In the right format ! YYYY-MM-DDTHH:MM:SS";
            } // If the String was unable to be parsed.
            return data.toString();
        }

        if(status.contains("?")){
            try {
                String [] Data = status.split("\\?");
                LocalDateTime date = LocalDateTime.parse(Data[1].split("=")[1]);
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                boolean flag= false;
                if (Data[0].toLowerCase(Locale.ROOT).contains("true") || Data[0].toLowerCase(Locale.ROOT).contains("1")){
                    flag=true;
                }
                else if (Data[0].toLowerCase(Locale.ROOT).contains("false") || Data[0].toLowerCase(Locale.ROOT).contains("0")){
                    flag=false;
                }

                Pmanager.getAllJobs(flag,date).forEach((e)-> {
                    data.add(e.getJob().toString());
                });
            }
            catch (Exception e) {
                System.out.println("Exception: " + e);
                return "This date Is not In the right format , Bad Request";
            } // If the String was unable to be parsed.

            return data.toString();
        }

        return "";
    }



}