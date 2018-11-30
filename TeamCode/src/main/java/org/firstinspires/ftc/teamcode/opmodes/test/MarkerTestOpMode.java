package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.systems.marker.Marker;

@Autonomous(name="")
public class MarkerTestOpMode extends LinearOpMode
{
    Marker marker;


    @Override
    public void runOpMode() throws InterruptedException
    {
        marker = new Marker(this);
        waitForStart();
        marker.place();
        sleep(1000);
        marker.reset();
    }
}
