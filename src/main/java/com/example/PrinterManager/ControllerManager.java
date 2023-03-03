package com.example.PrinterManager;

//import jdk.internal.org.objectweb.asm.util.Printer;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Integer.parseInt;

@SpringBootApplication
@RestController
@CrossOrigin

public class ControllerManager {
    private PrinterManager Pmanager;

    ControllerManager() {
        this.Pmanager = PrinterManager.getInstance();
    }

    public void Reset()
    {
        this.Pmanager.KillSingleton();
        this.Pmanager = PrinterManager.getInstance();
    }



    @GetMapping("/printers")
    String GetPrinterss() {
        ArrayList<String> data= new ArrayList<String>();

        Pmanager.getPrinters().forEach((printer) ->{
            data.add(Pmanager.getPrinter(printer.getId()).toString());
        });
            return data.toString();
    }

    @DeleteMapping("/printers/{id}")
    void deletePrinterByID( @PathVariable int id) {
        Pmanager.disconnect(id);
        System.out.println("Printer "+id+" Disconnected");
    }

    @PutMapping("/printers")
    void addPrinter(@RequestBody String id){
        JSONObject dataJson = new JSONObject(id);
        int flag=0;
        for(printer e : Pmanager.getPrinters()) {
            if(e.getId() == parseInt(dataJson.get("id").toString()))
            {
                System.out.println("unleagal printer add, id exist");
                return;}
        };
        Pmanager.addPrinter(parseInt(dataJson.get("id").toString()),true);
        System.out.println("Printer Added, The id is :"+ parseInt(dataJson.get("id").toString()));
    }

    @GetMapping("/printers/{id}")
    String getPrinterByID( @PathVariable int id) {
        return Pmanager.getPrinterNoJobs(id).toString();
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

        if(dateString==null && status!=null){
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
                LocalDateTime date = LocalDateTime.parse(dateString);
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

        if(dateString!=null && status!=null){
            try {
                LocalDateTime date = LocalDateTime.parse(dateString);
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                boolean flag= false;
                if (status.toLowerCase(Locale.ROOT).contains("true") || status.toLowerCase(Locale.ROOT).contains("1")){
                    flag=true;
                }
                else if (status.toLowerCase(Locale.ROOT).contains("false") || status.toLowerCase(Locale.ROOT).contains("0")){
                    flag=false;
                }

                Pmanager.getAllJobs(flag,date).forEach((e)-> {
                    data.add(e.getJob().toString());
                });
            }
            catch (Exception e) {
                System.out.println("Exception: " + e);
                return "This date Is not In the right format, needs to be: YYYY-MM-DDTHH:MM:SS , Bad Request";
            } // If the String was unable to be parsed.

            return data.toString();
        }

        return "No jobs for this filters";
    }

    public static void main(String[] args) {
        SpringApplication.run(ControllerManager.class, args);}

}
