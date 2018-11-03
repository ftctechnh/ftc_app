package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.ParkingLocation;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SingleSampling;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.StartingPosition;

@Autonomous(name="Depot - No Parking - Single")
public class DepotJustTeamMarker extends SingleSampling {
    @Override
    public void runOpMode() throws InterruptedException {
        startingPosition = StartingPosition.DEPOT;
        parkingLocation = ParkingLocation.DEPOT;
        super.runOpMode();
    }
}
