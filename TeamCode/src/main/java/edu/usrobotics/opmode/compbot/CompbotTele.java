package edu.usrobotics.opmode.compbot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.usrobotics.opmode.RobotOp;

/**
 * Created by dsiegler19 on 10/13/16.
 */
@TeleOp(name="Compbot TeleOp", group="Compbot")
public class CompbotTele extends RobotOp {

    ElapsedTime time;

    CompbotHardware robot = new CompbotHardware();

    boolean isLiftServoOpen = true;

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

        float harvesterInput = 0;

        float liftInput = 0;

        float shooterInput = 0;

        //Harvester
        harvesterInput += gamepad2.right_trigger;
        harvesterInput += -gamepad2.left_trigger;
        harvesterInput += gamepad2.left_trigger;

        harvesterInput += (gamepad2.left_bumper ? -1 : 0);

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

        //Lift Servo
        if(gamepad2.dpad_left){

            isLiftServoOpen = true;

        }

        if(gamepad2.dpad_right){

            isLiftServoOpen = false;

        }

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

        float shooterPower = Math.min(shooterInput, 1);

        double liftServoPosition = (isLiftServoOpen ? robot.liftServoOpenPosition : robot.liftServoClosePosition);

        robot.frontRight.setPower(frPower);
        robot.frontLeft.setPower(flPower);
        robot.backRight.setPower(brPower);
        robot.backLeft.setPower(blPower);

        robot.liftServo.setPosition(liftServoPosition);

        if(gamepad1.a){

            robot.frontRight.setPower(1);

        }

        if(gamepad1.b){

            robot.frontLeft.setPower(1);

        }

        if(gamepad1.x){

            robot.backRight.setPower(1);

        }

        if(gamepad1.y){

            robot.backLeft.setPower(1);

        }

        robot.harvester.setPower(harvesterPower);

        robot.shooterRight.setPower(shooterPower);
        robot.shooterLeft.setPower(shooterPower);

        robot.lift.setPower(liftPower);

        telemetry.addData("Front right encoder: ", robot.frontRight.getCurrentPosition());
        telemetry.addData("Front left encoder: ", robot.frontLeft.getCurrentPosition());
        telemetry.addData("Back right encoder: ", robot.backRight.getCurrentPosition());
        telemetry.addData("Back left encoder: ", robot.backLeft.getCurrentPosition());

        telemetry.addData("GP1 Right Stick X", gamepad1.right_stick_x);
        telemetry.addData("GP1 Right Stick Y", gamepad1.right_stick_y);
        telemetry.addData("GP1 Left Stick X", gamepad1.left_stick_x);

        telemetry.addData("GP2 Right Trigger", gamepad2.right_trigger);
        telemetry.addData("GP2 Left Trigger", gamepad2.left_trigger);
        telemetry.addData("GP2 Right Stick Y", gamepad2.right_stick_y);
        telemetry.addData("GP2 Left Stick Y", gamepad2.left_stick_y);
        telemetry.addData("GP2 DPAD Right", gamepad2.dpad_right);
        telemetry.addData("GP2 DPAD Left", gamepad2.dpad_left);

        telemetry.addData("frInputs", frInputs);
        telemetry.addData("flInputs", flInputs);
        telemetry.addData("brInputs", brInputs);
        telemetry.addData("blInputs", blInputs);

        telemetry.addData("harvesterInput", harvesterInput);

        telemetry.addData("shooterInput", shooterInput);

        telemetry.addData("liftInput", liftInput);

        telemetry.addData("isLiftServoOpen", isLiftServoOpen);
        telemetry.addData("liftServoPosition", liftServoPosition);

    }
}
