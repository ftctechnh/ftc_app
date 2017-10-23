package edu.usrobotics.opmode.mecanumbot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import edu.usrobotics.opmode.RobotOp;

/**
 * Created by dsiegler19 on 10/13/16.
 */
@TeleOp(name="Mecanum TeleOp", group="MecanumBot")
public class MecanumBotTele extends RobotOp {

    MecanumBotHardware robot = new MecanumBotHardware();

    private int bottomBlockLiftPos = 100;
    private int topBlockLiftPos = 750;

    @Override
    public void init () {

        super.init();

        robot.init(hardwareMap);

    }

    @Override
    public void loop(){

        float frInputs = 0;
        float flInputs = 0;
        float brInputs = 0;
        float blInputs = 0;

        float creepyness = gamepad1.left_trigger;
        float multiplier = 1f - (creepyness * 0.66f);

        float liftPower = gamepad2.right_stick_y * 0.4f;

        if(!robot.topLimitSwitch.getState()){

            // liftPower = Math.min(liftPower, 0f);
            topBlockLiftPos = robot.blockLift.getCurrentPosition();

        }

        // if(!robot.bottomLimitSwitch.getState()){

            // liftPower = Math.max(liftPower, 0f);
            // bottomBlockLiftPos = robot.blockLift.getCurrentPosition();

        // }

        if(gamepad2.b){

            robot.openGripper();

        }

        if(gamepad2.x){

            robot.closeGripper();

        }

        // Skid steering
        frInputs -= gamepad1.left_stick_x;
        brInputs -= gamepad1.left_stick_x;
        flInputs += gamepad1.left_stick_x;
        blInputs += gamepad1.left_stick_x;

        // Forward and backwards
        frInputs -= gamepad1.right_stick_y;
        flInputs -= gamepad1.right_stick_y;
        brInputs -= gamepad1.right_stick_y;
        blInputs -= gamepad1.right_stick_y;

        // Strafing
        frInputs -= gamepad1.right_stick_x;
        flInputs += gamepad1.right_stick_x;
        brInputs += gamepad1.right_stick_x;
        blInputs -= gamepad1.right_stick_x;

        frInputs *= multiplier;
        flInputs *= multiplier;
        brInputs *= multiplier;
        blInputs *= multiplier;

        DcMotorSimple.Direction frDirection = (frInputs >= 0 ?
                (robot.frCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.frCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));
        DcMotorSimple.Direction flDirection = (flInputs >= 0 ?
                (robot.flCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.flCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));
        DcMotorSimple.Direction brDirection = (brInputs >= 0 ?
                (robot.brCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.brCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));
        DcMotorSimple.Direction blDirection = (blInputs >= 0 ?
                (robot.blCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.blCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));

        robot.frontRight.setDirection(frDirection);
        robot.frontLeft.setDirection(flDirection);
        robot.backRight.setDirection(brDirection);
        robot.backLeft.setDirection(blDirection);

        float frPower = Math.min(Math.abs(frInputs), 1);
        float flPower = Math.min(Math.abs(flInputs), 1);
        float brPower = Math.min(Math.abs(brInputs), 1);
        float blPower = Math.min(Math.abs(blInputs), 1);

        robot.frontRight.setPower(frPower);
        robot.frontLeft.setPower(flPower);
        robot.backRight.setPower(brPower);
        robot.backLeft.setPower(blPower);

        robot.blockLift.setPower(liftPower);

        telemetry.addData("Top pressed", robot.topLimitSwitch.getState());
        // telemetry.addData("Bottom pressed", robot.bottomLimitSwitch.getState());

        telemetry.addData("Right gripper pos", robot.gripperRight.getPosition());
        telemetry.addData("Left gripper pos", robot.gripperLeft.getPosition());

        telemetry.addData("GP1 Right Stick X", gamepad1.right_stick_x);
        telemetry.addData("GP1 Right Stick Y", gamepad1.right_stick_y);
        telemetry.addData("GP1 Left Stick X", gamepad1.left_stick_x);

        telemetry.addData("frInputs", frInputs);
        telemetry.addData("flInputs", flInputs);
        telemetry.addData("brInputs", brInputs);
        telemetry.addData("blInputs", blInputs);

        telemetry.update();

    }

}
