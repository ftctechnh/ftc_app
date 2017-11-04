package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorDistance9330;
import org.firstinspires.ftc.teamcode.subsystems.ColorSensor9330;
import org.firstinspires.ftc.teamcode.subsystems.CrystalArm9330;
import org.firstinspires.ftc.teamcode.subsystems.Drive9330;
import org.firstinspires.ftc.teamcode.subsystems.Gyro9330;
import org.firstinspires.ftc.teamcode.subsystems.Vuforia9330;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by robot on 9/25/2017.
 */

@Autonomous(name="AutoVuforia9330", group="Opmode")  // @Autonomous(...) is the other common choice
public class AutoVuforia9330 extends LinearOpMode {

    Hardware9330 robotMap = new Hardware9330();
    ColorDistance9330 colorDistance;
    ColorSensor9330 cs9330;
    //Vuforia9330 PictographScan = new Vuforia9330();
    //Gyro9330 gyro = new Gyro9330(robotMap);
    Drive9330 drive;
    CrystalArm9330 crystalarm;
    //Integer TurnError = 1;
    //Double TurnSpeed = 0.1;
    //VuforiaTrackables info;
    Double PictoYRotation;
    Double PictoZTranslation;
    String PictoImageType;
    Integer ColorRed;
    Integer ColorGreen;
    Integer ColorBlue;
    Integer ColorAlpha;
    boolean onRedTeam;

    public void log(String name, Object value) {
        telemetry.clear();
        telemetry.addData(name,value);
        telemetry.update();
    }

    public void checkStop() {
        if (isStopRequested()) stop();
    }

    public void updatePictographInfo(HashMap hm) {
        if (!hm.isEmpty()) {
            Set set = hm.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                if (me.getKey() == "Y Rotation") PictoYRotation = (Double) me.getValue();
                else if (me.getKey() == "Z Translation (Distance)")
                    PictoZTranslation = (Double) me.getValue();
                else if (me.getKey() == "Image Position") PictoImageType = (String) me.getValue();
                //log(me.getKey().toString(), me.getValue()); //Adds value and Info to telemetry
            }
        }
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
        crystalarm = new CrystalArm9330(robotMap);
        colorDistance = new ColorDistance9330(robotMap);
        //gyro.init();    //initializes gyro
       //info = PictographScan.init(hardwareMap,true);   //initializes Vuforia

        log("Info","Initialized. Press start when ready.");
        waitForStart();
       /* while(opModeIsActive()) {

            log("Info","Searching for image...");
            while (PictoImageType == null) {    //While pictograph hasn't been found, scan for it
                telemetry.clear();
                updatePictographInfo(PictographScan.checkPosition(info));
                checkStop();
            }

            log("Info","Found image! Centering to the wall...");
            while (-TurnError > PictoYRotation || TurnError < PictoYRotation) { //while current rotation is outside allowed error
                updatePictographInfo(PictographScan.checkPosition(info));   //update current positioning
                log("Rotation of pictogram", PictoYRotation.toString());
                if (PictoYRotation > 0) //Align self more parallel to the wall
                    drive.gyroTurn(2, TurnSpeed);
                else
                    drive.gyroTurn(-2, TurnSpeed);

                checkStop();
            }

            drive.stopDrive();
            log("Info","Centered to the wall!"); */

            while (ColorRed == null || ColorBlue == null) { //while color is unknown
                updateColorDistance(colorDistance.getInfo()); // Check the color of the pad beneath you
                checkStop();
            }

                crystalarm.lowerArmServo();
                sleep(800);

                if (ColorRed > ColorBlue) {
                    onRedTeam = true;
                    log("Info", "We are red! Knocking down blue.");
                    if(cs9330.r() > cs9330.b()){
                        //drive.gyroTurn(90, TurnSpeed,false);
                        drive.driveForward(0.5);
                        sleep(300);
                        drive.stopDrive();
                        crystalarm.raiseArmServo();
                        //drive.gyroTurn(180, TurnSpeed,false);
                    }else{
                        //drive.gyroTurn(-90,TurnSpeed,false);
                        drive.driveForward(-0.5);
                        sleep(300);
                        drive.stopDrive();
                        crystalarm.raiseArmServo();
                    }
                } else {
                    onRedTeam = false;
                    log("Info", "We are blue! Knocking down red.");
                    if(cs9330.r() > cs9330.b()){
                        //drive.gyroTurn(-90, TurnSpeed,false);
                        drive.driveForward(-0.5);
                        sleep(300);
                        drive.stopDrive();
                        crystalarm.raiseArmServo();
                    }else{
                        //drive.gyroTurn(90, TurnSpeed,false);
                        drive.driveForward(0.5);
                        sleep(300);
                        drive.stopDrive();
                        crystalarm.raiseArmServo();
                        //drive.gyroTurn(180, TurnSpeed,false);
                    }
                }
            log("Info", "Target knocked down!");

                /*
            info = PictographScan.init(hardwareMap,true);   //initializes Vuforia

            while(opModeIsActive()) {

                log("Info","Searching for image...");
                while (PictoImageType == null) {    //While pictograph hasn't been found, scan for it
                    telemetry.clear();
                    updatePictographInfo(PictographScan.checkPosition(info));
                    checkStop();
                }

                log("Info","Found image! Centering to the wall...");
                while (-TurnError > PictoYRotation || TurnError < PictoYRotation) { //while current rotation is outside allowed error
                    updatePictographInfo(PictographScan.checkPosition(info));   //update current positioning
                    log("Rotation of pictogram", PictoYRotation.toString());
                    if (PictoYRotation > 0) //Align self more parallel to the wall
                        drive.gyroTurn(2, TurnSpeed,false);
                    else
                        drive.gyroTurn(-2, TurnSpeed,false);

                    checkStop();
                }

                drive.stopDrive();
                log("Info","Centered to the wall!");
                */

            while(!isStopRequested() && robotMap.touch.getState()) {
            //wait for manual direction to end code
            }
            stop();
        }

    }



