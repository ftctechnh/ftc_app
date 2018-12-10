package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SuperiorSampling;

@Autonomous(name="Depo to other crater")
public class DepoToOtherCrater extends SuperiorSampling {
    @Override
    public void runOpMode() throws InterruptedException {
        this.goal = EndGoal.RED_CRATER;
        this.startingPosition = StartingPosition.DEPOT;
        super.runOpMode();
    }
}
