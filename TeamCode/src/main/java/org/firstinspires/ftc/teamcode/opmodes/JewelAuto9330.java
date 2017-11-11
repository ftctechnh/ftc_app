package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorDistance9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorSensor9330;
import org.firstinspires.ftc.teamcode.subsystems.JewelArm9330;
import org.firstinspires.ftc.teamcode.subsystems.Drive9330;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by robot on 11/10/2017.
 */
@Autonomous(name="JewelAuto9330", group="Opmode")
public class JewelAuto9330 extends LinearOpMode{

    Hardware9330 robotMap = new Hardware9330();
    ColorDistance9330 colorDistance;
    ColorSensor9330 cs9330;
    Drive9330 drive;
    JewelArm9330 jewelArm;
    Integer ColorRed;
    Integer ColorGreen;
    Integer ColorBlue;
    Integer ColorAlpha;
    boolean onRedTeam; // will be used to reverse the motor direction based off of alliance

    public void log(String name, Object value) {
        telemetry.clear();
        telemetry.addData(name,value);
        telemetry.update();
    }

    public void checkStop() {
        if (isStopRequested()) stop();
    }

    public void updateColorDistance(HashMap hm) {
        if (!hm.isEmpty()) {
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Alpha") ColorAlpha = (Integer)me.getValue();
                else if (me.getKey() == "Red") ColorRed = (Integer)me.getValue();
                else if (me.getKey() == "Green") ColorGreen = (Integer)me.getValue();
                else if (me.getKey() == "Blue") ColorBlue = (Integer)me.getValue();
                //log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        log("Info","Initializing. Please wait.");

        //Everything commented out at the top is only there if moving it to the bottom was a mistake

        robotMap.init(hardwareMap); //initializes hardware map
        cs9330 = new ColorSensor9330(robotMap);
        drive = new Drive9330(robotMap);
        jewelArm = new JewelArm9330(robotMap);
        colorDistance = new ColorDistance9330(robotMap);

        log("Info","Initialized. Press start when ready.");
        waitForStart();

        while (ColorRed == null || ColorBlue == null) { //while color is unknown
            updateColorDistance(colorDistance.getInfo()); // Check the color of the pad beneath you
            checkStop();
        }

        jewelArm.lowerArmServo();
        sleep(800);

        if (ColorRed > ColorBlue) {
            onRedTeam = true;
            log("Info", "We are red! Knocking down blue.");
            if(cs9330.r() > cs9330.b()){
                //drive.gyroTurn(90, TurnSpeed,false);
                drive.driveForward(-0.5);
                sleep(300);
                drive.stopDrive();
                jewelArm.raiseArmServo();
                //drive.gyroTurn(180, TurnSpeed,false);
            }else{
                //drive.gyroTurn(-90,TurnSpeed,false);
                drive.driveForward(0.5);
                sleep(300);
                drive.stopDrive();
                jewelArm.raiseArmServo();
            }
        } else {
            onRedTeam = false;
            log("Info", "We are blue! Knocking down red.");
            if(cs9330.r() > cs9330.b()){
                //drive.gyroTurn(-90, TurnSpeed,false);
                drive.driveForward(0.5);
                sleep(300);
                drive.stopDrive();
                jewelArm.raiseArmServo();
            }else{
                //drive.gyroTurn(90, TurnSpeed,false);
                drive.driveForward(-0.5);
                sleep(300);
                drive.stopDrive();
                jewelArm.raiseArmServo();
                //drive.gyroTurn(180, TurnSpeed,false);
            }
        }
        log("Info", "Target knocked down!");

        while(!isStopRequested() && robotMap.touch.getState()) {
            //wait for manual direction to end code
        }
        stop();
    }

}
