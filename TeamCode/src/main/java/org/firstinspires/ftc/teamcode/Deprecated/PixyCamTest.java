package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PixyCamTest extends OpMode
{

    PixyCam leftPixyCam;
    PixyCam.Block red1Block;
    PixyCam.Block blue1Block;
    ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void init()
    {

        leftPixyCam = hardwareMap.get(PixyCam.class, "leftPixy");

    }

    @Override
    public void loop()
    {
        while (true) {
            if (elapsedTime.milliseconds() > 50) // Update every twentieth of a second.
            {
                elapsedTime.reset();
                red1Block = leftPixyCam.GetBiggestBlock(1);
                telemetry.addData("Left red ball:", red1Block.toString());
                blue1Block = leftPixyCam.GetBiggestBlock(2);
                telemetry.addData("Left blue ball:", blue1Block.toString());
            }
            telemetry.update();
        }
    }
}