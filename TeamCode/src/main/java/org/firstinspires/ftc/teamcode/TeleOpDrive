package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "Tel", group = "Sensor")
public class TeleOpDrive extends LinearOpMode{

    HardwarePushBot robot = new HardwarePushBot();


    @Override
    public void runOpMode() throws InterruptedException {

        // Intialize the robot's hardware from HardwareMap amd allows you to run all this code within TeleOp
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Hello Driver", "Press Play Button");
        telemetry.update();



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Drive variables
            double rightPower = -gamepad1.right_stick_y * 0.8;
            double leftPower = -gamepad1.left_stick_y * 0.8;
            double reverse_Thres = -0.3;
            double forward_Thres = 0.3;
            double drive_Speed = 0.7;
            double turn_Speed = 0.5;


            // Send calculated power to wheels
            // Drive system
            if (rightPower < forward_Thres && rightPower > reverse_Thres && leftPower < forward_Thres && leftPower > reverse_Thres) {
                DriveMethods.stopRightMotors();
                DriveMethods.stopLeftMotors();
            } else if (rightPower != 0.0 && leftPower != 0.0) {
                DriveMethods.driveRight(rightPower * drive_Speed);
                DriveMethods.driveLeft(leftPower * drive_Speed);
            } else if (rightPower != 0.0 && leftPower == 0.0) {
                DriveMethods.driveRight(rightPower * drive_Speed);
            } else if (leftPower != 0.0 && rightPower == 0.0) {
                DriveMethods.driveLeft(leftPower * drive_Speed);
            } else if (rightPower < reverse_Thres && leftPower > forward_Thres) {
                DriveMethods.driveLeft(leftPower * turn_Speed);
                DriveMethods.driveRight(rightPower * turn_Speed);
            } else if (rightPower > forward_Thres && leftPower < reverse_Thres) {
                DriveMethods.driveLeft(leftPower * turn_Speed);
                DriveMethods.driveRight(rightPower * turn_Speed);
            }

        }



    }
}
