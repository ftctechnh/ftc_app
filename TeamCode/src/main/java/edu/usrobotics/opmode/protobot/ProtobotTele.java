package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import edu.usrobotics.opmode.RobotOp;

/**
 * Created by dsiegler19 on 10/13/16.
 */
@TeleOp(name="Protobot TeleOp", group="Protobot")
public class ProtobotTele extends RobotOp {

    ProtobotHardware robot = new ProtobotHardware();

    boolean gateOpen = false;

    boolean rightBumperPressedLastTick = false;

    @Override
    public void init () {

        super.init();

        robot.init(hardwareMap);

        robot.frontRight.setDirection(robot.frCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        robot.frontLeft.setDirection(robot.flCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        robot.backRight.setDirection(robot.brCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        robot.backLeft.setDirection(robot.blCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop(){

        float frInputs = 0;
        float flInputs = 0;
        float brInputs = 0;
        float blInputs = 0;

        float harvesterInputs = 0;

        harvesterInputs = gamepad1.right_trigger;


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

        if(gamepad1.right_bumper && !rightBumperPressedLastTick){

            gateOpen = !gateOpen;
            rightBumperPressedLastTick = true;

        }

        else if(!gamepad1.right_bumper){

            rightBumperPressedLastTick = false;

        }

        float frPower = Math.min(Math.abs(frInputs), 1);
        float flPower = Math.min(Math.abs(flInputs), 1);
        float brPower = Math.min(Math.abs(brInputs), 1);
        float blPower = Math.min(Math.abs(blInputs), 1);

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

        robot.frontRight.setPower(frPower);
        robot.frontLeft.setPower(flPower);
        robot.backRight.setPower(brPower);
        robot.backLeft.setPower(blPower);

        //robot.harvester.setPower(harvesterInputs);

        //robot.gate.setPosition(gateOpen ? robot.gateOpenedPosition : robot.gateClosedPosition);

        if(gamepad1.a){

            robot.frontRight.setPower(1);

        }

        if(gamepad1.b){

            robot.frontLeft.setPower(1);

        }

        if(gamepad1.x){

            robot.backRight.setPower(1);

        }

        if(gamepad1.y) {

            robot.backLeft.setPower(1);

        }

        telemetry.addData("Front right encoder: ", robot.frontRight.getCurrentPosition());
        telemetry.addData("Front left encoder: ", robot.frontLeft.getCurrentPosition());
        telemetry.addData("Back right encoder: ", robot.backRight.getCurrentPosition());
        telemetry.addData("Back left encoder: ", robot.backLeft.getCurrentPosition());

        telemetry.addData("GP1 Right Stick X", gamepad1.right_stick_x);
        telemetry.addData("GP1 Right Stick Y", gamepad1.right_stick_y);
        telemetry.addData("GP1 Left Stick X", gamepad1.left_stick_x);

        telemetry.addData("frInputs", frInputs);
        telemetry.addData("flInputs", flInputs);
        telemetry.addData("brInputs", brInputs);
        telemetry.addData("blInputs", blInputs);

        telemetry.addData("harvesterInputs", harvesterInputs);

    }
}
