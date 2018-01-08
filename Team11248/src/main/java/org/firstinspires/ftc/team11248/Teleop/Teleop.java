package org.firstinspires.ftc.team11248.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.team11248.Hardware.Claw;
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

        //DIRECTION SWAP

//        if ( (gamepad1.b && !prevGP1.b) || (gamepad2.b && !prevGP2.b) ) {
//
//            if(robot.getOffsetAngle() == 0){
//
//                robot.setOffsetAngle(HolonomicDriver_11248.BACK_OFFSET);
//                robot.frontClaw.release();
//                robot.backClaw.release();
//
//            } else {
//                robot.setOffsetAngle(0);
//                robot.frontClaw.release();
//                robot.backClaw.release();
//
//            }
//
//        }

        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            robot.setOffsetAngle(0);
            robot.frontClaw.open();
            robot.backClaw.release();

        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
            robot.setOffsetAngle(HolonomicDriver_11248.BACK_OFFSET);
            robot.backClaw.open();
            robot.frontClaw.release();

        }


        //DRIVE TRAIN
        robot.drive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, true);
        if (gamepad1.y && !prevGP1.y) robot.toggleDriftMode();
        robot.setFastMode(gamepad1.right_bumper);



        //LIFTS

        Gamepad frontLiftGamepad = (robot.getOffsetAngle() == 0) ? gamepad1 : gamepad2;
        Gamepad backLiftGamepad = (robot.getOffsetAngle() == 0) ? gamepad2 : gamepad1;

        Gamepad frontLiftGamepadPrev = (robot.getOffsetAngle() == 0) ? prevGP1 : prevGP2;
        Gamepad backLiftGamepadPrev = (robot.getOffsetAngle() == 0) ? prevGP2 : prevGP1;



        // Front lift controls

        if (frontLiftGamepad.a && !frontLiftGamepadPrev.a) {

            if (robot.frontClaw.state == Claw.Position.OPEN) // Grab if open (Can go from closed to grab)
                robot.frontClaw.grab();

            else  // if claw is closed or grabbed then open
                robot.frontClaw.open();
        }

        if (frontLiftGamepad.x && !frontLiftGamepadPrev.x)
            if (robot.frontClaw.state == Claw.Position.RELEASE) {
                robot.frontClaw.open();

            } else {  // if claw is open or grabbed then close
                robot.frontClaw.release();
            }

        //Sets arm motor to whatever right trigger is
        if (frontLiftGamepad.right_trigger > 0)
            robot.setFrontLiftPower(frontLiftGamepad.right_trigger);
        else if (frontLiftGamepad.left_trigger > 0)
            robot.setFrontLiftPower(-frontLiftGamepad.left_trigger);
        else
            robot.setFrontLiftPower(0);







        // Back lift controls

        if (backLiftGamepad.a && !backLiftGamepadPrev.a) {

            if (robot.backClaw.state == Claw.Position.OPEN) // Grab if open (Can go from closed to grab)
                robot.backClaw.grab();

            else  // if claw is closed or grabbed then open
                robot.backClaw.open();
        }

        if (backLiftGamepad.x && !backLiftGamepadPrev.x)
            if (robot.backClaw.state == Claw.Position.RELEASE) {
                robot.backClaw.open();

            } else {  // if claw is open or grabbed then close
                robot.backClaw.release();
            }

        //Sets arm motor to whatever right trigger is
        if (backLiftGamepad.right_trigger > 0)
            robot.setBackLiftPower(backLiftGamepad.right_trigger);

        else if (backLiftGamepad.left_trigger > 0)
            robot.setBackLiftPower(-backLiftGamepad.left_trigger);

        else
            robot.setBackLiftPower(0);






            //Recaptures all previous values of Gamepad 1 for debouncing
        try {
            prevGP1.copy(gamepad1);
        } catch (RobotCoreException e1) {
            e1.printStackTrace();
        }

        try {
            prevGP2.copy(gamepad2);
        } catch (RobotCoreException e1) {
            e1.printStackTrace();
        }
    }
}


