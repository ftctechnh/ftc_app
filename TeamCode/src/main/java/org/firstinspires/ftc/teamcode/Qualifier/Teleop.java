package org.firstinspires.ftc.teamcode.Qualifier;


import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import RicksCode.Bill_Adapted.ConfigFileHandler;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.FAST;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.MID;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.SLOW;
import static java.lang.Math.atan;


@TeleOp(name = "zMoo", group = "8045")  // @Autonomous(...) is the other common choice
//@Disabled
public class Teleop extends OpMode {

    RobotRR gromit;

    // variables used during the configuration process

    double turnDirection;

    private ElapsedTime runtime = new ElapsedTime();
    double timeLeft;
    double startclosetime = 0.0;
    double startclamptime = 0.0;

    //Booleans
    public boolean backIsReleased = true;
    public boolean back2IsReleased = true;
    public boolean rightbtnIsReleased = true;
    public boolean rightbtn2IsReleased = true;
    public boolean xIsReleased = true;
    //public boolean aIsReleased = true;
    public boolean start2IsReleased = true;
    public boolean righttriggerIsReleased = true;
    public boolean tristanmode = false;
    boolean onehitwonder = false;
    boolean out = true;                          // what the heck does out mean?
    int blocks = 0;

    //SLOW SERVO
    public boolean elbowmoving = false;
    public double elbowtotalmove = 0;
    double elbowstarttime = 0.0;
    double elbowstartpos = 0.0;
    double movetime = 0;
    double elbowtarget = 0.0;


    public boolean leftbumperIsReleased = true;
    public boolean leftttriggerIsReleased = true;
    public boolean glyphLiftismoving = false;
    public boolean delayLift = false;
    public boolean delayClamp = false;
    public boolean glyphSensed = false;
    public int manualLiftDelay = 200;
    public int glyphSensedDelay = 0;
    boolean glyphinit = false;


    public boolean padupIsReleased = true;
    public boolean paddownIsReleased = true;
    public boolean aIsReleased = true;

    public int liftTarget = 0;
    public boolean trainon = false;
    boolean reliczeroing = false;
    double relicspeed = .6;

    // file variables
    public int elbowdowntime = 2000;
    public double elbowclampposition = 0.4;

    @Override
    public void init() {

        gromit = new RobotRR();
        gromit.init(hardwareMap);
        telemetry.addData("Status", "Initialized");

        turnDirection = 1;
        timeLeft = 120;
//        writeTeleDataToTxtFile(hardwareMap.appContext);
        readTeleDataFromTxtFile(hardwareMap.appContext);
//       lastLoadTime = -10000;
//        telemetry.addData("elbowmovetime", elbowdowntime);
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        timeLeft = 120 - runtime.seconds();

        double sharpIRVoltage = gromit.driveTrain.sharpIRSensor.getVoltage();
        double IRdistance = 18.7754 * Math.pow(sharpIRVoltage, -1.51);
        telemetry.addData("ElbowTime from file", elbowdowntime);
        telemetry.addData("Sharp IR V ", sharpIRVoltage);
        telemetry.addData("Sharp IR ", "cm %4.1f ", IRdistance);
        RobotLog.ii("[Gromit] IR", Double.toString(IRdistance));
//        RobotLog.ii("[Gromit] IR", Double.toString(IRdistance), " Ticks " + Integer.toString(gromit.driveTrain.left_front.getCurrentPosition()));

        //------------------------------------------------------------------------------
        //toggle  drive direction-when the button was released and it is now pressed.
        //------------------------------------------------------------------------------
        if (gamepad1.right_stick_button) {
            if (rightbtnIsReleased) {
                rightbtnIsReleased = false;
                gromit.driveTrain.frontIsForward = !gromit.driveTrain.frontIsForward;
            }
        } else {
            rightbtnIsReleased = true;
        }
        //------------------------------------------------------------------------------
        //------------------------------------------------------------------------------
        //Lift/lower glyph  x/a by increments,   y/b manually
        //------------------------------------------------------------------------------
        if (gamepad1.start) {
            gromit.glyphTrain.glyphlifttop("top");
        } else if (gamepad1.left_stick_button) {
            gromit.glyphTrain.glyphlifttop("bottom");

        }

/**
 * NEW LIFT CODE
 */
        //RAISE LIFT
        /////////////////
        if (gamepad1.x || gamepad2.x) {  // raise lift
            if (xIsReleased) {
                startclosetime = runtime.milliseconds();    // start timer  set boolean
                delayLift = true;//Start the delay
                xIsReleased = false;                                              // button toggle
                if (blocks == 0) {//LIFT FIRST SECTION
                    gromit.glyphTrain.glyphclampupper("close");
                    blocks = 0;
                }
                //LIFT SECOND SECTION
                else {
                    gromit.glyphTrain.glyphclamp("close");
                    gromit.glyphTrain.liftIndex = Math.min(gromit.glyphTrain.liftIndex + 1, 2);   //add one to index, max is 2
//  check to see if you're lower than the next lower position by 400 ticks, stop there first.
                    if (gromit.glyphTrain.lift_motor.getCurrentPosition() + 200 < gromit.glyphTrain.liftPosition[gromit.glyphTrain.liftIndex - 1]) {
                        gromit.glyphTrain.liftIndex -= 1;
                    }
                    liftTarget = gromit.glyphTrain.liftPosition[gromit.glyphTrain.liftIndex];  // set the new Target
                }

                glyphLiftismoving = true;
            }
        } else {
            xIsReleased = true;
        }
// check to see if enough time has elapsed to start lift   (automatic lift after clamp)
        if (delayClamp && runtime.milliseconds() - startclosetime > glyphSensedDelay) {     // this is the delay time in milliseconds
            delayClamp = false;
            glyphSensedDelay = 0;
            startclamptime = runtime.milliseconds();            // start timer to make sure lift waits the right amount of time set boolean
            delayLift = true;
             if (blocks == 0) {
                gromit.glyphTrain.glyphclampupper("close");
            } else {
                gromit.glyphTrain.glyphclamp("close");
            }
        }
        if (delayLift && runtime.milliseconds() - startclamptime > manualLiftDelay) {     // this is the delay time in milliseconds
            delayLift = false;
            glyphSensedDelay = 0;
             if (blocks == 0) {
                gromit.glyphTrain.glyphlifttop("top");
                blocks = 1;
            } else {
                gromit.glyphTrain.lift_motor.setPower(1.0);   // start the motor going up
            }
        }

        if (gamepad1.a || gamepad2.a) {         //lower
            if (aIsReleased) {
                gromit.glyphTrain.glyphclamp("wide");   // OPEN BOTH SEROVS
                gromit.glyphTrain.glyphclampupper("open");
                aIsReleased = false;
                glyphLiftismoving = true;
                gromit.glyphTrain.liftIndex = 0;  //down always goes to zero
                liftTarget = 0;
                gromit.glyphTrain.lift_motor.setPower(-0.9);   // start the motor going down
                gromit.glyphTrain.glyphlifttop("bottom");//Lower second Stage
//                gromit.glyphTrain.liftIndex = Math.max(gromit.glyphTrain.liftIndex - 1, 0);  //subtract one from index, min is
//                gromit.glyphTrain.liftGlyphIndex(gromit.glyphTrain.liftIndex);  //lower
                blocks = 0;
            }
        } else {
            aIsReleased = true;
        }
        // check if target is reached yet
        if (glyphLiftismoving) {
            if (liftTarget == 0) {  // going down
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() <= liftTarget + 200) {  // slow down before overshooting zero
                    gromit.glyphTrain.lift_motor.setPower(-0.3);

                }
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() <= liftTarget) {//Cut the motor off at 0
                    gromit.glyphTrain.lift_motor.setPower(0.0);
                    glyphLiftismoving = false;
                }
            } else {//GOING UP
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() >= liftTarget) { //If it exceeds the target cut it off
                    gromit.glyphTrain.lift_motor.setPower(0.0);
                    glyphLiftismoving = false;
                }
            }
        }
        //------------------------------------------------------------------------------
        // manual glyph lift
        if (gamepad1.y || gamepad2.y) {
            glyphLiftismoving = false;
            if (gromit.glyphTrain.lift_motor.getCurrentPosition() < gromit.glyphTrain.upperLiftLimit) {
                gromit.glyphTrain.lift_motor.setPower(1.0);
            } else {
                gromit.glyphTrain.lift_motor.setPower(0.0);
            }
        } else if (gamepad1.b || gamepad2.b) {
            glyphLiftismoving = false;
            if (gromit.glyphTrain.lift_motor.getCurrentPosition() > gromit.glyphTrain.lowerLiftLimit) {
                gromit.glyphTrain.lift_motor.setPower(-0.8);
            } else {
                gromit.glyphTrain.lift_motor.setPower(0.0);
            }

        } else if (!glyphLiftismoving) {
            gromit.glyphTrain.lift_motor.setPower(0.0);
        }

        //set drive speed
        if (gamepad1.left_bumper) {
            gromit.driveTrain.setSpeedMode(FAST);
            relicspeed = 1.0;
        } else if (gamepad1.left_trigger > 0.1) {
            gromit.driveTrain.setSpeedMode(SLOW);
            relicspeed = 0.2;
        } else {
            gromit.driveTrain.setSpeedMode(MID);
            relicspeed = 0.6;
        }
//on and off glyph intake
        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            if (paddownIsReleased) {
                paddownIsReleased = false;
                if (trainon) {
                    trainon = false;
                    gromit.glyphTrain.stopGlyphMotors();
                } else {
                    trainon = true;
                    gromit.glyphTrain.startGlyphMotors(1.0);
                }
            }
        } else {
            paddownIsReleased = true;
        }
        //BACKWARDS
        if (gamepad1.dpad_left || gamepad2.dpad_left) {
            if (padupIsReleased) {
                padupIsReleased = false;
                if (trainon) {
                    trainon = false;
                    gromit.glyphTrain.stopGlyphMotors();
                } else {
                    trainon = true;
                    gromit.glyphTrain.startGlyphMotors(-0.4);
                }
            }
        } else {
            padupIsReleased = true;
        }

        //  check for incoming block  here
        if (trainon) {
            if (gromit.glyphTrain.sensorDistance.getDistance(DistanceUnit.CM) < 12 && !glyphSensed) {     // if block is sensed set boolean
                glyphSensed = true;
            } else if (glyphSensed && gromit.glyphTrain.sensorDistance.getDistance(DistanceUnit.CM) > 12) {     // if block was already sensed (sense the back end)
                glyphSensed = false;
                startclosetime = runtime.milliseconds();    // start timer  set boolean
                //delayLift = true;                            // check for time to lift
                delayClamp = true;
                glyphSensedDelay = 300;
                // set target  as 1 (assume we're at zero for now
                liftTarget = gromit.glyphTrain.liftPosition[1];  // set the new Target
                glyphLiftismoving = true;     // turn on manual override

            }
        }

        //Second controller Clamps
        if (gamepad2.right_trigger > 0.1) {
            if (gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.deploydistance) {
                if (blocks != 0) {
                    gromit.glyphTrain.glyphclamp("open");
                }
            } else {
                gromit.relicArm.clawOpen();
            }
        } else if (gamepad2.right_bumper) {
            if (gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.deploydistance) {
                if (blocks != 0) {
                    gromit.glyphTrain.glyphclamp("close");
                }
            } else {
                gromit.relicArm.clawClose();
            }
        }
        if (gamepad2.left_trigger > 0.1 && gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.deploydistance) {
            //Open Top Clamps
            gromit.glyphTrain.glyphclampupper("open");
        }
        else  if (gamepad2.left_bumper && gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.deploydistance) {
            //Close top clamps
            gromit.glyphTrain.glyphclampupper("close");
        }



        // glyph clamp
        if (gamepad1.right_trigger > 0.1) {
            if (blocks != 0) {
                gromit.glyphTrain.glyphclamp("open");
            }
        } else if (gamepad1.right_bumper) {
            if (blocks != 0) {
                gromit.glyphTrain.glyphclamp("close");
            }
        }

        //Boolean Prototype
        /*if (gamepad1.back) {
            if (backIsReleased) {
                backIsReleased = false;
                if (tristanmode) {
                    tristanmode = false;
                } else {
                    tristanmode = true;
                }
            }
        } else {
            backIsReleased = true;
        }*/

//        if (tristanmode) {
        // gromit.driveTrain.drivevector(gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection * gamepad1.left_stick_x);
//        } else {
        gromit.driveTrain.drivesmart(-gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection * gamepad1.left_stick_x);
//        }
        /**
         * RELIC CONTROLS
         */
        //SLOW RELIC ELBOW
        //Zero position
        if (gamepad2.start && gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.deploydistance) {
            if (start2IsReleased) {//IF CHANGE IN STATE
                start2IsReleased = false;
                movetime = 500;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();//Start time
                elbowtarget = gromit.relicArm.elbowdown;
                elbowtotalmove = elbowtarget - elbowstartpos;
            }
        } else {
            start2IsReleased = true;
        }
        //Middle Position
        if (gamepad2.left_trigger > 0.1 && gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.deploydistance) {
            if (leftttriggerIsReleased) {//IF CHANGE IN STATE
                leftttriggerIsReleased = false;
                movetime = 500;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();//Start time
                if (gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.relicArmMotorMax - 1000) {
                    elbowtarget = gromit.relicArm.elbowup + .09;
                } else {
                    elbowtarget = gromit.relicArm.elbowup;
                }
                elbowtotalmove = elbowtarget - elbowstartpos;
            }
        } else {
            leftttriggerIsReleased = true;
        }
        //TOP POSITION
        if (gamepad2.left_bumper && gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.deploydistance) {
            if (leftbumperIsReleased) {//IF CHANGE IN STATE
                leftbumperIsReleased = false;
                movetime = 700;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();//Start time
                elbowtarget = gromit.relicArm.elbowtop;
                elbowtotalmove = elbowtarget - elbowstartpos;
                //Start relic arm in to zero it for easy driving
                reliczeroing = true;
                gromit.relicArm.relicArmMotor.setPower(-0.6);
            }
        } else {
            leftbumperIsReleased = true;
        }
        if (elbowmoving) {
            gromit.relicArm.relicElbowServo.setPosition(((runtime.milliseconds() - elbowstarttime) / movetime) * elbowtotalmove + elbowstartpos);
            if (runtime.milliseconds() > elbowstarttime + movetime) {
                elbowmoving = false;
            }
        }
        if (reliczeroing) {
            if (gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.deploydistance) {
                gromit.relicArm.relicArmMotor.setPower(0.0);
                reliczeroing = false;
            }
        }


        //Clamp slowly
        if (gamepad2.right_stick_y > 0.1) {
            gromit.relicArm.relicClawServo.setPosition(gromit.relicArm.relicClawServo.getPosition() + .005);
        } else if (gamepad2.right_stick_y < -0.1) {
            gromit.relicArm.relicClawServo.setPosition(gromit.relicArm.relicClawServo.getPosition() - .005);
        }

        //Elbow Slowly
        if (gamepad2.left_stick_y < -0.1 && gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.deploydistance) {
            gromit.relicArm.relicElbowServo.setPosition(gromit.relicArm.relicElbowServo.getPosition() + 0.004);
        } else if (gamepad2.left_stick_y > 0.1 && gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.deploydistance) {
            gromit.relicArm.relicElbowServo.setPosition(gromit.relicArm.relicElbowServo.getPosition() - 0.004);
        }


        // RELIC ARM IN/OUT
        if (gamepad1.dpad_up && gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.relicArmMotorMax && !onehitwonder && !reliczeroing) {
            if (gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.deploydistance && !glyphinit) {
                glyphinit = true;
                gromit.relicArm.relicElbowServo.setPosition(gromit.relicArm.elbowup);
            }
            gromit.relicArm.relicArmMotor.setPower(relicspeed);
        } else if (gamepad1.dpad_down && gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.relicArmMotorMin && !onehitwonder && !reliczeroing) {
            gromit.relicArm.relicArmMotor.setPower(-relicspeed);
        } else if (!onehitwonder && !reliczeroing) {
            gromit.relicArm.relicArmMotor.setPower(0.0);
        }

        ///ONE BUTTON RELIC RECOVERY
        if (gamepad2.right_stick_button) {
            if (rightbtn2IsReleased) {//IF CHANGE IN STATE
                rightbtn2IsReleased = false;
                onehitwonder = true;
                gromit.relicArm.relicArmMotor.setPower(0.9);  // start powering out the arm

                //Begin ELbow movev
                movetime = elbowdowntime;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();                            //Start time
//                elbowtarget = gromit.relicArm.elbowup + .05;
                elbowtarget = elbowclampposition;                     //value from text file
                elbowtotalmove = elbowtarget - elbowstartpos;
            }
        } else {
            rightbtn2IsReleased = true;
        }
        if (onehitwonder) {
            if (gromit.relicArm.relicArmMotor.getCurrentPosition() > 4900 && out) {
                gromit.relicArm.relicArmMotor.setPower(0.0);                          // all the way out, stop the motor
                out = false;
                //gromit.relicArm.relicElbowServo.setPosition(elbowtarget);
                // maybe a wait here to make sure the elbow is all the way down?
                sleep(300);
                gromit.relicArm.clawOpen();      // unclamp
                sleep(500);
                while (gromit.relicArm.relicClawServo.getPosition() > 0.45) {           // wait for claw to open enough?
                }
                gromit.relicArm.relicArmMotor.setPower(-0.7);                          // rewind the arm
                movetime = 700;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();//Start time
                elbowtarget = gromit.relicArm.elbowtop;                                 // set elbow target
                elbowtotalmove = elbowtarget - elbowstartpos;
            } else if (gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.deploydistance && !out) {    // out changes if it is on the way out or in
                gromit.relicArm.relicArmMotor.setPower(0.0);
                out = true;
                onehitwonder = false;
            }
        }
        //Telemetry
        //telemetry.addLine("Time Left: " + timeLeft);
        //telemetry.addData("liftindex", gromit.glyphTrain.liftIndex);
        //telemetry.addData("liftPosition", gromit.glyphTrain.lift_motor.getCurrentPosition());
        telemetry.addData("glyph sensor", gromit.glyphTrain.sensorDistance.getDistance(DistanceUnit.CM));
        telemetry.addData("relicArmTicks", gromit.relicArm.relicArmMotor.getCurrentPosition());


    }


    /**
     * This method puts the current thread to sleep for the given time in msec.
     * It handles InterruptException where it recalculates the remaining time
     * and calls sleep again repeatedly until the specified sleep time has past.
     *
     * @param sleepTime specifies sleep time in msec.
     */
    public static void sleep(long sleepTime) {
        long wakeupTime = System.currentTimeMillis() + sleepTime;

        while (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
            sleepTime = wakeupTime - System.currentTimeMillis();
        }
    }   //sleep

    @Override
    public void stop() {
        gromit.driveTrain.stopMotors();
        gromit.glyphTrain.stopGlyphMotors();
        gromit.glyphTrain.lift_motor.setPower(0.0);
        //               gromit.relicArm.stoprelic...
    }


    public void readTeleDataFromTxtFile(Context context) {
        // setup initial configuration parameters here
        //initializeValues();
//    private String configFileName = "8045TeleOp.txt";
//    private String directoryPath = "/sdcard/FIRST/";
        String teleOpFilePath = "/sdcard/FIRST/8045TeleOp.txt";
        File tmpDir = new File(teleOpFilePath);
//        telemetry.addLine("Reading from file method");
//        telemetry.update();
        if (tmpDir.exists()) {
            try {
                FileReader fr = new FileReader(teleOpFilePath);
                BufferedReader br = new BufferedReader(fr);
                String s;

                s = br.readLine();               // no Loop, but this checks for the end of the file
                elbowdowntime = Integer.parseInt(br.readLine());          //read elbow time


                s = br.readLine();               // no Loop, but this checks for the end of the file
                elbowclampposition = Double.parseDouble(br.readLine());          //read elbow time

                fr.close();                                            // close the file

            } catch (IOException ex) {
                System.err.println("Couldn't read this: " + teleOpFilePath);//idk where this is printing

            }
        } else {
            writeTeleDataToTxtFile(context);
        }
    }

    public boolean writeTeleDataToTxtFile(Context context) {
        // may want to write configuration parameters to a file here if they are needed for teleop too!
//        String fileName = "test.txt";
//        String directoryPath = "/sdcard/FIRST/";
//        String filePath = directoryPath + fileName;
        new File("/sdcard/FIRST/").mkdir();        // Make sure that the directory exists
        String teleOpFilePath = "/sdcard/FIRST/8045TeleOp.txt";

        int i = 0;
        try {
            FileWriter fw = new FileWriter(teleOpFilePath);
            fw.write(("Elbow Time (mseconds)" + "\n"));
            fw.write((elbowdowntime + "\n"));
            fw.write(("Elbow clamp position" + "\n"));
            fw.write((elbowclampposition + "\n"));

            fw.close();
            return true;

        } catch (IOException ex) {
            System.err.println("Couldn't write this file: " + teleOpFilePath);
            return false;
        }
        //telemetry.addData("ConfigFile saved to", filePath);
        //telemetry.update();
        //sleep(500);

    }

}
