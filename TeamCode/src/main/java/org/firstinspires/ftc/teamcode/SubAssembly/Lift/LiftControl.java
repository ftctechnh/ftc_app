package org.firstinspires.ftc.teamcode.SubAssembly.Lift;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/* Sub Assembly Class
 */
public class LiftControl {
    /* Declare private class object */
    //private Telemetry telemetry;         /* local copy of telemetry object from opmode class */
    HardwareMap hwMap = null;     /* local copy of HardwareMap object from opmode class */
    private String name = "Lift";

    //initializing motors, servos, and sensors
    private DcMotor LifterRightM = null;
    private DcMotor LifterLeftM = null;
    private Servo LockRightS;
    private Servo LockLeftS;
    private TouchSensor LifterButtonT = null;
    private TouchSensor LifterButtonB = null;


    public LiftControl() {
    }

    /* Subassembly constructor */
    public void init(HardwareMap ahwMap) {
        /* Set local copies from opmode class */
        hwMap = ahwMap;
        // Map hardware devices
        LifterRightM = hwMap.dcMotor.get("LifterRightM");
        LifterLeftM = hwMap.dcMotor.get("LifterLeftM");
        LockRightS = hwMap.servo.get("LockRightS");
        LockLeftS = hwMap.servo.get("LockLeftS");

        //reverses some motors
        LifterLeftM.setDirection(DcMotor.Direction.REVERSE);

        LifterRightM.setPower(0);
        LifterLeftM.setPower(0);


    }

    //setting power to lower the robot or reach the lift up
    public void Extend() {
        while (!LifterButtonT.isPressed()) {
            LifterLeftM.setPower(0.35);
            LifterRightM.setPower(0.35);
        }

    }

    //setting power to raise the robot or pull the lift back in
    public void Retract() {
        while (!LifterButtonB.isPressed()) {
            LifterLeftM.setPower(-0.35);
            LifterRightM.setPower(-0.35);
        }
    }

    //locking the servos into place to hold position
    public void Lock() {
        LockRightS.setPosition(0.44);
        LockLeftS.setPosition(0.4);
    }

    //unlocking the servos
    public void Unlock() {
        LockRightS.setPosition(0.77);
        LockLeftS.setPosition(0.1);

    }
}