package org.firstinspires.ftc.teamcode.SubAssembly.Lift;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/* Sub Assembly Class
 */
public class LiftControl {
    /* Declare private class object */
    //private Telemetry telemetry;         /* local copy of telemetry object from opmode class */
    HardwareMap hwMap;     /* local copy of HardwareMap object from opmode class */


    //initializing motors, servos, and sensors
    private DcMotor LifterRightM;
    private DcMotor LifterLeftM;
    private Servo LockRightS;
    private Servo LockLeftS;
    public TouchSensor LifterButtonT;
    public TouchSensor LifterButtonB;

    /* Subassembly constructor */
    public LiftControl() {
    }

    public void init(HardwareMap ahwMap) {
        /* Set local copies from opmode class */
        hwMap = ahwMap;


        /* Map hardware devices */

        LifterRightM = hwMap.dcMotor.get("LifterRightM");
        LifterLeftM = hwMap.dcMotor.get("LifterLeftM");
        LockRightS = hwMap.servo.get("LockRightS");
        LockLeftS = hwMap.servo.get("LockLeftS");
        LifterButtonB = hwMap.touchSensor.get("LifterButtonB");
        LifterButtonT = hwMap.touchSensor.get("LifterButtonT");

        LockRightS.setPosition(0.77);
        LockLeftS.setPosition(0.1);
        LifterRightM.setPower(0);
        LifterLeftM.setPower(0);
        LifterRightM.setDirection(DcMotor.Direction.REVERSE);
    }

    public void Extend() {
        LifterLeftM.setPower(0.50);
        LifterRightM.setPower(0.50);
    }

    public void Retract() {
        LifterLeftM.setPower(-0.50);
        LifterRightM.setPower(-0.50);
    }

    public void Stop() {
        LifterLeftM.setPower(0);
        LifterRightM.setPower(0);
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