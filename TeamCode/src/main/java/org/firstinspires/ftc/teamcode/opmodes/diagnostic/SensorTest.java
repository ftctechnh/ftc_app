package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Testing Sensor Code", group="Test")
@Disabled
public class SensorTest extends OpMode {

    BotHardwareOld bot;

    @Override
    public void init(){
        bot = new BotHardwareOld();

        bot.init(this, false);

    }

    @Override
    public void start(){
        //Vuf.start();
    }

    @Override
    public void loop() {
        telemetry.addData("Color Sensor Left Red", bot.leftSensor.red());
        telemetry.addData("Color Sensor Left Blue", bot.leftSensor.blue());
        telemetry.addData("Color Sensor Right Red", bot.rightSensor.red());
        telemetry.addData("Color Sensor Right Blue", bot.rightSensor.blue());
        telemetry.addData("Ultra Left", bot.distSensorLeft.getUltrasonicLevel());
        telemetry.addData("Ultra Right", bot.distSensorRight.getUltrasonicLevel());
    }

    @Override
    public void stop() {
        super.stop();
        //Vuf.stop();
    }
}



