package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
    HeadingSensor sensor;
    static final float slowFactor = 0.1f;

    protected BotHardware bot = new BotHardware(this);

    @Override
    public void init() {
        bot.init();
        bot.start();
        sensor = bot.getHeadingSensor();
    }

    @Override
    public void start() {
        leftEncoderLast = BotHardware.Motor.frontLeft.motor.getCurrentPosition();
        rightEncoderLast = BotHardware.Motor.frontRight.motor.getCurrentPosition();
        lastHeading = sensor.getHeading();

        telemetry.addLine().addData("XPos", new Func<Double>() {
            @Override
            public Double value() {
                return xpos;
            }
        });

        telemetry.addLine().addData("YPos", new Func<Double>() {
            @Override
            public Double value() {
                return ypos;
            }
        });

        telemetry.addLine().addData("Heading", new Func<Float>() {
            @Override
            public Float value() {
                return lastHeading;
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
        xpos += Math.cos(lastHeading)  * netForward;
        ypos += Math.sin(lastHeading) * netForward;

        leftEncoderLast = nowEncoderLeft;
        rightEncoderLast = nowEncoderRight;

        bot.setLeftDrive(gamepad1.left_stick_y * slowFactor);
        bot.setRightDrive(gamepad1.right_stick_y * slowFactor);
    }

    @Override
    public void stop() {
        super.stop();
    }
}
