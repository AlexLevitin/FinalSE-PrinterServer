package com.example.PrinterManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerManagerTest {
ControllerManager cm = new ControllerManager();
PrinterManager pm = new PrinterManager();
    @Test
    void addPrinterValidInput()
    {
        cm.addPrinter("3");
        assertEquals("{\"printer3\":{\"liveness\":true,\"id\":3}}",cm.getPrinterByID(3));
    }

    @Test
    void addPrinterInvalidInputNotInteger()
    {
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            cm.addPrinter("3d");
            ;
        });

        String expectedMessage = "For input string";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void getPrinterssNoPrintersAdded()
    {
        String s = cm.GetPrinterss();
        assertEquals("[]",s);
    }
    @Test
    void getPrinterssWithPrintersAdded()
    {
        cm.addPrinter("1");
        String s = cm.GetPrinterss();
        assertEquals("[{\"printer1\":{\"Jobs\":{},\"liveness\":true,\"id\":1}}]",s);
    }

    @Test
    void getPrinterByIDPrinterExists() {
        cm.addPrinter("1");
        String res = cm.getPrinterByID(1);
        assertEquals("{\"printer1\":{\"liveness\":true,\"id\":1}}",res);
    }
    @Test
    void getPrinterByIDPrinterDoesntExist() {
        cm.addPrinter("1");
        String res = cm.getPrinterByID(3);
        assertEquals("{}",res);
    }

    @Test
    void getPrinterByIDAndAllJobsValid() {
        cm.addPrinter("2");
        int jobId = cm.AddPrintingJobAndReturnId("{data:Gumigam}", 2);
        String s = cm.getPrinterByIDAndAllJobs(2);
        assertTrue(s.contains("\"id\":"+jobId+",\"timeForTheJob\":0.007,\"content\":\"Gumigam\",\"status\":false}},\"liveness\":true,\"id\":2}}")
                && s.contains("{\"printer2\":{\"Jobs\":{"));
    }
    @Test
    void getPrinterByIDAndAllJobsNoJobs() {
        cm.addPrinter("2");
        String s = cm.getPrinterByIDAndAllJobs(2);
        assertEquals("{\"printer2\":{\"Jobs\":{},\"liveness\":true,\"id\":2}}",s);
    }
    @Test
    void getPrinterByIDAndAllJobsPrinterDoesntExist() {
        String s = cm.getPrinterByIDAndAllJobs(2);
        assertEquals("{}",s);
    }

    @Test
    void deletePrinterByID() {
        cm.addPrinter("5");
        cm.deletePrinterByID(5);
        String s = cm.getPrinterByID(5);
        assertTrue(s.contains("{\"liveness\":false"));
    }
    @Test
    void updateLivnessLivePrinter() {
        cm.addPrinter("1");
        cm.UpdateLivness(1);
        String s = cm.getPrinterByID(1);
        assertTrue(s.contains("{\"liveness\":true"));
    }
    @Test
    void updateLivnessDisconnectedPrinter() {
        cm.addPrinter("1");
        cm.deletePrinterByID(1);
        cm.UpdateLivness(1);
        String s = cm.getPrinterByID(1);
        assertTrue(s.contains("{\"liveness\":true"));
    }

    @Test
    void addPrintingJobAndReturnIdValidPrinter() {
        cm.addPrinter("1");
        cm.AddPrintingJobAndReturnId("{data:dio}",1);
        int jobId = cm.AddPrintingJobAndReturnId("{data:jojo}",1);
        String s = cm.getPrinterByIDAndAllJobs(1);
        assertTrue(jobId == jobId && s.contains("jojo"));
    }
    @Test
    void addPrintingJobAndReturnIdInvalidPrinter() {
        cm.addPrinter("1");
        int jobId = cm.AddPrintingJobAndReturnId("{data:jojo}",2);
        String s = cm.getPrinterByIDAndAllJobs(1);
        assertTrue(jobId == -1);
    }

    @Test
    void addPrintingJobAndReturnIdLongString() {
        cm.addPrinter("1");
        String val = "sara shara shir sameach";
        int jobId = cm.AddPrintingJobAndReturnId("{data:\""+val+"\"}",1);
        String s = cm.getPrinterByIDAndAllJobs(1);
        assertTrue(s.contains("\",\"id\":"+jobId) && s.contains(val));
    }
    @Test
    void updateJobStatusFalseToTrue() {
        cm.addPrinter("1");
        int jobId = cm.AddPrintingJobAndReturnId("{data:GOAT}",1);
        cm.updateJobStatus(0);
        String s = cm.GetJobDetails(0);
        assertTrue(s.contains("\"status\":true"));
    }
    @Test
    void updateJobStatusTrueToFalse() {
        cm.addPrinter("1");
        int jobId = cm.AddPrintingJobAndReturnId("{data:Yakov}",1);
        cm.updateJobStatus(0);
        cm.updateJobStatus(0);
        String s = cm.GetJobDetails(jobId);
        assertTrue(s.contains("\"status\":false"));
    }

    @Test
    void getJobDetailsValid() {
        cm.addPrinter("17");
        int jobId = cm.AddPrintingJobAndReturnId("{data:\"Yakov Reznik the GOAT\"}",17);
        String s = cm.GetJobDetails(jobId);
        assertTrue(s.contains("\"id\":"+jobId+",\"timeForTheJob\":0.021,\"content\":\"Yakov Reznik the GOAT\",\"status\":false}"));
    }
    @Test
    void getJobDetailsWrongID() {
        cm.addPrinter("17");
        int jobId = cm.AddPrintingJobAndReturnId("{data:\"Yakov Reznik the GOAT\"}",17);
        String s = cm.GetJobDetails(2);
        assertEquals("{}",s);
    }

    @Test
    void getStatistics() {
        cm.addPrinter("2");
        cm.addPrinter("7");
        cm.deletePrinterByID(7);
        int job1 = cm.AddPrintingJobAndReturnId("{data:\"master of puppets\"}",2);
        int job2 = cm.AddPrintingJobAndReturnId("{data:\"black\"}",2);
        int job3 = cm.AddPrintingJobAndReturnId("{data:\"ride the lightning\"}",2);
        cm.updateJobStatus(job2);
        int job4 = cm.AddPrintingJobAndReturnId("{data:\"rust in peace\"}",7);
        int job5 = cm.AddPrintingJobAndReturnId("{data:\"symphony of destruction\"}",7);
        int job6 = cm.AddPrintingJobAndReturnId("{data:\"distopia\"}",7);
        cm.updateJobStatus(job4);
        cm.updateJobStatus(job5);
        int completeJobs =3;
        int incompleteJobs =3;
        int livePrinters =1;
        int notLivePrinters =1;
        String s = cm.GetStatistics();
        assertTrue(s.contains("{\"Incomplete Jobs\":"+incompleteJobs+",\"notLivePrinters\":"+notLivePrinters+",\"Complete Jobs\":"+completeJobs+",\"livePrinters\":"+livePrinters+""));
    }

    @Test
    void getStatisticsEmpty() {

        int completeJobs =0;
        int incompleteJobs =0;
        int livePrinters =0;
        int notLivePrinters =0;
        String s = cm.GetStatistics();
        assertTrue(s.contains("{\"Incomplete Jobs\":"+incompleteJobs+",\"notLivePrinters\":"+notLivePrinters+",\"Complete Jobs\":"+completeJobs+",\"livePrinters\":"+livePrinters+""));
    }
    @Test
    void getPrinterJobsFilteredNoJobsWithGivenStatus() {
        cm.addPrinter("1");
        int jobId = cm.AddPrintingJobAndReturnId("{data:\"mid or feed\"}", 1);
        cm.updateJobStatus(jobId);
        String b = "false";
        String s = cm.GetPrinterJobsFiltered(1,b);
        assertEquals("[]", s);
    }
    @Test
    void getPrinterJobsFilteredJobsWithGivenStatus() {
        cm.addPrinter("1");
        String val = "top or chop";
        int jobId = cm.AddPrintingJobAndReturnId("{data:\""+val+"\"}", 1);
        cm.updateJobStatus(jobId);
        String b = "true";
        String s = cm.GetPrinterJobsFiltered(1,b);
        assertTrue(s.contains("\"id\":"+jobId+",\"timeForTheJob\":0.011,\"content\":\""+val+"\",\"status\":"+b+"}]"));
    }
    @Test
    void getPrinterJobsFilteredJobsNoStatus() {
        cm.addPrinter("1");
        String val1 = "top or chop";
        int jobId1 = cm.AddPrintingJobAndReturnId("{data:\""+val1+"\"}", 1);
        cm.updateJobStatus(jobId1);
        String val2 = "mid or feed";
        int jobId2 = cm.AddPrintingJobAndReturnId("{data:\""+val2+"\"}", 1);
        String b = null;
        String s = cm.GetPrinterJobsFiltered(1,b);
        assertTrue(s.contains("\"id\":"+jobId1+",\"timeForTheJob\":0.011,\"content\":\""+val1+"")
        && s.contains("\"id\":"+jobId2+",\"timeForTheJob\":0.011,\"content\":\""+val2+""));
    }

    @Test
    void getPrinterJobsFilteredSuper() {
    }


}