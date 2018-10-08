package org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/* Sub Assembly Class
 */
public class DriveControl {
    /* Declare private class object */
    private Telemetry telemetry = null;         /* local copy of telemetry object from opmode class */
    private HardwareMap hardwareMap = null;     /* local copy of HardwareMap object from opmode class */
    private String name = "Drive Train";

    //initializing motors
    private DcMotor rightFront;
    private DcMotor leftFront;
    private DcMotor rightBack;
    private DcMotor leftBack;

    /* Declare public class object */
    public VoltageSensor Battery = null;

    /* Subassembly constructor */
    public DriveControl(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        telemetry.addLine(name + " initialize");

        /* Map hardware devices */
        Battery = hardwareMap.voltageSensor.get("Expansion Hub 2");

        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

    }

    //setting power to move forward
    public void moveForward(double speed) {
        rightFront.setPower(1);
        leftFront.setPower(1);
        rightBack.setPower(1);
        leftBack.setPower(1);
    }

    //setting power to move backward
    public void moveBackward(double speed) {
        rightFront.setPower(-1);
        leftFront.setPower(-1);
        rightBack.setPower(-1);
        leftBack.setPower(-1);
    }

    //setting power to turn left
    public void turnLeft(double speed) {
        rightFront.setPower(-1);
        leftFront.setPower(1);
        rightBack.setPower(-1);
        leftBack.setPower(1);
    }

    //setting power to turn right
    public void turnRight(double speed) {
        rightFront.setPower(1);
        leftFront.setPower(-1);
        rightBack.setPower(1);
        leftBack.setPower(-1);
    }

    //setting power to 0
    public void stop (double speed) {
        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);
    }

    /* Subassembly methods */
    public void test() {
        telemetry.addLine(name + " test");
        telemetry.addData("Battery level = ", "%.2f V", Battery.getVoltage());
    }
}