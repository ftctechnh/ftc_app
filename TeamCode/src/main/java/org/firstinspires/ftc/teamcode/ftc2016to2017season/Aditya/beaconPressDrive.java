package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "beaconPressDrive")
@Disabled
public class beaconPressDrive extends AutonomousGeneral {

    String currentTeam = "red";
    //this declares the current team. Make sure you only use "red" for read, and "blue" for blue
    String currentColor = "blank";

    @Override
    public void runOpMode(){

        initiate();

        waitForStart();

        while(currentColor != currentTeam){
            //This turns the robot and reads the color until it sees the color it wants
            turnLeft(0.25);
            readNewColorLeft();
        }

        encoderDrive(0.85, 25, 22, 15);

        readNewColorLeft();

        if(currentColor == currentTeam){

            telemetry.addData("It worked! The current color is", currentColor);
            telemetry.update();
        }
    }

//    public void readNewColor(){
//
//        String currentColor = "blank";
//
//        if(colorSensor.red() > colorSensor.blue()){
//            currentColor = "red";
//
//            telemetry.addData("current color is red", colorSensor.red());
//            telemetry.update();
//        }
//
//        else if(colorSensor.red() > colorSensor.blue()){
//            currentColor = "blue";
//
//            telemetry.addData("current color is blue", colorSensor.blue());
//            telemetry.update();
//        } else {
//
//            currentColor = "blank";
//        }
//    }
}