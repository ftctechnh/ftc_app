package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Aus on 11/7/2017.
 */

//ok so this is the program that confirmed that the color sensors don't actually suck. this is just a basic program so don't use it until i do some things to make it so it doesn't crash the robot controller it stops

@Autonomous(name = "colortest", group = "Concept")
@Disabled
public class colortest extends RedRecovery {

    //Start with the basic declaration of variable strings that the phones will read

    ColorSensor cS;

    public void runOpMode() throws InterruptedException {
        //Start with the basic declaration of variable strings that the phones will read

        cS = hardwareMap.colorSensor.get("cs1");
        // Now do anything else you need to do in the initilazation phase, like calibrating the gyros, setting a color sensors lights off, etc.


        cS.enableLed(false);
        telemetry.addData("it did the thing", 1);
        telemetry.addData("Anything you need to know before starting", 1);
        telemetry.addData("What is up? ", "The Sky.");
        telemetry.update();
        waitForStart();
        cS.enableLed(true);
        while (true) {
            getmaxpoints();
        }
    }

    public void getmaxpoints(){
        if(cS.green()>cS.red() && cS.green() > cS.blue()){
            telemetry.addData("what why is green higher", "fix this now");
        }
        else if(cS.blue()>cS.red()){
            telemetry.addData("Blue rules the world", "wooooooo");
        }
        else if(cS.red() > cS.blue()){
            telemetry.addData("Red rules the world", "wooooooo");
        }
        telemetry.update();
    }

}
