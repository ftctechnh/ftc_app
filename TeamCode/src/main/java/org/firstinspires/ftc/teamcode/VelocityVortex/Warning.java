package org.firstinspires.ftc.teamcode.VelocityVortex;

import org.firstinspires.ftc.teamcode.VelocityVortex.VelocityVortexHardware;

/**
 * Created by spmce on 11/4/2016.
 */
public class Warning extends VelocityVortexHardware {
    //------------Telemetry Warnings------------
    //Determine if warning generated
    private boolean warningGenerated = false;
    private boolean motorWarningGenerated = false;
    private boolean driveWarningGenerated = false;
    private boolean servoWarningGenerated = false;
    private boolean sensorWarningGenerated = false;
    /**
     * Init
     */
    /*public void init(){
        super.init();
        //Initialize Warnings Generated and Warning Messages
        initWarnings(); //Provide telemetry data to a class user
    }*/
    //------------Init Warnings Method------------
    void initWarnings() {
        warningGenerated = false;
        super.setWarningMessage(warningMessage = "Can't map: ");
        motorWarningGenerated = false;
        super.setMotorWarningMessage(motorWarningMessage = "Motors: ; ");
        driveWarningGenerated = false;
        super.setDriveWarningMessage(driveWarningMessage = "DriveMotors: ");
        servoWarningGenerated = false;
        super.setSensorWarningMessage(servoWarningMessage = "Servos: ; ");
        sensorWarningGenerated = false;
        super.setServoWarningMessage(sensorWarningMessage = "Sensors: ; ");
    }
    //------------Warnings------------
    boolean getWarningGenerated      () {return warningGenerated;}
    boolean getMotorWarningGenerated () {return motorWarningGenerated;}
    boolean getDriveWarningGenerated () {return driveWarningGenerated;}
    boolean getServoWarningGenerated () {return servoWarningGenerated;}
    boolean getSensorWarningGenerated() {return sensorWarningGenerated;}

    //String getWarningMessage      () {return warningMessage;}
    //String getMotorWarningMessage () {return motorWarningMessage;}
    //String getDriveWarningMessage () {return driveWarningMessage;}
    //String getServoWarningMessage () {return servoWarningMessage;}
    //String getSensorWarningMessage() {return servoWarningMessage;}

    void setWarning (String opModeExceptionMessage) {
        if (warningGenerated)
            super.setWarningMessage(warningMessage += ", ");
        warningGenerated = true;
        super.setWarningMessage(warningMessage += opModeExceptionMessage);
    }
    void setMotorWarning (String opModeExceptionMessage) {
        if (motorWarningGenerated)
            super.setMotorWarningMessage(motorWarningMessage += ", ");
        warningGenerated = true;
        motorWarningGenerated = true;
        super.setMotorWarningMessage(motorWarningMessage += opModeExceptionMessage);
    }
    void setDriveWarning (String opModeExceptionMessage) {
        if (driveWarningGenerated)
            super.setDriveWarningMessage(driveWarningMessage += ", ");
        warningGenerated = true;
        driveWarningGenerated = true;
        super.setDriveWarningMessage(driveWarningMessage += opModeExceptionMessage);
    }
    void setServoWarning (String opModeExceptionMessage) {
        if (servoWarningGenerated)
            super.setServoWarningMessage(servoWarningMessage += ", ");
        warningGenerated = true;
        servoWarningGenerated = true;
        super.setServoWarningMessage(servoWarningMessage += opModeExceptionMessage);
    }
    void setSensorWarning (String opModeExceptionMessage) {
        if (sensorWarningGenerated)
            super.setSensorWarningMessage(sensorWarningMessage += ", ");
        warningGenerated = true;
        sensorWarningGenerated = true;
        super.setSensorWarningMessage(sensorWarningMessage += opModeExceptionMessage);
    }
}
