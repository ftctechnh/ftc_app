package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.ParkingLocation;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SingleSampling;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.StartingPosition;

@Autonomous(name="Crater - Other color - Single")
@Disabled
public class CraterToOtherCrater extends SingleSampling {
    @Override
    public void runOpMode() throws InterruptedException {
        startingPosition = StartingPosition.CRATER;
        parkingLocation = ParkingLocation.OTHER_COLOR_CRATER;
        super.runOpMode();
    }
}
