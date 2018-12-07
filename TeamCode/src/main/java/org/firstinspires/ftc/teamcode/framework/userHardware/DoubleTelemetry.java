package org.firstinspires.ftc.teamcode.framework.userHardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.Logger;
import org.upacreekrobotics.dashboard.Dashboard.dashboardtelemetry;

public class DoubleTelemetry {

    private LogMode loggingMode = LogMode.INFO;

    private Telemetry telemetry;
    private dashboardtelemetry dashtelem;
    private Logger logger;

    public DoubleTelemetry(Telemetry telemetry, dashboardtelemetry dashtelem, Logger logger) {
        this.telemetry = telemetry;
        this.dashtelem = dashtelem;
        this.logger = logger;
    }

    public void setLogMode(LogMode mode) {
        this.loggingMode = mode;
    }

    public void addData(Object caption, Object data) {
        addData(LogMode.INFO, caption, data);
    }

    public void addData(Object data) {
        addData(LogMode.INFO, data);
    }

    public void addDataDB(Object data) {
        addDataDB(LogMode.INFO, data);
    }

    public void addDataPhone(Object data) {
        addDataPhone(LogMode.INFO, data);
    }

    public void addData(LogMode mode, Object caption, Object data) {
        if (loggingMode.shouldLog(mode)) {
            telemetry.addData(String.valueOf(caption), String.valueOf(data));
            String message = String.valueOf(caption) + ": " + String.valueOf(data);
            dashtelem.write(message);
            dashtelem.info(message);
        }
    }

    public void addData(LogMode mode, Object data) {
        if (loggingMode.shouldLog(mode)) {
            telemetry.addData("", String.valueOf(data));
            dashtelem.write(String.valueOf(data));
            dashtelem.info(String.valueOf(data));
        }
    }

    public void addDataDB(LogMode mode, Object data) {
        if (loggingMode.shouldLog(mode)) {
            dashtelem.write(String.valueOf(data));
        }
    }

    public void addDataPhone(LogMode mode, Object data) {
        if (loggingMode.shouldLog(mode)) {
            telemetry.addData("", String.valueOf(data));
            dashtelem.info(String.valueOf(data));
        }
    }

    public void update() {
        try {
            telemetry.update();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        dashtelem.updateInfo();
    }

    public void log(Object data) {
        logger.log(String.valueOf(data));
    }

    public enum LogMode {
        INFO(0),
        DEBUG(1),
        ERROR(2);

        private int level;

        LogMode(int level) {
            this.level = level;
        }

        private boolean shouldLog(LogMode mode) {
            return (mode.getValue() >= this.getValue());
        }

        private int getValue() {
            return level;
        }
    }
}
