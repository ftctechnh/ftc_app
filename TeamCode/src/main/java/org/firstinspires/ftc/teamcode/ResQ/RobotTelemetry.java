/**
 * Created by spmce on 2/9/2016.
 */
package org.firstinspires.ftc.teamcode.ResQ;

public class RobotTelemetry extends RobotHardware {

    public RobotTelemetry() {
        warningGenerated = false;
        warningMessage = "Can't map: ";
    }

    public void init() {
        super.init();
        telemetry.addData("1", "Init Hook Position: " + getPosition(hook));
        telemetry.addData("2", "Init Spinner Position: " + getPosition(spinner));
        telemetry.addData("3" , "Init Left Drive Power: " + getPower(left) + " , " + leftDrive);
        telemetry.addData("4" , "Init Right Drive Power: " + getPower(right)+ " , " + rightDrive);
        telemetry.addData("5" , "Init Back Left Drive Power: " + getPower(backLeft) + " , " + backLeftDrive);
        telemetry.addData("6" , "Init Back Right Drive Power: " + getPower(backRight) + " , " + backRightDrive);
    }
    public void loop() {
        super.init();
        init();
    }

    /**
     * Update the telemetry's first message with the specified message.
     */
    public void setFirstMessage (String pMessage) {telemetry.addData("00", pMessage);}
    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void setErrorMessage (String pMessage) {setFirstMessage("ERROR: " + pMessage);}
}
