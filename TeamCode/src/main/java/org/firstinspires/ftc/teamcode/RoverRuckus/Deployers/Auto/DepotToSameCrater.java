package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.ParkingLocation;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SingleSampling;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.StartingPosition;

@Autonomous(name="Depotr - Same color - Single")
@Disabled
public class DepotToSameCrater extends SingleSampling {
    @Override
    public void runOpMode() throws InterruptedException {
        startingPosition = StartingPosition.DEPOT;
        parkingLocation = ParkingLocation.SAME_COLOR_CRATER;
        super.runOpMode();
    }
}
