package org.firstinspires.ftc.team2981;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team2981.structural.*;

/**
 * Created by 200462069 on 10/14/2017.
 */

@TeleOp(name = "TeleOp", group="Regular")
public class DriverOp extends OpMode {
    RobotHardware robot;
    Sensors sensors;
    Vision vision;

    @Override
    public void init() {
        //robot = new RobotHardware(hardwareMap);
        //sensors = new Sensors(hardwareMap);
        vision = new Vision(hardwareMap);
        vision.start();
    }

    @Override
    public void loop() {
        telemetry.addData("VuMark", "%s visible", vision.track());
    }

    @Override
    public void stop(){
        vision.stop();
    }


}
