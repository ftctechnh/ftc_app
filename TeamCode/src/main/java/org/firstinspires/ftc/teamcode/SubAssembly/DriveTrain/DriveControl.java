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
    private DcMotor FrontRightM;
    private DcMotor  FrontLeftM;
    private DcMotor BackRightM;
    private DcMotor BackLeftM;

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

        FrontRightM = hardwareMap.dcMotor.get("FrontRightM");
        FrontLeftM = hardwareMap.dcMotor.get("FrontLeftM");
        BackRightM = hardwareMap.dcMotor.get("BackRightM");
        BackLeftM = hardwareMap.dcMotor.get(" BackLeftM");
        BackLeftM.setDirection(DcMotor.Direction.REVERSE);

        FrontRightM.setPower(0);
        FrontLeftM.setPower(0);
        BackRightM.setPower(0);
        BackLeftM.setPower(0);

    }

    //setting power to move forward
    public void moveForward(double speed) {
        FrontRightM.setPower(1);
        FrontLeftM.setPower(1);
        BackRightM.setPower(1);
        BackLeftM.setPower(1);
    }

    //setting power to move backward
    public void moveBackward(double speed) {
        FrontRightM.setPower(-1);
        FrontLeftM.setPower(-1);
        BackRightM.setPower(-1);
        BackLeftM.setPower(-1);
    }

    //setting power to turn left
    public void turnLeft(double speed) {
        FrontRightM.setPower(-1);
        FrontLeftM.setPower(1);
        BackRightM.setPower(-1);
        BackLeftM.setPower(1);
    }

    //setting power to turn right
    public void turnRight(double speed) {
        FrontRightM.setPower(1);
        FrontLeftM.setPower(-1);
        BackRightM.setPower(1);
        BackLeftM.setPower(-1);
    }

    //setting power to 0
    public void stop (double speed) {
        FrontRightM.setPower(0);
        FrontLeftM.setPower(0);
        BackRightM.setPower(0);
        BackLeftM.setPower(0);
    }



}
