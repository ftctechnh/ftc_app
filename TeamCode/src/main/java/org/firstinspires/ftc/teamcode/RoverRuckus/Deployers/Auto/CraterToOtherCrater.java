package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SophisticatedSampling;

@Autonomous(name="Crater to other crater")
public class CraterToOtherCrater extends SophisticatedSampling {
    @Override
    public void runOpMode() throws InterruptedException {
        this.goal = EndGoal.RED_CRATER;
        this.startingPosition = StartingPosition.CRATER;
        super.runOpMode();
    }
}
