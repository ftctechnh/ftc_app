package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FTC_Team_0267 on 10/23/2017.
 * By: Chase Hunt
 */
@Autonomous(name = "0267AUTONOMOUS",group = "Pushbot")
public class autonomous2017 extends LinearOpMode {
    private String startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());;
    private ElapsedTime runtime = new ElapsedTime();
    Hardware267Bot robot = new Hardware267Bot();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        //while (!opModeIsActive())  J.C.D.- I don't think this will work right
        //{
        //    sleep(1000);
        //}
        /*
        robot.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armMotor.setTargetPosition(80);
        robot.armMotor.setPower(1);
        while (robot.armMotor.getCurrentPosition() < 80) {}
        robot.armMotor.setPower(0);
        robot.armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        sleep(10000);
        robot.leftMotor.setPower(-0.5);
        robot.rightMotor.setPower(-0.5);
        sleep(3300);
        robot.leftMotor.setPower(0.5);
        robot.rightMotor.setPower(-0.5);
        sleep(700);
        robot.leftMotor.setPower(-0.5);
        robot.rightMotor.setPower(-0.5);
        sleep(3000);
        robot.leftMotor.setPower(-0);
        robot.rightMotor.setPower(-0);
        */
        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Step 1:  Drive forward for 3 seconds
        robot.leftMotor.setPower(0.8);
        robot.rightMotor.setPower(0.8);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Moving forward off of balancing stone.");
            telemetry.update();
        }

        // Step 2:  Spin right for 1.3 seconds
        robot.leftMotor.setPower(0.5);
        robot.rightMotor.setPower(-0.5);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Turning Left");
            telemetry.update();
        }

        // Step 3:  Drive Backwards for 1 Second
        robot.leftMotor.setPower(0.8);
        robot.rightMotor.setPower(0.8);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.0)) {
            telemetry.addData("Path", "Going forward");
            telemetry.update();
        }

        // Step 4:  Stop.
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        //robot.leftClaw.setPosition(1.0);
        //robot.rightClaw.setPosition(0.0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
