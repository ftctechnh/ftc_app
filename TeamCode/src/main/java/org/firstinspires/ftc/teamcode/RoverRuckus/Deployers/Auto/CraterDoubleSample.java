package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SuperiorSampling;

@Autonomous(name="Crater double sampling")
public class CraterDoubleSample extends SuperiorSampling {
    @Override
    public void runOpMode() throws InterruptedException {
        this.goal = EndGoal.BLUE_DOUBLE_SAMPLE;
        this.startingPosition = StartingPosition.CRATER;
        super.runOpMode();
    }
}
