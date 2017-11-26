package org.firstinspires.ftc.teamcode.GMR.Robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.BlockLift;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by pston on 11/12/2017
 */

public class Robot {

    public DriveTrain driveTrain;
    public BlockLift blockLift;

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private NavxMicroNavigationSensor gyroReference;
    private IntegratingGyroscope gyro;

    private DcMotor liftMotor;
    private Servo topLeftGrab;
    private Servo topRightGrab;
    private Servo bottomLeftGrab;
    private Servo bottomRightGrab;

    private Servo rightColor;
    private Servo leftColor;


    public Robot (HardwareMap hardwareMap, Telemetry telemetry) {

        gyro = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        leftFront = hardwareMap.dcMotor.get("leftfront");
        rightFront = hardwareMap.dcMotor.get("rightfront");
        leftRear = hardwareMap.dcMotor.get("leftrear");
        rightRear = hardwareMap.dcMotor.get("rightrear");

        liftMotor = hardwareMap.dcMotor.get("liftmotor");
        topLeftGrab = hardwareMap.servo.get("topleftgrab");
        topRightGrab = hardwareMap.servo.get("toprightgrab");
        bottomLeftGrab = hardwareMap.servo.get("bottomleftgrab");
        bottomRightGrab = hardwareMap.servo.get("bottomrightgrab");

        rightColor = hardwareMap.servo.get("rightArm");
        leftColor = hardwareMap.servo.get("leftArm");

        rightColor.setPosition(0);
        leftColor.setPosition(.85);

        driveTrain = new DriveTrain(leftFront, rightFront, leftRear, rightRear, gyro, telemetry);

        blockLift = new BlockLift(liftMotor, topLeftGrab, topRightGrab, bottomLeftGrab, bottomRightGrab);
    }

}
