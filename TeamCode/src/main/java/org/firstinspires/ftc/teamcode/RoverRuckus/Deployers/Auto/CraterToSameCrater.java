package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SuperiorSampling;

@Autonomous(name="Crater to same crater")
public class CraterToSameCrater extends SuperiorSampling {
    @Override
    public void runOpMode() throws InterruptedException {
        this.goal = EndGoal.BLUE_CRATER;
        this.startingPosition = StartingPosition.CRATER;
        super.runOpMode();
    }
}
