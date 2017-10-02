package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.AutonomousGeneral_charlie;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "charlie red auto")
@Disabled

//due to a problem in initialization, all the drive motors should be given opposite values of what they are supposed to be
public class redAuto_charlie extends AutonomousGeneral_charlie {

    //circumference of point turn: 2*pi*r = 2* pi * 10.5 inches = 65.973
    //
    boolean second_beacon_press = false;
    String currentTeam = "red";
    private ElapsedTime runtime = new ElapsedTime();
    double[] timeprofile = new double[30];
    int profile_index = 0;
    public double firstODSReadBack;
    public double robotRadius = 9.5;
    //String currentColor = "blank";

    @Override
    public void runOpMode() {


        initiate();


        //sleep(3000);
        waitForStart();
       // encoderDrive(0.7,16.49,-16.49,5);//turn right 90 degrees
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



        //drive and shoot
        encoderDrive(0.5, 10, 10, 5);
        stopMotors();
        sleep(100);
        encoderShoot(0.5);
        shooting_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake_motor.setPower(1);
        sleep(2500);
        intake_motor.setPower(0);
        encoderShoot(0.5);
        sleep(100);


        //drive forward a little
        encoderDrive(0.5,5,5,5);



        //turn and drive toward wall
        strafeLeft(0.3);
        sleep(1000);
        stopMotors();



    }

    public boolean ODSBackRead(){
        if(ODSBack.getRawLightDetected() > (baseline2 * 3)){
            return true;
        } else{
            return false;
        }
    }

    /*public void turnRightEnc(double speed, double degrees, double timeOUT ){
        double leftIN = -((2*Math.PI)*(robotRadius)) * (degrees/360);
        double rightIN = ((2*Math.PI)*(robotRadius)) * (degrees/360);
        encoderDrive(speed, leftIN, rightIN, timeOUT);
    }

    public void turnLeftEnc(double speed, double degrees, double timeOUT ){
        double leftIN = ((2*Math.PI)*(robotRadius)) * (degrees/360);
        double rightIN = -((2*Math.PI)*(robotRadius)) * (degrees/360);
        encoderDrive(speed, leftIN, rightIN, timeOUT);
    }*/
}
//-------------------------------------------------------------------//


