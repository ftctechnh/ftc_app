package org.firstinspires.ftc.teamcode.Scotts_Things;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Scotts_Things.HardwareFile2019.*;

@Autonomous(name = "AutoTestEncoders2019")
@Disabled
public class AutoPushbotEncoder extends LinearOpMode {

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 3.75;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    int targetDistance;

    private ElapsedTime runtime = new ElapsedTime();

    HardwareFile2019 robot = new HardwareFile2019();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.mapHardware(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

            encoderDrive(1.00, 10.00, 2.00);


        }

    }

    public void encoderDrive(Double power, Double distance, Double wait) {

        targetDistance = frontLeft.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);
        frontLeft.setTargetPosition(targetDistance);
        frontRight.setTargetPosition(targetDistance);
        backLeft.setTargetPosition(targetDistance);
        backRight.setTargetPosition(targetDistance);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();

        frontLeft.setPower(Math.abs(power));
        frontRight.setPower(Math.abs(power));
        backLeft.setPower(Math.abs(power));
        backRight.setPower(Math.abs(power));

        while (opModeIsActive() && (runtime.seconds() < wait) &&
                ((frontLeft.isBusy() && frontRight.isBusy()) && (backRight.isBusy() && backRight.isBusy()))) {

        }

        robot.wheelStop();

        robot.runWithEncoders();

    }
}
