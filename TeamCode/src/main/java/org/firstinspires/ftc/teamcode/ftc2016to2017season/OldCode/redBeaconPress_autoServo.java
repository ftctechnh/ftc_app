package org.firstinspires.ftc.teamcode.ftc2016to2017season.OldCode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "redBeacon1")
@Disabled
public class redBeaconPress_autoServo extends AutonomousGeneral {



    //
    boolean second_beacon_press = false;
    String currentTeam = "red";
    private ElapsedTime runtime = new ElapsedTime();
    double[] timeprofile = new double[10];
    //String currentColor = "blank";

    @Override
    public void runOpMode() {


        initiate();



        waitForStart();

        second_beacon_press = false;

        //newEncoderDrive(1,-2,0,0.1);
        runtime.reset();
        timeprofile[0] = runtime.milliseconds();
        newEncoderDriveShoot(1,135,125,1.5, 1, .1); // 150 = 5 feettime 2.5

        //lineAlign();
        //runtime.reset();
        servoBeaconPress();

        }


    public void moveToNextBeacon() {
        second_beacon_press = true;
      //  sleep(250);
        newEncoderDrive(1, 15, 15, .5);
        sleep(100);
        intake_motor.setPower(.8);
        newEncoderDriveShoot(1, -40, 40
                , 2, 1, 1.6);
//        gyro_leftTurn(270, 1);
//        shooting_motor.setPower(.8);
        sleep(100);
        shooting_motor.setPower(0);
        intake_motor.setPower(0);
        //sleep(450);
//        newEncoderDrive(1, 105, 105, 1);
//        servoBeaconPress();
}


    public void lineAlign() {

        setMotorsModeToColorSensing();
        straightDrive(1);
        while (whiteLineDetectedFront() == false) {



         //   rangeCorrection();

        }
        stopMotors();

        encoderDrive(1, -3, -3, 1);


        setMotorsModeToColorSensing();


        while (whiteLineDetectedBack() == false) {

            newTurnRight(1);
        }

        front_right_motor.setPower(0);
        front_left_motor.setPower(0);
        back_right_motor.setPower(0);
        back_left_motor.setPower(0);


    }

    public void servoBeaconPress(){
        boolean left_detected = false;
        boolean beacon_press_success = false;

        timeprofile[1] = runtime.milliseconds();
        lineAlign();
        timeprofile[2] = runtime.milliseconds();


        while (rangeSensor.getDistance(DistanceUnit.CM) > 11) {
            straightDrive(-1);
        }
        stopMotors();
        timeprofile[3] = runtime.milliseconds();


        readNewColorLeft();
        readNewColorRight();


        if(currentColorBeaconLeft.equals("blank")){
            printColorsSeen();
            setMotorsModeToColorSensing();
            while(currentColorBeaconLeft.equals("blank")){
                turnRight(1);
                readNewColorLeft();
                printColorsSeen();
            }
            stopMotors();
        }
        else if(currentColorBeaconRight.equals("blank")){
            printColorsSeen();
            setMotorsModeToColorSensing();
            while(currentColorBeaconRight.equals("blank")){
                turnLeft(1);
                readNewColorRight();
                printColorsSeen();
            }
            stopMotors();
        }
        //aligns with beacon if out of range

        if(currentColorBeaconLeft.equals(currentTeam)){
            beaconPresser.setPosition(0.0);
            left_detected = true;
        }
        else if(currentColorBeaconRight.equals(currentTeam)){
            beaconPresser.setPosition(1.0);
            left_detected = false;
        }
        //allign servo!



        pressBeaconButton();
        //presses beacon!
        timeprofile[4] = runtime.milliseconds();
        //telemetry.update();
        readNewColorRight();
        readNewColorLeft();
        moveToNextBeacon();
        timeprofile[5] = runtime.milliseconds();
        for(int i = 0; i < 6; i++){
            telemetry.addData(""+i+": ", timeprofile[i]);
        }
        telemetry.update();
        sleep(30000);
        //presses beacon!


        // below evaluate beacon press result and move to next step if it is success and handle failures if failures are seen
       /* if (left_detected == true)
        {
            if(currentColorBeaconRight.equals(currentTeam))
            {
                beacon_press_success = true;
            }
        }
        else
        {
            if(currentColorBeaconLeft.equals(currentTeam))
            {
                beacon_press_success = true;
            }
            if((currentColorBeaconLeft.equals("blank"))){
                beacon_press_success = true;
                beaconPresser.setPosition(1.0);
                sleep(100);
                beaconPresser.setPosition(0.0);
            }
        }

        if (beacon_press_success == true)
        {
            if (second_beacon_press == false)
            {
              moveToNextBeacon();
            }
            else
            {
                parkCenterVortex();
            }
        }
        else
        {
            printColorsSeen();

            if ((currentColorBeaconLeft == currentColorBeaconRight) &&
                    (currentColorBeaconLeft != "blank") &&
                    (second_beacon_press == false))
            {
                sleep(4000);

                pressBeaconButton();
            }
            else{
                telemetry.addData("failed", "we are currently working on a solution :)");
                telemetry.update();
                if(left_detected){
                    beaconPresser.setPosition(0.0);
                }
                else
                {
                    beaconPresser.setPosition(1.0);
                }
                pressBeaconButton();

            }
            sleep(500);

            parkCenterVortex();
        }*/
    }

    public void printColorsSeen(){
        telemetry.addData("left color", currentColorBeaconLeft);
        telemetry.addData("right color", currentColorBeaconRight);
        //telemetry.update();
    }

    public void parkCenterVortex()
    {
        if (second_beacon_press)
        {
            newEncoderDrive(1, 22, -22, .5);
            newEncoderDrive(1, 140, 140, 1);
            sleep(500);
            newEncoderDrive(1, -10, -10, .2);
            newEncoderDrive(1, 30, 30, .5);
        }
        else
        {
            newEncoderDrive(1, -22, 22, 2);
            newEncoderDrive(1, 140, 100, 2);
            sleep(500);
            newEncoderDrive(1, -10, -10, .2);
            newEncoderDrive(1, 30, 30, .5);
        }
    }


    public void pressBeaconButton()
    {
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+10;

        encoderDrive(1, -distFromWall, -distFromWall, 1);
//
        sleep(500);
//
        encoderDrive(1, distFromWall, distFromWall, 1);

//        encoderDrive(.3, -distFromWall, -distFromWall, 1);
//        sleep(500);
//        encoderDrive(.3, distFromWall, distFromWall, 1);
        beaconPresser.setPosition(0.0);
    }

}
//-------------------------------------------------------------------//


