package org.firstinspires.ftc.teamcode.Steven;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by inspirationteam on 12/11/2016.
 */

public class GyroTest extends OpMode {

    GyroSensor gyroSensor;

    @Override
    public void init() {
        gyroSensor = hardwareMap.gyroSensor.get("gyroSensor");
        
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start(){

    }

    @Override
    public void loop() {


    }

    @Override
    public void stop(){

    }
}
