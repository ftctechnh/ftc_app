package edu.usrobotics.opmode.compbot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import edu.usrobotics.opmode.RobotOp;
import edu.usrobotics.opmode.protobot.ProtobotHardware;

/**
 * Created by dsiegler19 on 10/13/16.
 */
@TeleOp(name="Compbot TeleOp", group="Compbot")
public class CompbotTele extends RobotOp {

    CompbotHardware robot = new CompbotHardware();

    @Override
    public void init () {

        super.init();

        robot.init(hardwareMap);

        robot.frontRight.setDirection(robot.frCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        robot.frontLeft.setDirection(robot.flCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        robot.backRight.setDirection(robot.brCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        robot.backLeft.setDirection(robot.blCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void loop(){

        float frInputs = 0;
        float flInputs = 0;
        float brInputs = 0;
        float blInputs = 0;

        float harvesterInput = 0;

        float liftInput = 0;

        float shooterInput = 0;

        //Harvester
        harvesterInput += gamepad2.right_trigger;
        harvesterInput -= gamepad2.left_trigger;

        //Shooter
        shooterInput = gamepad2.right_stick_y;

        //Lift
        liftInput = gamepad2.left_stick_y;

        //Forward and backwards
        frInputs += -gamepad1.right_stick_y;
        flInputs += -gamepad1.right_stick_y;
        brInputs += -gamepad1.right_stick_y;
        blInputs += -gamepad1.right_stick_y;

        //Strafing
        frInputs -= gamepad1.right_stick_x;
        flInputs += gamepad1.right_stick_x;
        brInputs += gamepad1.right_stick_x;
        blInputs -= gamepad1.right_stick_x;

        //Skid steering
        frInputs -= gamepad1.left_stick_x;
        brInputs -= gamepad1.left_stick_x;
        flInputs += gamepad1.left_stick_x;
        blInputs += gamepad1.left_stick_x;

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

        DcMotorSimple.Direction harvesterDirection = (harvesterInput >= 0 ?
                (robot.harvesterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.harvesterCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));

        robot.frontRight.setDirection(frDirection);
        robot.frontLeft.setDirection(flDirection);
        robot.backRight.setDirection(brDirection);
        robot.backLeft.setDirection(blDirection);

        robot.harvester.setDirection(harvesterDirection);

        float frPower = Math.min(Math.abs(frInputs), 1);
        float flPower = Math.min(Math.abs(flInputs), 1);
        float brPower = Math.min(Math.abs(brInputs), 1);
        float blPower = Math.min(Math.abs(blInputs), 1);

        float harvesterPower = harvesterInput;

        float liftPower = liftInput;

        float shooterPower = shooterInput;

        robot.frontRight.setPower(frPower);
        robot.frontLeft.setPower(flPower);
        robot.backRight.setPower(brPower);
        robot.backLeft.setPower(blPower);

        robot.harvester.setPower(harvesterPower);

        robot.shooterRight.setPower(shooterPower);
        robot.shooterLeft.setPower(shooterPower);

        robot.lift.setPower(liftPower);

        telemetry.addData("GP1 Right Stick X", gamepad1.right_stick_x);
        telemetry.addData("GP1 Right Stick Y", gamepad1.right_stick_y);
        telemetry.addData("GP1 Left Stick X", gamepad1.left_stick_x);

        telemetry.addData("GP2 Right Trigger", gamepad2.right_trigger);
        telemetry.addData("GP2 Left Trigger", gamepad2.left_trigger);
        telemetry.addData("GP2 Right Stick Y", gamepad2.right_stick_y);
        telemetry.addData("GP2 Left Stick Y", gamepad2.left_stick_y);

        telemetry.addData("frInputs", frInputs);
        telemetry.addData("flInputs", flInputs);
        telemetry.addData("brInputs", brInputs);
        telemetry.addData("blInputs", blInputs);

        telemetry.addData("harvesterInput", harvesterInput);

        telemetry.addData("shooterInput", shooterInput);

        telemetry.addData("liftInput", liftInput);

    }
}
