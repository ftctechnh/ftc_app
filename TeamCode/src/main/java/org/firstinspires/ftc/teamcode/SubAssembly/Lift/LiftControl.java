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
    private Telemetry telemetry;         /* local copy of telemetry object from opmode class */
    private HardwareMap hardwareMap;     /* local copy of HardwareMap object from opmode class */
    private String name = "Lift";

    //initializing motors, servos, and sensors
    private DcMotor LifterRightM;
    private DcMotor LifterLeftM;
    private Servo LockRightS;
    private Servo LockLeftS;
    private TouchSensor LifterButtonT;
    private TouchSensor LifterButtonB;


    /* Subassembly constructor */
    public LiftControl(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        telemetry.addLine(name + " initialize");

        // Map hardware devices
        LifterRightM = hardwareMap.dcMotor.get("LifterRightM");
        LifterLeftM = hardwareMap.dcMotor.get("LifterLeftM");
        LockRightS = hardwareMap.servo.get("LockRightS");
        LockLeftS = hardwareMap.servo.get("LockLeftS");

        //reverses some motors
        LifterLeftM.setDirection(DcMotor.Direction.REVERSE);

        LifterRightM.setPower(0);
        LifterLeftM.setPower(0);


    }

    public LiftControl() {
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
        LockRightS.setPosition(0);
        LockLeftS.setPosition(0);
    }

    //unlocking the servos
    public void Unlock() {
        LockLeftS.setPosition(180);
        LockRightS.setPosition(180);
    }
}