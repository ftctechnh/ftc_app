package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 10/27/2017.
 * Teleop!
 */

@TeleOp(name="Teleop")
public class Teleop extends OpMode {
    private static final float slowFactor = 0.5f;
    private static final double SERVO_INC_MAX = 0.02;
    private static final double SERVO_INC_MIN = 0.001;
    private static final int LIFT_COUNTS = 4250;

    BotHardware bot = new BotHardware(this);
    private boolean lastA = false;
    private boolean robotSlow = true;
    private boolean motorsSet = false;

    private int leftPos;
    private int rightPos;

    public void init() {
        bot.init();
        DcMotor[] ray = bot.getMotorRay();
        for(DcMotor motor : ray)
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.setFrontDrop(BotHardware.ServoE.frontDropUp);
        bot.setDropPos(BotHardware.ServoE.backDropUp);
    }

    public void start() {
        leftPos = BotHardware.Motor.liftLeft.motor.getCurrentPosition();
        rightPos = BotHardware.Motor.liftRight.motor.getCurrentPosition();
    }

    public void loop() {
        if(gamepad1.a && !lastA) robotSlow = !robotSlow;
        lastA = gamepad1.a;

        if(gamepad1.left_trigger > 0) bot.setDropPos(Range.clip(bot.getDropPos() + Range.scale(gamepad1.left_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));
        else if(gamepad1.right_trigger > 0) bot.setDropPos(Range.clip(bot.getDropPos() - Range.scale(gamepad1.right_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.backDropDown, BotHardware.ServoE.backDropUp));

        if(gamepad1.b) bot.setDropPos(0.7);

        if(gamepad2.left_trigger > 0) bot.setFrontDrop(Range.clip(bot.getFrontDrop() - Range.scale(gamepad2.left_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.frontDropDown, BotHardware.ServoE.frontDropUp));
        else if(gamepad2.right_trigger > 0) bot.setFrontDrop(Range.clip(bot.getFrontDrop() + Range.scale(gamepad2.right_trigger, 0, 1, SERVO_INC_MIN, SERVO_INC_MAX), BotHardware.ServoE.frontDropDown, BotHardware.ServoE.frontDropUp));

        /*
        if(BotHardware.Motor.liftLeft.motor.getCurrentPosition() - leftPos > LIFT_COUNTS + 10
                || BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos > LIFT_COUNTS + 10
                || BotHardware.Motor.liftLeft.motor.getCurrentPosition() - leftPos < -10
                || BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos < -10
                || Math.abs(BotHardware.Motor.liftRight.motor.getCurrentPosition() - BotHardware.Motor.liftLeft.motor.getCurrentPosition()) > 25) {
            if(BotHardware.Motor.liftLeft.motor.getCurrentPosition() - leftPos > LIFT_COUNTS + 10) BotHardware.Motor.liftLeft.motor.setPower(-0.2);
            if(BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos > LIFT_COUNTS + 10) BotHardware.Motor.liftRight.motor.setPower(-0.2);
            if(BotHardware.Motor.liftLeft.motor.getCurrentPosition() - leftPos < -10) BotHardware.Motor.liftLeft.motor.setPower(0.2);
            if(BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos < -10) BotHardware.Motor.liftRight.motor.setPower(0.2);
            if(Math.abs(BotHardware.Motor.liftRight.motor.getCurrentPosition() - BotHardware.Motor.liftLeft.motor.getCurrentPosition()) > 25) {
                if (BotHardware.Motor.liftRight.motor.getCurrentPosition() > BotHardware.Motor.liftLeft.motor.getCurrentPosition()) {
                    if(BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos > LIFT_COUNTS / 2) BotHardware.Motor.liftRight.motor.setPower(-0.2);
                    else BotHardware.Motor.liftLeft.motor.setPower(0.2);
                }
                else{
                    if(BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos > LIFT_COUNTS / 2) BotHardware.Motor.liftLeft.motor.setPower(-0.2);
                    else BotHardware.Motor.liftRight.motor.setPower(0.2);
                }
            }
        }
        */

        else if(gamepad2.left_bumper && BotHardware.Motor.liftLeft.motor.getCurrentPosition() - leftPos > 0 && BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos > 0) bot.setLiftMotors(-1.0f);
        else if(gamepad2.right_bumper && BotHardware.Motor.liftLeft.motor.getCurrentPosition() - leftPos < LIFT_COUNTS && BotHardware.Motor.liftRight.motor.getCurrentPosition() - rightPos < LIFT_COUNTS) bot.setLiftMotors(1.0f);
        else bot.setLiftMotors(0);

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
            bot.setLeftDrive(gamepad1.left_stick_y);
            bot.setRightDrive(gamepad1.right_stick_y);
            if(motorsSet) {
                DcMotor[] ray = bot.getMotorRay();
                for(DcMotor motor : ray) motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                motorsSet = false;
            }
        }


        //if(gamepad1.b) bot.dropStick();
        //else bot.liftStick();

        bot.getStickBase().setPosition(BotHardware.ServoE.stickBaseHidden);

        telemetry.addData("Front Drop", bot.getFrontDrop());
        telemetry.addData("Back Drop", bot.getDropPos());
        telemetry.addData("Robot Slow", robotSlow);
    }

    public void stop() {
        bot.stopAll();
    }
}
