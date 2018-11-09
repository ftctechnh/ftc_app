package org.upacreekrobotics.dashboard;

import java.text.SimpleDateFormat;

public class Date {

    private SimpleDateFormat df;
    private SimpleDateFormat dtf;

    public Date(){
        df = new SimpleDateFormat("MM/dd/yyyy");
        dtf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
    }

    public String getDate(){
        return df.format(new java.util.Date());
    }

    public String getDateTime(){
        return dtf.format(new java.util.Date());
    }
}
