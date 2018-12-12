package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//DogeCV Imports
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous (name = "AutoTestCrater", group = "Rohan")
public class AutoTestCrater extends LinearOpMode {

    HardwareBruinBot robot = new HardwareBruinBot();

    /**Settings for mineral distances NEED TO ADD PUSHMINERAL AND LOGIC // */
    double clicksToCenter = -200;
    double clicksBetweenMineral = 400;
    double millisecondsToPush = 200;

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


  /*  private static final String VUFORIA_KEY = "AU8YYQn/////AAAAGXFrahulqEjulTTNdLAcySmHbygwS4xr5RSUhekqrTcj7ErEbW1t0GxYBrB1fZFvZQcM3NCjk0dHuDH0I5cqwrblwH33sSHg0IO6XB9zE60YKnY2UiLPE8H9DQLAZjBoAAoOoNhJJQuFD2+hxs0vU74jNqyvyvsGUqqHQ7aj2EMCEbP4p6xElobK2w374MQsFvtnviNJ/pGZxeFlzta1W/DXRpq7xJY9+1eheCOGrRrkzIvS5i/L/nb9OKUP5kwJefb4oi0wMi7O1xxSMfUq+Aq1JfI4sXTXgLM/Z7dPb9zod+x8Kl9GnJ2e43OUGYqChmcpKKH0SguasN741T3zcrs1+iynm9ATD4NOk87F56xT";
    VuforiaLocalizer vuforia;
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
*/
    private ElapsedTime runtime = new ElapsedTime();

    //public boolean found() { return GoldAlignExample.isFound(); }
    public boolean isAligned() { return detector.getAligned(); }
    public double getX() { return detector.getXPosition(); }

    //----------------------------------runOpMode-------------------------------------------------------
    public void runOpMode () {


        // Initialize the Robot
        robot.init(hardwareMap);
        robot.gyro.calibrate();
        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && robot.gyro.isCalibrating()) {
            sleep(50);
            idle();
            if (isStopRequested()) stop();

        }

        //DOGECV Variable setting rotation angle;
        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.PERFECT_AREA; // Can also be PERFECT_AREA
        detector.perfectAreaScorer.perfectArea = 5000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable();

        // Wait for the Start button to be pushed ----------------------------START----------------------------------------------
        while (!isStarted()) {
            // Put things to do prior to start in here
            telemetry.addData(">", "Robot Heading = %d", robot.gyro.getIntegratedZValue());
            telemetry.update();
            if (isStopRequested()) stop();

        }
        while (opModeIsActive()) {
            int landingLevel = -2090;  // Target level to land
            double latchPower;
            //Reset the Encoder
            robot.landerLatchLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //Lower the Robot from the lander
            while (robot.landerLatchLift.getCurrentPosition() > landingLevel) {
                robot.landerLatchLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                latchPower = -0.2;
                robot.landerLatchLift.setPower(latchPower);

            }
            robot.landerLatchLift.setPower(0);


            // Find the Gold mineral and knock it off the spot
            // Initialize a counter to count our attempts to get a little closer to the mineral
/*        double mineralCt = 0;
        boolean bumpSuccess = false;
        // Strafe a little closer to the minerals
        //gyroStrafe(0.5,0);
        //sleep(500);
        stopBot();

        while (mineralCt <1 && !bumpSuccess) {
            sleep(200); // Give the detector a second to register the cube
            if (detector.isFound()) {
                telemetry.addLine("Found it, going to mineralBump");
                telemetry.addData("mineralCt = ", mineralCt);
                telemetry.update();
                sleep(2000);
                bumpSuccess = mineralBump(1);
            } else {
                //gyroSpin(-12);  // Spin to the left to look for the mineral
                gyroHold(-0.4, 0,.9);
                sleep(2000);
                if (detector.isFound()) {
                    bumpSuccess = mineralBump(1);
                } else {
                    //gyroSpin(12);  // Spin to the right to look for the mineral
                    gyroHold(0.4, 0,1.8);
                    sleep(2000);

                    if (detector.isFound()) {
                        bumpSuccess = mineralBump(1);
                    }
                }
            }
            if (!bumpSuccess) {
                mineralCt = mineralCt + 1;
                //gyroSpin(0);
                //moveBot(0,0,0.5,0.5);
                //sleep(1000);
                stopBot();

            }
        }
*/
            //Clear the hook
            gyroHold(-0.5, 0, 0.2);
            // Move away from the lander
            gyroStrafe(0.5, 0);
            sleep(1000);
            stopBot();
            gyroSpin(0);

            /**TRIAL CODE TO TRY SORTING LOGIC (need to add to Near Depot Also) */
            telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
            telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.
            //telemetry.addData("error", error);
            //telemetry.addData("detectorCt",detectorCt);
            telemetry.update();

            //Turn off the camera
            detector.disable();

            // Move closer to the wall
            gyroHold(0.4, 0, 1.5);

            // Rotate to a heading of 315R
            gyroSpin(315);

            // Move towards the wall until 7 inches away while maintaining a heading of 270
            while (robot.rangeSensor.getDistance(DistanceUnit.INCH) > 7) {
                gyroStrafe(.5, 315);
            }
            stopBot();

            //Drive backwards maintaining 2-4 inches from the wall toward the depot
            while (sonarDistance() > 18) {
                double wsteer = wallSteer(5);
                moveBot(0.2, 0, wsteer, 0.5);
            }
            stopBot();
            //detector.disable();

            int dropTarget = 3200;  // Target for dropping totem
            int levelTarget = 500;  // Target for holding arm forward and level
            double rotatePower;
            //Reset the Encoder
            robot.armRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //Rotate the arm to level position
            while (robot.armRotate.getCurrentPosition() < levelTarget) {
                robot.armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rotatePower = 0.25;
                robot.armRotate.setPower(rotatePower);
            }
            robot.armRotate.setPower(0);
            //Drop the Totem
            robot.rightMineral.setPower(0.7);
            sleep(1000);
            robot.rightMineral.setPower(0);


            // Let's try wall crawling for a time, then turning and homing on the crater with the distance sensor
            while (sonarDistance() < 72) {
                double wsteer = wallSteer(7);
                moveBot(-0.25, 0, wsteer, 0.5);
            }
            stopBot();
            //Rotate the arm over the crater
            while (robot.armRotate.getCurrentPosition() < dropTarget) {
                robot.armRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rotatePower = 0.25;
                robot.armRotate.setPower(rotatePower);
            }
            robot.armRotate.setPower(0);
            //  Extend the arm
            while (robot.extendArmFrontStop.getState() == false) { // As long as the front limit switch isn't pressed, move the arm forward

                robot.armExtend.setPower(-0.15);
            }
            robot.armExtend.setPower(0);  // Otherwise set the power to zero

        break;
        }

        stop();
    }


    public void moveToEncoder (double clicks) {
        double drivePower;
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        if (clicks > 0) {
            drivePower = 0.3;
            while (robot.rightFrontDrive.getCurrentPosition()<clicks) {
                robot.rightFrontDrive.setPower(drivePower);
                robot.leftFrontDrive.setPower(drivePower);
                robot.rightRearDrive.setPower(drivePower);
                robot.leftRearDrive.setPower(drivePower);
            }


        }
        else {
            drivePower = -0.3;
            while (robot.rightFrontDrive.getCurrentPosition()>clicks) {
                robot.rightFrontDrive.setPower(drivePower);
                robot.leftFrontDrive.setPower(drivePower);
                robot.rightRearDrive.setPower(drivePower);
                robot.leftRearDrive.setPower(drivePower);
            }


        }
        robot.rightFrontDrive.setPower(0);
        robot.leftFrontDrive.setPower(0);
        robot.rightRearDrive.setPower(0);
        robot.leftRearDrive.setPower(0);
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
        // This function uses the side mounted ultrasonic sensor to return a
        // steering signal based on a desired distance from the wall
        // Tested to work as a strafe input to moveBot()
        // distance is in inches
        double steer = 0;
        steer = robot.rangeSensor.getDistance(DistanceUnit.INCH) - distance;
        steer = (double)Range.clip(steer, -0.25, 0.25);
    return steer;
    }

    public void gyroSpin(double heading) {
        // This function spins the robot in place to a desired heading

        // Get the current heading error between actual and desired
        double error = getError(heading);
        // While we are greater than 5 degrees from desired heading (5 seems to work best)
        while (Math.abs(error) > 5) {
            // Rotate the robot in the correct direction.
            // Don't use more than 0.3 input power or it goes too fast
            if (error < 0 && Math.abs(error) > 5) {
                moveBot(0, -0.3, 0, 0.4);
            } else {
                moveBot(0, 0.3, 0, 0.4);
            }
            //Check the error again for the next loop
            error = getError(heading);
            telemetry.addData("Heading",robot.gyro.getIntegratedZValue());
            telemetry.update();
        }
        stopBot();
    }

    public void gyroStrafe ( double speed, double heading){
        // This function will strafe the robot at a given speed while holding a heading

        double error = getError(heading);
        double deadband = 3;
            error = getError(heading);
            if (error < 0 && Math.abs(error) > deadband) {
                // Nagative error greater than 5 degrees, left of desired heading, input positive rotation
                moveBot(0, -.25, speed, 0.6);
            } else if (error > 0 && Math.abs(error) > deadband){
                // Positive Error greater than 5 degrees, right of desired heading, input negative rotation
                moveBot(0, 0.25, speed, 0.6);
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
        // This function drives on a specified heading for a given time
        // Time is in seconds!!!!!
        ElapsedTime holdTimer = new ElapsedTime();
        double error;
        double PCoeff = 0.01;
        // keep looping while we have time remaining.
        holdTimer.reset();
        while ((holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            //error = Range.clip(getError(angle),-0.3,0.3);
            error = PCoeff * getError(angle);
            moveBot(speed,error,0,0.3);
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
        while (robotError > 180) { robotError -= 360;}
        while (robotError <= -180) {robotError += 360;}
        return robotError;
    }

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    public double sonarDistance (){
        // Returns distance from the sonar sensor over an average of 4 values
        // Trying to get around noise in the sensor
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

    public boolean mineralBump(double time){
        // This function will rotate to center on the mineral, drive to bump it, and
        // return to its original position
        // Set left and right boundaries for alignment

        // Set our initial getAligned limits
        detector.setAlignSettings(0,30);
        double leftLimit = 290;
        double rightLimit = 310;
        double gyroTemp;
        double error = 0;
        double detectorCt = 0;

        telemetry.addLine("in MineralBump!");
        telemetry.update();
        sleep(2000);

        while( detectorCt <= 30) {
            // Calculate the heading error
            error = getXError(300,30);
            // Command the bot to move
            moveBot(0,error,0,0.15);
            // Count up how long it remains aligned.  Could also use gyro heading being stable?
            if (error == 0) {
                detectorCt = detectorCt + 1;
            } else {
                detectorCt = 0;
            }
            telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
            telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.
            telemetry.addData("error", error);
            telemetry.addData("detectorCt",detectorCt);
            telemetry.update();
        }
        // Save the aligned heading
        gyroTemp = robot.gyro.getIntegratedZValue();
        // Strafe on the heading for the appropriate time
        gyroStrafe(0.5,gyroTemp);
        sleep((long)time*1000);
        //gyroSpin(gyroTemp+90);
        //gyroHold(0.4,gyroTemp+90, 2);
        stopBot();
        // Return to somewhere near the starting position
        gyroStrafe(-0.5,gyroTemp);
        sleep((long)time*1000);
        //gyroHold(-0.4,gyroTemp+90,2);
        stopBot();
        // return to the starting heading
        gyroSpin(0);
        return true;
    }


    public double  getXError (double centerPixel, double deadBand) {
        // This function accepts a center pixel location (usually 300)
        // and a dead band around that pixel and provides a
        // proportional steering signal back
        double responseSignal = 0;
        double error = detector.getXPosition() - centerPixel;
        double slope = 0.01;  // Proportional multipler
        if ( Math.abs(error) <= deadBand ){
            // Error is inside deadband
            responseSignal = 0;
        } else
        {
            responseSignal = slope * error;
        }
        return responseSignal;

    }

}


