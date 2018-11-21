package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous (name = "AutoRedDepotCoachVince", group = "Vince")
public class AutoRedDepotCoachVince extends LinearOpMode {

    HardwareBruinBot robot = new HardwareBruinBot();

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    static final double     DRIVE_SPEED             = 0.4;     // Nominal speed for better accuracy.
    static final double     TURN_SPEED              = 0.2;     // Nominal half speed for better accuracy.

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

    static final short     STARTING_HEADING        = 315;      // Used to set the gyro offset

    static final double     fwdSpeed                = 0.1;  // Forward Speed, Normally 0.1
    static final double     rotate                  = 0.1; // Rotation Speed
    static double           strafe                  = 0.1;  // Strafe Speed


    private GoldAlignDetector detector;

    private ElapsedTime     runtime = new ElapsedTime();
    //public boolean found() { return GoldAlignExample.isFound(); }
    //public boolean isAligned() { return detector.getAligned(); }
    //public double getX() { return detector.getXPosition(); }
    //----------------------------------runOpMode-------------------------------------------------------
    public void runOpMode () {

        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Initialize the Robot
        robot.init(hardwareMap);
        robot.gyro.calibrate();
        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }
        robot.gyro.setZAxisOffset(STARTING_HEADING);

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



        // Wait for the Start button to be pushed ----------------------------START----------------------------------------------
        while (!isStarted()) {
            // Put things to do prior to start in here
            telemetry.addData(">", "Robot Heading = %d", robot.gyro.getIntegratedZValue());
            telemetry.update();
        }



/*<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AutoMineralRohan2.java
            //For a 0.5 second period, move the robot forward away from the lander;
            // I don't think we need this - "sleep" takes care of this while (runtime.seconds() < 0.5)
            if(detector.getXPosition() < 240)
            {
                //double rotate = 0.2;
                hwMap.leftFrontDrive.setPower(rotate); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(rotate); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(-rotate); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(-rotate); //= drive + strafe - rotate;

            }
            else if(detector.getXPosition() > 370)
            {
                //double rotate = -0.2;
                hwMap.leftFrontDrive.setPower(-rotate); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(-rotate); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(rotate); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(rotate); //= drive + strafe - rotate;
            }
            else
            {
                move(0,0, 0.25);
                sleep(200);
            }
=======
        // Lower the Robot from the lander
        robot.landerLatchLift.setPower(0.3);
        sleep(2000);
        robot.landerLatchLift.setPower(0);

        // Move the robot a little bit backwards to unhook
        moveBot(-fwdSpeed,0,0);
        sleep(200);
        stopBot();
>>>>>>> 03c93fbfd1c18a04d9c6499b70c97050e661b31b:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AutoRedDepotCoachVince.java

        // Move the robot forward until it sees the Red line with the color sensor
        while (robot.colorSensor.red() < 160) {
            moveBot(0,0,strafe);
        }
        stopBot();

        // Find the Gold mineral and knock it off the spot

        // Move at a heading of 315 until directly in front of the North vuforia mark or until XX distance from the wall
        gyroHold(fwdSpeed,315,2000);

        // Rotate to a heading of 270
        gyroTurn(0,270);

        // Move towards the wall until 2-4 inches away
        while(robot.rangeSensor.getDistance(DistanceUnit.INCH) > 4){
            moveBot(0,0,strafe);

        }
        // Drive backwards maintaining 2-4 inches from the wall until you see the red tape line
        while(robot.colorSensor.red() < 140){
            moveBot(-fwdSpeed,wallSteer(3),0);
        }
        // Drop the totem

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AutoMineralRohan2.java
        //Now we move the robot forward slightly to push the mineral.
        move(0,0,0.25);
        sleep(500);
        stopBot();
        move(0,0,-0.25);
        sleep(500);
        move(0.1,0,0);
        sleep(1000);
        move(0,0.1,0);
        sleep(250);
        move(0.1,0,0);
        sleep(500);
        move(-0.1,0,0);
        sleep(2500);

=======
        // Drive forwards maintaining 2-4 inches from the wall until...You get to the crater?
        // Let's try wall crawling for a time, then turning and homing on the crater with the distance sensor

        //  Turn 180 and deploy the arm

        //This loop runs until the gold mineral is found;
        //Need to change this to "while not detected" like in the GoldAlignExample program;

        stopBot();
>>>>>>> 03c93fbfd1c18a04d9c6499b70c97050e661b31b:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AutoRedDepotCoachVince.java

    }
    public void moveBot(double drive, double rotate, double strafe)
    {
        // This module takes inputs, normalizes them to DRIVE_SPEED, and drives the motors
        float maxDrive;
        float frontMax;
        float rearMax;


        // Find the maximum value of the inputs and normalize
        frontMax = Math.max(Math.abs((float)drive + (float)strafe + (float)rotate), Math.abs((float)drive - (float)strafe - (float)rotate));
        rearMax = Math.max(Math.abs((float)drive - (float)strafe + (float)rotate), Math.abs((float)drive + (float)strafe - (float)rotate));
        maxDrive = Math.max(frontMax, rearMax);
        maxDrive = (float) Math.max(maxDrive,(float)DRIVE_SPEED);
        drive = drive/maxDrive;
        strafe = strafe/maxDrive;
        rotate = rotate/maxDrive;

        robot.leftFrontDrive.setPower(drive + strafe + rotate);
        robot.leftRearDrive.setPower(drive - strafe + rotate);
        robot.rightFrontDrive.setPower(drive - strafe - rotate);
        robot.rightRearDrive.setPower(drive + strafe - rotate);

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
        return steer;
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
    /*public void gyroTurn (  double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
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

   /** public void gyroHold( double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
          //  onHeading(speed, angle, P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
      //  stopBot();
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
   /** boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
      /** error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            //leftSpeed  = 0.0;
            //rightSpeed = 0.0;
            onTarget = true;
        }
        else {
        //    steer = getSteer(error, PCoeff);
            //rightSpeed  = speed * steer;
            //leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        moveBot(speed,steer,0);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", speed);

        return onTarget;
    }

    /*
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
   /** public double getError(double targetAngle) {

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

    public void mineralDance(){


        while (detector.isFound()) {

            //boolean isAligned = false;
            //!! This is where we need to poll the detector
            telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
            telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.
            telemetry.addLine("testing");
            if(detector.getAligned())
            {
                //fwdSpeed = 0.1;
            }
            else
            {
                //fwdSpeed = 0.0;
            }

            //For a 0.5 second period, move the robot forward away from the lander;
            // I don't think we need this - "sleep" takes care of this while (runtime.seconds() < 0.5)
            if(detector.getXPosition() < 270)
            {
                //double rotate = 0.2;
                robot.leftFrontDrive.setPower(fwdSpeed + rotate); //= drive + strafe + rotate;
                robot.leftRearDrive.setPower(fwdSpeed + rotate); //= drive - strafe + rotate;
                robot.rightFrontDrive.setPower(fwdSpeed - rotate); //= drive - strafe - rotate;
                robot.rightRearDrive.setPower(fwdSpeed - rotate); //= drive + strafe - rotate;

            }
            else if(detector.getXPosition() > 330)
            {
                //double rotate = -0.2;
                robot.leftFrontDrive.setPower(fwdSpeed - rotate); //= drive + strafe + rotate;
                robot.leftRearDrive.setPower(fwdSpeed - rotate); //= drive - strafe + rotate;
                robot.rightFrontDrive.setPower(fwdSpeed + rotate); //= drive - strafe - rotate;
                robot.rightRearDrive.setPower(fwdSpeed + rotate); //= drive + strafe - rotate;
            }
            else
            {
                robot.leftFrontDrive.setPower(fwdSpeed); //= drive + strafe + rotate;
                robot.leftRearDrive.setPower(fwdSpeed); //= drive - strafe + rotate;
                robot.rightFrontDrive.setPower(fwdSpeed); //= drive - strafe - rotate;
                robot.rightRearDrive.setPower(fwdSpeed); //= drive + strafe - rotate;
            }

            sleep(50);
            /*hwMap.leftFrontDrive.setPower(0.00); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(0.00); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(0.00); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(0.00); //= drive + strafe - rotate;*/

            //Keep moving and aligning so long as you can see the gold mineral
            /*while (!detector.getAligned()) {

                //Detect the rotation angle to the mineral;
                rotate = detector.getXPosition()/480;

                //The motors will be set to rotate based on the magnitude of misalignment;
                //!! We actually need to scale the "0.1" and scale the "rotate" so that the robot moves and the rotation angle equals "rotation". Could use gyro here;

                hwMap.leftFrontDrive.setPower(0.1 + rotate); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(0.1 + rotate); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(0.1 - rotate); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(0.1 - rotate); //= drive + strafe - rotate;

            }*/
            //This is where we no longer see the mineral and stop the robot
            telemetry.update();
        }

        //Now we move the robot forward slightly to push the mineral.
           /* hwMap.leftFrontDrive.setPower(0.1); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(0.1); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(0.1); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(0.1); //= drive + strafe - rotate;
        sleep(1000);*/
    }

//}
