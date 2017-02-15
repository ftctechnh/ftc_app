package org.firstinspires.ftc.teamcode.opmode.misc;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

/**
 * Created by 292486 on 10/13/2016.
 */
@TeleOp
@Disabled
public class EncoderTrackingHolonomic extends OpMode {

    HolonomicRobot robot = new HolonomicRobot();

    double encoderValFLeft = 0;
    double encoderValFRight = 0;
    double encoderValBLeft = 0;
    double encoderValBRight = 0;

    double encoderVal = 0;

    private double y, x, l, r;

    @Override
    public void init()
    {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        y = -gamepad1.left_stick_y; //For some reason, the left y-axis stick is negative when pushed forward
        x = gamepad1.right_stick_x;
        l = gamepad1.left_trigger;
        r = gamepad1.right_trigger;

        robot.arcade(y, x, l, r);

        if(gamepad1.a) {
            resetEncoders();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }

        if(gamepad1.x) {
            run();
        }

        //telemetry.addData("Loop, enc val ", "L%d & R%d", encoderValFLeft, encoderValFRight);
        telemetry.addData("Front Left: ", "P%4.3f E%5d", robot.frontLeft.getPower(), robot.frontLeft.getCurrentPosition());
        telemetry.addData("Front Right: ", "P%4.3f E%5d", robot.frontRight.getPower(), robot.frontRight.getCurrentPosition());
        telemetry.addData("Back Left: ", "P%4.3f E%5d", robot.backLeft.getPower(), robot.backLeft.getCurrentPosition());
        telemetry.addData("Back Right: ", "P%4.3f E%5d", robot.backRight.getPower(), robot.backRight.getCurrentPosition());
        telemetry.update();
    }

    private void resetEncoders() {
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while(robot.frontLeft.getCurrentPosition() != 0) {}

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void run() {
        while((Math.abs(encoderValFLeft) + Math.abs(encoderValFRight)/2 < Math.abs(encoderVal))) {
            robot.move(0.5, -0.5, 0.5, -0.5);
            telemetry.addData("Running, enc val ", "L%d & R%d", encoderValFLeft, encoderValFRight);
            telemetry.update();
        }
        robot.stop();
    }
}
