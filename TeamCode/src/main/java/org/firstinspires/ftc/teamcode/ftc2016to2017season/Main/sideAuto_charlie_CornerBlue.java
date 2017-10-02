package org.firstinspires.ftc.teamcode.ftc2016to2017season.Main;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "Blue sideAuto charles corner ")
@Disabled
public class sideAuto_charlie_CornerBlue extends AutonomousGeneral_charlie {


    //
    boolean second_beacon_press = false;
    String currentTeam = "red";
    private ElapsedTime runtime = new ElapsedTime();
    double[] timeprofile = new double[30];
    int profile_index = 0;
    //public double firstODSReadBack;
    //String currentColor = "blank";

    @Override
    public void runOpMode() {


        initiate();


        //sleep(3000);
        waitForStart();
        //firstODSReadBack = ODSBack.getRawLightDetected();
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



        //newEncoderDrive(0.75, 17, 17, 2);

        encoderMecanumDrive(0.55, -70, -70, 5,0);
        sleep(200);
        encoderShoot(0.5);
        intake_motor.setPower(1);
        sleep(4000);
        intake_motor.setPower(0);
        encoderShoot(0.5);
        sleep(250);

        encoderMecanumDrive(0.3,-38.1,-38.1,5,0);
        encoderMecanumDrive(0.55,34,-34,5,0);
        encoderMecanumDrive(0.7,-152,-152,10,0);
       /* encoderMecanumDrive(0.75, 5, 5, 1, 2);
        readNewColorLeft();
        if (currentColorBeaconLeft.equals(currentTeam)){
            encoderMecanumDrive(0.75, 5, 5, 1, 2);

        }*/
    }

    public boolean ODSBackRead(){
        if(ODSBack.getRawLightDetected() > (baseline2 * 3)){
            return true;
        } else{
            return false;
        }
    }
}
//-------------------------------------------------------------------//


