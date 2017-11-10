package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.libraries.TelemetryLogLib;

/**
 * Created by Noah on 4/2/2017.
 */
@Autonomous(name="Telemetry Fun", group="Test")
@Disabled
public class TelemetryLogTest extends OpMode {

    int stupid = 0;

    @Override
    public void init(){
        telemetry = new TelemetryLogLib.TelemetryLog(this);
        telemetry.addData("Broken?", "Guess not");
    }

    @Override
    public void init_loop(){
        telemetry.addData("Stupid", stupid++);
    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){

    }
}
