package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.StupidColor;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 10/27/2017.
 * Teleop!
 */

@TeleOp(name="Teleop")
public class Teleop extends OpMode {
    private static final float slowFactor = 0.5f;
    private static final float fastFactor = 0.75f;
    private static final double SERVO_INC_SHAKE = 0.1;
    private static final double SERVO_INC_MAX = 0.02;
    private static final double SERVO_INC_MIN = 0.001;
    private static final int LIFT_COUNTS = 2800; //4250
    private static final int LIFT_BOTTOM_COUNTS = 730;
    private static final int LIFT_INC_COUNTS = 100;
    private static final double BUCKET_SHAKE_INTERVAL = 0.04; //seconds

    BotHardware bot = new BotHardware(this);
    private boolean lastA = false;
    private boolean robotSlow = true;
    private boolean motorsSet = false;
    ColorSensor frontColor;
    ColorSensor backColor;

    private enum LiftState {
        RAISING,
        LOWERING,
        MID,
    }
    private LiftState liftState = LiftState.LOWERING;

    private int leftPos;
    private int rightPos;

    private boolean servoSet = false;
    private double lastTime = 0;

    public void init() {
        bot.init();
        DcMotor[] ray = bot.getMotorRay();
        for(DcMotor motor : ray)
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        try {
            frontColor = new StupidColor(hardwareMap.get(AdafruitI2cColorSensor.class, "fc"));
            backColor = new StupidColor(hardwareMap.get(AdafruitI2cColorSensor.class, "bc"));
            frontColor.enableLed(false);
            backColor.enableLed(false);
        }
        catch (Exception e) {
            //hmmmmm
        }
    }

    public void start() {
        bot.start();
        bot.setFrontDrop(0.34);
        bot.setDropPos(BotHardware.ServoE.backDropUp);
        leftPos = BotHardware.Motor.liftLeft.motor.getCurrentPosition() + 50;
        rightPos = BotHardware.Motor.liftRight.motor.getCurrentPosition() + 50;
        //lift run to position mode
        BotHardware.Motor.liftLeft.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BotHardware.Motor.liftRight.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BotHardware.Motor.liftLeft.motor.setTargetPosition(leftPos);
        BotHardware.Motor.liftRight.motor.setTargetPosition(rightPos);
        BotHardware.Motor.liftLeft.motor.setPower(1.0);
        BotHardware.Motor.liftRight.motor.setPower(1.0);
    }

    public void loop() {
        //calc deltas
        final int leftLiftPosDelta =  BotHardware.Motor.liftLeft.motor.getCurrentPosition() - leftPos;
        final int rightLiftPosDelta = BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos;

        if(gamepad1.a && !lastA) robotSlow = !robotSlow;
        lastA = gamepad1.a;

        if(gamepad1.b || gamepad2.b) bot.setDropPos(0.7);

        if(gamepad1.left_bumper) bot.setFrontDrop(Range.clip(bot.getFrontDrop() - SERVO_INC_MAX, BotHardware.ServoE.frontDropDown, BotHardware.ServoE.frontDropUp));
        else if(gamepad1.right_bumper) bot.setFrontDrop(Range.clip(bot.getFrontDrop() + SERVO_INC_MAX, BotHardware.ServoE.frontDropDown, BotHardware.ServoE.frontDropUp));

        //check if going down
        if(gamepad2.left_bumper && liftState != LiftState.LOWERING) {
            BotHardware.Motor.liftLeft.motor.setTargetPosition(leftPos);
            BotHardware.Motor.liftRight.motor.setTargetPosition(rightPos);
            BotHardware.Motor.liftLeft.motor.setPower(1.0);
            BotHardware.Motor.liftRight.motor.setPower(1.0);
            liftState = LiftState.LOWERING;
        }
        else if((gamepad2.right_bumper || (gamepad1.right_trigger > 0 && leftLiftPosDelta < LIFT_BOTTOM_COUNTS && rightLiftPosDelta < LIFT_BOTTOM_COUNTS)) && liftState != LiftState.RAISING) {
            BotHardware.Motor.liftLeft.motor.setTargetPosition(leftPos + LIFT_COUNTS);
            BotHardware.Motor.liftRight.motor.setTargetPosition(rightPos + LIFT_COUNTS);
            BotHardware.Motor.liftLeft.motor.setPower(1.0);
            BotHardware.Motor.liftRight.motor.setPower(1.0);
            liftState = LiftState.RAISING;
        }
        else if(!gamepad2.left_bumper && !gamepad2.right_bumper && liftState != LiftState.MID) {
            //calculate lift position such that they are on the same proportion
            int avgDelta;
            if(liftState == LiftState.RAISING) avgDelta = avgDelta = Range.clip((int)((double)(leftLiftPosDelta + rightLiftPosDelta) / 2.0) + 150, 0, LIFT_COUNTS);
            else avgDelta = avgDelta = Range.clip((int)((double)(leftLiftPosDelta + rightLiftPosDelta) / 2.0), 0, LIFT_COUNTS);
            BotHardware.Motor.liftLeft.motor.setTargetPosition(leftPos + avgDelta);
            BotHardware.Motor.liftRight.motor.setTargetPosition(rightPos + avgDelta);
            BotHardware.Motor.liftLeft.motor.setPower(0.5);
            BotHardware.Motor.liftRight.motor.setPower(0.5);
            liftState = LiftState.MID;
        }

        if(gamepad1.left_trigger > 0)
            bot.setDropPos(Range.clip(bot.getDropPos() + Range.scale(gamepad1.left_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));
        else if(gamepad1.right_trigger > 0 && liftState != LiftState.RAISING)
            bot.setDropPos(Range.clip(bot.getDropPos() - Range.scale(gamepad1.right_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));

        double time = getRuntime();
        if((gamepad2.x || gamepad1.x) && time - lastTime >= BUCKET_SHAKE_INTERVAL) {
            telemetry.addData("Interval", time - lastTime);
            lastTime = time;
            if(servoSet) bot.setDropPos(Range.clip(bot.getDropPos() - SERVO_INC_SHAKE, BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));
            else bot.setDropPos(Range.clip(bot.getDropPos() + SERVO_INC_SHAKE, BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));
            servoSet = !servoSet;
        }
        else if(!gamepad2.x && !gamepad1.x) servoSet = false;

        telemetry.addData("Drop", BotHardware.ServoE.backDropLeft.servo.getPosition());

        bot.setSuckLeft(gamepad2.left_stick_y);
        bot.setSuckRight(gamepad2.right_stick_y);

        //if(dropperDown) bot.dropBack();
        //else bot.raiseBack();

        if(robotSlow) {
            if(!motorsSet) {
                DcMotor[] ray = bot.getMotorRay();
                for(DcMotor motor : ray) motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                motorsSet = true;
            }
            bot.setLeftDrive(gamepad1.left_stick_y * slowFactor);
            bot.setRightDrive(gamepad1.right_stick_y * slowFactor);
        }
        else {
            bot.setLeftDrive(gamepad1.left_stick_y * fastFactor);
            bot.setRightDrive(gamepad1.right_stick_y * fastFactor);
            if(motorsSet) {
                //DcMotor[] ray = bot.getMotorRay();
                //for(DcMotor motor : ray) motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                motorsSet = false;
            }
        }


        bot.getStickBase().setPosition(BotHardware.ServoE.stickBaseHidden);

        telemetry.addData("Front Drop", bot.getFrontDrop());
        telemetry.addData("Back Drop", bot.getDropPos());
        telemetry.addData("Robot Slow", robotSlow);
        telemetry.addData("Lift Left", leftLiftPosDelta);
        telemetry.addData("Lift Right", rightLiftPosDelta);
    }

    public void stop() {
        bot.stopAll();
    }
}
