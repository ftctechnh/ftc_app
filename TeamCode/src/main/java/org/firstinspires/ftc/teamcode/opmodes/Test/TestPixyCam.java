package org.firstinspires.ftc.teamcode.opmodes.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.components.pixy.PixyCam;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.lang.reflect.Field;
import java.util.*;

@TeleOp(name="TestPixyCam", group="Linear Opmode")
public class TestPixyCam extends OpMode
{

    PixyCam pixy;
    ElapsedTime elapsedTime = new ElapsedTime();

    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init()
    {
        pixy = hardwareMap.get(PixyCam.class, "PixyCam");
        pixy.setBlockCount(3);
    }


    @Override
    public void loop()
    {
        if (elapsedTime.milliseconds() > 1000) {
            elapsedTime.reset();
            ArrayList<PixyCam.Block> blocks = pixy.getBlocks();
            for(PixyCam.Block block : blocks){
                telemetry.addData("Signature",block.signature);
                telemetry.addData("Xcenter",block.xCenter);
                telemetry.addData("yCenter",block.yCenter);
            }
            telemetry.update();
        }

    }


}