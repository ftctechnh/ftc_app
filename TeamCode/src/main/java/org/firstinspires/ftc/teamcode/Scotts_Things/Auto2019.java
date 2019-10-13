package org.firstinspires.ftc.teamcode.Scotts_Things;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static org.firstinspires.ftc.teamcode.Scotts_Things.HardwareFile2019.*;

@Autonomous(name = "AutoTestEncoders2019")
@Disabled
public class Auto2019 extends LinearOpMode {

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    int targetDistance;

    HardwareFile2019 robot = new HardwareFile2019();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.mapHardware(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {


        }

    }

    public void encoderDrive(Double power, Double distance, Double wait) {

        targetDistance = frontLeft.getCurrentPosition() + (int) (distance * COUNTS_PER_INCH);

    }
}
