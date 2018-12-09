package org.firstinspires.ftc.teamcode.framework.userHardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.Logger;
import org.upacreekrobotics.dashboard.Dashboard.dashboardtelemetry;

public class DoubleTelemetry {

    private LogMode loggingMode = LogMode.INFO;
    private LogMode defaultLogMode = LogMode.TRACE;

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

    public void setDefaultLogMode(LogMode mode) {
        defaultLogMode = mode;
    }

    public void addData(Object caption, Object data) {
        addData(defaultLogMode, caption, data);
    }

    public void addData(Object data) {
        addData(defaultLogMode, data);
    }

    public void addDataDB(Object data) {
        addDataDB(defaultLogMode, data);
    }

    public void addDataPhone(Object data) {
        addDataPhone(defaultLogMode, data);
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
            telemetry.addLine(String.valueOf(data));
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
            telemetry.addLine(String.valueOf(data));
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

    public void stop(){
        logger.stop();
    }

    public enum LogMode {
        TRACE(0),
        INFO(1),
        DEBUG(2),
        ERROR(3);

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
