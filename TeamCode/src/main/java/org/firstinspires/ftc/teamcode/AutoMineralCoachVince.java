package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous (name = "AutoMineralCoachVince", group = "Rohan")
public class AutoMineralCoachVince extends LinearOpMode {

    HardwareBruinBot hwMap = new HardwareBruinBot();

    private GoldAlignDetector detector;

    private ElapsedTime     runtime = new ElapsedTime();
    //public boolean found() { return GoldAlignExample.isFound(); }
    //public boolean isAligned() { return detector.getAligned(); }
    //public double getX() { return detector.getXPosition(); }
    public void runOpMode () {

        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Initiaize Gyro
        ModernRoboticsI2cGyro gyro    = null;
        final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
        final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
        final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable



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


        //Initialize hardware;
        hwMap.init(hardwareMap);
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        // Send telemetry message to alert driver that we are calibrating;
        telemetry.addData(">", "Calibrating Gyro");    //
        telemetry.update();

        gyro.calibrate();

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && gyro.isCalibrating())  {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Robot Ready.");    //
        telemetry.update();

        // Wait for the Start button to be pushed
        while (!isStarted()) {
            // Put things to do prior to start in here
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.update();
        }
        double fwdSpeed=0.0;  // Forward Speed, Normally 0.1
        double rotate = 0.1; // Rotation Speed
        double strafe = 0.1;  // Strafe Speed

        hwMap.landerLatchLift.setPower(0.3);
        sleep(2000);
        hwMap.landerLatchLift.setPower(0);

        //This loop runs until the gold mineral is found;
        //Need to change this to "while not detected" like in the GoldAlignExample program;
        while (detector.isFound()) {
        //boolean isAligned = false;
        //!! This is where we need to poll the detector
           telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
           telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.
           telemetry.addLine("testing");
           if(detector.getAligned())
           {
               fwdSpeed = 0.1;
           }
           else
           {
               fwdSpeed = 0.0;
           }

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


    }
    public void move(double drive, double rotate, double strafe)
    {
        // This module takes inputs, normalizes them to 1, and drives the motors
        float maxDrive;
        float frontMax;
        float rearMax;

        // Find the maximum value of the inputs and normalize
        frontMax = Math.max(Math.abs((float)drive + (float)strafe + (float)rotate), Math.abs((float)drive - (float)strafe - (float)rotate));
        rearMax = Math.max(Math.abs((float)drive - (float)strafe + (float)rotate), Math.abs((float)drive + (float)strafe - (float)rotate));
        maxDrive = Math.max(frontMax, rearMax);
        maxDrive = Math.max(maxDrive,1);
        drive = drive/maxDrive;
        strafe = strafe/maxDrive;
        rotate = rotate/maxDrive;

        hwMap.leftFrontDrive.setPower(drive + strafe + rotate);
        hwMap.leftRearDrive.setPower(drive - strafe + rotate);
        hwMap.rightFrontDrive.setPower(drive - strafe - rotate);
        hwMap.rightRearDrive.setPower(drive + strafe - rotate);

    }
    public void stopBot()
    {
        // This function stops the robot
        hwMap.leftFrontDrive.setPower(0);
        hwMap.leftRearDrive.setPower(0);
        hwMap.rightFrontDrive.setPower(0);
        hwMap.rightRearDrive.setPower(0);
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
    /*
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
*/
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
    /*boolean onHeading(double speed, double angle, double PCoeff) {
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
        robot.leftDrive.setPower(leftSpeed);
        robot.rightDrive.setPower(rightSpeed);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }
*/
    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    /*public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }
*/


}
