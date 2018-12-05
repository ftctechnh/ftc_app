package org.firstinspires.ftc.teamcode.SubAssembly.Claimer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/* Sub Assembly Class
 */
public class ClaimerControl {
    /* Declare private class object */
    private LinearOpMode opmode = null;     /* local copy of opmode class */

    //initializing motors
    private Servo ClaimerS;

    /* Subassembly constructor */
    public ClaimerControl() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        opMode.telemetry.addLine("Claimer Control" + " initialize");
        opMode.telemetry.update();

        /* Set local copies from opmode class */
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        /* Map hardware devices */
        ClaimerS = hwMap.servo.get("ClaimerS");

        ClaimerS.setPosition(0.075);
    }


    public void drop() {
        ClaimerS.setPosition(0.5);
    }


    public void reset() {
        ClaimerS.setPosition(0.075);
    }


}


