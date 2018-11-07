package org.firstinspires.ftc.teamcode.SubAssembly.Claimer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/* Sub Assembly Class
 */
public class ClaimerControl {
    /* Declare private class object */
    //private Telemetry telemetry;         /* local copy of telemetry object from opmode class */
    HardwareMap hwMap;     /* local copy of HardwareMap object from opmode class */

    //initializing motors
    private Servo ClaimerS;

      /* Subassembly constructor */
    public ClaimerControl(){
    }

    public void init(HardwareMap ahwMap) {
        /* Set local copies from opmode class */
        hwMap = ahwMap;


        /* Map hardware devices */

        ClaimerS = hwMap.servo.get("ClaimerS");

        ClaimerS.setPosition(1);


    }


    public void drop() {
        ClaimerS.setPosition(1.0);


    }


    public void reset() {
        ClaimerS.setPosition(0.55);

    }


    }

