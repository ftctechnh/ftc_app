package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "beaconPressDrive")
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
            readNewColor();
        }

        encoderDrive(0.85, 25, 22, 15);

        readNewColor();

        if(currentColor == currentTeam){

            telemetry.addData("It worked! The current color is", currentColor);
            telemetry.update();
        }
    }

    public void readNewColor(){

        String currentColor = "blank";

        if(colorSensor.red() > colorSensor.blue()){
            currentColor = "red";

            telemetry.addData("current color is red", colorSensor.red());
            telemetry.update();
        }

        else if(colorSensor.red() > colorSensor.blue()){
            currentColor = "blue";

            telemetry.addData("current color is blue", colorSensor.blue());
            telemetry.update();
        } else {

            currentColor = "blank";
        }
    }
}