package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral_newName;
/**
 * Created by inspirationteam on 12/28/2016.
 */

@Autonomous(name="Beacon Press Linear Auto", group="Pushbot")

public class beaconpress_linearauto extends AutonomousGeneral_newName {

    private ElapsedTime     runtime = new ElapsedTime();

    static  int             INITIAL_SHOOTERPOS;

    @Override
    public void runOpMode() {

        initiate();

        waitForStart();

        newBeacon("blue");


    }
}


