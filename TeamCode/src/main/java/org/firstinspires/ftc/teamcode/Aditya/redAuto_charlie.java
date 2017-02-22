package org.firstinspires.ftc.teamcode.Aditya;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral_charlie;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "charlie red auto")
public class redAuto_charlie extends AutonomousGeneral_charlie {


    //
    boolean second_beacon_press = false;
    String currentTeam = "red";
    private ElapsedTime runtime = new ElapsedTime();
    double[] timeprofile = new double[30];
    int profile_index = 0;
    //String currentColor = "blank";

    @Override
    public void runOpMode() {


        initiate();


        //sleep(3000);
        waitForStart();

        second_beacon_press = false;
        runtime.reset();
        timeprofile[profile_index++] = runtime.milliseconds();
        //setMotorsModeToEncDrive();
        //stopMotors();
        //encoderDriveShootRed(.7,145,130,3, 2, 1);
        //charlieBeaconPress(true);
        //while (true) {
        //  strafeRight(0.4);
        //}



        newEncoderDrive(0.75, 17, 17    , 3);
        stopMotors();
        sleep(250);
        charlieEncShoot(0.5);
        while (System.currentTimeMillis() < System.currentTimeMillis() + 3000) {
            intake_motor.setPower(1);
        }
        intake_motor.setPower(0);
        charlieEncShoot(0.5);

        //In this part, we go straight, and shoot two particles!


    }
}
//-------------------------------------------------------------------//


