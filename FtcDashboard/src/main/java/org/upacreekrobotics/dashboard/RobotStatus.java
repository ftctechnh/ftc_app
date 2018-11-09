package org.upacreekrobotics.dashboard;

import com.qualcomm.robotcore.util.RobotLog;

public class RobotStatus {
    public enum OpModeStatus {
        INIT,
        RUNNING,
        STOPPED
    }

    private boolean available;
    private String activeOpMode;
    private OpModeStatus activeOpModeStatus;
    private String warningMessage, errorMessage;

    public RobotStatus() {
        this.available = false;
        this.activeOpMode = "None";
        this.activeOpModeStatus = OpModeStatus.STOPPED;
        this.warningMessage = "";
        this.errorMessage = "";
    }

    public RobotStatus(String activeOpMode, OpModeStatus activeOpModeStatus) {
        this.available = true;
        this.activeOpMode = activeOpMode;
        this.activeOpModeStatus = activeOpModeStatus;
        this.warningMessage = RobotLog.getGlobalWarningMessage();
        this.errorMessage = RobotLog.getGlobalErrorMsg();
    }

    public String getActiveOpMode() {
        return activeOpMode;
    }
}
