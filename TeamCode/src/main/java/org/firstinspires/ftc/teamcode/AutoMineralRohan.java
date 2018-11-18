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
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

import java.util.ArrayList;
import java.util.List;

@Autonomous (name = "AutoMineralCoachVince", group = "Rohan")
public class AutoMineralCoachVince extends LinearOpMode {

    HardwareBruinBot hwMap = new HardwareBruinBot();

    //intializing all Vurforia stuff ; copied from ConceptVuforiaNavRoverRuckus
    private static final String VUFORIA_KEY = " -- YOUR NEW VUFORIA KEY GOES HERE  --- ";

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor

    // Select which camera you want use.  The FRONT camera is the one on the same side as the screen.
    // Valid choices are:  BACK or FRONT
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;

    VuforiaLocalizer vuforia;


    private GoldAlignDetector detector;

    private ElapsedTime     runtime = new ElapsedTime();

    //public boolean found() { return GoldAlignExample.isFound(); }
    //public boolean isAligned() { return detector.getAligned(); }
    //public double getX() { return detector.getXPosition(); }

    /**Mile Marker 10**/
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

        // All of the Vuforia initialization that is copied from ConceptVuforiaRoverRuckus inside the OpMode
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * We can pass Vuforia the handle to a camera preview resource (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets that for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        VuforiaTrackables targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * This Rover Ruckus sample places a specific target in the middle of each perimeter wall.
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        /**
         * To place the BlueRover target in the middle of the blue perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Then, we translate it along the Y axis to the blue perimeter wall.
         */
        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        /**
         * To place the RedFootprint target in the middle of the red perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 180 around the field's Z axis so the image is flat against the red perimeter wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative Y axis to the red perimeter wall.
         */
        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        /**
         * To place the FrontCraters target in the middle of the front perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 90 around the field's Z axis so the image is flat against the front wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative X axis to the front perimeter wall.
         */
        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        //Establish depot location
        float depotXPos = 10;
        float depotYPos = 20;

        /**
         * To place the BackSpace target in the middle of the back perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it -90 around the field's Z axis so the image is flat against the back wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the X axis to the back perimeter wall.
         */
        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        /**
         * Create a transformation matrix describing where the phone is on the robot.
         *
         * The coordinate frame for the robot looks the same as the field.
         * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
         * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
         *
         * The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
         * pointing to the LEFT side of the Robot.  It's very important when you test this code that the top of the
         * camera is pointing to the left side of the  robot.  The rotation angles don't work if you flip the phone.
         *
         * If using the rear (High Res) camera:
         * We need to rotate the camera around it's long axis to bring the rear camera forward.
         * This requires a negative 90 degree rotation on the Y axis
         *
         * If using the Front (Low Res) camera
         * We need to rotate the camera around it's long axis to bring the FRONT camera forward.
         * This requires a Positive 90 degree rotation on the Y axis
         *
         * Next, translate the camera lens to where it is on the robot.
         * In this example, it is centered (left to right), but 110 mm forward of the middle of the robot, and 200 mm above ground level.
         */
        //Need to Correct these for the actual Robot Parameters

        final int CAMERA_FORWARD_DISPLACEMENT  = 110;   // eg: Camera is 110 mm in front of robot center
        final int CAMERA_VERTICAL_DISPLACEMENT = 200;   // eg: Camera is 200 mm above ground
        final int CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Start Tracking when Autonomous selected");
        telemetry.update();
        waitForStart();

        /** Start tracking the data sets we care about. */
        targetsRoverRuckus.activate();

        telemetry.addData(">", "Robot Ready.");    //
        telemetry.update();

        // Wait for the Start button to be pushed
        while (!isStarted()) {
            // Put things to do prior to start in here
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.update();
        }

        /**Mile Marker 20**/

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

        /**Mile Marker 30*/*

        //Now we move the robot forward slightly to push the mineral.
        //These are still very rough estimates... they need to be changed
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

        //Below is code to get the robot to the depot position
        //Determine if we can see a Target
        // If a target is visible, then move toward the depot.  If target not visible will have to take last
        // known position and move pre-programmed amount toward depot

        /**Mile Marker 40**/

        /**How do we get RobotVector to return two variables??***/
        OpenGLMatrix location = getRobotVector();
        targetVisible = getRobotVector();
        boolean InDepot;

        if !targetVisible {
            //Move robot from minerals to depot in a pre-programmed manner **NEED TO FILL IN**
            move(3, 4, 0);
        }

        //If we see a target, then move toward that target for a small distance and then
        // re-assess and keep repeating until the target disappears or reached the depot
        else {

        while !InDepot () {
                OpenGLMatrix location = getRobotVector();

            // express position (translation) of robot in inches. Trying the expression "location" rather than lastLocation
            VectorF translation = location.getTranslation();
            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);

            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
            double RobotXPos = translation.get(0);
            double RobotYPos = translation.get(1);
            //Establish the vector to the Depot
                //MoveVector = DepotPosition - RobotPosition
                //Provide inputs to Robot move method that are corrected for the rotation of the robot
                //MoveVectorRobotCentric = MoveVector rotated by -RobotAngle

            double DeltaXPos = depotXPos - RobotXPos;
            double DeltaYPos = depotYPos - RobotYPos;
            double RotationToDepot = Math.atan(DeltaYPos/DeltaXPos);
            double DistanceToDepot = Math.sqrt(DeltaXPos^2 + DeltaYPos^2);
            double RotationFromRobot = RotationToDepot - RobotRotation;

            //Need some fancy math here to tell the robot to move in the direction RotationFromRobot
                move(RotationFromRobot, 0, -RotationFromRobot);

                /**Mile Marker 50**
                */

                //Check distance to depot; if the distance is small, then we are 'In Depot'
                    if ((DistanceToDepot)<0.5) {
                        InDepot = true;
                    }
                    else {
                        InDepot = false;
                    }
                    //If we've lost view of the target, then move the rest of the way to target
                    if !targetVisible {
                    move(RotationFromRobot, 0, DistanceToDepot);
                    InDepot = true;
                    telemetry.addData("Visible Target", "none");
                telemetry.update();
                    }
                }
            }
            //Reached the depot. **NOW Drop the token and move to crater!!**
        }
        //End of OpMode


    }

    public void move(double drive, double rotate, double strafe)
    {
        // This module takes inputs, normalizes them to 1, and drives the motors
        float maxDrive;
        float frontMax;
        float rearMax;

        // In addition to normalize, cube the values so that small inputs allow fine control
        drive = Math.pow(drive, 3);
        strafe = Math.pow(strafe, 3);
        rotate = Math.pow(rotate, 3);

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

    //This is creating a method that can return the vector position of the robot
    public OpenGLMatrix getRobotVector () {
        // check all the trackable target to see which one (if any) is visible.
        targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telemetry.addData("Visible Target", trackable.getName());
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                    return lastLocation;
                    //how do we also return targetVisible?
                    return targetVisible;
                }
                break;
            }
        }
    }


// Below is code to spin the robot using the gyro, commented out
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
