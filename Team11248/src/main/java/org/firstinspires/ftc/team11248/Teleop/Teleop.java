package org.firstinspires.ftc.team11248.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.team11248.Hardware.HolonomicDriver_11248;
import org.firstinspires.ftc.team11248.Robot11248;

/**
 * Created by Tony_Air on 11/6/17.
 */


@TeleOp(name="Drive")
public class Teleop extends OpMode {

    Robot11248 robot;
    private Gamepad prevGP1, prevGP2;
    boolean toggleOpen = true;

    @Override
    public void init() {

        robot = new Robot11248(hardwareMap, telemetry);
        robot.init();

        prevGP1 = new Gamepad();
        prevGP2 = new Gamepad();

        try {
            prevGP1.copy(gamepad1);
            prevGP2.copy(gamepad2);
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }

        robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.setOffsetAngle(0);
        robot.turnTelemetryOn(false);
        robot.deactivateColorSensors();
    }

    @Override
    public void loop() {

        //Direction Contron
        if (gamepad1.dpad_up) {
            robot.setOffsetAngle(0);
            robot.backClaw.close();
            robot.frontClaw.open();

        } else if (gamepad1.dpad_down) {
            robot.setOffsetAngle(HolonomicDriver_11248.BACK_OFFSET);
            robot.backClaw.open();
            robot.frontClaw.close();

        }


        robot.drive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, true);

        robot.setFastMode(gamepad1.right_bumper);


        if (robot.getOffsetAngle() == HolonomicDriver_11248.BACK_OFFSET) {
            if (gamepad1.a && !prevGP1.a) {
                if (toggleOpen)
                    robot.backClaw.open();
                else
                    robot.backClaw.release();

                toggleOpen = !toggleOpen;
            }

            if (gamepad1.x && !prevGP1.x)
                robot.backClaw.close();

            //Sets arm motor to whatever right trigger is
            if (gamepad1.right_trigger > 0)
                robot.setBackLiftPower(gamepad1.right_trigger);
            else if (gamepad1.left_trigger > 0)
                robot.setBackLiftPower(-gamepad1.left_trigger);
            else
                robot.setBackLiftPower(0);

        } else {

            if (gamepad1.a && !prevGP1.a) {
                if (toggleOpen)
                    robot.frontClaw.open();
                else
                    robot.frontClaw.release();

                toggleOpen = !toggleOpen;
            }

            if (gamepad1.x && !prevGP1.x)
                robot.frontClaw.close();


        }


        //Recaptures all previous values of Gampad 1 for debouncing
        try {
            prevGP1.copy(gamepad1);
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }
    }
}


