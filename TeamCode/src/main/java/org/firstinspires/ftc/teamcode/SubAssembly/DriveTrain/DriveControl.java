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
    private DcMotor right;
    private DcMotor left;

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

        right = hardwareMap.dcMotor.get("leftWheel");
        left = hardwareMap.dcMotor.get("rightWheel");
        right.setDirection(DcMotor.Direction.REVERSE);

        right.setPower(0);
        left.setPower(0);

    }

    //setting power to move forward
    public void moveForward(double speed) {
        right.setPower(1);
        left.setPower(1);
    }

    //setting power to move backward
    public void moveBackward(double speed) {
        right.setPower(-1);
        left.setPower(-1);
    }

    //setting power to turn left
    public void turnLeft(double speed) {
        right.setPower(1);
        left.setPower(-1);
    }

    //setting power to turn right
    public void turnRight(double speed) {
        right.setPower(-1);
        left.setPower(1);
    }

    //setting power to 0
    public void stop (double speed) {
        right.setPower(0);
        left.setPower(0);
    }

    /* Subassembly methods */
    public void test() {
        telemetry.addLine(name + " test");
        telemetry.addData("Battery level = ", "%.2f V", Battery.getVoltage());
    }
}