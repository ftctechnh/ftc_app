package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

/**
 * Created by 292486 on 9/20/2016.
 */

@TeleOp(name = "Holonomic Drive", group = "Teleop Drive")
public class HolonomicDrive extends OpMode {

    HolonomicRobot robot = new HolonomicRobot();
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    private double y, x, l, r;
    //////////////////////////
    @Override
    public void init() {
        robot.init(hardwareMap);    //Initialize all the robot components
    }

    @Override
    public void init_loop(){

    }
    //////////////////////////
    @Override
    public void start(){
        timer.reset();
    }

    @Override
    public void loop() {
        y = -gamepad1.left_stick_y; //For some reason, the left y-axis stick is negative when pushed forward
        x = gamepad1.right_stick_x;
        l = gamepad1.left_trigger;
        r = gamepad1.right_trigger;

        if(gamepad1.x)
        {
            y /= 2.0;
            x /= 2.0;
            l /= 2.0;
            r /= 2.0;
        }

        robot.arcade(y, x, l, r);

        if(gamepad2.x)
        {
            robot.shooterRed.setPower(0.75);
            robot.shooterBlue.setPower(0.75);
        } else if(gamepad2.a)
        {
            robot.shooterRed.setPower(1);
            robot.shooterBlue.setPower(1);
        } else {
            robot.shooterRed.setPower(0);
            robot.shooterBlue.setPower(0);
        }

        if(gamepad2.right_bumper)
        {
            robot.intake.setPower(.75);
        } else if(gamepad2.left_bumper){
            robot.intake.setPower(-0.75);
        } else {
            robot.intake.setPower(0);
        }
    }
}
