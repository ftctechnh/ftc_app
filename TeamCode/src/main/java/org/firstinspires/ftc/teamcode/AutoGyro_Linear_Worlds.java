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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware_8045Worlds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.abs;


@Autonomous(name="Gromit:  Auto", group="Team")
//@Disabled

public class AutoGyro_Linear_Worlds extends LinearOpMode {



    /* Declare OpMode members. */
    Hardware_8045Worlds gromit    = new Hardware_8045Worlds();   // Use a 8045 hardware
    //DeviceInterfaceModule dim;

    // Create timer to toggle LEDs
    public ElapsedTime runtime = new ElapsedTime();
    public boolean targetisfront;
    //Variables moved to Hardware_8045

    //except for color sensors
//    public MuxColorSensor muxColor;
    //public int[] ports = {0,1,2,3,4,5};


    @Override
    public void runOpMode() {
        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/
        telemetry.addLine("WAIT!  Calibrating Gyro");    // Send telemetry message to alert driver that we are calibrating;
        telemetry.addLine("");    // Send telemetry message to alert driver that we are calibrating;
        telemetry.addLine("Back Button reverts to code values");    // Send telemetry message to alert driver that we are calibrating;
        telemetry.update();

        gromit.init(hardwareMap,true);
        //gromit.imu = hardwareMap.get(BNO055IMU.class, "imu"); //Moved to Hardware_8045

        /**
         * Zero The launcher if it isn't already
         */
       double irvoltage = gromit.sharp.getVoltage();
        //double irdistance = -7.411 * Math.pow(irvoltage, 3) + 52.356 * Math.pow(irvoltage, 2) - 125.222 * irvoltage + 111.659;
        if(irvoltage <= gromit.loaderthreshold){
            gromit.motorlauncher.setPower(-0.4);
            gromit.servoloader.setPosition(gromit.loaderclosed);
            while(irvoltage <= gromit.loaderthreshold){
                irvoltage = gromit.sharp.getVoltage();
                //irdistance = -7.411*Math.pow(irvoltage,3) + 52.356 *Math.pow(irvoltage,2) -125.222*irvoltage +111.659 ;
                idle();
            }
            gromit.motorlauncher.setPower(0.0);
            gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
            //gromit.servoloader.setPosition(gromit.loaderopen);

        }

        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        //Tried to move this to the init call above

        telemetry.addData(">", "Robot Ready.");//Send Telemetry that the robot is ready to run
        telemetry.update();



        // get menu values
        OurMenu("8045config.txt");

        //Disable 3 color sensprs on the  other side of the robot
        //int[] ports = {};

        if(gromit.TeamisBlue){gromit.ports[0] = 0;
            gromit.ports[1] = 1;
            gromit.ports[2] = 4;
            gromit.ports[3] = 5;
        }
        else{gromit.ports[0] = 2;
            gromit.ports[1] = 3;
            gromit.ports[2] = 4;
            gromit.ports[3] = 5;
        }

//        // Mux setup and initialization
//        int milliSeconds = 10;
//        muxColor = new MuxColorSensor(gromit.hwMap, "mux", "ada",
//                gromit.ports, milliSeconds,
//                MuxColorSensor.GAIN_60X);//Change this for potential different Gain

        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/
        gromit.lightsblue.setPower(0.0);
        gromit.servoloader.setPosition(gromit.loaderclosed);
        gromit.rightbluecutoff = (gromit.rightaveragecolor - 3)/100.0;
        gromit.leftbluecutoff = (gromit.leftaveragecolor - 3)/100.0;
        gromit.rightredcutoff = (gromit.rightaveragecolor + 3)/100.0;
        gromit.leftredcutoff = (gromit.leftaveragecolor + 3)/100.0;
        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
//        int[] rightballcrgb = gromit.muxColor.getCRGB(gromit.ports[4]);
//        double redblueright = 1.0*rightballcrgb[1]/rightballcrgb[3];
//        gromit.lightsgreen.setPower(0.5);
//
//        while (!isStarted() && (redblueright >= gromit.rightbluecutoff)){
//
//            rightballcrgb = gromit.muxColor.getCRGB(gromit.ports[5]);
//            redblueright = 1.0*rightballcrgb[1]/rightballcrgb[3];
//            telemetry.addData("Sensor 4, R vd B", "%5.2f ",redblueright);
//            telemetry.addData("CRGB", "%5d %5d %5d %5d",
//                    rightballcrgb[0], rightballcrgb[1], rightballcrgb[2], rightballcrgb[3]);
//
//
//            telemetry.update();
//            idle();
//        }
//        telemetry.clear();
//        gromit.lightsgreen.setPower(0.0);
        while (!isStarted()) {
            //Too many sensors to update each loop update once a second
            //boolean even = (((int) (runtime.time()*2) & 0x01) == 0);
//            double[] angles = gromit.getAngles();                          // display the current angles
//            telemetry.addData("", "Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);

            // send the info back to driver station using telemetry function.
//            telemetry.addData("ods Raw :", gromit.odsSensor.getRawLightDetected());
//            if (gamepad1.start) { muxColor.startPolling(); }

//            // Mux Color Readings
//            for (int i = 0; i < gromit.ports.length; i++) {
//                int[] crgb = muxColor.getCRGB(gromit.ports[i]);
//
//                telemetry.addLine("Sensor " + gromit.ports[i]);
//                telemetry.addData("CRGB", "%5d %5d %5d %5d",
//                        crgb[0], crgb[1], crgb[2], crgb[3]);
//                telemetry.addData("R:B   B:R", "%5.2f %5.2f ",
//                        1.0*crgb[1]/crgb[3], 1.0*crgb[3]/crgb[1]);
//            }
            //Maxbotix Sensor
            double voltage = gromit.maxbotixleft.getVoltage();
            double MaxbotixDistance = voltage * (512 / 5);
            telemetry.addData("", "Maxbotix volts, Distance(cm): %4.2f  %4.1f", voltage, MaxbotixDistance);
            //Sharp Ir sensor
            irvoltage = gromit.sharp.getVoltage();
            telemetry.addData("", "Sharp Loader Sensor (V), : %4.2f  ", irvoltage );
            double ballvoltage = gromit.ballsensor.getVoltage();
            telemetry.addData("", "Ball Sensor (V), : %4.2f  ", ballvoltage );
            telemetry.addData("Ball Sensor2(V)", gromit.ballsensor2isEmpty.getState() );


            // display the menu values that are set
            telemetry.addLine("----------------------------READY TO RUN!--------------");
            if (gromit.TeamisBlue) { telemetry.addLine("BLUE Alliance");   }   //If Blue Highlight
            else            { telemetry.addLine("RED Alliance");    }   //Otherwise must be Red

            // display the program mode
            telemetry.addLine().addData(gromit.menulabel[1], gromit.menuvalue[1] + "  " + gromit.modename[gromit.menuvalue[1]]);

            //  loop to display the configured items  if the index matches add arrow.
            for ( int i=2; i< gromit.menuvalue.length; i++){ telemetry.addLine().addData(gromit.menulabel[i], gromit.menuvalue[i]); }
            telemetry.addLine("----------------------------READY TO RUN!--------------");




            if(gamepad1.back){
                if(gromit.back1isreleased){
                    gromit.launcherzeroing = true;
                    gromit.back1isreleased = false;
                    gromit.motorlauncher.setPower(-0.4);
                    gromit.servoloader.setPosition(gromit.loaderclosed);

                    irvoltage = gromit.sharp.getVoltage();
                    //irdistance = -7.411*Math.pow(irvoltage,3) + 52.356 *Math.pow(irvoltage,2) -125.222*irvoltage +111.659 ;
                    while(irvoltage <= gromit.loaderthreshold){
                        irvoltage = gromit.sharp.getVoltage();
                        //irdistance = -7.411*Math.pow(irvoltage,3) + 52.356 *Math.pow(irvoltage,2) -125.222*irvoltage +111.659 ;
                        idle();
                    }
                    gromit.motorlauncher.setPower(0.0);
                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
                    //gromit.servoloader.setPosition(gromit.loaderopen);
                }
            }
            else{gromit.back1isreleased = true;}
//
//            if(gromit.launcherzeroing){
//                double irvoltage = gromit.sharp.getVoltage();
//                double irdistance = -7.411*Math.pow(irvoltage,3) + 52.356 *Math.pow(irvoltage,2) -125.222*irvoltage +111.659 ;
//
//                if(irdistance <= 10.0){
//                    gromit.motorlauncher.setPower(0.0);
//                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
//                    gromit.launcherzeroing = false;
//                    gromit.servoloader.setPosition(gromit.loaderopen);
//                }
//            }
            telemetry.update();
            idle();
        }
        telemetry.clear();
        /**********************************************************************************************\
         |-------------------------------------START OF GAME--------------------------------------------|
         \**********************************************************************************************/
        resetStartTime();      // start timer
        int launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
        int shots = 0;
        gromit.motorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(20);
//        gromit.motorleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        gromit.motorright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gromit.motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //^^^^Should this be in a void^^^^


//        gyroturnonewheel(gromit.heading1,"both");
//        sleep(5000);
//        gyroturnonewheel(-gromit.heading1,"both");
//        sleep(30000);
//        gyrodrive(24,0,gromit.GYRO_DRIVE_SPEED);
//        sleep(5000);
//        gyrodrive(-24,0,gromit.GYRO_DRIVE_SPEED);
//        sleep(30000);

//        gyrodrivetoline(0,gromit.GYRO_DRIVE_SPEED*0.5);
//        sleep(5000);
//        gyrodrivetoline(0,gromit.GYRO_DRIVE_SPEED*0.5);
//        sleep(5000);

        gromit.servodeflector.setPosition(gromit.deflectorup);                                        // lift up backsplash.

        if( (gromit.programmode == 0 || gromit.programmode == 2) ) {                                           /**Beacon Route mode 0*/
            sleep(1000*gromit.secondstodelay);   // delay for other team;

            /**  Here's the code for picking up the partner's ball  order of steps?? */
            if (gromit.PickUpPartnerBall){
                                                                                // might need to drive forward here?  gyrodrive?
                gromit.motorsweeper.setPower(0.5);                               // Turn on sweeper
                gromit.particles = gromit.particles+1;                                                                // might need to drive backward here?
                sleep(200);                                                                // wait a little bit?
//                if(gromit.TeamisBlue) {
//                    gyroturnonewheel(gromit.heading0,"left",false);
//                } else{
//                    gyroturnonewheel(gromit.heading0,"right",false);
//                }

            }
            if(!gromit.TeamisBlue){ //WE NEED TO CHECK THIS OUT?
                gromit.distance0 = gromit.distance0+2;
            }
            /** Drive 0 - forward to shoot*/

//            gyrodrive(gromit.distance0, gromit.heading0, gromit.GYRO_DRIVE_SPEED/2,false); //drive forward a little

            /** mode 2 turn  to shoot*/
//            if( ( gromit.programmode == 2) ) {                                           /** try to hit out slices */
//                if (gromit.TeamisBlue){gyroturnonewheel(gromit.supermodeturnblue,"both",false);}
//                else{gyroturnonewheel(gromit.supermodeturnred,"both",false);}
//            }
//
//            else {                                                           /** normal mode */
//                if (!gromit.TeamisBlue){gyroturnonewheel(-15.0,"both",false);}    /** if Red turn different amt normal mode? */
//            }
            /** Shoot Particles */
            if (gromit.particles  > 0 ) {
                for (int i = 1; i <= gromit.particles; i++) {
                    //SHOOT PARTICLE 1
                    //Shooting using Sharp Sensor
                    gromit.servoloader.setPosition(gromit.loaderclosed - 0.1);                          //try to jostle balls if stuck
                    gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
                    irvoltage = gromit.sharp.getVoltage();
//                    irdistance = -7.411 * Math.pow(irvoltage, 3) + 52.356 * Math.pow(irvoltage, 2) - 125.222 * irvoltage + 111.659;
                    // sleep(100); // loader servo should already be closed, no longer need to sleep.
                    gromit.motorlauncher.setPower(-0.5);   /** FIRE  */

                    while (opModeIsActive() && (irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800 ) ) {
//                    while (                     irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800) {
                        irvoltage = gromit.sharp.getVoltage();
                        //irdistance = -7.411 * Math.pow(irvoltage, 3) + 52.356 * Math.pow(irvoltage, 2) - 125.222 * irvoltage + 111.659;
                        idle();
                    }
                    gromit.motorlauncher.setPower(0.0);
                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
                    gromit.servoloader.setPosition(gromit.loaderopen);
                    if  (i < gromit.particles) {          // only wait if you still have more to fire...
                        double now =getRuntime();
                        while (opModeIsActive() && ( gromit.ballsensor.getVoltage()< gromit.ballthreshold && gromit.ballsensor2isEmpty.getState() && getRuntime() < now + 1.5 )) {   /** wait till loaded, or 1.7 seconds */
                            idle();
                        }
                        gromit.servoloader.setPosition(gromit.loaderclosed - 0.1);                          //try to jostle balls if stuck
                        sleep(350);   // let ball settle,  shouldn't have to worry about closing servo gate again.
                        //sleep(1500);
                    }
                }
            }
            //      gyrodrive(10, 0, gromit.DRIVE_SPEED); //drive back to the start

            if( ( gromit.programmode == 2) ) {                                           /** turn on sweeper if supermode */
                gromit.motorsweeper.setPower(gromit.sweeperpower);
                gromit.detectballs = true;
            }
            else if(gromit.programmode == 0){
                gromit.motorsweeper.setPower(0.0);
                gromit.detectballs = false;
            }

            /** Drive 1 - forward to wall  */
        if(!gromit.TeamisBlue){ //WE NEED TO CHECK THIS OUT?
            gromit.distance1 = gromit.distance1-3;
        }
            if (!gromit.TeamisBlue) { gromit.heading1 = -gromit.heading1;   }

            gromit.servoCR.setPower(-0.3);
            //gyroturnonewheel(gromit.heading1, "both",false);
            gromit.servoCR.setPower(0.5);
            //sleep(50);

            //gyrodrive(gromit.distance1, gromit.heading1, gromit.GYRO_DRIVE_SPEED*1.0,gromit.detectballs);                       // first drive to wall
            gromit.servoCR.setPower(0.0);
            /** Drive 2 - backup into corner  */
//            if (!gromit.TeamisBlue) {
//                gromit.heading2 = -(gromit.heading2-6);
//                gromit.distance2 = gromit.distance2 + 3;
//            }                       // red team turns in opposite direction
//            //sleep(50);
//            gyroturnonewheel(gromit.heading2,"both",false);                                                              // turn to head back into corner
//            //sleep(50);
//            gyrodrive(gromit.distance2, gromit.heading2, gromit.GYRO_DRIVE_SPEED*1.0,gromit.detectballs);                       //backup to corner & Wall
//
//
//            /** Drive 3 - parallel to wall to white line   suck in buttons*/
//            gromit.servoCR.setPower(0.15); sleep(5); gromit.servoCR.setPower(0.05);
//            //sleep(50);
//            gyroturnonewheel(gromit.heading3, "both",false);                                             //turn against wall, one or two wheels better?
//            //sleep(100);
//            gyrodrivetoline(gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4,false);                             // drive along wall to first white line
//
//            /**Sense Color*/
//            muxColor.startPolling();
//            sleep(10);
//            readcolors();
//            if(targetisfront){
//                gyrodrive( 3, gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4,false);
//            }
//            //This method takes care of pressing the button
//            pushbutton();
//            //gromit.servoCR.setPower(-0.7); sleep(gromit.buttontime); gromit.servoCR.setPower(0.7); sleep(gromit.buttontime); gromit.servoCR.setPower(0.05);  // button servo
//
//            /** Drive 4 - Parallel to wall to second line  */
//        if(gromit.TeamisBlue){
//            gromit.heading3 = gromit.heading3-3;
//        }
//        else{
//            gromit.heading3 = gromit.heading3+3;
//        }
//            gyrodrive(gromit.distance4, gromit.heading4, gromit.GYRO_DRIVE_SPEED*0.8,gromit.detectballs);                       // drive forward fast for most of the way
//            sleep(100);
//            gyrodrivetoline(gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4,false);                             // drive along wall to second white line
//            sleep(30);
//            readcolors();
//            if(targetisfront){
//                gyrodrive( 4, gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4,false);
//            }
//            pushbutton();
//
//            /** Drive 5 - head to the center cap ball  */
//            if(!gromit.TeamisBlue){
//                gromit.heading5 = gromit.heading5+5;
//            }
//            if (!gromit.TeamisBlue) { gromit.heading5 = -gromit.heading5; }                                 // red team turns in opposite direction
//
//            if(  gromit.programmode == 2) {
//                gromit.motorsweeper.setPower(0.75);
//            }
//            gyroturnonewheel(gromit.heading5,"both",false);                                                       // turn towards cap ball
//            gyrodrive(gromit.distance5 * 0.40, gromit.heading5, gromit.GYRO_DRIVE_SPEED*1.0,gromit.detectballs);
//            gromit.motorsweeper.setPower(0.0);
//
//            if(  gromit.programmode == 2  && (gromit.ballsensor.getVoltage() > gromit.ballthreshold || !gromit.ballsensor2isEmpty.getState()) ) {    /** Super mode & ball sensed*/
//                sleep(100);    //Is this necessary?                                                               /**fire  third ball**/
//                gromit.lightsgreen.setPower(0.5);
//                gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
//                irvoltage = gromit.sharp.getVoltage();
//                gromit.servoloader.setPosition(gromit.loaderclosed);
//                gromit.motorlauncher.setPower(-0.5);
//
//                    while (opModeIsActive() && (irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800 ) ) {
//                        irvoltage = gromit.sharp.getVoltage();
//                        idle();
//                    }
//                    gromit.motorlauncher.setPower(0.0);
//                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
//                    gromit.servoloader.setPosition(gromit.loaderopen);
//                //wait for ball to roll in
//                double now = getRuntime();
//                while (opModeIsActive() && ( gromit.ballsensor.getVoltage()< gromit.ballthreshold && gromit.ballsensor2isEmpty.getState() && getRuntime() < now + 1 )) {   /** wait till loaded, or 1.7 seconds */
//                    idle();
//                }
//                if (gromit.ballsensor.getVoltage() > gromit.ballthreshold || !gromit.ballsensor2isEmpty.getState()) {
//                    //sleep(100);     // is this necessary?                                                    /**fire  third ball**/
//                    gromit.servoloader.setPosition(gromit.loaderclosed);
//                    gromit.lightsgreen.setPower(0.5);
//                    gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
//                    irvoltage = gromit.sharp.getVoltage();
//
//                    gromit.motorlauncher.setPower(-0.5);
//
//                    while (opModeIsActive() && (irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800 ) ) {
//                        irvoltage = gromit.sharp.getVoltage();
//                        idle();
//                    }
//                    gromit.motorlauncher.setPower(0.0);
//                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
//                    gromit.servoloader.setPosition(gromit.loaderopen);
//                }
//
//
//            }
//
//            gyrodrive(gromit.distance5 * 1.0, gromit.heading5, gromit.GYRO_DRIVE_SPEED, false);                   /** go hit cap ball & Park */
//
//
//            /** end of Main Mode & Super Mode */


        }

//        /**Begin Judging Demo Mode*/
//        else if(gromit.programmode == 1) {
//            //Test Super-sweepermode for judging pannel
//
//            //readcolors();
////            if(targetisfront){
////                gyrodrive( 4, gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4,false);
////            }
//            targetisfront = false;
//            pushbutton();
//        }


        /**
         * SIMPLE CAPBALL MODE, Simple Ramp Mode, and Defense Mode
         */
//        else if((gromit.programmode == 3 || gromit.programmode == 4 || gromit.programmode ==5) && opModeIsActive()) {                   /** This is the simple Capball route */
//            gromit.detectballs = false;
//            // capball mode
//            if (gromit.TeamisBlue) {                   /** if Red turn different amt */
//                gyroturnonewheel(-40, "right", gromit.detectballs); //right is the direction it turns
//                gyrodrive(15, -40, gromit.GYRO_DRIVE_SPEED, gromit.detectballs);
//            } else {
//                gyroturnonewheel(35, "left", gromit.detectballs); //right is the direction it turns
//                gyrodrive(15, 35, gromit.GYRO_DRIVE_SPEED, gromit.detectballs);
//
//            }
//            sleep(300);
//            /** Shoot Particles */
//
//            if (gromit.particles > 0) {
//                for (int i = 1; i <= gromit.particles; i++) {
//                    //SHOOT PARTICLES
//                    gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
//                    irvoltage = gromit.sharp.getVoltage();
//                    gromit.servoloader.setPosition(gromit.loaderclosed);
//                    // sleep(100); // loader servo should already be closed, no longer need to sleep.
//                    gromit.motorlauncher.setPower(-0.5);   /** FIRE  */
//                    while (opModeIsActive() && (irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800)) {
////                    while (                     irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800) {
//                        irvoltage = gromit.sharp.getVoltage();
//                        //irdistance = -7.411 * Math.pow(irvoltage, 3) + 52.356 * Math.pow(irvoltage, 2) - 125.222 * irvoltage + 111.659;
//                        idle();
//                    }
//                    gromit.motorlauncher.setPower(0.0);
//                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
//                    gromit.servoloader.setPosition(gromit.loaderopen);
//
//                    if (i < gromit.particles) {          // only wait if you still have more to fire...
//                        double now = getRuntime();
//                        while (opModeIsActive() && (gromit.ballsensor.getVoltage() < gromit.ballthreshold && gromit.ballsensor2isEmpty.getState() && getRuntime() < now + 1.7)) {   /** wait till loaded, or 1.7 seconds */
//                            idle();
//                        }
//                        sleep(100);   // let ball settle,  shouldn't have to worry about closing servo gate again.
//                    }
//                }
//            }
//            if(gromit.programmode == 3 || gromit.programmode == 4) {
//                sleep(10000);   // delay for other team;
//            }
//        }

//        if(gromit.programmode == 3){
//            if (gromit.TeamisBlue) {                   /** if Red turn different amt */
//                gyroturnonewheel(-40, "right",false); //right is the direction it turns
//                gyrodrive(25, -40, gromit.GYRO_DRIVE_SPEED,false);
//            } else {
//                gyroturnonewheel(35, "right",false); //right is the direction it turns
//                gyrodrive(25, 35, gromit.GYRO_DRIVE_SPEED,false);
//
//            }
//
//        }

         /**
         * Ramp Autonomous
         * */
//        if(gromit.programmode == 4){
//            if(gromit.TeamisBlue){
//            gyroturnonewheel(-100, "both",false); //right is the direction it turns
//            gyrodrive(47, -100, gromit.GYRO_DRIVE_SPEED,false);
//            }
//            else{
//                gyroturnonewheel(100, "both",false); //right is the direction it turns
//                gyrodrive(47, 100, gromit.GYRO_DRIVE_SPEED,false);
//            }
//
//        }

         /**
         * Defensive Autonomous
         * */
//        if(gromit.programmode == 5){
//            /**Drive Around Cap Ball*/
//            float turn1 = -63;
//            float distance1 = 34;
//            float turn2 = 0;
//            float distance2 = 14;
//            float turn3 = 26;
//            float distance3 = 27;
//            float turn4 = 0;
//            float distance4 = 15;
//            if(!gromit.TeamisBlue){
//                turn1 = -turn1;
//                turn2 = -turn2;
//                turn3 = -turn3;
//                turn4 = -turn4;
//
//                distance3 = distance3 + 3;
//            }
//            gyroturnonewheel(turn1, "both",false); //right is the direction it turns
//            gyrodrive(distance1, turn1, gromit.GYRO_DRIVE_SPEED,false);
//
//            if(gromit.TeamisBlue){
//                if(gromit.hitcapball){
//                    gyroturnonewheel(turn2+15, "left",false); //right is the direction it turns
//                    sleep(50);
//                }
//                gyroturnonewheel(turn2, "left",false); //right is the direction it turns
//            }
//            else{//RED
//                if(gromit.hitcapball){
//                    gyroturnonewheel(turn2-20, "right",false); //right is the direction it turns
//                    sleep(50);
//                }
//                gyroturnonewheel(turn2, "right",false); //right is the direction it turns
//            }
//
//            gyrodrive(distance2, turn2, gromit.GYRO_DRIVE_SPEED,false);
//
//            gyroturnonewheel(turn3, "both",false); //right is the direction it turns
//            while( getRuntime() < 10.75){
//                idle();
//            }
//            gyrodrive(distance3, turn3, gromit.GYRO_DRIVE_SPEED,false);
//
//            gyroturnonewheel(0, "both",false); //right is the direction it turns
//
//            gyrodrive(distance4, turn4, gromit.GYRO_DRIVE_SPEED,false);
//            sleep(4000);
//            gyrodrive(-2, 0, gromit.GYRO_DRIVE_SPEED,false);
//            telemetry.clear();
//            while( getRuntime()<26.0){
//                double robot = gromit.robotsensor.getVoltage();
//                telemetry.addData("", "Robot Sensor  %4.1f", robot );
//                telemetry.update();
//                idle();
//            }
//            if(gromit.TeamisBlue) {
//                gyroturnonewheel(4, "both", false); //right is the direction it turns
//                gyrodrive(-35, 4, gromit.GYRO_DRIVE_SPEED, false);
//            }
//            else{
//                gyroturnonewheel(-4, "both", false); //right is the direction it turns
//                gyrodrive(-35, -4, gromit.GYRO_DRIVE_SPEED, false);
//            }
////            gyroturnonewheel(-2, "both",false); //right is the direction it turns
////            gyrodrive(-35, -2, gromit.GYRO_DRIVE_SPEED,false);
//
//        }


        while (opModeIsActive()) {

            idle();
        }

    }













    /**MENUIING JOYSTICK CONTROL IN INIT
     * Menu to change variables corresponding to gyrodrives and turns and delays etc
     * * *************************************************************************************************************************/

    public void OurMenu(String fileName) {

        int i;
        String directoryPath = "/sdcard/FIRST/";
        String filePath = directoryPath + fileName;
        new File(directoryPath).mkdir();        // Make sure that the directory exists

        // if the left bumper is down skip reading the file!
        if (!gamepad1.back) {

            // read labels & values from file before we start the init loop
            //==========================================
            //String filePath    = "/sdcard/FIRST/8045_Config.txt";

            i = 0;
            try {
                FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);
                String s;
                while ((s = br.readLine()) != null && i < gromit.menuvalue.length) {                // read the label then the value they are on separate lines
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
        } else{
            telemetry.addLine("****** File NOT read from PHONE ********");
            telemetry.update();
            sleep(2000);
        }


//==========================================
//This while loop is the "editing" loop
        int index = 0;

        boolean yisreleased = true;
        boolean aisreleased = true;
        boolean xisreleased = true;
        boolean bisreleased = true;
        boolean rbumperisreleased = true;
        boolean rjoyisreleased = true;
        boolean editmode = false;

        while (!isStarted() && !gamepad1.right_stick_button ) {

            // update the values we want to use
            gromit.TeamisBlue     = (gromit.menuvalue[0] != 0);
            gromit.programmode    = gromit.menuvalue[1];
            gromit.particles      = gromit.menuvalue[2];
            gromit.secondstodelay = gromit.menuvalue[3];
            gromit.heading0       = gromit.menuvalue[4];
            gromit.distance0      = gromit.menuvalue[5];
            gromit.heading1       = gromit.menuvalue[6];
            gromit.distance1      = gromit.menuvalue[7];
            gromit.heading2       = gromit.menuvalue[8];
            gromit.distance2      = gromit.menuvalue[9];
            gromit.heading3       = gromit.menuvalue[10];
            gromit.distance3      = gromit.menuvalue[11];
            gromit.heading4       = gromit.menuvalue[12];
            gromit.distance4      = gromit.menuvalue[13];
            gromit.heading5       = gromit.menuvalue[14];
            gromit.distance5      = gromit.menuvalue[15];
            gromit.rightaveragecolor = gromit.menuvalue[16];
            gromit.leftaveragecolor = gromit.menuvalue[17];
            gromit.supermodeturnblue      = gromit.menuvalue[18];
            gromit.supermodeturnred       = gromit.menuvalue[19];
            gromit.PickUpPartnerBall      = (gromit.menuvalue[21] != 0);

            //telemetry.addLine().addData(gromit.mode[gromit.programmode]);
            telemetry.addLine("===> Press Joystick to exit MENU mode <===" );
            telemetry.addLine("#######    " + gromit.teamname[gromit.menuvalue[0]] +"    " + gromit.modename[gromit.programmode] + "    #######");

            // output current settings to the screen  (1st two have labels... token is red/blue treat it separately.

//           if (gromit.menuvalue[0] == 1) {       //If Blue Highlight if index is 0
//               if (index == 0 && editmode ){telemetry.addLine("BLUE Alliance  <=======");} else {telemetry.addLine("BLUE Alliance");}
//            } else {                    //Otherwise must be Red Highlight if index is 0
//                if (index == 0 && editmode ){telemetry.addLine("RED Alliance  <=======");} else {telemetry.addLine("RED Alliance");}
//            }
//          loop to display the configured items  if the index matches add arrow.
            if (0 == index && editmode) {
                telemetry.addLine().addData(gromit.menulabel[0], gromit.menuvalue[0] + "  " + gromit.teamname[gromit.menuvalue[0]]+"  <=======");
            }else{
                telemetry.addLine().addData(gromit.menulabel[0], gromit.menuvalue[0] + "  " + gromit.teamname[gromit.menuvalue[0]]);
            }

            if (1 == index && editmode) {
                telemetry.addLine().addData(gromit.menulabel[1], gromit.menuvalue[1] + "  " + gromit.modename[gromit.menuvalue[1]]+"  <=======");
            }else{
                telemetry.addLine().addData(gromit.menulabel[1], gromit.menuvalue[1] + "  " + gromit.modename[gromit.menuvalue[1]]);
            }



//          loop to display the configured items  if the index matches add arrow.
            for ( i=2; i < gromit.menuvalue.length; i++){
                //telemetry.addData("*",values[i]);
                if (i == index && editmode) {
                    telemetry.addLine().addData(gromit.menulabel[i], gromit.menuvalue[i]+"  <=======");

                }else{
                    telemetry.addLine().addData(gromit.menulabel[i], gromit.menuvalue[i]);
                }

            }
            telemetry.addLine("");

            // blink lights for red/blue  while in the menu method
            boolean even = (((int) (runtime.time()) & 0x01) == 0);             // Determine if we are on an odd or even second
            if ( gromit.TeamisBlue) {
                gromit.dim.setLED(gromit.BLUE_LED, even); // blue for true
                gromit.dim.setLED(gromit.RED_LED, false);
                if(even) {
                    gromit.lightsblue.setPower(0.5);
                }
                else{
                    gromit.lightsblue.setPower(0.0);
                }
            } else {
                gromit.dim.setLED(gromit.RED_LED,  even); // Red for false
                gromit.dim.setLED(gromit.BLUE_LED, false);
            }

            //THIS IS EDIT MODE=============================================
            //          if we haven't pressed settings done bumper, look for button presses, blink the LED
            if (editmode) {
                //menu buttons,  Y,A increase/decrease the index   X,B increase/decrease the value  (debounce logic )
                if (gamepad1.a) {
                    if (aisreleased) {
                        aisreleased = false;
                        index += 1;
                    }
                } else {
                    aisreleased = true;
                }
                if (gamepad1.y) {
                    if (yisreleased) {
                        yisreleased = false;
                        index -= 1;
                    }
                } else {
                    yisreleased = true;
                }
                if (index >= gromit.menuvalue.length) index = 0;          // limit the bounds of index
                if (index < 0) index = gromit.menuvalue.length - 1 ;

                // increase or decrease the values
                if (gamepad1.b) {
                    if (bisreleased) {
                        bisreleased = false;
                        gromit.menuvalue[index] += 1;
                    }
                } else {
                    bisreleased = true;
                }
                if (gamepad1.x) {
                    if (xisreleased) {
                        xisreleased = false;
                        gromit.menuvalue[index] -= 1;
                    }
                } else {
                    xisreleased = true;
                }

                // limit the values of some buttons
                if (gromit.menuvalue[0] >  1) gromit.menuvalue[0] = 0;          //team color is boolean
                if (gromit.menuvalue[0] <  0) gromit.menuvalue[0] = 1;          //team color is boolean
                if (gromit.menuvalue[1] >= 7) gromit.menuvalue[1] = 0;          //modes 0,1,2,3,4,5,6
                if (gromit.menuvalue[1] <  0) gromit.menuvalue[1] = 6;          //
                if (gromit.menuvalue[2] >= 3) gromit.menuvalue[2] = 0;          //Particles, 0,1,2
                if (gromit.menuvalue[2] <  0) gromit.menuvalue[2] = 2;          //
                if (gromit.menuvalue[3] <  0) gromit.menuvalue[3] = 0;          // Delay can't be negative
                if (gromit.menuvalue[20] >  1) gromit.menuvalue[20] = 0;          // Cap Ball boolean
                if (gromit.menuvalue[20] <  0) gromit.menuvalue[20] = 1;          //
                if (gromit.menuvalue[21] >  1) gromit.menuvalue[21] = 0;          //Pick up Partner Ball boolean
                if (gromit.menuvalue[21] <  0) gromit.menuvalue[21] = 1;          //


                //telemetry.addLine(" ");
                telemetry.addLine(" EDIT MODE: Right Bumper to EXIT");


                // check to exit the edit mode
                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                        rbumperisreleased = false;
                        editmode = false;

                    }
                }else {
                    rbumperisreleased = true;
                }
            }
            //END OF EDIT MODE===================================================
            else {

//          in ready mode check if you'd like to return to editing, add telemetry line.

                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                        editmode = true;
                        rbumperisreleased = false;
                    }
                }else {
                    rbumperisreleased = true;
                }


                telemetry.addLine("SAVE MODE: Right Bumper to Edit ");
                telemetry.addLine("Joystick to SAVE Setup ");

            }

            telemetry.update();
            idle();
        }


        // Init loop finished, move to running now  First write the config file
//==========================================
        try {
            FileWriter fw = new FileWriter(filePath);
            for ( i=0; i<=gromit.menuvalue.length-1; i++){
                fw.write((gromit.menulabel[i]));
                fw.write(System.lineSeparator());
                fw.write(Integer.toString(gromit.menuvalue[i]));
                fw.write(System.lineSeparator());
            }
//            fw.write(values);
//            fw.write(System.lineSeparator());
            fw.close();

        } catch (IOException ex) {
            System.err.println("Couldn't log this: "+filePath);
        }
        telemetry.addData("ConfigFile saved to", filePath);
        telemetry.update();
        sleep(500);
//==========================================


    }


}
