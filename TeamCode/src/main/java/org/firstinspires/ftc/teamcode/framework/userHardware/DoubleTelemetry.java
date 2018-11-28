package org.firstinspires.ftc.teamcode.framework.userHardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.upacreekrobotics.dashboard.Dashboard.dashboardtelemetry;

public class DoubleTelemetry {

    private Telemetry telemetry;
    private dashboardtelemetry dashtelem;

    public DoubleTelemetry(Telemetry telemetry, dashboardtelemetry dashtelem) {
        this.telemetry = telemetry;
        this.dashtelem = dashtelem;
    }

    public void addData(String caption, String data) {
        telemetry.addData(caption, data);
        String message = caption + ": " + data;
        dashtelem.write(message);
        dashtelem.info(message);
    }

    public void addData(String caption, double data) {
        addData(caption, Double.toString(data));
    }

    public void addData(String caption, int data) {
        addData(caption, Integer.toString(data));
    }

    public void addData(String data) {
        telemetry.addData("", data);
        dashtelem.write(data);
        dashtelem.info(data);
    }

    public void addData(int data) {
        telemetry.addData("", data);
        dashtelem.write(data);
        dashtelem.info(data);
    }

    public void addData(double data) {
        telemetry.addData("", data);
        dashtelem.write(data);
        dashtelem.info(data);
    }

    public void addDataDS(String data) {
        telemetry.addData("", data);
        dashtelem.info(data);
    }

    public void addDataDS(int data) {
        telemetry.addData("", data);
        dashtelem.info(data);
    }

    public void addDataDS(double data) {
        telemetry.addData("", data);
        dashtelem.info(data);
    }

    public void addDataDB(String data) {
        dashtelem.write(data);
    }

    public void addDataDB(int data) {
        dashtelem.write(data);
    }

    public void addDataDB(double data) {
        dashtelem.write(data);
    }

    public void addDataPhone(String data) {
        telemetry.addData("", data);
    }

    public void addDataPhone(int data) {
        telemetry.addData("", data);
    }

    public void addDataPhone(double data) {
        telemetry.addData("", data);
    }

    public void update() {
        try {
            telemetry.update();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        dashtelem.updateInfo();
    }
}
