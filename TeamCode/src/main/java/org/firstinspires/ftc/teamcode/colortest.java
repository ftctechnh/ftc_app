package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Aus on 11/7/2017.
 */

@Autonomous(name = "Autonomous", group = "Autonomous")

public class colortest extends AutonomousSetup {

    //Start with the basic declaration of variable strings that the phones will read

    ColorSensor cS;

    public void runOpMode() throws InterruptedException {
        //Start with the basic declaration of variable strings that the phones will read

        cS = hardwareMap.colorSensor.get("cs1");
        // Now do anything else you need to do in the initilazation phase, like calibrating the gyros, setting a color sensors lights off, etc.


        cS.enableLed(true);
        telemetry.addData("it did the thing", 1);
        telemetry.addData("Anything you need to know before starting", 1);
        telemetry.addData("What is up? ", "The Sky.");
        telemetry.update();
        waitForStart();

        while (true) {
            getmaxpoints();
        }
    }

    public void getmaxpoints(){
        telemetry.update();
        if(cS.red() > cS.blue()){
            telemetry.addData("Red rules the world ", "wooooooo");
        }
        else if(cS.blue()>cS.red()){
            telemetry.addData("Blue rules the world ", "wooooooo");
        }
        telemetry.update();
    }

}
