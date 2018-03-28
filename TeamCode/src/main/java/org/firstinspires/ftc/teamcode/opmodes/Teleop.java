package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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
    private static final float fastFactor = 0.7f;
    private static final double SERVO_INC_SHAKE = 0.1;
    private static final double SERVO_INC_MAX = 0.02;
    private static final double SERVO_INC_MIN = 0.001;
    private static final int LIFT_COUNTS = 2800; //4250
    private static final int LIFT_BOTTOM_COUNTS = 730;
    private static final int LIFT_INC_COUNTS = 100;
    private static final double BUCKET_SHAKE_INTERVAL = 0.04; //seconds
    private static final double BUCKET_FLAT = 0.53;
    //private static final int BUCKET_LIFT_COUNTS = 50;
    //private static final int INTAKE_LIFT_COUNTS = 1200;
    private static final int MIN_VELOCITY = 550;

    protected BotHardware bot = new BotHardware(this);
    private boolean lastA = false;
    private boolean robotSlow = true;
    private boolean motorsSet = false;
    ColorSensor frontColor;
    ColorSensor backColor;

    private boolean suckMotorStopped = false;
    private boolean suckLeftStopped = false;
    private boolean suckMotorRamping = false;
    private int suckMotorRampCount = 0;

    private int liftPos;

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
        bot.setLights(0.5);
    }

    public void start() {
        gamepad1.setJoystickDeadzone(0.05f);
        gamepad2.setJoystickDeadzone(0.05f);
        bot.start();
        bot.setFrontDrop(0.34);
        bot.setDropPos(BotHardware.ServoE.backDropUp);
        BotHardware.Motor.lift.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftPos = BotHardware.Motor.lift.motor.getCurrentPosition();
    }

    public void loop() {
        //calc deltas
        final int liftPosDelta = BotHardware.Motor.lift.motor.getCurrentPosition() - liftPos;

        if(gamepad1.a && !lastA) robotSlow = !robotSlow;
        lastA = gamepad1.a;

        if(gamepad1.b || gamepad2.b) bot.setDropPos(BUCKET_FLAT);
        if(gamepad2.a) bot.setDropPos(BotHardware.ServoE.backDropUp);

        if(gamepad1.left_bumper) bot.setFrontDrop(Range.clip(bot.getFrontDrop() - SERVO_INC_MAX, BotHardware.ServoE.frontDropDown, BotHardware.ServoE.frontDropUp));
        else if(gamepad1.right_bumper) bot.setFrontDrop(Range.clip(bot.getFrontDrop() + SERVO_INC_MAX, BotHardware.ServoE.frontDropDown, BotHardware.ServoE.frontDropUp));
        else if(gamepad2.dpad_up) bot.setFrontDrop(BotHardware.ServoE.frontDropUp);
        else if(gamepad2.dpad_down) bot.setFrontDrop(BotHardware.ServoE.frontDropDown);

        //lowering
        if(gamepad2.left_bumper) {
            bot.setLiftMotors(-1.0f);
            BotHardware.Motor.lift.motor.setTargetPosition(liftPos);
        }
        else if(gamepad2.right_bumper) {
            bot.setLiftMotors(1.0f);
            BotHardware.Motor.lift.motor.setTargetPosition(liftPos + LIFT_COUNTS);
        }
        else bot.setLiftMotors(0);

        if(gamepad1.left_trigger > 0)
            bot.setDropPos(Range.clip(bot.getDropPos() + Range.scale(gamepad1.left_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));
        else if(gamepad1.right_trigger > 0) {
            if(liftPosDelta > LIFT_BOTTOM_COUNTS) bot.setDropPos(Range.clip(bot.getDropPos() - Range.scale(gamepad1.right_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));
            else {
                BotHardware.Motor.lift.motor.setTargetPosition(liftPos + LIFT_BOTTOM_COUNTS + 50);
                bot.setLiftMotors(1.0f);
            }
        }

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

        if(gamepad2.left_stick_y != 1 || gamepad2.right_stick_y != 1) {
            bot.setSuckLeft(gamepad2.left_stick_y);
            bot.setSuckRight(gamepad2.right_stick_y);
            suckMotorRamping = true;
            suckMotorStopped = false;
            suckMotorRampCount = 5;
        }
        else {
            telemetry.addData("Metering", true);

            double lastRight = BotHardware.Motor.suckRight.motor.getVelocity(AngleUnit.DEGREES);
            double lastLeft = BotHardware.Motor.suckLeft.motor.getVelocity(AngleUnit.DEGREES);

            if(suckMotorRamping) {
                BotHardware.Motor.suckLeft.motor.setPower(1);
                BotHardware.Motor.suckRight.motor.setPower(1);
                if(lastRight > MIN_VELOCITY && lastLeft > MIN_VELOCITY) suckMotorRampCount++;
                else suckMotorRampCount = 0;
                if(suckMotorRampCount >= 10) {
                    suckMotorRamping = false;
                    suckMotorRampCount = 0;
                }
            }
            else if(!suckMotorStopped && (lastRight < MIN_VELOCITY || lastLeft < MIN_VELOCITY)) {
                if(lastLeft < lastRight) {
                    BotHardware.Motor.suckRight.motor.setPower(0);
                    suckLeftStopped = false;
                }
                else {
                    BotHardware.Motor.suckLeft.motor.setPower(0);
                    suckLeftStopped = true;
                }
                suckMotorStopped = true;
            }
            else if(suckMotorStopped && ((suckLeftStopped && lastRight > MIN_VELOCITY) || (!suckLeftStopped && lastLeft > MIN_VELOCITY))) {
                suckMotorStopped = false;
                suckMotorRamping = true;
            }
        }

        //if(dropperDown) bot.dropBack();
        //else bot.raiseBack();

        if(gamepad1.y) {
            bot.setLeftDrive(gamepad1.left_stick_y);
            bot.setRightDrive(gamepad1.right_stick_y);
        }
        else if(robotSlow) {
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
        telemetry.addData("Lift", liftPos);
    }

    public void stop() {
        bot.stopAll();
    }
}
