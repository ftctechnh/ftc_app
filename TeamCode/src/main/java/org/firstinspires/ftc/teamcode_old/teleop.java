/*
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SuperRegionals.Hardware_8045SuperReg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Math.abs;

@TeleOp(name = "Gromit: Tele", group = "Team")
//@Disabled

public class teleop extends LinearOpMode {


    /* Declare OpMode members. */
    Hardware_8045Worlds gromit = new Hardware_8045Worlds();   // Use a Pushbot's hardware
    //DeviceInterfaceModule dim;
    public MuxColorSensor muxColor;
    // Create timer to toggle LEDs
    public ElapsedTime runtime = new ElapsedTime();

    //Variables moved to Hardware_8045


    @Override
    public void runOpMode() {
        /*
         * Initialize the standard drive system variables.
         * The init() method of the hardware class does most of the work here
         */
        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/
        telemetry.addLine("Running Init");    // Send telemetry message to alert driver that we are calibrating;
        //telemetry.addLine("");    // Send telemetry message to alert driver that we are calibrating;
        //telemetry.addLine("Back Button reverts to code values");    // Send telemetry message to alert driver that we are calibrating;
        //telemetry.update();
        //gromit.imuenabled = false;
        gromit.init(hardwareMap, false);
        ReadConfigFile("8045config.txt");   // read config file

        //gromit.imu = hardwareMap.get(BNO055IMU.class, "imu"); //Moved to Hardware_8045

        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        //Tried to move this to the init call above
        gromit.lightsgreen.setPower(0.0);
        gromit.lightsblue.setPower(0.0);
        telemetry.addData(">", "Robot Ready.");//Send Telemetry that the robot is ready to run
        telemetry.update();

        gromit.motorforklift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        gromit.motorforkliftright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        gromit.motorleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sleep(50);
        gromit.motorforklift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gromit.motorforkliftright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int forkliftzeroposition = gromit.motorforklift.getCurrentPosition();
        int lockposition = gromit.motorforklift.getCurrentPosition();
        gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition(); /**THIS OVERWRITES THE ONE IN INIT*/
        int shots = 0;
        boolean forkliftunlocked = false;
        boolean endgame = false;
        double presstime = 0;
        double voltageleft;
        double voltageright;

        double MaxbotixDistanceLeft;
        double MaxbotixDistanceRight;
        double InRangeDistance = 40.0;

        gromit.ports[0] = 4;
        gromit.ports[1] = 5;

        gromit.rightbluecutoff = (gromit.rightaveragecolor - 3) / 100.0;
        gromit.leftbluecutoff = (gromit.leftaveragecolor - 3) / 100.0;
        gromit.rightredcutoff = (gromit.rightaveragecolor + 3) / 100.0;
        gromit.leftredcutoff = (gromit.leftaveragecolor + 3) / 100.0;
        // Mux setup and initialization
        int milliSeconds = 10;
        muxColor = new MuxColorSensor(gromit.hwMap, "mux", "ada",
                gromit.ports, milliSeconds,
                MuxColorSensor.GAIN_60X);//Change this for potential different Gain



        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/


        // Wait for the game to start
        while (!isStarted()) {

            telemetry.addData("", "====== Ready to Run");
            voltageleft = gromit.maxbotixleft.getVoltage();
            MaxbotixDistanceLeft = voltageleft * (512 / 5);
            voltageright = gromit.maxbotixright.getVoltage();
            MaxbotixDistanceRight = voltageright * (512 / 5);
            telemetry.addData("", "Maxbotix volts Left, Distance(cm): %4.2f  %4.1f", voltageleft, MaxbotixDistanceLeft);
            telemetry.addData("", "Maxbotix volts Right, Distance(cm): %4.2f  %4.1f", voltageright, MaxbotixDistanceRight);
            //Sharp Ir sensor

            double irvoltagetest = gromit.sharp.getVoltage();
            double irdistancetest = -7.411 * Math.pow(irvoltagetest, 3) + 52.356 * Math.pow(irvoltagetest, 2) - 125.222 * irvoltagetest + 111.659;
            telemetry.addData("", "Sharp volts, Distance(cm): %4.2f  %4.1f", irvoltagetest, irdistancetest);
            telemetry.addData("Ball Sensor  (V)", gromit.ballsensor.getVoltage());
            telemetry.addData("Ball Sensor2 (V)", gromit.ballsensor2isEmpty.getState());
//            } else {
//                gromit.back1isreleased = true;
//            }
//
//            if (gromit.launcherzeroing) {
//                double irvoltage = gromit.sharp.getVoltage();
//                //double irdistance = -7.411 * Math.pow(irvoltage, 3) + 52.356 * Math.pow(irvoltage, 2) - 125.222 * irvoltage + 111.659;
//                if (irvoltage >= gromit.loaderthreshold) {
//                    gromit.motorlauncher.setPower(0.0);
//                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
//                    gromit.launcherzeroing = false;
//                }
//            }


            telemetry.update();
            idle();
        }
        /**********************************************************************************************\
         |-------------------------------------START OF GAME--------------------------------------------|
         \**********************************************************************************************/

        resetStartTime();      // start timer
        gromit.drivespeed = 0.8;
        gromit.servodeflector.setPosition(gromit.deflectorup);

//        gromit.motorsweeper.setPower(0.5);
        //gromit.sweeperon = true;
        while (opModeIsActive()) {

            /** continuous rotation servo  for button pusher  */
            if (gamepad1.dpad_left) {
                gromit.servoCR.setPower(-0.5);
            } else if (gamepad1.dpad_right) {
                gromit.servoCR.setPower(0.5);
            } else if (getRuntime() > 2.0 && getRuntime() < presstime + 2.1) {
                if (getRuntime() <= presstime + 1.0) {
                    gromit.servoCR.setPower(-0.5);
                    gromit.dpadupisreleased = true;
                } else
                    gromit.servoCR.setPower(0.5);
            } else if (gamepad1.dpad_up) {
                if (gromit.dpadupisreleased) {
                    gromit.servoCR.setPower(-0.5);
                    presstime = getRuntime();
                    gromit.dpadupisreleased = false;
                }
            } else {
                gromit.dpadupisreleased = true;
                gromit.servoCR.setPower(-0.0);
            }  // button is released
            /**
             * Smart Sweeping
             */
            if (gamepad1.a) {
                if (gromit.a1isreleased) {
                    gromit.a1isreleased = false;
                    if (gromit.smartsweeping) {                                                     //Turn off Smart Sweeping
                        gromit.smartsweeping = false;
                        gromit.motorsweeper.setPower(0.0);
                        gromit.sweeperon = false;
                    } else {                                                                        //Turn on Smart Sweeping
                        gromit.smartsweeping = true;
                        gromit.motorsweeper.setPower(gromit.sweeperpowerauto);
                        gromit.sweeperon = true;
                    }
                }
            } else {
                gromit.a1isreleased = true;
            }
            /**
             * SMART SWEEPER
             */
            if (gromit.smartsweeping) {
                gromit.lightsblue.setPower(1.0);                                                    //Signal that you are Smart Sweeping
                gromit.rightballcrgb = muxColor.getCRGB(gromit.ports[1]);
                gromit.redblueright = 1.0 * gromit.rightballcrgb[1] / gromit.rightballcrgb[3];

                gromit.leftballcrgb = muxColor.getCRGB(gromit.ports[0]);
                gromit.redblueleft = 1.0 * gromit.leftballcrgb[1] / gromit.leftballcrgb[3];

//                telemetry.addData("Ball Right: ", gromit.redblueright);
//                telemetry.addData("Ball Left", gromit.redblueleft);
//                telemetry.addData("Right red cutoff: ", gromit.rightredcutoff);
//                telemetry.addData("Left red cutoff", gromit.leftredcutoff);

                if (gromit.TeamisBlue) {//BLUE
                    if ((gromit.redblueright >= gromit.rightredcutoff) || (gromit.redblueleft >= gromit.leftredcutoff)) {
                        if (gromit.sweeperforward == true) {
                            gromit.motorsweeper.setPower(gromit.sweeperpowerspit * 2);
                            gromit.sweeperforward = false;
                        }
                    } else {
                        if (gromit.sweeperforward == false) {
                            gromit.motorsweeper.setPower(gromit.sweeperpowerauto);
                            gromit.sweeperforward = true;
                        }
                    }
                }
                else{//RED
                    if ((gromit.redblueright <= gromit.rightbluecutoff) || (gromit.redblueleft <= gromit.leftbluecutoff)) {
                        if(gromit.sweeperforward == true) {
                            gromit.motorsweeper.setPower(gromit.sweeperpowerspit);
                            gromit.sweeperforward = false;
                        }
                    }
                    else {
                        if(gromit.sweeperforward == false){
                            gromit.motorsweeper.setPower(gromit.sweeperpowerauto);
                            gromit.sweeperforward = true;
                        }
                    }
                }
            }
            else {
                gromit.lightsblue.setPower(0.0);                                                    //Signal that you are not in Smart Sweeping
            }
            /**
             * Regular Sweeper Controls
             */
            if (gamepad1.right_bumper) {
                if (gromit.rightbumperisreleased) {
                    gromit.rightbumperisreleased = false;
                    gromit.righttriggerisreleased = true;                                           //Change booleans
                    if(gromit.smartsweeping){
                        gromit.motorsweeper.setPower(gromit.sweeperpower);
                        gromit.sweeperon = true;
                        gromit.smartsweeping = false;
                    }
                    else if (!gromit.sweeperon) {
                        gromit.motorsweeper.setPower(gromit.sweeperpower);
                        gromit.sweeperon = true;
                    } else {
                        gromit.motorsweeper.setPower(0.0);
                        gromit.sweeperon = false;
                    }
                }
            } else if (gamepad1.right_trigger > 0.4) {
                if (gromit.righttriggerisreleased) {
                    gromit.righttriggerisreleased = false;
                    gromit.rightbumperisreleased = true;                                            //Change booleans
                    if(gromit.smartsweeping){
                        gromit.motorsweeper.setPower(gromit.sweeperpowerspit);
                        gromit.sweeperon = true;
                        gromit.smartsweeping = false;
                    }
                    else if (!gromit.sweeperon) {
                        gromit.motorsweeper.setPower(-0.1);
                        gromit.sweeperon = true;
                    } else {
                        gromit.motorsweeper.setPower(0.0);
                        gromit.sweeperon = false;
                    }
                }
            } else {
                gromit.rightbumperisreleased = true;
                gromit.righttriggerisreleased = true;
            }  // button is released if it isn't being pressed

            /**
             * Drive Controls might want cube input?
             */

            // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
            // 1 is full down
            // direction: left_stick_x ranges from -1 to 1, where -1 is full left
            // and 1 is full right
            //throttle = (speed * (-(gamepad1.right_stick_y) * -(gamepad1.right_stick_y)*-(gamepad1.right_stick_y)));
            // temp variables
//
            //Speed controls
            if (gamepad1.left_bumper) {
                gromit.drivespeed = 1.0; //Fast
            } else if (gamepad1.left_trigger > 0.2) {
                gromit.drivespeed = 0.4; //Slow

            } else {
                gromit.drivespeed = 0.8; //Normal
            }

            double turnspeed = 0.8;
            double throttle = ((-gamepad1.right_stick_y));// * -(gamepad1.right_stick_y)*-(gamepad1.right_stick_y)));
            float direction = gamepad1.right_stick_x;
            double right = throttle - (turnspeed * direction);
            double left = throttle + (turnspeed * direction);

            // scale the joystick value to make it easier to control
            // the robot more precisely at slower speeds.
            right = scaleInput(right);
            left = scaleInput(left);

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);
            // write the values to the motors
            gromit.motorright.setPower(right * gromit.drivespeed);
            gromit.motorleft.setPower(left * gromit.drivespeed);

//            telemetry.addData("drivespeed",gromit.drivespeed);
//            telemetry.addData("right",right);
//            telemetry.addData("left",left);
//            telemetry.addData("leftricger",gamepad1.left_trigger);
//            telemetry.update();
            /**
            * Ball Deflector toggle
            */
            if (gamepad1.y) {
                if (gromit.y1isreleased) {
                    gromit.y1isreleased = false;
                    if (gromit.deflectoroff) {
                        gromit.servodeflector.setPosition(gromit.deflectorup);
                        gromit.deflectoroff = false;
                    } else {
                        gromit.servodeflector.setPosition(gromit.deflectordown);
                        gromit.deflectoroff = true;
                    }
                }
            } else {
                gromit.y1isreleased = true;
            }

            /**
             * Forklift Control
             */
            // forklift  up, down
            if (forkliftunlocked) {                              // ignore forklift unless unlocked
                if (gamepad2.right_stick_y > 0.1 && gromit.motorforklift.getCurrentPosition() > forkliftzeroposition) {      // down if above zero position
                    gromit.motorforklift.setPower(-gamepad2.right_stick_y * 0.1);
                    gromit.motorforkliftright.setPower(-gamepad2.right_stick_y * 0.1);
                    gromit.forkliftlock = false;
//                    if (gromit.forkliftlock)
//                        lockposition = gromit.motorforklift.getCurrentPosition();                       // set lock position (override)
                } else if (gamepad2.right_stick_y < -0.1) {                                                                      // move lift up
                    gromit.motorforklift.setPower(-gamepad2.right_stick_y * 1.0);
                    gromit.motorforkliftright.setPower(-gamepad2.right_stick_y * 1.0);
                    if (gromit.forkliftlock)
                        lockposition = gromit.motorforklift.getCurrentPosition();                           // reset lock position (override)
                } else if (gamepad2.back) {                                                                                     // lock at curent height (hold)
                    if (gromit.back2isreleased) {
                        gromit.back2isreleased = false;
                        gromit.forkliftdropping = true;
//                    opentime = getRuntime();
                        gromit.motorforklift.setPower(0.5);
                    }
                } else {
                    gromit.back2isreleased = true;
                    if (!gromit.forkliftlock) {
                        gromit.motorforklift.setPower(0.0);
                        gromit.motorforkliftright.setPower(0.0);
                    }
                }

            } else if (gamepad1.back) {                       // back button to unlock and toggle boolean
                gromit.servoforklock.setPosition(gromit.forkopen);  //This unlocks the servo so the forks can go up
                gromit.servoforklockleft.setPosition(gromit.forkleftopen);  //This unlocks the servo so the forks can go up
                forkliftunlocked = true;
                gromit.servoforkthumb.setPosition(gromit.thumbopen);
                gromit.thumbdown = false;
                double forkliftcurrentposition = gromit.motorforklift.getCurrentPosition();
                gromit.motorforklift.setPower((1.0));         // lift forks
                gromit.motorforkliftright.setPower((1.0));
                while (opModeIsActive() && gromit.motorforklift.getCurrentPosition() < forkliftcurrentposition + 100) {
                    idle();
                }
                gromit.motorforklift.setPower((-0.05));         // drop forks
                gromit.motorforkliftright.setPower((-0.05));
                while (opModeIsActive() && gromit.motorforklift.getCurrentPosition() > forkliftcurrentposition + 70) {
                    idle();
                }
            }
            /**  lock forklift trigger up high, check button, toggle boolean and get current position  all the way up ~ 15000  */
            if (gamepad2.left_bumper) {
                if (gromit.leftbumper2isreleased) {
                    gromit.leftbumper2isreleased = false;
                    gromit.forkliftlock = !gromit.forkliftlock;
                    lockposition = gromit.motorforklift.getCurrentPosition();

                    // if forklift lock is turned off, stop motor, change position
                    if (!gromit.forkliftlock) {
                        //gromit.motorforklift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        gromit.motorforklift.setPower(0.0);
                        gromit.motorforkliftright.setPower(0.0);
                    }
                }
            } else {
                gromit.leftbumper2isreleased = true;
            }  // button is released

            if (gromit.forkliftlock) {
                if (gromit.motorforklift.getCurrentPosition() < lockposition) {
                    gromit.motorforklift.setPower(0.6);
                    gromit.motorforkliftright.setPower(0.6);
                } else {
                    gromit.motorforklift.setPower(0.08);
                    gromit.motorforkliftright.setPower(0.08);
                }  /// minor amount of power to keep lift from dropping?
            }
            /** Auto open thumb when forklift drops
             * gromit.servoforkthumb.setPosition(gromit.thumbopen);
             11479
             */

            // telemetry.addData("Forklift Counts", gromit.motorforklift.getCurrentPosition());
            if (gamepad2.right_stick_y > 0.1 && gromit.motorforklift.getCurrentPosition() > 3000) {      // down if above zero position
                gromit.servoforkthumb.setPosition(gromit.thumbopen);
            }
            /**
             * Rapid Fire Boolean
             */
            if (gamepad2.y && !gromit.launcherfiring) {
                if (gromit.y2isreleased) {
                    gromit.y2isreleased = false;
                    if(!gromit.rapidfire) {
                        gromit.rapidfire = true;
                    }
                    else{
                        gromit.rapidfire =false;
                    }
                }
            }
            else{
                gromit.y2isreleased = true;
            }
            /***
             * Rapid Fire Mode
             */
        if(gromit.rapidfire){
            if(gromit.firingaball){                //Fire
//                if (gromit.ballsensor.getVoltage() > gromit.ballthreshold || !gromit.ballsensor2isEmpty.getState()) {
//                    gromit.servoloader.setPosition(gromit.loaderclosed);
//                }
//                else{
//                    gromit.servoloader.setPosition(gromit.loaderopen);
//                }

                double irvoltage = gromit.sharp.getVoltage();
                //double irdistance = -7.411 * Math.pow(irvoltage, 3) + 52.356 * Math.pow(irvoltage, 2) - 125.222 * irvoltage + 111.659;
                if(gromit.motorlauncher.getCurrentPosition() <= gromit.firingposition - 1000) {
                    gromit.servoloader.setPosition(gromit.loaderopen);
                }
                if (irvoltage >= gromit.loaderthreshold && gromit.motorlauncher.getCurrentPosition() <= gromit.firingposition - 1000) {
                    gromit.servoloader.setPosition(gromit.loaderopen);
                    gromit.firingaball = false;
                    gromit.ballloaded = false;
                    gromit.motorlauncher.setPower(0.0);
                }

            }
            else if(gromit.ballloaded == false && (gromit.ballsensor.getVoltage() > gromit.ballthreshold || !gromit.ballsensor2isEmpty.getState())){
                    gromit.timesincesensor = getRuntime();
                    gromit.ballloaded = true;
                    gromit.servoloader.setPosition(gromit.loaderclosed);
            }

            else if(gromit.ballloaded && (getRuntime() - gromit.timesincesensor > 0.05) && !gromit.firingaball) {
                    gromit.firingaball = true;
                    gromit.motorlauncher.setPower(-0.75);
            }
            if(getRuntime()- gromit.timesincesensor > 2 && !gromit.ballloaded){
                    gromit.rapidfire = false;
                    //outta balls
            }
        }

            //Forklift thumb Manual Control
            if (gamepad1.x || gamepad2.x) {
                if (gromit.x1isreleased) {
                    gromit.x1isreleased = false;
                    if (gromit.thumbdown) {
                        gromit.servoforkthumb.setPosition(gromit.thumbopen);
                        gromit.thumbdown = false;
                    } else {
                        gromit.servoforkthumb.setPosition(gromit.thumbclosed);
                        gromit.thumbdown = true;
                    }
                }
            } else {
                gromit.x1isreleased = true;
            }

            /**
             * Launcher Continual shooting shooting
             */
            /*if (gamepad2.right_bumper ){
                if (gromit.rightbumper2isreleased){
                    gromit.servoloader.setPosition(gromit.loaderclosed);
                    gromit.rightbumper2isreleased = false;
                    shots--;
                    gromit.motorlauncher.setPower(-.75);
                    while(gromit.motorlauncher.getCurrentPosition()-gromit.launcherzeroposition > (shots*1680)){
                        idle();
                    }
                    gromit.motorlauncher.setPower(0.0);
                }
            }
            else { gromit.rightbumper2isreleased = true; }  // button is released if it isn't being pressed*/

            /**  ball sensor   closes servoloader */
            if (gromit.ballsensor.getVoltage() > gromit.ballthreshold || !gromit.ballsensor2isEmpty.getState()) {
                gromit.servoloader.setPosition(gromit.loaderclosed);
            } else if (!gromit.rapidfire){
                gromit.servoloader.setPosition(gromit.loaderopen);
            }
            //telemetry.addData("Ball Sensor V: ", gromit.ballsensor.getVoltage());
            //telemetry.addData("Ball Sensor2 (V)", gromit.ballsensor2isEmpty.getState() );

            /**Shooting until SharpIR Sensors stops it*/
            if (gamepad2.right_bumper) {
                if (gromit.rightbumper2isreleased) {
                    gromit.rightbumper2isreleased = false;
                    //shots--;
                    gromit.servoloader.setPosition(gromit.loaderclosed);
                    gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
                    gromit.motorlauncher.setPower(-.75);
                    gromit.launcherfiring = true;
                    if(gromit.smartsweeping){
                        gromit.motorsweeper.setPower(gromit.sweeperpower);
                        gromit.sweeperon = true;
                        gromit.smartsweeping = false;
                    }
                }
            } else {
                gromit.rightbumper2isreleased = true;
            }  // button is released if it isn't being pressed

            //telemetry.addData("Loader Sensor V: ", gromit.sharp.getVoltage());

            if (gromit.launcherfiring) {
                double irvoltage = gromit.sharp.getVoltage();
                //double irdistance = -7.411 * Math.pow(irvoltage, 3) + 52.356 * Math.pow(irvoltage, 2) - 125.222 * irvoltage + 111.659;
                if (irvoltage >= gromit.loaderthreshold && gromit.motorlauncher.getCurrentPosition() <= gromit.firingposition - 1000) {
                    gromit.launcherfiring = false;
                    gromit.motorlauncher.setPower(0.0);
                    gromit.servoloader.setPosition(gromit.loaderopen);
                }
            }

            /**
             *Launcher motor manual motor control
             */
            else if (gamepad2.y) {
                gromit.motorlauncher.setPower(-.5);
                gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
                shots = 0;
            } else if (gamepad2.a) {
                gromit.motorlauncher.setPower(.25);
                gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
                shots = 0;
            } else if (!gromit.rapidfire){
                gromit.motorlauncher.setPower(0.0);
            }
            //Backwards Shot
//            if (gamepad2.right_trigger > .2) {
//                if (gromit.righttrigger2isreleased) {
//                    gromit.servoloader.setPosition(gromit.loaderclosed);
//                    gromit.righttrigger2isreleased = false;
//                    shots++;
//                    double launchercurrentposition = gromit.motorlauncher.getCurrentPosition();
//                    gromit.motorlauncher.setPower(.75);
//                    while (gromit.motorlauncher.getCurrentPosition() < launchercurrentposition + 1680 && opModeIsActive()) {
//                        idle();
//                    }
//                    gromit.motorlauncher.setPower(0.0);
//                }
//            } else {
//                gromit.righttrigger2isreleased = true;
//            }  // button is released if it isn't being pressed
            //End Of Launcher Code


            /**  turn on lights to signify upcoming enggame, then off)  */
            if ((getRuntime() > gromit.endgamesignalstart && getRuntime() < gromit.endgamesignalstart + 2) ||
                    (getRuntime() > gromit.endgamesignalstart + 13 && getRuntime() < gromit.endgamesignalstart + 15)) {                       //
//                endgame = true;
//                boolean even;
                // gromit.lightsgreen.setPower(0.5);
//                if (getRuntime() < gromit.endgamesignalstart+5) {
//                    even = (((int) (runtime.time() *  2) & 0x01) == 0);
//                } else if (getRuntime() < gromit.endgamesignalstart+10) {
//                    even = (((int) (runtime.time() *  6) & 0x01) == 0);
//                } else {
//                    even = (((int) (runtime.time() * 15) & 0x01) == 0);
//                }
//
//                    even = (((int) (runtime.time() * 10) & 0x01) == 0);
//                if (even) {
//                    gromit.lightsblue.setPower(0.7);
//                } else {
//                    gromit.lightsblue.setPower(0.0);
//                }

                gromit.lightsblue.setPower(0.8); //  make blue lights turn on solid

            }
//            else {
//                endgame = false;
//            }

                /**
                 * Recognize if you are within range  an it's not endgame warning time
                 */
                voltageleft = gromit.maxbotixleft.getVoltage();
                MaxbotixDistanceLeft = voltageleft * (512 / 5);
              // voltageright = gromit.maxbotixright.getVoltage();
              // MaxbotixDistanceRight = voltageright * (512 / 5);
                //telemetry.addData("", "Maxbotix volts Left, Distance(cm): %4.2f  %4.1f", voltageleft, MaxbotixDistanceLeft);
                //telemetry.addData("", "Maxbotix volts Right, Distance(cm): %4.2f  %4.1f", voltageright, MaxbotixDistanceRight);

                //double irvoltagetest = gromit.sharp.getVoltage();
                //double irdistancetest = -7.411 * Math.pow(irvoltagetest, 3) + 52.356 * Math.pow(irvoltagetest, 2) - 125.222 * irvoltagetest + 111.659;
                //telemetry.addData("", "Sharp volts, Distance(cm): %4.2f  %4.1f", irvoltagetest, irdistancetest);

                if (MaxbotixDistanceLeft <= InRangeDistance && MaxbotixDistanceLeft >= 19.0 ) {       // turn on lights if in range and not endgame warning
                    gromit.lightsgreen.setPower(0.5);
                    //&& MaxbotixDistanceRight >= InRangeDistance
//                    if(MaxbotixDistanceRight <= InRangeDistance && MaxbotixDistanceRight >= 19.0) {
//                        gromit.lightsblue.setPower(0.0);
//                    }
//                    else {
//                        gromit.lightsblue.setPower(0.5);
//                    }
                } else {                                                      // turn off in not  in range
                    gromit.lightsgreen.setPower(0.0);
                    //gromit.lightsblue.setPower(0.0);
                }

//          }


            telemetry.update();
            idle();
        }

    }

    double scaleInput(double dVal) {

        // two linear lines:  slow for first half 0.5 gets mapped to 0.3  y=mx + b...
        double crossoverpointx = 0.5;
        double crossoverpointy = 0.3;
        double mappedvalue = 0.0;
        double returnvalue;

        // only worry about positive case for now
        if (Math.abs(dVal) <= crossoverpointx) {
            mappedvalue = (crossoverpointy / crossoverpointx) * Math.abs(dVal);
        } else if (Math.abs(dVal) > 0.5) {
            mappedvalue = ((1 - crossoverpointy) / (1 - crossoverpointx) * Math.abs(dVal) - (1 - crossoverpointy - crossoverpointy));
        }
        // get the sign of the input
        //        if (dVal != 0) {returnvalue = mappedvalue * (Math.abs(dVal)/dVal); }
        returnvalue = Math.signum(dVal) * mappedvalue;

        //telemetry.addData("Dval map ret","%.3f %.3f %.3f", dVal,mappedvalue,returnvalue);
        //telemetry.update();
        //sleep(5000);
        return returnvalue;



 /*       double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;

*/
    }


    public void ReadConfigFile(String fileName) {


        int i;


        if (!gamepad1.back) {

            String directoryPath = "/sdcard/FIRST/";
            String filePath = directoryPath + fileName;
            new File(directoryPath).mkdir();        // Make sure that the directory exists


            // read labels & values from file before we start the init loop
            //==========================================
            //String filePath    = "/sdcard/FIRST/8045_Config.txt";

            i = 0;
            try {
                FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);
                String s;
                while ((s = br.readLine()) != null && i <= 1) {                // read just the team color
                    gromit.menulabel[i] = s;                                   //Updates the label "string" of our array
                    s = br.readLine();
                    gromit.menuvalue[i] = Integer.parseInt(s);                //values is our integer array, converts string to integer
                    //System.out.println(s);
                    i += 1;                                         //Only to do with our array
                }
                fr.close();

            } catch (IOException ex) {
                System.err.println("Couldn't read this: " + filePath);//idk where this is printing
            }
        } else {
            telemetry.addLine("****** File NOT read from PHONE ********");
            telemetry.update();
            sleep(500);
        }


        // update the values we want to use
        gromit.TeamisBlue = (gromit.menuvalue[0] != 0);
        gromit.rightaveragecolor = (gromit.menuvalue[16]);
        gromit.leftaveragecolor = (gromit.menuvalue[17]);

    }

}