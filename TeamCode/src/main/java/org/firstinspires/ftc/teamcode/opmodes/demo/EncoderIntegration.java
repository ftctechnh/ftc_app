package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.opmodes.Teleop;
import org.opencv.core.Mat;

import java.io.FileNotFoundException;

/**
 * Created by Noah on 3/19/2018.
 */

@TeleOp(name="Encoder Integration")
public class EncoderIntegration extends OpMode {
    double xpos = 0;
    double ypos = 0;
    int leftEncoderLast = 0;
    int rightEncoderLast = 0;
    float lastHeading = 0;
    double lastNetForward = 0;

    HeadingSensor sensor;
    static final float slowFactor = 0.4f;

    protected BotHardware bot = new BotHardware(this);

    @Override
    public void init() {
        bot.init();
        DcMotor[] ray = bot.getMotorRay();
        for(DcMotor motor : ray)
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot.start();
        sensor = bot.getHeadingSensor();
    }

    @Override
    public void start() {
        leftEncoderLast = BotHardware.Motor.frontLeft.motor.getCurrentPosition();
        rightEncoderLast = BotHardware.Motor.frontRight.motor.getCurrentPosition();
        lastHeading = sensor.getHeading();

        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                telemetry.addData("XPos", xpos);
                telemetry.addData("YPos", ypos);
                telemetry.addData("Heading", lastHeading);
                telemetry.addData("Net Forward", lastNetForward);
            }
        });
    }

    @Override
    public void loop() {
        //Integrate!
        int nowEncoderLeft = BotHardware.Motor.frontLeft.motor.getCurrentPosition();
        int nowEncoderRight = BotHardware.Motor.frontRight.motor.getCurrentPosition();
        lastHeading = sensor.getHeading();

        //get net forward movement from averaging encoder counts
        double netForward = ((nowEncoderLeft - leftEncoderLast) + (nowEncoderRight - rightEncoderLast)) / 2.0;
        //use vector with heading and gyro and determine x and y movement
        xpos += Math.cos(Math.toRadians(lastHeading)) * netForward;
        ypos += Math.sin(Math.toRadians(lastHeading)) * netForward;

        leftEncoderLast = nowEncoderLeft;
        rightEncoderLast = nowEncoderRight;
        lastNetForward = netForward;

        bot.setLeftDrive(gamepad1.left_stick_y * slowFactor);
        bot.setRightDrive(gamepad1.right_stick_y * slowFactor);
    }

    @Override
    public void stop() {
        super.stop();
    }
}
