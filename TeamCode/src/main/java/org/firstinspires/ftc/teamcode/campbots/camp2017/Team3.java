package org.firstinspires.ftc.teamcode.campbots.camp2017;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
@Disabled
@TeleOp(name = "TEAM-3", group = "campbots")
public class Team3 extends OpMode {

    private DcMotor left;
    private DcMotor right;

    private int trackA = 0;
    private ElapsedTime timer = new ElapsedTime();
    private int mode = 0;
    private int MAXMODE = 2;

    @Override
    public void init() {

        // init the Wheels
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");


        // set wheel direction
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        gamepad1.setJoystickDeadzone(0.1f);
    }

    @Override
    public void loop() {

        boolean trigger = false;

        if (gamepad1.left_trigger != 0 || gamepad1.right_trigger != 0) {
            trigger = true;
            telemetry.addData("DRIVE", "tank mode");
            right.setPower(gamepad1.right_trigger);
            left.setPower(gamepad1.left_trigger);
        }
        switch (this.mode) {
            case 0:
                oneStickMode2();
            case 1:
                oneStickMode();
                break;
            case 2:
                tankMode();
                break;
            default:
        }




        if (gamepad1.a) {

            while (gamepad1.a) {
                telemetry.addData(">", "Stuck in loop " + timer.seconds());
                telemetry.update();

            }

            setNextMode();

        }



    }

    private void tankMode() {
        right.setPower(gamepad1.right_stick_y);
        left.setPower(gamepad1.left_stick_y);
    }

    private void oneStickMode() {

        float steer = gamepad1.left_stick_x / 2.0f;
        //Foward is negative

        if (steer > 0) {
            right.setPower(gamepad1.left_stick_y + steer);
            left.setPower(gamepad1.left_stick_y);
            telemetry.addData("DRIVE", "Right " + steer + "F:" + gamepad1.left_stick_y);
        } else {
            //steer is negative here
            right.setPower(gamepad1.left_stick_y);
            left.setPower(gamepad1.left_stick_y + -steer);
            telemetry.addData("DRIVE", "Left " + steer + "F:" + gamepad1.left_stick_y);
        }
    }

    private void oneStickMode2() {

        float steer = gamepad1.left_stick_x / 2.0f;
        //Foward is negative

        if (steer > 0) {
            right.setPower(gamepad1.left_stick_y + steer);
            left.setPower(gamepad1.left_stick_y - steer);
            telemetry.addData("DRIVE", "Right " + steer + "F:" + gamepad1.left_stick_y);
        } else {
            //steer is negative here
            right.setPower(gamepad1.left_stick_y + steer);
            left.setPower(gamepad1.left_stick_y + -steer);
            telemetry.addData("DRIVE", "Left " + steer + "F:" + gamepad1.left_stick_y);
        }
    }

    private void setNextMode() {
        this.mode = this.mode + 1;
        telemetry.addData("modeChanger", "new mode " + this.mode);
        if (this.mode > this.MAXMODE)
        {
            this.mode = 0;
            telemetry.addData("modeChanger", "reseting mode " + this.mode);
        }
        telemetry.update();
    }


}