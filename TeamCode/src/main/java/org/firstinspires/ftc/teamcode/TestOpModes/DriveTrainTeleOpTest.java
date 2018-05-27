package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

/**
 * Created by FTC 4316 on 10/29/2017
 */

public class DriveTrainTeleOpTest extends OpMode {

    private DriveTrain driveTrain;

    private double x;
    private double y;
    private double z;

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;

    private double power;
    private double finches;

    private boolean isFinished;

    NavxMicroNavigationSensor gyroReference;
    IntegratingGyroscope gyro;

    @Override
    public void init() {
        
        leftFront = hardwareMap.dcMotor.get("leftfront");
        rightFront = hardwareMap.dcMotor.get("rightfront");
        leftRear = hardwareMap.dcMotor.get("leftrear");
        rightRear = hardwareMap.dcMotor.get("rightrear");

        power = 0.1;
        finches = 5;

        gyroReference = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        gyro = gyroReference;

        driveTrain = new DriveTrain(leftFront, rightFront, leftRear, rightRear, gyro,  telemetry);

        isFinished = true;
    }

    @Override
    public void loop() {

            if (isFinished) {
                x = gamepad1.left_stick_x;
                y = -gamepad1.left_stick_y;
                z = gamepad1.right_stick_x;

                driveTrain.setMotorPower(x, y, z);
            }

            telemetry.addData("Current Yaw", driveTrain.getYaw());
            driveTrain.displayEncoders();

        }
    }
