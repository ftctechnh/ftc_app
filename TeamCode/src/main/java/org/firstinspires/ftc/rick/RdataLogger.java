package org.firstinspires.ftc.rick;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class RdataLogger {

    private File logFile;

    public RdataLogger() {

    }

    public RdataLogger(String fileName) {
        String directoryPath    = "/sdcard/FIRST/RDataLog/";
        String filePath         = directoryPath + fileName ;

       new File(directoryPath).mkdir();        // Make sure that the directory exists
        logFile = new File(filePath);
    }

    public RdataLogger(File f) {
        logFile =  f ;
    }

//    }

    public void log(String s) {

        try {
            FileWriter fw = new FileWriter(this.logFile,true);
            String date = new Date().toString();
            //Format formatter = new SimpleDateFormat("HH:mm:ss" );
            String timenow = date.substring(11,19);
            fw.write(timenow +" "+s);
            fw.write(System.lineSeparator());
            fw.close();


        } catch (IOException ex) {
            System.err.println("Couldn't log this: "+s);
        }

    }

}
