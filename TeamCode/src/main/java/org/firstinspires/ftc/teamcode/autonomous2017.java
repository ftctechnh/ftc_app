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
@Autonomous(name = "lazy forwards",group = "Pushbot")
public class autonomous2017 extends LinearOpMode {
    private String startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());;
    private ElapsedTime runtime = new ElapsedTime();
    Hardware267Bot robot = new Hardware267Bot();

    @Override
    public void runOpMode() throws InterruptedException {
        runtime.reset();
        robot.init(hardwareMap);
        while (!opModeIsActive())
        {
            sleep(1000);
        }
        /*
        robot.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armMotor.setTargetPosition(80);
        robot.armMotor.setPower(1);
        while (robot.armMotor.getCurrentPosition() < 80) {}
        robot.armMotor.setPower(0);
        robot.armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        */
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
    }
}
