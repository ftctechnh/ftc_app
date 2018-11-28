package org.firstinspires.ftc.teamcode.framework.userHardware.inputs;

import java.text.SimpleDateFormat;

public class Date {
    private SimpleDateFormat dtf;

    public Date() {
        dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public String getDate() {
        return dtf.format(new java.util.Date());
    }
}
