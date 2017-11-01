package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "newBlueBeaconPress_auto")
@Disabled
public class newblueBeaconPress_auto extends AutonomousGeneral {

    boolean second_beacon_press = false;
    String currentTeam = "blue";


    @Override
    public void runOpMode() {

        initiate();

        readNewColorLeft();

        waitForStart();

        second_beacon_press = false;

        encoderDriveShootBlue(0.2, -135, -108, 8.0, -20, 2);

        pressBeaconBlue();
        sleep(250);

        moveToNextBeaconBlue();

        sleep(150);

        pressBeaconBlue();
        }




    public void moveToNextBeaconBlue() {
        second_beacon_press = true;

        sleep(250);
        encoderDrive(0.4, -25, -25, 6);
        sleep(450);
        encoderDrive(0.4, 32, -32, 6);

        sleep(150);
        encoderDrive(0.5, -65, -65, 6);
    }



    public void pressBeaconBlue() {

            lineAlignBlue();

        allignRangeDist(7);

        readNewColorLeft();
        if (currentColor.equals(currentTeam)) {
            telemetry.addData("","correct color");
            telemetry.update();

            encoderDrive(0.2, 3.5, -3.5, 5);
            sleep(1000);
            encoderDrive(0.2, -5, -5, 5);
            sleep(300);
           encoderDrive(0.2, -3.5, 3.5, 5);

            sleep(300);
        } else {
            telemetry.addData("","incorrect color");
            telemetry.update();

            encoderDrive(0.2, -3.5, 3.5, 5);
            sleep(1000);
            encoderDrive(0.2, -5, -5, 5);
            sleep(300);
            encoderDrive(0.2, 5, -5, 5);
            sleep(300);
        }
    }



    public void lineAlignBlue() {

        while (whiteLineDetectedBack() == false) {

            straightDrive(-0.1);

           /* if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, -15, -15, 6);
                sleep(250);
                encoderDrive(0.6, 15, -15, 8);
            }*/
        }
        sleep(150);
        if (second_beacon_press == true)
        {
            encoderDrive(0.1, -15, -15, 4);
        }
        else
        {
            encoderDrive(0.1, -15, -15, 4);
        }

        sleep(150);

        while (whiteLineDetectedFront() == false) {

            newTurnLeft(0.1);
        }
        stopMotors();
        if (second_beacon_press == true)
        {
            encoderDrive(0.1, -3, 3, 4);
        }
        else
        {
          //  encoderDrive(0.1, -3, 3, 4);
        }
    }


}
//-------------------------------------------------------------------//


