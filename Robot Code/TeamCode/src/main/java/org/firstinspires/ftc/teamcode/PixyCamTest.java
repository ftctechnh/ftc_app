package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="PixyCamTest", group="Nullbot")
public class PixyCamTest extends OpMode
{

    PixyCam pixyCam;
    PixyCam.Block redBlock;
    PixyCam.Block blueBlock;
    ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void init()
    {

        pixyCam = hardwareMap.get(PixyCam.class, "leftPixyCam");
    }

    @Override
    public void loop()
    {
        if (elapsedTime.milliseconds() > 50) // Update every twentieth of a second.
        {
            elapsedTime.reset();
            redBlock = pixyCam.GetBiggestBlock(1);
            telemetry.addData("Red ball:", redBlock.toString());
            blueBlock = pixyCam.GetBiggestBlock(2);
            telemetry.addData("Blue ball:", blueBlock.toString());

            telemetry.addData("Blue ball:", blueBlock.toString());
        }
    }
}