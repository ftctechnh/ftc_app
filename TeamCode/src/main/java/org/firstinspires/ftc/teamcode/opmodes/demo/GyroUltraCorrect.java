package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;

/**
 * Created by Noah on 4/4/2018.
 */

@Autonomous(name = "Gyro Angle Corrections")
public class GyroUltraCorrect extends OpMode {
    MatbotixUltra ultra;
    BotHardware bot = new BotHardware(this);
    private static final double HEIGHT_CM = DistanceUnit.INCH.toCm(10.3);

    public void init() {
        bot.init();
        ultra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraright"));
        ultra.initDevice();
        ultra.startDevice();
        bot.start();
    }

    public void start() {

    }

    public void loop() {
        double angle = bot.getImu().getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle;
        int reading = ultra.getReading();
        telemetry.addData("Ultra", reading);
        telemetry.addData("Angle", angle);
        telemetry.addData("Corrected Ultra", reading - Math.sin(Math.toRadians(angle)) * HEIGHT_CM);
    }
}
