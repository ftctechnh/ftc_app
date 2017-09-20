package org.firstinspires.ftc.teamcode.VelocityVortex;

/**
 * Created by spmce on 11/3/2016.
 */
public class VelocityVortexTelemetry extends VelocityVortexHardware {
    /**
     * Init
     */
    public void initTele() {
        telemetry.addData("1","Initializing");
    }
    /**
     * Init Loop
     */
    public void initLoopTele() {
        warningTele();
        data();
    }
    public void startTele() {

    }
    public void loopTele() {
        warningTele();
        data();
    }
    public void stopTele() {

    }
    public void data() {
        // Shows telemetry on Driver Station Phone
        // ADD TELEMETRY DATA HERE:
        // DcMotors - Telemetry
        telemetry.addData("Front Left", leftDrivePower);
        telemetry.addData("Front Right",rightDrivePower);
        telemetry.addData("Back Right", backRightPower);
        telemetry.addData("Back Left", backLeftPower);
        telemetry.addData("Sweeper", sweeperPower);
        // Servos - Telemetry

        // Sensors - Telemetry

    }
    public void gamepadTele() {
        String game1 = gamepad1.toString();
        String game2 = gamepad2.toString();
    }
    public void warningTele() {
        Warning warning = new Warning();
        if (warning.getWarningGenerated())
            setFirstMessage(warning.getWarningMessage());
        if (warning.getDriveWarningGenerated())
            setDriveFirstMessage(warning.getDriveWarningMessage());
        if (warning.getMotorWarningGenerated())
            setMotorFirstMessage(warning.getMotorWarningMessage());
        if (warning.getServoWarningGenerated())
            setServoFirstMessage(warning.getServoWarningMessage());
        if (warning.getSensorWarningGenerated())
            setSensorFirstMessage(warning.getSensorWarningMessage());
    }
    /**
     * Update the telemetry's first message with the specified message.
     */
    public void setFirstMessage      (String pMessage) {telemetry.addData("000", pMessage);}
    public void setDriveFirstMessage (String pMessage) {telemetry.addData("001", pMessage);}
    public void setMotorFirstMessage (String pMessage) {telemetry.addData("002", pMessage);}
    public void setServoFirstMessage (String pMessage) {telemetry.addData("003", pMessage);}
    public void setSensorFirstMessage(String pMessage) {telemetry.addData("004", pMessage);}
    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void setErrorMessage (String pMessage) {setFirstMessage("ERROR: " + pMessage);}
}
