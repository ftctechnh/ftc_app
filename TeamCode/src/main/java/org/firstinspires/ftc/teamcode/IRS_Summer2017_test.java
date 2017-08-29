package org.firstinspires.ftc.teamcode;//Lots of important stuff

//Created by Eric on 8/28/2017

import com.qualcomm.robotcore.eventloop.opmode.Disabled;//more important stuff
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

@TeleOp (name="Eric: Test IRS", group="Eric IRS")//Name and things
@Disabled
public class IRS_Summer2017_test extends LinearOpMode{//public class

    IrSeekerSensor irs;//define irs

    @Override
    public void runOpMode() throws InterruptedException {//fixing some stuff 'cause of coding demands

        irs = hardwareMap.irSeekerSensor.get("sensor_irs"); //give irs a value

        waitForStart();//what the code implies

        while (opModeIsActive()){//what the code implies

            telemetry.addData("Strength",   String.valueOf(irs.getStrength()));//ir strength
            telemetry.addData("Angle",  irs.getAngle());//ir angle
            telemetry.addData("Pathagorusatized",  getPathagorus(irs.getStrength(), irs.getAngle()));//both pathogorusatized for fun
            telemetry.update();//what the code implies

        }

    }



    private double getPathagorus(int a, int b){//Pythagorean theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;
    }

    private double getPathagorus(double a, double b){//Override to double in case of angry sensors
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;
    }

}
