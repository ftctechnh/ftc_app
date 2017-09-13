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
package org.firstinspires.ftc.rick;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
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

@Autonomous(name="Rick: sharpir  ", group="zRick")
@Disabled

public class AutoGyro_sharpir extends LinearOpMode {



    /* Declare OpMode members. */
    Hardware_8045         gromit    = new Hardware_8045();   // Use a Pushbot's hardware
    //DeviceInterfaceModule dim;

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
        telemetry.addLine("Calibrating Gyro");    // Send telemetry message to alert driver that we are calibrating;
        telemetry.addLine("");    // Send telemetry message to alert driver that we are calibrating;
        telemetry.addLine("Back Button reverts to code values");    // Send telemetry message to alert driver that we are calibrating;
        telemetry.update();

        gromit.init(hardwareMap,true);
        //gromit.imu = hardwareMap.get(BNO055IMU.class, "imu"); //Moved to Hardware_8045
        AnalogInput SharpIR_right = hardwareMap.analogInput.get("ir_right");

        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        //Tried to move this to the init call above

        telemetry.addData(">", "Robot Ready.");//Send Telemetry that the robot is ready to run
        telemetry.update();

        // get menu values
        OurMenu("8045config.txt");

        /**********************************************************************************************\
        |--------------------------------------Init Loop-----------------------------------------------|
        \**********************************************************************************************/

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            double[] angles = gromit.getAngles();                          // display the current angles
            telemetry.addData("","Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);
            double IRvoltageR = SharpIR_right.getVoltage();
            double irdistanceR = -7.411*Math.pow(IRvoltageR,3) + 52.356 *Math.pow(IRvoltageR,2) -125.222*IRvoltageR +111.659 ;
            double MaxbotixDistancecm = IRvoltageR * (512/5) *2.54;
            telemetry.addData("","Analog volts, distance: %4.2f  %4.1f", IRvoltageR,MaxbotixDistancecm);

            // display the menu values that are set
            telemetry.addLine("----------------------------READY TO RUN!--------------");
            if (gromit.TeamisBlue) { telemetry.addLine("BLUE Alliance");   }   //If Blue Highlight
            else            { telemetry.addLine("RED Alliance");    }   //Otherwise must be Red
            //  loop to display the configured items  if the index matches add arrow. */
            for ( int i=1; i < gromit.menuvalue.length; i++){ telemetry.addLine().addData(gromit.menulabel[i], gromit.menuvalue[i]); }
            telemetry.addLine("----------------------------READY TO RUN!--------------");

            telemetry.update();
            idle();
        }
         /**********************************************************************************************\
         |-------------------------------------START OF GAME--------------------------------------------|
         \**********************************************************************************************/
        gromit.motorleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(50);
        gromit.motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //^^^^Should this be in a void^^^^

        gyroturn(gromit.heading1);
        gyrodrive(gromit.distance1,gromit.heading1,gromit.GYRO_DRIVE_SPEED);

        gyroturn(gromit.heading2);
        gyrodrive(gromit.distance2,gromit.heading2,gromit.GYRO_DRIVE_SPEED);
        sleep((1000)); //at first beacon
        gyroturn(gromit.heading3);    //Probably not necessary
        gyrodrive(gromit.distance3,gromit.heading3,gromit.GYRO_DRIVE_SPEED);

        while (opModeIsActive()) {
            double[] angles = gromit.getAngles();
            // this adds telemetry data using the telemetrize() method in the MasqAdafruitIMU class
            telemetry.addData("","Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);
            telemetry.update();

            // continuous rotation servo
            if (gamepad1.dpad_up) { gromit.servoCR.setPower(1.0); }
            else if (gamepad1.dpad_down) { gromit.servoCR.setPower(-1.0); }
            else  { gromit.servoCR.setPower(0.0); }


            // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
            // 1 is full down
            // direction: left_stick_x ranges from -1 to 1, where -1 is full left
            // and 1 is full right
            //throttle = (speed * (-(gamepad1.right_stick_y) * -(gamepad1.right_stick_y)*-(gamepad1.right_stick_y)));
            // temp variables
            double drivespeed = 1.0;
            double turnspeed = 0.8;
            double throttle = ((-gamepad1.right_stick_y));// * -(gamepad1.right_stick_y)*-(gamepad1.right_stick_y)));
            float direction = gamepad1.right_stick_x;
            double right = throttle -  (turnspeed * direction);
            double left =  throttle +  (turnspeed * direction);

            // scale the joystick value to make it easier to control
            // the robot more precisely at slower speeds.
            right = scaleInput(right);
            left =  scaleInput(left);

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // write the values to the motors
            gromit.motorright.setPower(right);
            gromit.motorleft.setPower(left);






            idle();
        }

    }

    double scaleInput(double dVal)  {

        // two linear lines:  slow for first half 0.5 gets mapped to 0.3  y=mx + b...
        double crossoverpointx = 0.5;
        double crossoverpointy = 0.3;
        double mappedvalue = 0.0;
        double returnvalue;

        // only worry about positive case for now
        if (Math.abs(dVal) <= crossoverpointx)   {
            mappedvalue = (crossoverpointy / crossoverpointx) * Math.abs(dVal);
        }else if (Math.abs(dVal)>0.5){
            mappedvalue = ( (1-crossoverpointy)/(1-crossoverpointx) * Math.abs(dVal) -  (1 - crossoverpointy - crossoverpointy));
        }
        // get the sign of the input
        //        if (dVal != 0) {returnvalue = mappedvalue * (Math.abs(dVal)/dVal); }
        returnvalue = Math.signum(dVal) * mappedvalue ;

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


    /**
     * This method returns applies motor power proportionately to turn to a certain angle using the Adafruit imu
     * accepts a target from 0 to 360 or -0 to -360
     */
    public void gyroturn(double target) {
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
            gromit.motorright.setPower( adjustment) ;       //max correction is 180, scale the speed.
            gromit.motorleft.setPower( -adjustment);       // min power 0.03?  add sign for negative correction

            //telemetry.addData("correction",correction);
            //telemetry.addData(gromit.getName(), gromit.telemetrize());
            //telemetry.addData("","Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);
            //telemetry.update();
            //idle();//Telemetry cannot be output in a hardware class

        }
        gromit.motorright.setPower(0.0);
        gromit.motorleft.setPower(0.0);

    }


    /**
     *  Method to drive on a fixed compass bearing (angle), based on encoder counts.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the desired position
     *  2) Driver stops the opmode running.
     *
     * @param speed      Target speed for forward motion.  Should allow for _/- variance for adjusting heading
     * @param distance   Distance (in inches) to move from current position.  Negative distance means move backwards.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */

    public void gyrodrive( double distance, double headingtarget, double speed) {


        double[] angles ;
        double correction ;
        int i = 0;
        int     LeftTarget;
        int     RightTarget;
        int     moveCounts;
        double  max;
        double  error;
        double  steer;
        double  leftSpeed;
        double  rightSpeed;

        moveCounts = (int)(distance * gromit.COUNTS_PER_INCH);
        LeftTarget = gromit.motorleft.getCurrentPosition() + moveCounts;
        RightTarget = gromit.motorright.getCurrentPosition() + moveCounts;
        gromit.motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//      Forward
        if (distance >0) {
            while (opModeIsActive()  && gromit.motorright.getCurrentPosition() <= RightTarget) {
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
        }
        // backward  reverse proportional coefficient probably needs to be less with rear wheel drive...
        else if (distance <0) {
            while (opModeIsActive()  && gromit.motorleft.getCurrentPosition() >= RightTarget) {
                angles = gromit.getAngles();
                correction = Range.clip((headingtarget - angles[0]) * gromit.PROPORTIONAL_DRIVE_COEFF/2, -1.0 + speed, 1.0 - speed);
                // set the motor powers  Range.clip(error * PCoeff, -1, 1);
                gromit.motorright.setPower(-speed + correction);       //max correction is 180, scale the speed.
                gromit.motorleft.setPower( -speed - correction);       // min power 0.03?  add sign for negative correction
                i += 1;
                // this adds telemetry data using the telemetrize() method in the MasqAdafruitIMU class
                telemetry.addData("", "Yaw:%.3f Pitch:%.3f Roll: %.3f Correction ", angles[0], angles[1], angles[2]);
                telemetry.update();
                idle();
            }
        }

        gromit.motorright.setPower(0.0);
        gromit.motorleft.setPower (0.0);

    }

 /**MENUIING JOYSTICK CONTROL IN INIT
 * Menu to change variables corresponging to gyrodrives and turns and delays etc
 * * *************************************************************************************************************************/

    public void OurMenu(String fileName) {

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

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
            gromit.TeamisBlue = (gromit.menuvalue[0] != 0);
            gromit.programmode    = gromit.menuvalue[1];
            gromit.secondstodelay = gromit.menuvalue[2];
            gromit.heading1   = gromit.menuvalue[3];
            gromit.distance1  = gromit.menuvalue[4];
            gromit.heading2   = gromit.menuvalue[5];
            gromit.distance2  = gromit.menuvalue[6];
            gromit.heading3   = gromit.menuvalue[7];
            gromit.distance3  = gromit.menuvalue[8];
            gromit.heading4   = gromit.menuvalue[9];
            gromit.distance4  = gromit.menuvalue[10];


            // output current settings to the screen  (1st token is red/blue treat it separately.
            if (gromit.menuvalue[0] == 1) {       //If Blue Highlight if index is 0
                if (index == 0 && editmode ){telemetry.addLine("BLUE Alliance  <=======");} else {telemetry.addLine("BLUE Alliance");}
            } else {                    //Otherwise must be Red Highlight if index is 0
                if (index == 0 && editmode ){telemetry.addLine("RED Alliance  <=======");} else {telemetry.addLine("RED Alliance");}
            }
//          loop to display the configured items  if the index matches add arrow.
            for ( i=1; i<gromit.menuvalue.length; i++){
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
                // change the background color to match the color detected by the RGB sensor.
                // pass a reference to the hue, saturation, and value array as an argument
                // to the HSVToColor method.
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.BLUE);
                    }
                });

            } else {
                gromit.dim.setLED(gromit.RED_LED,  even); // Red for false
                gromit.dim.setLED(gromit.BLUE_LED, false);
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.RED);
                    }
                });
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
                if (index < 0) index = gromit.menuvalue.length - 1;

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
                if (gromit.menuvalue[0] > 1) gromit.menuvalue[0] = 0;          //team color is boolean
                if (gromit.menuvalue[0] < 0) gromit.menuvalue[0] = 1;          //team color is boolean

                //telemetry.addLine(" ");
                telemetry.addLine(" EDIT MODE: Right Bumper to EXIT");


                // check to exit the edit mode
                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                        editmode = false;
                        rbumperisreleased = false;
                    }
                }
                rbumperisreleased = true;
            }
            //END OF EDIT MODE===================================================
            else {

//          in ready mode check if you'd like to return to editing, add telemetry line.

                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                        editmode = true;
                        rbumperisreleased = false;
                    }
                }
                rbumperisreleased = true;


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
            for ( i=0; i< gromit.menuvalue.length; i++){
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
