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

    CompbotHardware robot = new CompbotHardware();

    boolean isLiftServoOpen = false;

    boolean aButtonPressedLastTime = false;
    boolean bButtonPressedLastTime = false;
    boolean xButtonPressedLastTime = false;
    boolean yButtonPressedLastTime = false;

    boolean controlsReversed = false;

    double lockServoPosition = robot.lockServoStartPosition;

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
    public void start(){

        robot.start();

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

        double liftServoPosition;

        if(!controlsReversed){

            //Forward and backwards regular
            frInputs += -gamepad1.right_stick_y;
            flInputs += -gamepad1.right_stick_y;
            brInputs += -gamepad1.right_stick_y;
            blInputs += -gamepad1.right_stick_y;

            //Strafing regular
            frInputs -= gamepad1.right_stick_x;
            flInputs += gamepad1.right_stick_x;
            brInputs += gamepad1.right_stick_x;
            blInputs -= gamepad1.right_stick_x;

        }

        else{

            //Forward and backwards reversed
            frInputs += gamepad1.right_stick_y;
            flInputs += gamepad1.right_stick_y;
            brInputs += gamepad1.right_stick_y;
            blInputs += gamepad1.right_stick_y;

            //Strafing reversed
            frInputs += gamepad1.right_stick_x;
            flInputs -= gamepad1.right_stick_x;
            brInputs -= gamepad1.right_stick_x;
            blInputs += gamepad1.right_stick_x;

        }

        //Skid steering
        frInputs -= gamepad1.left_stick_x;
        brInputs -= gamepad1.left_stick_x;
        flInputs += gamepad1.left_stick_x;
        blInputs += gamepad1.left_stick_x;

        //Harvester
        harvesterInput += gamepad2.right_trigger;
        harvesterInput += -gamepad2.left_trigger;

        //Shooter
        if(gamepad2.right_bumper){

            shooterInput += 1;

        }

        else if(gamepad2.left_bumper){

            shooterInput -= 1;

        }

        //Lift
        liftInput = -gamepad2.left_stick_y;

        if(!gamepad2.dpad_down && liftInput < 0){

            liftInput = 0;

        }

        //Lift Servo
        if(gamepad2.a && !aButtonPressedLastTime){

            isLiftServoOpen = !isLiftServoOpen;
            aButtonPressedLastTime = true;

        }

        if(!gamepad2.a){

            aButtonPressedLastTime = false;

        }

        //Lock servo
        if(gamepad2.x && !xButtonPressedLastTime){

            lockServoPosition += robot.lockServoDelta;
            xButtonPressedLastTime = true;

        }

        if(!gamepad2.x){

            xButtonPressedLastTime = false;

        }

        if(gamepad2.y && !yButtonPressedLastTime){

            lockServoPosition += -robot.lockServoDelta;
            yButtonPressedLastTime = true;

        }

        if(!gamepad2.y){

            yButtonPressedLastTime = false;

        }

        //Reversing the controls
        if(gamepad1.b && !bButtonPressedLastTime){

            controlsReversed = !controlsReversed;
            bButtonPressedLastTime = true;

        }

        if(!gamepad1.b){

            bButtonPressedLastTime = false;

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

        DcMotorSimple.Direction rightShooterDirection = (shooterInput >= 0 ?
                (robot.rightShooterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.rightShooterCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));

        DcMotorSimple.Direction leftShooterDirection = (shooterInput >= 0 ?
                (robot.leftShooterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.leftShooterCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));

        DcMotorSimple.Direction liftDirection = (liftInput >= 0 ?
                (robot.liftCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE) :
                (robot.liftCorrectDirection ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD));

        robot.frontRight.setDirection(frDirection);
        robot.frontLeft.setDirection(flDirection);
        robot.backRight.setDirection(brDirection);
        robot.backLeft.setDirection(blDirection);

        robot.harvester.setDirection(harvesterDirection);

        robot.shooterRight.setDirection(rightShooterDirection);
        robot.shooterLeft.setDirection(leftShooterDirection);

        robot.lift.setDirection(liftDirection);

        float frPower = Math.min(Math.abs(frInputs), 1);
        float flPower = Math.min(Math.abs(flInputs), 1);
        float brPower = Math.min(Math.abs(brInputs), 1);
        float blPower = Math.min(Math.abs(blInputs), 1);

        float harvesterPower = Math.min(Math.abs(harvesterInput), 1);

        float liftPower = Math.min(Math.abs(liftInput), 1);

        float shooterPower = Math.min(Math.abs(shooterInput), 1);

        liftServoPosition = (isLiftServoOpen ? robot.liftServoOpenPosition : robot.liftServoClosePosition);

        robot.frontRight.setPower(frPower);
        robot.frontLeft.setPower(flPower);
        robot.backRight.setPower(brPower);
        robot.backLeft.setPower(blPower);

        robot.harvester.setPower(harvesterPower);

        robot.shooterRight.setPower(shooterPower);
        robot.shooterLeft.setPower(shooterPower);

        robot.lift.setPower(liftPower);

        robot.liftServo.setPosition(liftServoPosition);

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

        telemetry.addData("lockServoPosition", lockServoPosition);

        telemetry.addData("buttonPresserColorSensor", robot.buttonPresserColorSensor.red() + " " + robot.buttonPresserColorSensor.green() + " " + robot.buttonPresserColorSensor.blue());

        telemetry.addData("bottomFrontColorSensor", robot.bottomFrontColorSensor.red() + " " + robot.bottomFrontColorSensor.green() + " " + robot.bottomFrontColorSensor.blue());
        telemetry.addData("bottomRightColorSensor", robot.bottomRightColorSensor.red() + " " + robot.bottomRightColorSensor.green() + " " + robot.bottomRightColorSensor.blue());
        telemetry.addData("bottomLeftColorSensor", robot.bottomLeftColorSensor.red() + " " + robot.bottomLeftColorSensor.green() + " " + robot.bottomLeftColorSensor.blue());

    }
}
