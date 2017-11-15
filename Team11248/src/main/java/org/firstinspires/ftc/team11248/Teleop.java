package org.firstinspires.ftc.team11248;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.team11248.Hardware.HolonomicDriver_11248;

/**
 * Created by Tony_Air on 11/6/17.
 */


@TeleOp(name="Drive")
public class Teleop extends OpMode {

    Robot11248 robot;
    private Gamepad prevGP1, prevGP2;

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

        robot.setOffsetAngle(0);
        robot.turnTelemetryOn(true);
    }

    @Override
    public void loop() {


            //Controls Wheels
            if (gamepad1.dpad_up)
                robot.setOffsetAngle(0);

            else if (gamepad1.dpad_left)
                robot.setOffsetAngle(HolonomicDriver_11248.LEFT_OFFSET);

            else if (gamepad1.dpad_down)
                robot.setOffsetAngle(HolonomicDriver_11248.BACK_OFFSET);

            else if (gamepad1.dpad_right)
                robot.setOffsetAngle(HolonomicDriver_11248.RIGHT_OFFSET);



            robot.drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, true);

            robot.setFastMode(gamepad1.left_bumper);

            if (gamepad1.y && !prevGP1.y)
                robot.toggleSlow();

            telemetry.addData("",(robot.getIsSlow()?"SLOW":"FAST"));

            if (gamepad1.x && !prevGP1.x)
                robot.toggleDriftMode();

            telemetry.addData("",(robot.isDriftModeOn()?"DRIFT":"BREAK"));



            //Sets arm motor to whatever right trigger is
            if (gamepad1.right_trigger > 0)
                robot.setFrontLiftPower(gamepad1.right_trigger);
            else if (gamepad1.left_trigger > 0)
                robot.setFrontLiftPower(-gamepad1.left_trigger);
            else
                robot.setFrontLiftPower(0);



            //Recaptures all previous values of Gampad 1 for debouncing
            try {
                prevGP1.copy(gamepad1);
            } catch (RobotCoreException e) {
                e.printStackTrace();
        }
    }
}
