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
package org.firstinspires.ftc.teamcode.State;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.State.Hardware_8045;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.abs;



@Autonomous(name="Gromit: Auto", group="Team")
@Disabled

public class AutoGyro_Linear extends LinearOpMode {



    /* Declare OpMode members. */
    Hardware_8045 gromit    = new Hardware_8045();   // Use a 8045 hardware
    //DeviceInterfaceModule dim;

    // Create timer to toggle LEDs
    public ElapsedTime runtime = new ElapsedTime();
    public boolean targetisfront;
    //Variables moved to Hardware_8045




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


        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/
        gromit.lightsblue.setPower(0.0);
        gromit.servoloader.setPosition(gromit.loaderclosed);
        // light sensor stuff
        boolean bPrevState = true;
        boolean bLedOn = false;


//
        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            //Too many sensors to update each loop update once a second
            //boolean even = (((int) (runtime.time()*2) & 0x01) == 0);
                double[] angles = gromit.getAngles();                          // display the current angles
                telemetry.addData("", "Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);

                // send the info back to driver station using telemetry function.
                telemetry.addData("ods Raw :", gromit.odsSensor.getRawLightDetected());
                if (gamepad1.start) { gromit.muxColor.startPolling(); }

                    // Mux Color Readings
                for (int i = 0; i < gromit.ports.length; i++) {
                    int[] crgb = gromit.muxColor.getCRGB(gromit.ports[i]);

                    telemetry.addLine("Sensor " + gromit.ports[i]);
                    telemetry.addData("CRGB", "%5d %5d %5d %5d",
                            crgb[0], crgb[1], crgb[2], crgb[3]);
                    telemetry.addData("R:B   B:R", "%5.2f %5.2f ",
                            1.0*crgb[1]/crgb[3], 1.0*crgb[3]/crgb[1]);
                }
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

        if( (gromit.programmode == 0 || gromit.programmode == 2) ) {                                           /**Beacon Route mode 0*/
            sleep(1000*gromit.secondstodelay);   // delay for other team;

            /**  Here's the code for picking up the partner's ball  order of steps?? */
            if (gromit.PickUpPartnerBall){
                                                                                // might need to drive forward here?  gyrodrive?
                gromit.motorsweeper.setPower(0.5);                               // Turn on sweeper
                gromit.particles = gromit.particles+1;                                                                // might need to drive backward here?
                sleep(200);                                                                // wait a little bit?
                if(gromit.TeamisBlue) {
                    gyroturnonewheel(gromit.heading0,"left");
                } else{
                    gyroturnonewheel(gromit.heading0,"right");
                }

            }

            /** Drive 0 - forward to shoot*/
            gyrodrive(gromit.distance0, gromit.heading0, gromit.GYRO_DRIVE_SPEED/2); //drive forward a little

            /** mode 2 turn  to shoot*/
            if( ( gromit.programmode == 2) ) {                                           /** try to hit out slices */
                if (gromit.TeamisBlue){gyroturnonewheel(gromit.supermodeturnblue,"both");}
                else{gyroturnonewheel(gromit.supermodeturnred,"both");}
            }

            else {                                                           /** normal mode */
                if (!gromit.TeamisBlue){gyroturnonewheel(-15.0,"both");}    /** if Red turn different amt normal mode? */
            }
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
                        sleep(350);   // let ball settle,  shouldn't have to worry about closing servo gate again.
                        //sleep(1500);
                    }
                }
            }
            //      gyrodrive(10, 0, gromit.DRIVE_SPEED); //drive back to the start

            if( ( gromit.programmode == 2) ) {                                           /** turn on sweeper if supermode */
                gromit.motorsweeper.setPower(gromit.sweeperpower);
            }
            else if(gromit.programmode == 0){
                gromit.motorsweeper.setPower(0.0);
            }

            /** Drive 1 - forward to wall  */
        if(gromit.programmode == 2 && gromit.TeamisBlue){
            gromit.distance1 = gromit.distance1+2;
        }
            if (!gromit.TeamisBlue) { gromit.heading1 = -gromit.heading1;   }
            gyroturnonewheel(gromit.heading1, "both");
            sleep(50);
            gyrodrive(gromit.distance1, gromit.heading1, gromit.GYRO_DRIVE_SPEED*0.7);                       // first drive to wall

            /** Drive 2 - backup into corner  */
            if (!gromit.TeamisBlue) {
                gromit.heading2 = -gromit.heading2;
               // gromit.distance2 = gromit.distance2 - 2;
            }                       // red team turns in opposite direction
            sleep(50);
            gyroturnonewheel(gromit.heading2,"both");                                                              // turn to head back into corner
            sleep(50);
            gyrodrive(gromit.distance2, gromit.heading2, gromit.GYRO_DRIVE_SPEED*0.60);                       //backup to corner & Wall


            /** Drive 3 - parallel to wall to white line   suck in buttons*/
            gromit.servoCR.setPower(0.15); sleep(5); gromit.servoCR.setPower(0.05);
            //sleep(50);
            gyroturnonewheel(gromit.heading3, "both");                                             //turn against wall, one or two wheels better?
            sleep(50);
            gyrodrivetoline(gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4);                             // drive along wall to first white line

            /**Sense Color*/
            gromit.muxColor.startPolling();
            sleep(10);
            readcolors();
            if(targetisfront){
                gyrodrive( 4, gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4);
            }
            //This method takes care of pressing the button
            pushbutton();
            //gromit.servoCR.setPower(-0.7); sleep(gromit.buttontime); gromit.servoCR.setPower(0.7); sleep(gromit.buttontime); gromit.servoCR.setPower(0.05);  // button servo

            /** Drive 4 - Parallel to wall to second line  */
            gyrodrive(gromit.distance4, gromit.heading4, gromit.GYRO_DRIVE_SPEED*0.8);                       // drive forward fast for most of the way
            gyrodrivetoline(gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4);                             // drive along wall to second white line
            sleep(30);
            readcolors();
            if(targetisfront){
                gyrodrive( 4, gromit.heading3, gromit.GYRO_DRIVE_SPEED * 0.4);
            }
            pushbutton();

            /** Drive 5 - head to the center cap ball  */
            if(!gromit.TeamisBlue){
                gromit.heading5 = gromit.heading5+5;
            }
            if (!gromit.TeamisBlue) { gromit.heading5 = -gromit.heading5; }                                 // red team turns in opposite direction

            gyroturnonewheel(gromit.heading5,"both");                                                       // turn towards cap ball
            gyrodrive(gromit.distance5 * 0.40, gromit.heading5, gromit.GYRO_DRIVE_SPEED);
            gromit.motorsweeper.setPower(0.0);

            if(  gromit.programmode == 2  && (gromit.ballsensor.getVoltage() > gromit.ballthreshold || !gromit.ballsensor2isEmpty.getState()) ) {    /** Super mode & ball sensed*/
                sleep(100);    //Is this necessary?                                                               /**fire  third ball**/
                gromit.lightsgreen.setPower(0.5);
                gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
                irvoltage = gromit.sharp.getVoltage();
                gromit.servoloader.setPosition(gromit.loaderclosed);
                gromit.motorlauncher.setPower(-0.5);

                    while (opModeIsActive() && (irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800 ) ) {
                        irvoltage = gromit.sharp.getVoltage();
                        idle();
                    }
                    gromit.motorlauncher.setPower(0.0);
                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
                    gromit.servoloader.setPosition(gromit.loaderopen);
                //wait for ball to roll in
                double now = getRuntime();
                while (opModeIsActive() && ( gromit.ballsensor.getVoltage()< gromit.ballthreshold && gromit.ballsensor2isEmpty.getState() && getRuntime() < now + 1 )) {   /** wait till loaded, or 1.7 seconds */
                    idle();
                }
                if (gromit.ballsensor.getVoltage() > gromit.ballthreshold || !gromit.ballsensor2isEmpty.getState()) {
                    //sleep(100);     // is this necessary?                                                    /**fire  third ball**/
                    gromit.servoloader.setPosition(gromit.loaderclosed);
                    gromit.lightsgreen.setPower(0.5);
                    gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
                    irvoltage = gromit.sharp.getVoltage();

                    gromit.motorlauncher.setPower(-0.5);

                    while (opModeIsActive() && (irvoltage <= gromit.loaderthreshold || gromit.motorlauncher.getCurrentPosition() >= gromit.firingposition - 800 ) ) {
                        irvoltage = gromit.sharp.getVoltage();
                        idle();
                    }
                    gromit.motorlauncher.setPower(0.0);
                    gromit.launcherzeroposition = gromit.motorlauncher.getCurrentPosition();
                    gromit.servoloader.setPosition(gromit.loaderopen);
                }


            }

            gyrodrive(gromit.distance5 * 0.70, gromit.heading5, gromit.GYRO_DRIVE_SPEED);                   /** go hit cap ball & Park */


            /** end of Main Mode & Super Mode */

          /**  Begin Defensive Capball route  */

        }else if(gromit.programmode == 1 ) {
            readcolors();
            pushbutton();


            sleep(10000);
            sleep(1000*gromit.secondstodelay);   // delay for other team;
            // capball mode
            if (gromit.TeamisBlue) {                   /** if Red turn different amt */
                gyroturnonewheel(-40, "right"); //right is the direction it turns
                gyrodrive(10, -40, gromit.GYRO_DRIVE_SPEED);
            } else {
                gyroturnonewheel(40, "right"); //right is the direction it turns
                gyrodrive(10, -40, gromit.GYRO_DRIVE_SPEED);

            }
            sleep(300);
            /** Shoot Particles */

            if (gromit.particles  > 0 ) {
                for (int i = 1; i <= gromit.particles; i++) {
                    //SHOOT PARTICLE 1
                    gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
                    irvoltage = gromit.sharp.getVoltage();
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
                        while (opModeIsActive() && ( gromit.ballsensor.getVoltage()< gromit.ballthreshold && gromit.ballsensor2isEmpty.getState()  && getRuntime() < now + 1.7 )) {   /** wait till loaded, or 1.7 seconds */
                            idle();
                        }
                        sleep(100);   // let ball settle,  shouldn't have to worry about closing servo gate again.
                        //sleep(1500);
                    }
                }
            }

               /** better comments here would be good to describe route */       /** red numbers?? */

            gyroturnonewheel(-65, "both");                        //right is the direction it turns
            gyrodrive(48, -65, 1.5* gromit.GYRO_DRIVE_SPEED);     //move across the field
            //gyroturnonewheel(60, "left");                       //left is the direction it turns, hit the capball
            gyroturnonewheel(0, "left");                          //left is the direction it turns, turn to get around the cap ball
            gyrodrive( 20, 0,  1.5* gromit.GYRO_DRIVE_SPEED);
            gyroturnonewheel(-45, "both");
            gyrodrive( -10, -45, 1.5* gromit.GYRO_DRIVE_SPEED);   //Hit Ball

            gyroturnonewheel(0, "both");                          //turn back to zero
            gyrodrive( 20, 0, 1.5* gromit.GYRO_DRIVE_SPEED);      //go to the other side
            gyroturnonewheel(20, "both"); //turn back to zero
            gyrodrive( 20, 20, 1.5* gromit.GYRO_DRIVE_SPEED);     //go to the other side


        }
        /**
         * SIMPLE CAPBALL MODE
         */
        else if(gromit.programmode == 3 && opModeIsActive()) {                   /** test mode for things right now */
            // capball mode
            if (gromit.TeamisBlue) {                   /** if Red turn different amt */
                gyroturnonewheel(-40, "right"); //right is the direction it turns
                gyrodrive(15, -40, gromit.GYRO_DRIVE_SPEED);
            } else {
                gyroturnonewheel(35, "right"); //right is the direction it turns
                gyrodrive(15, 35, gromit.GYRO_DRIVE_SPEED);

            }
            sleep(300);
            /** Shoot Particles */

            if (gromit.particles  > 0 ) {
                for (int i = 1; i <= gromit.particles; i++) {
                    //SHOOT PARTICLE 1
                    gromit.firingposition = gromit.motorlauncher.getCurrentPosition();
                    irvoltage = gromit.sharp.getVoltage();
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
                        while (opModeIsActive() && ( gromit.ballsensor.getVoltage()< gromit.ballthreshold && gromit.ballsensor2isEmpty.getState()  && getRuntime() < now + 1.7 )) {   /** wait till loaded, or 1.7 seconds */
                            idle();
                        }
                        sleep(100);   // let ball settle,  shouldn't have to worry about closing servo gate again.
                        //sleep(1500);
                    }
                }
            }

            sleep(10000);   // delay for other team;

            if (gromit.TeamisBlue) {                   /** if Red turn different amt */
                gyroturnonewheel(-40, "right"); //right is the direction it turns
                gyrodrive(25, -40, gromit.GYRO_DRIVE_SPEED);
            } else {
                gyroturnonewheel(35, "right"); //right is the direction it turns
                gyrodrive(25, 35, gromit.GYRO_DRIVE_SPEED);

            }

        }


        while (opModeIsActive()) {

            idle();
        }

    }


    public void readcolors(){
        int[] front;
        int[] back;

        if (gromit.TeamisBlue){
            front = gromit.muxColor.getCRGB(gromit.ports[0]);
            back = gromit.muxColor.getCRGB(gromit.ports[1]);
        }
        else{
            front = gromit.muxColor.getCRGB(gromit.ports[2]);
            back = gromit.muxColor.getCRGB(gromit.ports[3]);
        }
        float rvbf = front[1] / front[3];                                                           //Red Versus Blue Front Sensor
        float rvbb = back[1] / back[3];                                                             //Red Versus Blue Back Sensor
        if (rvbf > rvbb){
                targetisfront = true; //Red Button is on front sensor
        }
        else{
                targetisfront = false; //Red Button is on Rear Sensor
        }
        if(gromit.TeamisBlue) {
            targetisfront = !targetisfront; //negate the target when blue to hit the blue button
        }
    }

    public void pushbutton() {
        if (!targetisfront) {        /** if the target is back, we can watch the front for a change */
            int[] colorvalues;
            int sensortestport;
            float ratio;
            gromit.servoCR.setPower(-0.7);
            double presstime = getRuntime();
            if (gromit.TeamisBlue) {
                sensortestport = gromit.ports[0]; //Reads zeroth sensor for Blue
            } else {
                sensortestport = gromit.ports[2];//Reads second sensor for Red
            }
            colorvalues = gromit.muxColor.getCRGB(sensortestport);
            if (gromit.TeamisBlue) {
                ratio = colorvalues[3] / colorvalues[1]; //[1] is red [3] is blue value from sensor CRGB
            } else {
                ratio = colorvalues[1] / colorvalues[3]; //[1] is red [3] is blue value from sensor CRGB
            }
            while (opModeIsActive() && getRuntime() < presstime + 2 && (ratio < 1.2)) {
                colorvalues = gromit.muxColor.getCRGB(sensortestport);
                if (gromit.TeamisBlue) {
                    ratio = colorvalues[3] / colorvalues[1]; //[1] is red [3] is blue value from sensor CRGB
                } else {
                    ratio = colorvalues[1] / colorvalues[3]; //[1] is red [3] is blue value from sensor CRGB
                }

                //telemetry.addData("ratio        ", ratio);
                //telemetry.update();
                idle();

            }
            gromit.servoCR.setPower(0.7);
            sleep(100);
            gromit.servoCR.setPower(0.15);

        } else {     // target is  in the front, so can't used the second sensor to read while pushing.
            gromit.servoCR.setPower(-0.7); sleep(gromit.buttontime); gromit.servoCR.setPower(0.7); sleep(gromit.buttontime/2); gromit.servoCR.setPower(0.15);
        }
    }
/*
       //gromit.servoCR.setPower(-0.7); sleep(gromit.buttontime); gromit.servoCR.setPower(0.7); sleep(gromit.buttontime); gromit.servoCR.setPower(0.05);  // button servo

        int sensortestport;
        float initialratio;
        float ratio;
        int[] colorvalues;
        gromit.servoCR.setPower(-0.7);
        double presstime = getRuntime();
        if (gromit.TeamisBlue){
            sensortestport = gromit.ports[1]; //Reads first sensor for Blue
        }
        else{
            sensortestport = gromit.ports[3];//Reads third sensor for Red
        }
        colorvalues = gromit.muxColor.getCRGB(sensortestport);
        if (gromit.TeamisBlue){
            initialratio = colorvalues[3] / colorvalues[1]; //[1] is red [3] is blue value from sensor CRGB
        }
        else{
            initialratio = colorvalues[1] / colorvalues[3]; //[1] is red [3] is blue value from sensor CRGB
        }
        ratio = initialratio;
        while (opModeIsActive() && getRuntime() < presstime + 10 && (ratio < 1.1* initialratio) ){
            colorvalues = gromit.muxColor.getCRGB(sensortestport);
            if (gromit.TeamisBlue){
                ratio = colorvalues[3] / colorvalues[1]; //[1] is red [3] is blue value from sensor CRGB
            }
            else{
                ratio = colorvalues[1] / colorvalues[3]; //[1] is red [3] is blue value from sensor CRGB
            }
            telemetry.addData("Initial Ratio",initialratio);
            telemetry.addData("ratio        ", ratio);
            telemetry.update();
            idle();
        }
        gromit.servoCR.setPower(0.7);
        sleep(1000);
        gromit.servoCR.setPower(0.0);


    }
*/
    /**
     * This method returns applies motor power proportionately to turn to a certain angle using the Adafruit imu
     * accepts a target from 0 to 360 or -0 to -360
     */

    public void gyroturnonewheel(double target, String dir /*boolean right*/) {
        double[] angles = gromit.getAngles();

        double correction = target - angles[0];
        if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
        if (correction >=  180) correction -=360;

        while( abs(correction) >= gromit.HEADING_THRESHOLD && opModeIsActive()  ) {      // within 2.0 degrees is ok usually over corrects by a bit
            angles =  gromit.getAngles();
            correction = target - angles[0];
            if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
            if (correction >=  180) correction -=360;
            // set the motor powers
            double adjustment = Range.clip((Math.signum(correction) * gromit.TURN_MIN_SPEED + gromit.PROPORTIONAL_TURN_COEFF * correction / 180), -1, 1);  // adjustment is motor power: sign of correction *0.07 (base power)  + a proportional bit
            if(dir == "right") {
                adjustment = Range.clip (  adjustment*1.5, -1, 1);
                //gromit.motorright.setPower(adjustment);       //max correction is 180, scale the speed.
                gromit.motorleft.setPower(-adjustment);       // min power 0.03?  add sign for negative correction
            }
            else if (dir == "left") {
               adjustment = Range.clip (  adjustment*1.5, -1, 1);
                gromit.motorright.setPower(adjustment);       //max correction is 180, scale the speed.
                //gromit.motorleft.setPower(-adjustment);       // min power 0.03?  add sign for negative correction
            }
            else {
                gromit.motorright.setPower(adjustment);       //max correction is 180, scale the speed.
                gromit.motorleft.setPower(-adjustment);       // min power 0.03?  add sign for negative correction
            }

            telemetry.addData("correction",correction);
            telemetry.addData("","Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);
            telemetry.update();
            //idle();//Telemetry cannot be output in a hardware class

        }
        gromit.motorright.setPower(0.0);
        gromit.motorleft.setPower(0.0);

    }

    /**
     * This file illustrates the concept of driving a path based on Gyro heading and encoder counts.
     * It uses the common Pushbot hardware class to define the drive on the robot.
     * The code is structured as a LinearOpMode
     *
     * The code REQUIRES that you DO have encoders on the wheels,
     *   otherwise you would use: PushbotAutoDriveByTime;
     *
     *  This code ALSO requires that you have a Modern Robotics I2C gyro with the name "gyro"
     *   otherwise you would use: PushbotAutoDriveByEncoder;
     *
     *  This code requires that the drive Motors have been configured such that a positive
     *  power command moves them forward, and causes the encoders to count UP.
     *
     *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
     *
     *  In order to calibrate the Gyro correctly, the robot must remain stationary during calibration.
     *  This is performed when the INIT button is pressed on the Driver Station.
     *  This code assumes that the robot is stationary when the INIT button is pressed.
     *  If this is not the case, then the INIT should be performed again.
     *
     *  Note: in this example, all angles are referenced to the initial coordinate frame set during the
     *  the Gyro Calibration process, or whenever the program issues a resetZAxisIntegrator() call on the Gyro.
     *
     *  The angle of movement/rotation is assumed to be a standardized rotation around the robot Z axis,
     *  which means that a Positive rotation is Counter Clock Wise, looking down on the field.
     *  This is consistent with the FTC field coordinate conventions set out in the document:
     *  ftc_app\doc\tutorial\FTC_FieldCoordinateSystemDefinition.pdf
     *
     * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
     * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
     */
    public void gyrodrive( double distance, double headingtarget, double speed) {


        double[] angles ;
        double correction ;

        int     LeftTarget;
        int     RightTarget;
        int     moveCounts;


        moveCounts = (int)(distance * gromit.COUNTS_PER_INCH);
        LeftTarget = gromit.motorleft.getCurrentPosition() + moveCounts;
        RightTarget = gromit.motorright.getCurrentPosition() + moveCounts;
        gromit.motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        gromit.motorleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        gromit.motorright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


//      Forward
        if (distance >0) {
            double power = 0.0;
            while (opModeIsActive()  && gromit.motorright.getCurrentPosition() <= RightTarget) {
                angles = gromit.getAngles();
                correction = Range.clip((headingtarget - angles[0]) * gromit.PROPORTIONAL_DRIVE_COEFF, -1.0 + speed, 1.0 - speed);
                // set the motor powers  Range.clip(error * PCoeff, -1, 1);
                // ramp up
                power += 0.1;
                if (power > speed)  power = speed;
                gromit.motorright.setPower(power + correction);       //max correction is 180, scale the speed.
                gromit.motorleft.setPower(power - correction);       // min power 0.03?  add sign for negative correction
                //
                //telemetry.addData("", "Yaw:%.3f Pitch:%.3f Roll: %.3f Correction %.3f", angles[0], angles[1], angles[2],correction);
                //telemetry.addData("right target",  RightTarget);
                //telemetry.addData("pos", gromit.motorright.getCurrentPosition() );
                //telemetry.addData("count",gromit.motorright.getCurrentPosition() );
                //telemetry.update();
                idle();
            }
        }
        // backward  reverse proportional coefficient probably needs to be less with rear wheel drive...
        else if (distance <0) {
            double power = 0.0;
            while (opModeIsActive()  && gromit.motorright.getCurrentPosition() >= RightTarget) {
                angles = gromit.getAngles();
                correction = Range.clip((headingtarget - angles[0]) * gromit.PROPORTIONAL_DRIVE_COEFF/2, -1.0 + speed, 1.0 - speed);
                // set the motor powers  Range.clip(error * PCoeff, -1, 1);
                // ramp up
                power += 0.1;
                if (power > speed)  power = speed;
                gromit.motorright.setPower(-power + correction);       //max correction is 180, scale the speed.
                gromit.motorleft.setPower( -power - correction);       // min power 0.03?  add sign for negative correction

                // this adds telemetry data using the telemetrize() method in the MasqAdafruitIMU class
                //telemetry.addData("right target",  RightTarget);
                //telemetry.addData("pos", gromit.motorright.getCurrentPosition() );
                //telemetry.addData("cor", correction );
                //telemetry.update();
                idle();

            }
        }

        gromit.motorright.setPower(0.0);
        gromit.motorleft.setPower (0.0);

    }

    public void gyrodrivetoline(double headingtarget, double speed) {


        double[] angles ;
        double correction ;
        double odslevel = .6;   // MR ods light level for white line

        gromit.motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


//      Assume your driving forward

        while (opModeIsActive()  && gromit.odsSensor.getRawLightDetected() <= odslevel) {

            angles = gromit.getAngles();
            correction = Range.clip((headingtarget - angles[0]) * gromit.PROPORTIONAL_DRIVE_COEFF, -1.0 + speed, 1.0 - speed);
            // set the motor powers  Range.clip(error * PCoeff, -1, 1);
            gromit.motorright.setPower(speed + correction);       //max correction is 180, scale the speed.
            gromit.motorleft.setPower(speed - correction);       // min power 0.03?  add sign for negative correction
            //
            //telemetry.addData("", "Yaw:%.3f Pitch:%.3f Roll: %.3f Correction %.3f", angles[0], angles[1], angles[2],correction);
            //telemetry.addData("right target",  RightTarget);
            //telemetry.addData("pos", gromit.motorright.getCurrentPosition() );
            //telemetry.update();
            idle();
        }
        // backward  reverse proportional coefficient probably needs to be less with rear wheel drive...

        gromit.motorright.setPower(0.0);
        gromit.motorleft.setPower (0.0);

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
            gromit.supermodeturnblue      = gromit.menuvalue[16];
            gromit.supermodeturnred       = gromit.menuvalue[17];
            gromit.PickUpPartnerBall      = (gromit.menuvalue[18] != 0);

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
                if (gromit.menuvalue[1] >= 4) gromit.menuvalue[1] = 0;          //modes 0,1,2,3
                if (gromit.menuvalue[1] <  0) gromit.menuvalue[1] = 3;          //
                if (gromit.menuvalue[2] >= 3) gromit.menuvalue[2] = 0;          //Particles, 0,1,2
                if (gromit.menuvalue[2] <  0) gromit.menuvalue[2] = 2;          //
                if (gromit.menuvalue[3] <  0) gromit.menuvalue[3] = 0;          // Delay can't be negative
                if (gromit.menuvalue[18] >  1) gromit.menuvalue[18] = 0;          // boolean
                if (gromit.menuvalue[18] <  0) gromit.menuvalue[18] = 1;          //


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
