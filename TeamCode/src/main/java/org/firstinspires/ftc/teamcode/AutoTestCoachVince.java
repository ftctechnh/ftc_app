package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous (name = "AutoTestCoachVince", group = "Vince")
public class AutoTestCoachVince extends LinearOpMode {

    HardwareBruinBot robot = new HardwareBruinBot();

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    static final double     DRIVE_SPEED             = 0.4;     // Nominal speed for better accuracy.
    static final double     TURN_SPEED              = 0.2;     // Nominal half speed for better accuracy.

    static final double     HEADING_THRESHOLD       = 5 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.1;     // Larger is more responsive, but also less stable

    static final short     STARTING_HEADING        = 315;      // Used to set the gyro offset
    static final double     fwdSpeed                = 0.5;  // Forward Speed, Normally 0.1
    static final double     rotate                  = 0.5; // Rotation Speed
    static final double     strafe                  = 0.5;  // Strafe Speed

    private GoldAlignDetector detector;



    private ElapsedTime runtime = new ElapsedTime();
    //public boolean found() { return GoldAlignExample.isFound(); }
    //public boolean isAligned() { return detector.getAligned(); }
    //public double getX() { return detector.getXPosition(); }
    //----------------------------------runOpMode-------------------------------------------------------
    public void runOpMode () {


        // Initialize the Robot
        robot.init(hardwareMap);
        robot.gyro.calibrate();
        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }
        //robot.gyro.setZAxisOffset(STARTING_HEADING);

        /*detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable();
        //Variable setting rotation angle;


*/
        // Wait for the Start button to be pushed ----------------------------START----------------------------------------------
        while (!isStarted()) {
            // Put things to do prior to start in here
            telemetry.addData(">", "Robot Heading = %d", robot.gyro.getIntegratedZValue());
            telemetry.update();
        }


 /*       // Stressing Drive Test Sequence
        moveBot(1,0,0,1);
        sleep(300);
        moveBot(0,0,1,1);
        sleep(300);
        moveBot(-1,0,0,1);
        sleep(300);
        moveBot(0,0,-1,1);
        sleep(300);
        gyroSpin(179);
        gyroSpin(0);
        sleep(300);
        moveBot(1,1,1,1);
        sleep(300);
        stopBot();
*/

        // Lower the Robot from the lander
        //robot.landerLatchLift.setPower(0.3);
        //sleep(2000);
        //robot.landerLatchLift.setPower(0);

        // Move the robot a little bit backwards to unhook
        //moveBot(-fwdSpeed,0,0);
        //sleep(200);
        //stopBot();
        //sleep(3000);
        // Move the robot forward until it sees the Red line with the color sensor
        /*while (robot.colorSensor.red() < 2 && robot.colorSensor.green() > 0 && robot.colorSensor.blue() < 0) {
            moveBot(0,0,strafe);
        }
        stopBot();
        sleep(3000);
        */
        // Find the Gold mineral and knock it off the spot

        // Move at a heading of 315 until directly in front of the North vuforia mark or until XX distance from the wall
        //gyroHold(fwdSpeed,315,2000);
        //sleep(3000);
        // Rotate to a heading of 270
        //gyroSpin(270);
        //sleep (3000);
        //gyroSpin(90);

        // Move towards the wall until 7 inches away while maintaining a heading of 270
        //while(robot.rangeSensor.getDistance(DistanceUnit.INCH) > 7){
            //gyroStrafe(0);
       //}
       //stopBot();
         //Drive backwards maintaining 2-4 inches from the wall until you see the red tape line
        while(sonarDistance() > 12){
            double wsteer=wallSteer(5);
            moveBot(0.2,0,wsteer,0.5);
        }
        stopBot();
        // Drop the totem

        // Drive forwards maintaining 2-4 inches from the wall until...You get to the crater?
        // Let's try wall crawling for a time, then turning and homing on the crater with the distance sensor

        //  Turn 180 and deploy the arm


}
    public void moveBot(double drive, double rotate, double strafe, double scaleFactor)
    {
        // This module takes inputs, normalizes them, applies a scaleFactor, and drives the motors
        double wheelSpeeds[] = new double[4];
        wheelSpeeds[0] = drive + strafe - rotate;
        wheelSpeeds[1] = drive - strafe - rotate;
        wheelSpeeds[2] = drive - strafe + rotate;
        wheelSpeeds[3] = drive + strafe + rotate;

        double maxMagnitude = Math.abs(wheelSpeeds[0]);

        // Check the WheelSpeeds to find the maximum value
        for (int i = 1; i < wheelSpeeds.length; i++)
        {
            double magnitude = Math.abs(wheelSpeeds[i]);
            if (magnitude > maxMagnitude)
            {
                maxMagnitude = magnitude;
            }
        }
        // Normalize the WheelSpeeds against the max value
        if (maxMagnitude > 1.0)
        {
            for (int i = 0; i < wheelSpeeds.length; i++)
            {
                wheelSpeeds[i] /= maxMagnitude;
            }
        }
        // Drive the motors scaled by scaleFactor
        robot.leftFrontDrive.setPower(scaleFactor * wheelSpeeds[0]);
        robot.leftRearDrive.setPower(scaleFactor * wheelSpeeds[1]);
        robot.rightFrontDrive.setPower(scaleFactor * wheelSpeeds[2]);
        robot.rightRearDrive.setPower(scaleFactor * wheelSpeeds[3]);

        //telemetry.addData("drive",drive);
        //telemetry.addData("strafe",strafe);
        //telemetry.addData("rotate",rotate);
        //telemetry.addData("scaleFactor",scaleFactor);
        //telemetry.update();
    }
    public void stopBot()
    {
        // This function stops the robot
        robot.leftFrontDrive.setPower(0);
        robot.leftRearDrive.setPower(0);
        robot.rightFrontDrive.setPower(0);
        robot.rightRearDrive.setPower(0);
    }

    public double wallSteer( double distance) {
        // distance is in inches
        double steer = 0;
        steer = robot.rangeSensor.getDistance(DistanceUnit.INCH) - distance;
        steer = (double)Range.clip(steer, -0.2, 0.2);
    return steer;
    }

    public void gyroSpin(double heading) {
        double error = getError(heading);
        while (Math.abs(error) > 5) {
            error = getError(heading);
            if (error < 0 && Math.abs(error) > 5) {
                moveBot(0, .3, 0, 0.4);
            } else {
                moveBot(0, -0.3, 0, 0.4);
            }
            telemetry.addData("Heading",robot.gyro.getIntegratedZValue());
            telemetry.update();
        }
        stopBot();
    }

    public void gyroStrafe ( double speed, double heading){


        double error = getError(heading);

            error = getError(heading);
            if (error < 0 && Math.abs(error) > 5) {
                // Nagative error greater than 5 degrees, left of desired heading, input positive rotation
                moveBot(0, .1, 0.6, 0.6);
            } else if (error > 0 && Math.abs(error) > 5){
                // Positive Error greater than 5 degrees, right of desired heading, input negative rotation
                moveBot(0, -0.1, 0.6, 0.6);
            } else {
                // Robot is on course
                moveBot(0, 0, speed, 0.6);
            }

    }
    /**
     *  Method to obtain & hold a heading for a finite amount of time
     *  Move will stop once the requested time has elapsed
     *
     * @param speed      Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     * @param holdTime   Length of time (in seconds) to hold the specified heading.
     */

    public void gyroHold( double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeading(speed, angle, P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
        stopBot();
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
        //double leftSpeed;
        //double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            //leftSpeed  = 0.0;
            //rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            //rightSpeed  = speed * steer;
            //leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        moveBot(speed,steer,0, speed);

        return onTarget;
    }



    /*
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - robot.gyro.getIntegratedZValue();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    public double sonarDistance (){
        // Returns distance from the sonar sensor over an average of 4 values
        // Trying to get around nose in the sensor
        // 110 is the scaling factor between voltage and distance in INCHES
        // based on data collected on 11/17/2018
        double average;
        average = robot.sonarSensor.getVoltage();
        sleep(1);
        average = average + robot.sonarSensor.getVoltage();
        sleep(1);
        average = average + robot.sonarSensor.getVoltage();
        sleep(1);
        average = average + robot.sonarSensor.getVoltage();
        return (average*110)/4;

    }

}


