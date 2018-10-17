package org.firstinspires.ftc.teamcode.SubAssembly.Lift;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/* Sub Assembly Class
 */
public class LiftControl {
    /* Declare private class object */
    private Telemetry telemetry = null;         /* local copy of telemetry object from opmode class */
    private HardwareMap hardwareMap = null;     /* local copy of HardwareMap object from opmode class */
    private String name = "Lift";

    //initializing motors
    private DcMotor  LifterRightM;
    private DcMotor  LifterLeftM;
    private Servo LockRightS;
    private Servo LockLeftS;
    

    /* Subassembly constructor */
    public LiftControl(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        telemetry.addLine(name + " initialize");

        /* Map hardware devices */

        LifterRightM = hardwareMap.dcMotor.get("LifterRightM");
        LifterLeftM = hardwareMap.dcMotor.get("LifterLeftM");
        LockRightS = hardwareMap.servo.get("LockRightS");
        LockLeftS = hardwareMap.servo.get("LockLeftS");

        //reverses some motors
        LifterLeftM.setDirection(DcMotor.Direction.REVERSE);

        LifterRightM.setPower(0);
        LifterLeftM.setPower(0);


    }

    //setting power to move forward
    public void goUp () {



    }


    //setting power to 0
    public void goDown () {



    }



}
