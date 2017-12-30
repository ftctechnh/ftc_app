package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 10/27/2017.
 * Teleop!
 */

@TeleOp(name="Teleop")
public class Teleop extends OpMode {
    private static final float slowFactor = 0.5f;

    BotHardware bot = new BotHardware(this);
    private boolean suckerDown = false;
    private boolean lastA = false;
    private boolean robotSlow = false;
    private boolean lastBumper = false;

    private boolean lastBumper2 = false;
    private boolean dropperDown = false;
    private boolean motorsSet = false;

    public void init() {
        bot.init();
        DcMotor[] ray = bot.getMotorRay();
        for(DcMotor motor : ray)
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void start() {

    }

    public void loop() {
        if(gamepad1.a && !lastA) robotSlow = !robotSlow;
        lastA = gamepad1.a;

        if((gamepad1.left_bumper || gamepad1.right_bumper) && !lastBumper) suckerDown = !suckerDown;
        lastBumper = gamepad1.left_bumper || gamepad1.right_bumper;

        if(gamepad1.left_trigger > 0) {
            BotHardware.ServoE.backDropLeft.servo.setPosition((BotHardware.ServoE.leftBackDropUp > BotHardware.ServoE.leftBackDropDown ? BotHardware.ServoE.leftBackDropDown : BotHardware.ServoE.leftBackDropUp) + (gamepad1.left_trigger * Math.abs(BotHardware.ServoE.leftBackDropDown - BotHardware.ServoE.leftBackDropUp)));
            BotHardware.ServoE.backDropRight.servo.setPosition((BotHardware.ServoE.rightBackDropUp > BotHardware.ServoE.rightBackDropDown ? BotHardware.ServoE.rightBackDropDown : BotHardware.ServoE.rightBackDropUp) + (gamepad1.left_trigger * Math.abs(BotHardware.ServoE.rightBackDropDown - BotHardware.ServoE.rightBackDropUp)));
        }
        else {
            BotHardware.ServoE.backDropLeft.servo.setPosition(BotHardware.ServoE.leftBackDropUp);
            BotHardware.ServoE.backDropRight.servo.setPosition(BotHardware.ServoE.rightBackDropUp);
        }

        if(gamepad1.right_trigger > 0) BotHardware.ServoE.stick.servo.setPosition(BotHardware.ServoE.stickUp + (gamepad1.right_trigger * (BotHardware.ServoE.stickDown - BotHardware.ServoE.stickUp)));
        else BotHardware.ServoE.stick.servo.setPosition(BotHardware.ServoE.stickUp);

        telemetry.addData("Drop", BotHardware.ServoE.backDropLeft.servo.getPosition());

        BotHardware.ContiniuosServoE.LiftLeft.servo.setPower(gamepad2.left_stick_y);
        BotHardware.ContiniuosServoE.LiftRight.servo.setPower(gamepad2.right_stick_y);

        //if(suckerDown) bot.dropFront();
        //else bot.raiseFront();

        //if(gamepad2.left_trigger > 0) bot.setLift(gamepad2.left_trigger);
        //else if (gamepad2.right_trigger > 0) bot.setLift(-gamepad2.right_trigger);
        //else bot.setLift(0);

        if((gamepad2.left_bumper || gamepad2.right_bumper) && !lastBumper2) dropperDown = !dropperDown;
        lastBumper2 = gamepad2.left_bumper || gamepad2.right_bumper;

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
            BotHardware.Motor.green.motor.setPower(0.5);
        }
        else {
            bot.setLeftDrive(gamepad1.left_stick_y);
            bot.setRightDrive(gamepad1.right_stick_y);
            if(motorsSet) {
                DcMotor[] ray = bot.getMotorRay();
                for(DcMotor motor : ray) motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                motorsSet = false;
            }
            BotHardware.Motor.green.motor.setPower(0);
            lastA = gamepad1.a;
        }


        //if(gamepad1.b) bot.dropStick();
        //else bot.liftStick();

        bot.getStickBase().setPosition(BotHardware.ServoE.stickBaseHidden);

        telemetry.addData("Front Drop", suckerDown);
        telemetry.addData("Robot Slow", robotSlow);
    }

    public void stop() {
        bot.stopAll();
    }
}
