package org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "blueBeacon")
@Disabled
public class blueBeacon extends AutonomousGeneral {



    //
    boolean second_beacon_press = false;
    String currentTeam = "blue";
    private ElapsedTime runtime = new ElapsedTime();
    double[] timeprofile = new double[30];
    int profile_index =0;
    //String currentColor = "blank";

    @Override
    public void runOpMode() {


        initiate();


        sleep(3000);
        waitForStart();

        second_beacon_press = false;
        runtime.reset();
        timeprofile[profile_index++] = runtime.milliseconds();
        setMotorsModeToEncDrive();
        stopMotors();
        encoderDriveShootBlue(.7,-145,-130,3, -2, 1);
        servoBeaconPress();
        }


    public void moveToNextBeacon() {
        second_beacon_press = true;
      //  sleep(250);
        newEncoderDrive(0.7, 15, 15, .5);
        sleep(100);
        intake_motor.setPower(.8);

        encoderDriveShootBlue(.6,-31,31,3, -20, 1); // 150 = 5 feettime 2.5

        sleep(100);
        shooting_motor.setPower(0);
        intake_motor.setPower(0);
        //sleep(450);
        setMotorsModeToEncDrive();
        stopMotors();
        encoderDrive(.7, -105, -105, 5);
        servoBeaconPress();
}


    public void lineAlign() {

        double targetDistLine = back_left_motor.getCurrentPosition() - (int) (20 * getCountsPerCm());
        setMotorsModeToColorSensing();
        straightDrive(-0.7);
        while (whiteLineDetectedFront() == false && back_left_motor.getCurrentPosition() > targetDistLine)
        {

        }
     //   timeprofile[profile_index++] = runtime.milliseconds();
        stopMotors();

        if(back_left_motor.getCurrentPosition() <= targetDistLine){//if you havent reached the white line yet, correct for that
            encoderDrive(.7,-15,15,5);
            encoderDrive(.7, 20,20,5);
            encoderDrive(.7,15,-15,5);

            lineAlign();
        }
        else
        {

            //   timeprofile[profile_index++] = runtime.milliseconds();
            if (second_beacon_press) {
                // encoderDrive(.7, -5, -5, 1);
            } else {
                //encoderDrive(.7, -5, -5, 1);
            }


            setMotorsModeToColorSensing();
            //   timeprofile[profile_index++] = runtime.milliseconds();

            targetDistLine = back_left_motor.getCurrentPosition() + (int) (40 * getCountsPerCm());
            newTurnRight(.7);
            while (whiteLineDetectedBack() == false && back_left_motor.getCurrentPosition()<targetDistLine) {


            }

            //timeprofile[profile_index++] = runtime.milliseconds();
            front_right_motor.setPower(0);
            front_left_motor.setPower(0);
            back_right_motor.setPower(0);
            back_left_motor.setPower(0);
            if(back_left_motor.getCurrentPosition()>= targetDistLine){
                double walldist = rangeSensor.getDistance(DistanceUnit.CM);
                newTurnLeft(.7);
                while(whiteLineDetectedBack() == false && walldist>= rangeSensor.getDistance(DistanceUnit.CM)){
                    //search for a minimum distance, which would be when the robot is perpendicular to the wall
                }
                front_right_motor.setPower(0);
                front_left_motor.setPower(0);
                back_right_motor.setPower(0);
                back_left_motor.setPower(0);
            }
        }


    }

    public void servoBeaconPress(){
        boolean left_detected = false;
        boolean beacon_press_success = false;

        timeprofile[profile_index++] = runtime.milliseconds();
        lineAlign();
        timeprofile[profile_index++] = runtime.milliseconds();


        while (rangeSensor.getDistance(DistanceUnit.CM) > 11) {
            straightDrive(-0.7);
        }
        stopMotors();
        timeprofile[profile_index++] = runtime.milliseconds();


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
        timeprofile[profile_index++] = runtime.milliseconds();
        //telemetry.update();
        readNewColorRight();
        readNewColorLeft();
        if (second_beacon_press)
        {
            parkCenterVortex();
        }
        else {
            moveToNextBeacon();
        }
        timeprofile[profile_index++] = runtime.milliseconds();
        for(int i = 0; i < profile_index; i++){
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

            encoderDrive(.7, -15, 15, 5);
            setMotorsModeToEncDrive();
            stopMotors();
            encoderDrive(.8, 135, 145, 5);
            sleep(500);
            encoderDrive(.7, -10, -10, 5);
            encoderDrive(.7, 30, 30, 5);
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
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+8;

        encoderDrive(.8, -distFromWall, -distFromWall, 1);
//
        sleep(200);
//
        encoderDrive(.8, distFromWall, distFromWall, 1);

//        encoderDrive(.3, -distFromWall, -distFromWall, 1);
//        sleep(500);
//        encoderDrive(.3, distFromWall, distFromWall, 1);
        beaconPresser.setPosition(0.0);
    }

}
//-------------------------------------------------------------------//


