package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp (name = "turtle")
public class Test extends OpMode {
    @Override
    public void init(){

    }

    @Override
    public void loop(){
        telemetry.addLine("This is working");
    }
}
