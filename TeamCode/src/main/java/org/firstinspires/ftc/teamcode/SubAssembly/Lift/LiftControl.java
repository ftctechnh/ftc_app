package org.firstinspires.ftc.teamcode.SubAssembly.Lift;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/* Sub Assembly Class
 */
public class LiftControl {
    /* Constants */
    final double LIFT_SPEED = 1.0;

    /* Declare private class object */
    private LinearOpMode opmode = null;     /* local copy of opmode class */

    private DcMotor LifterRightM;
    private DcMotor LifterLeftM;
    private Servo LockRightS;
    private Servo LockLeftS;
    private boolean locked;

    /* Declare public class object */
    public TouchSensor LifterButtonT;
    public TouchSensor LifterButtonB;

    /* Subassembly constructor */
    public LiftControl() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        opMode.telemetry.addLine("Lift Control" + " initialize");
        opMode.telemetry.update();

        /* Set local copies from opmode class */
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        /* Map hardware devices */
        LifterRightM = hwMap.dcMotor.get("LifterRightM");
        LifterLeftM = hwMap.dcMotor.get("LifterLeftM");
        LockRightS = hwMap.servo.get("LockRightS");
        LockLeftS = hwMap.servo.get("LockLeftS");
        LifterButtonB = hwMap.touchSensor.get("LifterButtonB");
        LifterButtonT = hwMap.touchSensor.get("LifterButtonT");

        Unlock();
        LifterRightM.setPower(0);
        LifterLeftM.setPower(0);
        LifterRightM.setDirection(DcMotor.Direction.REVERSE);
    }

    public void Extend() {
        if (!locked) {
            LifterLeftM.setPower(LIFT_SPEED);
            LifterRightM.setPower(LIFT_SPEED);
        }
    }

    public void Retract() {
        if (!locked) {
            LifterLeftM.setPower(-LIFT_SPEED);
            LifterRightM.setPower(-LIFT_SPEED);
        }
    }

    public void Stop() {
        LifterLeftM.setPower(0);
        LifterRightM.setPower(0);
    }

    //locking the servos into place to hold position
    public void Lock() {
        LockRightS.setPosition(0.44);
        LockLeftS.setPosition(0.4);
        locked = true;
    }

    //unlocking the servos
    public void Unlock() {
        LockRightS.setPosition(0.77);
        LockLeftS.setPosition(0.1);
        locked = false;
    }
}