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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

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

@Autonomous(name="Gromit: Gyro", group="zRick")
@Disabled

public class Gromit_Menu_Gyro extends LinearOpMode {



    /* Declare OpMode members. */
    HardwareGromit         gromit    = new HardwareGromit();   // Use a Pushbot's hardware
    //DeviceInterfaceModule dim;

    // Create timer to toggle LEDs
    public ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    static final double     DRIVE_SPEED             = 0.6;     // Nominal speed for better accuracy.
    static final double     TURN_SPEED              = 0.4;     // Nominal half speed for better accuracy.

    static final double     HEADING_THRESHOLD       = 2.0 ;     // As tight as we can make it with an integer gyro
    static final double     TURN_MIN_SPEED          = 0.07;     //Speed it should approach as it comes near to the target
    static final double     PROPORTIONAL_TURN_COEFF = 0.7;      //fudge factor for turning Larger is more responsive, but also less stable an could take longer
    static final double     PROPORTIONAL_DRIVE_COEFF= 0.03;     // Larger is more responsive, but also less stable

    static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    static final int    RED_LED     = 1;     // Red LED Channel on DIM

    public int      nitems = 4;    // easier to just include 0 as the first element!
    public String[] menulabel = {"Blue Team","Mode", "Delay", "Distance 1","Turn 1"};

    public int[]    menuvalue = {1,1,10,20,45};
    public int      programmode,secondstodelay,distance1,turn1;
    public boolean  TeamisBlue;



    @Override
    public void runOpMode() {
        /*
         * Initialize the standard drive system variables.
         * The init() method of the hardware class does most of the work here
         */
        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/
        telemetry.addData(">", "Calibrating Gyro");    // Send telemetry message to alert driver that we are calibrating;
        telemetry.update();

        gromit.init(hardwareMap);
        //gromit.imu = hardwareMap.get(BNO055IMU.class, "imu"); //Moved to Hardware_8045

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
            double[] angles = getAngles();                          // display the current angles
            telemetry.addData("","Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);

            // display the menu values that are set
            telemetry.addLine("----------------------------READY TO RUN!--------------");
            if (TeamisBlue) { telemetry.addLine("BLUE Alliance");   }   //If Blue Highlight
            else            { telemetry.addLine("RED Alliance");    }   //Otherwise must be Red
            //  loop to display the configured items  if the index matches add arrow. */
            for ( int i=1; i<=nitems; i++){ telemetry.addLine().addData(menulabel[i], menuvalue[i]); }
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

        gyroturn(turn1);
        gyrodrive(distance1,turn1,DRIVE_SPEED);
        sleep(1000);
        gyrodrive(-distance1,turn1,DRIVE_SPEED);

        while (opModeIsActive()) {
            double[] angles = getAngles();
            // this adds telemetry data using the telemetrize() method in the MasqAdafruitIMU class
            telemetry.addData("","Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);
            telemetry.update();

            // continuous rotation servo
            if (gamepad1.dpad_up) { gromit.servoCR.setPower(1.0); }
            else if (gamepad1.dpad_down) { gromit.servoCR.setPower(-1.0); }
            else  { gromit.servoCR.setPower(0.0); }



            idle();
        }

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
    public void gyroDrive ( double angle, double distance,  double speed)
    {

        int     newLeftTarget;
        int     newRightTarget;
        int     moveCounts;
        double  max;
        double  error;
        double  steer;
        double  leftSpeed;
        double  rightSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            moveCounts = (int)(distance * COUNTS_PER_INCH);
            newLeftTarget = gromit.motorleft.getCurrentPosition() + moveCounts;
            newRightTarget = gromit.motorright.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            gromit.motorleft.setTargetPosition(newLeftTarget);
            gromit.motorright.setTargetPosition(newRightTarget);

            gromit.motorleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            gromit.motorright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
            gromit.motorleft.setPower(speed);
            gromit.motorright.setPower(speed);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                   (gromit.motorleft.isBusy() && gromit.motorright.isBusy())) {

                // adjust relative speed based on heading error.
                error = getError(angle);                    // degrees away from target
                steer = getSteer(error, PROPORTIONAL_DRIVE_COEFF);     //  return Range.clip(error * PCoeff, -1, 1

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1.0;

                leftSpeed = speed - steer;
                rightSpeed = speed + steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0)
                {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                gromit.motorleft.setPower(leftSpeed);
                gromit.motorright.setPower(rightSpeed);

                // Display drive status for the driver.
                telemetry.addData("Err/St",  "%5.1f/%5.1f",  error, steer);
                telemetry.addData("Target",  "%7d:%7d",      newLeftTarget,  newRightTarget);
                telemetry.addData("Actual",  "%7d:%7d",      gromit.motorleft.getCurrentPosition(),
                                                             gromit.motorright.getCurrentPosition());
                telemetry.addData("Speed",   "%5.2f:%5.2f",  leftSpeed, rightSpeed);
                telemetry.update();
            }

            // Stop all motion;
            gromit.motorleft.setPower(0);
            gromit.motorright.setPower(0);

            // Turn off RUN_TO_POSITION
            gromit.motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            gromit.motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     *  Method to spin on central axis to point in a new direction.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the heading (angle)
     *  2) Driver stops the opmode running.
     *
     * @param speed Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
/*    public void gyroTurn (  double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }
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

        moveCounts = (int)(distance * COUNTS_PER_INCH);
        LeftTarget = gromit.motorleft.getCurrentPosition() + moveCounts;
        RightTarget = gromit.motorright.getCurrentPosition() + moveCounts;
        gromit.motorleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        gromit.motorright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//      Forward
        if (distance >0) {
            while (opModeIsActive()  && gromit.motorleft.getCurrentPosition() <= RightTarget) {
                angles = getAngles();
                correction = Range.clip((headingtarget - angles[0]) * PROPORTIONAL_DRIVE_COEFF, -1.0 + speed, 1.0 - speed);
                // set the motor powers  Range.clip(error * PCoeff, -1, 1);
                gromit.motorright.setPower(speed + correction);       //max correction is 180, scale the speed.
                gromit.motorleft.setPower(speed - correction);       // min power 0.03?  add sign for negative correction
                //
                telemetry.addData("", "Yaw:%.3f Pitch:%.3f Roll: %.3f Correction %.3f", angles[0], angles[1], angles[2],correction);
                telemetry.addData("", "tgt: %.3f correction %.3f", headingtarget, correction);
                telemetry.update();
                idle();
            }
        }
        // backward  reverse proportional coefficient probably needs to be less with rear wheel drive...
        else if (distance <0) {
            while (opModeIsActive()  && gromit.motorleft.getCurrentPosition() >= RightTarget) {
                angles = getAngles();
                correction = Range.clip((headingtarget - angles[0]) * PROPORTIONAL_DRIVE_COEFF/2, -1.0 + speed, 1.0 - speed);
                // set the motor powers  Range.clip(error * PCoeff, -1, 1);
                gromit.motorright.setPower(-speed + correction);       //max correction is 180, scale the speed.
                gromit.motorleft.setPower( -speed - correction);       // min power 0.03?  add sign for negative correction
                i += 1;
                // this adds telemetry data using the telemetrize() method in the MasqAdafruitIMU class
                telemetry.addData("", "Yaw:%.3f Pitch:%.3f Roll: %.3f Correction ", angles[0], angles[1], angles[2]);
                telemetry.addData("", "Speed %.3f Correction %.3f s+c %.3f", speed, correction, speed + correction);
                telemetry.update();
                idle();
            }
        }



        gromit.motorright.setPower(0.0);
        gromit.motorleft.setPower (0.0);
        sleep(2000);
    }



    public void gyroturn(double target) {
        double[] angles = getAngles();

        double correction = target - angles[0];
        if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
        if (correction >=  180) correction -=360;

        while( abs(correction) >= HEADING_THRESHOLD && opModeIsActive()  ) {      // within 2.0 degrees is ok usually over corrects by a bit
            angles =  getAngles();
            correction = target - angles[0];
            if (correction <= -180) correction +=360;   // correction should be +/- 180 (to the left negative, right positive)
            if (correction >=  180) correction -=360;
            // set the motor powers
            double adjustment = Range.clip( (Math.signum(correction)*TURN_MIN_SPEED + PROPORTIONAL_TURN_COEFF*correction/180),-1,1);  // adjustment is motor power: sign of correction *0.07 (base power)  + a proportional bit
            gromit.motorright.setPower( adjustment) ;       //max correction is 180, scale the speed.
            gromit.motorleft.setPower( -adjustment);       // min power 0.03?  add sign for negative correction

            //telemetry.addData("correction",correction);
            //telemetry.addData(gromit.getName(), gromit.telemetrize());
            telemetry.addData("","Yaw: %.3f  Pitch: %.3f  Roll: %.3f", angles[0], angles[1], angles[2]);
            telemetry.update();
            idle();

        }
        gromit.motorright.setPower(0.0);
        gromit.motorleft.setPower (0.0);

    }



    /**
     * Perform one cycle of closed loop heading control.
     *
     * @param speed     Desired speed of turn.
     * @param angle     Absolute Angle (in Degrees) relative to last gyro reset.
     *                  0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                  If a relative angle is required, add/subtract from current heading.
     * @param PCoeff    Proportional Gain coefficient
     * @return
     */
    boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        gromit.motorleft.setPower(leftSpeed);
        gromit.motorright.setPower(rightSpeed);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        double[] angles = getAngles();
        //robotError = targetAngle - gyro.getIntegratedZValue();
        robotError = targetAngle - angles[0];
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @param PCoeff  Proportional Gain Coefficient
     * @return
     */

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }
    /*private void setImuParameters() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.useExternalCrystal = true;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.pitchMode = BNO055IMU.PitchMode.WINDOWS;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu.initialize(parameters);
    }*/
    /**
     * This method returns a 3x1 array of doubles with the yaw, pitch, and roll in that order.
     * The equations used in this method came from:
     * https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles#Euler_Angles_from_Quaternion
     */
    public double[] getAngles() {

        Quaternion quatAngles = gromit.imu.getQuaternionOrientation();

        double w = quatAngles.w;
        double x = quatAngles.x;
        double y = quatAngles.y;
        double z = quatAngles.z;

        // for the Adafruit IMU, yaw and roll are switched
        double roll = Math.atan2( 2*(w*x + y*z) , 1 - 2*(x*x + y*y) ) * 180.0 / Math.PI;
        double pitch = Math.asin( 2*(w*y - x*z) ) * 180.0 / Math.PI;
        double yaw = Math.atan2( 2*(w*z + x*y), 1 - 2*(y*y + z*z) ) * 180.0 / Math.PI;

        return new double[]{yaw, pitch, roll};
    }



//****************************************************************************************************************************************

    public void OurMenu(String fileName) {



        String directoryPath    = "/sdcard/FIRST/";
        String filePath         = directoryPath + fileName ;
        new File(directoryPath).mkdir();        // Make sure that the directory exists
        //logFile = new File(filePath);

        //menulabel = new String[];
        //menuvalue = new int[];

// read labels & values from file before we start the init loop
        //==========================================
        //String filePath    = "/sdcard/FIRST/8045_Config.txt";

        int i = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null  && i <= nitems){                // read the label then the value they are on separate lines
                menulabel[i] = s;                                   //Updates the label "string" of our array
                s = br.readLine();
                menuvalue[i] = Integer.parseInt(s);                //values is our integer array, converts string to integer
                //System.out.println(s);
                i += 1;                                         //Only to do with our array
            }
            fr.close();

        } catch (IOException ex) {
            System.err.println("Couldn't read this: "+filePath);//idk where this is printing
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
            TeamisBlue = (menuvalue[0] != 0);
            programmode    = menuvalue[1];
            secondstodelay = menuvalue[2];
            distance1 = menuvalue[3];
            turn1   = menuvalue[4];

            // output current settings to the screen  (1st token is red/blue treat it separately.
            if (menuvalue[0] == 1) {       //If Blue Highlight if index is 0
                if (index == 0 && editmode ){telemetry.addLine("BLUE Alliance  <=======");} else {telemetry.addLine("BLUE Alliance");}
            } else {                    //Otherwise must be Red Highlight if index is 0
                if (index == 0 && editmode ){telemetry.addLine("RED Alliance  <=======");} else {telemetry.addLine("RED Alliance");}
            }
//          loop to display the configured items  if the index matches add arrow.
            for ( i=1; i<=nitems; i++){
                //telemetry.addData("*",values[i]);
                if (i == index && editmode) {
                    telemetry.addLine().addData(menulabel[i], menuvalue[i]+"  <=======");

                }else{
                    telemetry.addLine().addData(menulabel[i], menuvalue[i]);
                }

            }
            telemetry.addLine("");

            // blink lights for red/blue  while in the menu method
            boolean even = (((int) (runtime.time()) & 0x01) == 0);             // Determine if we are on an odd or even second
            if ( TeamisBlue) {
                gromit.dim.setLED(BLUE_LED, even); // blue for true
                gromit.dim.setLED(RED_LED, false);
            } else {
                gromit.dim.setLED(RED_LED,  even); // Red for false
                gromit.dim.setLED(BLUE_LED, false);
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
                if (index > nitems) index = 0;          // limit the bounds of index
                if (index < 0) index = nitems;

                // increase or decrease the values
                if (gamepad1.b) {
                    if (bisreleased) {
                        bisreleased = false;
                        menuvalue[index] += 1;
                    }
                } else {
                    bisreleased = true;
                }
                if (gamepad1.x) {
                    if (xisreleased) {
                        xisreleased = false;
                        menuvalue[index] -= 1;
                    }
                } else {
                    xisreleased = true;
                }

                // limit the values of some buttons
                if (menuvalue[0] > 1) menuvalue[0] = 0;          //team color is boolean
                if (menuvalue[0] < 0) menuvalue[0] = 1;          //team color is boolean

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
            for ( i=0; i<=nitems; i++){
                fw.write((menulabel[i]));
                fw.write(System.lineSeparator());
                fw.write(Integer.toString(menuvalue[i]));
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
