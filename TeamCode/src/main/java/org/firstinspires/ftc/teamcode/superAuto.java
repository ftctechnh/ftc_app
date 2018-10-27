package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.sun.tools.javac.util.Constants.format;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.mmPerInch;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

abstract public class superAuto extends LinearOpMode {
    //FR = Front Right, FL = Front Left, BR = Back Right, BL = Back Left.
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    DcMotor motorConL;
    DcMotor motorConR;
    DcMotor motorFlip;

    static final float ridgeDepth = 4;
    //gyro flipped is -1 is the gyro is inverted, otherwise it is 1.
    static final int gyroFlipped = -1;
    RelicRecoveryVuMark[] boxOrder = new RelicRecoveryVuMark[4];

    BNO055IMU imu;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    Orientation angles;
    Acceleration gravity;

    ModernRoboticsI2cRangeSensor rangeSensor;
    RelicRecoveryVuMark vuMark;

    VuforiaTrackables targetsRoverRuckus;
    List<VuforiaTrackable> allTrackables;
    OpenGLMatrix lastLocation;


    boolean iAmRed;
    boolean iAmBlue = !iAmRed;

    private ElapsedTime runtime = new ElapsedTime();

    void setUp() {

        iAmBlue = !iAmRed;

        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();
    }


    void configureGyro() {
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
    }

    void mapHardware() {

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");


        motorFlip = hardwareMap.dcMotor.get("motorFlip");
        motorFlip.setDirection(DcMotor.Direction.FORWARD);
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.REVERSE);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.FORWARD);
        motorConL = hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR = hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);
    }

    void composeTelemetry() {

        boolean autoClear = false;
        telemetry.setAutoClear(autoClear);
        telemetry.addLine("starting");
        telemetry.update();

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                gravity = imu.getGravity();
            }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override
                    public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override
                    public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel * gravity.xAccel
                                        + gravity.yAccel * gravity.yAccel
                                        + gravity.zAccel * gravity.zAccel));
                    }
                });
    }


    void congfigVuforia() {
         final String TAG = "Vuforia Navigation Sample";
        OpenGLMatrix lastLocation = null;
        VuforiaLocalizer vuforia;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AYhHUgX/////AAABmTO0g2PsdUqpg5xo" +
                "96O7OkOB7qrwOjE24wV71lIm/MF9g96awd677rj7LrgQKUJAewgWkAAxn1MUJtUyiq" +
                "9iesjKF+QNXlKr5qCAb69hI268sYjjCJ+PqVBtMrlcIG1F4l2osl9zIk9tYAYfLXKl" +
                "T351h1yRW1AqAdHJaHwt861ztrh4EW/1WjOV3/yT4SDtrJivhfmU0c51IqPUEJ0xqbWFr2saxvS/cSkH4e+hFIImM/jIw5JkaizeznuFTA" +
                "TnWTq9Spp/EhPPaQXJtScNP3DDaNDdfiqT9opwsxuQlEe1YF19sHjtenGD7/PcUlQXVS+VKbxaSqd/Cnq4+/3bOuhNzFoTVbBKZ16DQ9EZeCJM";

        //PUT NEW KEY HERE^^^^^
         parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
         vuforia = ClassFactory.createVuforiaLocalizer(parameters);
            VuforiaTrackables stonesAndChips = vuforia.loadTrackablesFromAsset("FTC_2016-17");
            VuforiaTrackable redClose2Ramp = stonesAndChips.get(3);
            redClose2Ramp.setName("RedTarget");  // gears

            VuforiaTrackable redFarFromRamp = stonesAndChips.get(1);
            redFarFromRamp.setName("RedTarget");  // tools

            VuforiaTrackable blueFarFromRamp = stonesAndChips.get(2);
            blueFarFromRamp.setName("BlueTarget");  // legos

            VuforiaTrackable blueClose2Ramp = stonesAndChips.get(0);
            blueClose2Ramp.setName("BlueTarget");  // wheels

            /** For convenience, gather together all the trackable objects in one easily-iterable collection */
            List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
            allTrackables.addAll(stonesAndChips);
            float mmPerInch        = 25.4f;
            float mmBotWidth       = 18 * mmPerInch;            // ... or whatever is right for your robot
            float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels
            OpenGLMatrix redClose2RampLoc = OpenGLMatrix
                    /* Then we translate the target off to the RED WALL. Our translation here
                    is a negative translation in X.*/
                    .translation(-mmFTCFieldWidth/2, -298.333f, 0)
                    .multiplied(Orientation.getRotationMatrix(
                            /* First, in the fixed (field) coordinate system, we rotate 90deg in X, then 90 in Z */
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 90, 0));
            redClose2Ramp.setLocation(redClose2RampLoc);
            RobotLog.ii(TAG, "Red Close to Ramp=%s", format(redClose2RampLoc));

            OpenGLMatrix redFarFromRampLoc = OpenGLMatrix
                    /* Then we translate the target off to the RED WALL. Our translation here
                    is a negative translation in X.*/
                    .translation(-mmFTCFieldWidth/2, 895, 0)
                    .multiplied(Orientation.getRotationMatrix(
                            /* First, in the fixed (field) coordinate system, we rotate 90deg in X, then 90 in Z */
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 90, 0));
            redFarFromRamp.setLocation(redFarFromRampLoc);
            RobotLog.ii(TAG, "Red Far From Ramp=%s", format(redFarFromRampLoc));

            OpenGLMatrix blueClose2RampLoc = OpenGLMatrix
                    /* Then we translate the target off to the RED WALL. Our translation here
                    is a negative translation in X.*/
                    .translation(298.333f,-mmFTCFieldWidth/2,0)
                    .multiplied(Orientation.getRotationMatrix(
                            /* First, in the fixed (field) coordinate system, we rotate 90deg in X, then 90 in Z */
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 0, 0));
            blueClose2Ramp.setLocation(blueClose2RampLoc);
            RobotLog.ii(TAG, "Blue Close To Ramp=%s", format(blueClose2RampLoc));

            /*
             * To place the Stones Target on the Blue Audience wall:
             * - First we rotate it 90 around the field's X axis to flip it upright
             * - Finally, we translate it along the Y axis towards the blue audience wall.
             */
            OpenGLMatrix blueFarFromRampLoc= OpenGLMatrix
                    /* Then we translate the target off to the Blue Audience wall.
                    Our translation here is a positive translation in Y.*/
                    .translation(-895, mmFTCFieldWidth/2, 0)
                    .multiplied(Orientation.getRotationMatrix(
                            /* First, in the fixed (field) coordinate system, we rotate 90deg in X */
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 0, 0));
            blueFarFromRamp.setLocation(blueFarFromRampLoc);
            RobotLog.ii(TAG, "Blue Far From Ramp =%s", format(blueFarFromRampLoc));
            OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                    .translation(mmBotWidth/2,0,0)
                    .multiplied(Orientation.getRotationMatrix(
                            AxesReference.EXTRINSIC, AxesOrder.YZY,
                            AngleUnit.DEGREES, -90, 0, 0));
            RobotLog.ii(TAG, "phone=%s", format(phoneLocationOnRobot));
            ((VuforiaTrackableDefaultListener)redClose2Ramp.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
            ((VuforiaTrackableDefaultListener)redFarFromRamp.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
            ((VuforiaTrackableDefaultListener)blueClose2Ramp.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
            ((VuforiaTrackableDefaultListener)blueFarFromRamp.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);

            /** Wait for the game to begin */
            /** Start tracking the data sets we care about. */
            stonesAndChips.activate();

            float robotX;
            float robotY;
            float robotBearing;
    }

    void configVuforiaRoverRuckus() {
        final String TAG = "Vuforia Navigation Sample";
        OpenGLMatrix lastLocation = null;
        VuforiaLocalizer vuforia;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AYhHUgX/////AAABmTO0g2PsdUqpg5xo" +
                "96O7OkOB7qrwOjE24wV71lIm/MF9g96awd677rj7LrgQKUJAewgWkAAxn1MUJtUyiq" +
                "9iesjKF+QNXlKr5qCAb69hI268sYjjCJ+PqVBtMrlcIG1F4l2osl9zIk9tYAYfLXKl" +
                "T351h1yRW1AqAdHJaHwt861ztrh4EW/1WjOV3/yT4SDtrJivhfmU0c51IqPUEJ0xqbWFr2saxvS/cSkH4e+hFIImM/jIw5JkaizeznuFTA" +
                "TnWTq9Spp/EhPPaQXJtScNP3DDaNDdfiqT9opwsxuQlEe1YF19sHjtenGD7/PcUlQXVS+VKbxaSqd/Cnq4+/3bOuhNzFoTVbBKZ16DQ9EZeCJM";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        targetsRoverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);

        final float mmPerInch       = 25.4f;
        final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
        final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor
        final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);


        final int CAMERA_FORWARD_DISPLACEMENT  = 101;   // eg: Camera is 110 mm in front of robot center
        final int CAMERA_VERTICAL_DISPLACEMENT = 189;   // eg: Camera is 200 mm above ground
        final int CAMERA_LEFT_DISPLACEMENT     = -184;     // eg: Camera is ON the robot's center line

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        -90, -90, 0));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }
        targetsRoverRuckus.activate();
    }

    double getLocation(int xOry) {
        while (opModeIsActive()) {


            // check all the trackable target to see which one (if any) is visible.
            Boolean targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;

                    // getUpdatedRobotLocation() will return null if no new information is available since
                    // the last time that call was made, or if the trackable is not currently visible.
                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    break;
                }
            }

            // Provide feedback as to where the robot is located (if we know).
            if (targetVisible) {
                // express position (translation) of robot in inches.
                VectorF translation = lastLocation.getTranslation();
                telemetry.addData("Pos (mm)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                        translation.get(0) , translation.get(1) , translation.get(2));

                // express the rotation of the robot in degrees.
                Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
                return translation.get(xOry);
            }
            else {
                configureGyro();
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                double currentHeading = angles.firstAngle;
                int degrees = (int)currentHeading;

                while (targetVisible == false) {
                    degrees+=10;
                    pivotTo(degrees);
                }
                 }
            telemetry.update();

        }
        return 0;
    }

    void goToPoint(double DestinationX, double DestinationY){
        double Theta = Math.toRadians(getHeading());
        double CurrentX, CurrentY,X,Y;//X and Y JOYstick coordinates
        boolean go = true;
        int i = 0;

        // While not at desired location
        while (go) {
            CurrentX = getLocation(0); //Eventually I would like to directly take the readings. These #s are made up...
            CurrentY = getLocation(1);
            X = (DestinationX - CurrentX);
            Y = (DestinationY - CurrentY);
            telemetry.addData("DestinationX ",DestinationX);
            telemetry.addData("DestinationY ",DestinationY );
            telemetry.addData("X ",X );
            telemetry.addData("Y ",Y );
            double divBy = Math.max(Math.abs(X), Math.abs(Y));
            double chgDistance = Math.sqrt((X*X) + (Y*Y));
            X= X/divBy;
            Y= Y/divBy;
            telemetry.addData("Max Divisor: ", divBy );
            telemetry.addData("X Normalized:  ",X );
            telemetry.addData("Y Normalized: ",Y );

            if (Math.abs(chgDistance) >= 2) {
                //double posx = Vuforia_JoystickX(X,Y, Theta);
                //double posy = Vuforia_JoystickY(X,Y, Theta);
                double posx = X;
                double posy = Y;
                telemetry.addData("chgDistance ", chgDistance);
                telemetry.addData("CurrentX ", CurrentX);
                telemetry.addData("CurrentY ",CurrentY );
                telemetry.addData("posx ",posx );
                telemetry.addData("posy ",posy );
                telemetry.addData("i", i);
                telemetry.update();
                Wait(10);

                //Power the motors
                if ( ( posy != 0) || ( posx != 0 ) ) {
                    double FRBLPower = ((-posy) - posx)*0.3;
                    double FLBRPower = ((-posy) + posx)*0.3;
                    motorFR.setPower( FRBLPower );
                    motorFL.setPower( FLBRPower );
                    motorBR.setPower( FLBRPower );
                    motorBL.setPower( FRBLPower );

                }

            } else
            {
                motorFR.setPower(0);
                motorFL.setPower(0);
                motorBR.setPower(0);
                motorBL.setPower(0);
                go = false;
            }
            i++;

        }
        sR();
    }


    public double Vuforia_JoystickX(double X, double Y, double Theta)
    {
        double JoystickX = (X*Math.cos(Theta))- (Y*Math.sin(Theta));
        telemetry.addData("JoystickX", "%f", JoystickX);
        return JoystickX;
    }

    public double Vuforia_JoystickY(double X, double Y, double Theta)
    {
        double JoystickY = (X*Math.sin(Theta))+(Y*Math.cos(Theta));
        telemetry.addData("JoystickY", "%f", JoystickY);
        return JoystickY;

    }

    void cryptoState(int targetHeading, float basePosx, float basePosy) {
        runtime.reset();

        // Grab first distance
        double currentDist = rangeSensor.getDistance(DistanceUnit.CM);
        double previousDist = currentDist;

        // Red game
        if (iAmRed) {
            if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                vuMark = RelicRecoveryVuMark.RIGHT;
            }
            boxOrder[0] = RelicRecoveryVuMark.UNKNOWN;
            boxOrder[1] = RelicRecoveryVuMark.RIGHT;
            boxOrder[2] = RelicRecoveryVuMark.CENTER;
            boxOrder[3] = RelicRecoveryVuMark.LEFT;
        }
        // Blue game
        else {
            if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                vuMark = RelicRecoveryVuMark.LEFT;
            }
            boxOrder[0] = RelicRecoveryVuMark.LEFT;
            boxOrder[1] = RelicRecoveryVuMark.CENTER;
            boxOrder[2] = RelicRecoveryVuMark.RIGHT;
            boxOrder[3] = RelicRecoveryVuMark.UNKNOWN;
        }

        // Simple state machine
        int i = 0;
        boolean go = true;
        RelicRecoveryVuMark state = boxOrder[i];

        // While not our state do ...
        while (go) {

            // Move & take reading
            adjustHeading(targetHeading, basePosx, basePosy);
            previousDist = currentDist;
            currentDist = rangeSensor.getDistance(DistanceUnit.CM);

            // Found a divider, change state
            if ((currentDist - previousDist) >= 4) {
                i++;
                state = boxOrder[i];
                if (state == vuMark) {
                    go = false;
                }
            }

            // Debugging info
            telemetry.addData("Previous Distance after read: ", previousDist);
            telemetry.addData("Current Distance after read: ", currentDist);
            telemetry.addData("State", state);
            telemetry.addData("i", i);
            telemetry.update();

        }
        telemetry.addData("We have made it this far... ", state);
        // Stop the robot
        sR();

    }

    void followHeading(int targetHeading, double time, float basePosx, float basePosy) {
        //angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //double currentHeading = angles.firstAngle;
        runtime.reset();
        while (((runtime.seconds() < time))) {
            adjustHeading(targetHeading, basePosx, basePosy);
        }
        sR();
    }

    void adjustHeading(int targetHeading, float basePosx, float basePosy) {
        angles =(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
        telemetry.addData("First Angle", angles.firstAngle);
        if (angles != null) {
            double currentHeading =  gyroFlipped * angles.firstAngle;
            double addPower = (targetHeading - currentHeading) * .025;
            telemetry.addData("add power", addPower);

            double FRBLPower = (-basePosy) - basePosx;
            double FLBRPower = (-basePosy) + basePosx;
            motorFR.setPower(FRBLPower + addPower);
            motorFL.setPower(FLBRPower - addPower);
            motorBR.setPower(FLBRPower + addPower);
            motorBL.setPower(FRBLPower - addPower);
        }
    }

    void flip() {
        distCorrector(20);
        motorFlip.setPower(.5d);
        Wait(1d);
        motorFlip.setPower(0d);
        Wait(1);
        motorFlip.setPower(-.5d);
        Wait(.5);
        motorFlip.setPower(0d);
        distCorrector(15);
        distCorrector(25);
    }
    void fancyGyroPivot (double target) {

        //First set up variables

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double raw =  (gyroFlipped) * angles.firstAngle;
        float fudgeFactor = .25f;
        double wheelPower = .3;
        double convert = raw;
        double dflt = (target - raw);
        double alt = (360 - Math.abs(dflt));
        boolean right;

        while ((convert < (target - fudgeFactor)) || (convert > (target + fudgeFactor)))
        {
            telemetry.clearAll();
            telemetry.addData("Raw: ", raw);
            telemetry.addData("Convert: ", convert);
            telemetry.addData("Default", dflt);
            telemetry.addData("Alt", alt);
            telemetry.update();

            // determine if we are moving right or left
            if (alt < dflt || dflt < 0)
            {
                right = false;
            }
            else
            {
                right = true;
            }

            //set power to motor
            if (right = false)
            {
                motorFL.setPower(wheelPower);
                motorBL.setPower(wheelPower);
                motorFR.setPower(-wheelPower);
                motorBR.setPower(-wheelPower);
            }
            else
            {
                motorFL.setPower(-wheelPower);
                motorBL.setPower(-wheelPower);
                motorFR.setPower(wheelPower);
                motorBR.setPower(wheelPower);
            }

            //read the heading and find the shortest path
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles != null) {
                raw = (gyroFlipped)* angles.firstAngle;
                dflt = (target - raw);
                alt = (360 - Math.abs(dflt));

                //determines if we need to convert to compass
                if (Math.abs(dflt) > 180) {
                    convert = 360 - Math.abs(raw);

                } else {
                    convert = raw;
                }
            }
        }
        sR();
    }

    public double getHeading()
    {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentHeading =   (gyroFlipped)* angles.firstAngle;
        telemetry.addData("Heading", currentHeading);
        telemetry.update();
        return currentHeading;
    }

    void pivotTo(int target) {
        //Pivot to counterclockwise is positive.
        //Pivot to clockwise is negative.
        float fudgeFactor = .25f;


        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentHeading = angles.firstAngle;
        double wheelPower = .3;

        while ((currentHeading < (target - fudgeFactor)) || (currentHeading > (target + fudgeFactor))) {
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (angles != null) {
                currentHeading = angles.firstAngle;
                telemetry.update();
                if (target - currentHeading > 0) {
                    motorFL.setPower(-wheelPower);
                    motorBL.setPower(-wheelPower);
                    motorFR.setPower(wheelPower);
                    motorBR.setPower(wheelPower);
                } else {
                    motorFL.setPower(wheelPower);
                    motorBL.setPower(wheelPower);
                    motorFR.setPower(-wheelPower);
                    motorBR.setPower(-wheelPower);
                }
            }

        }
        sR();
    }

    public double compassConverter(double raw)
    {
           double compass = 3;

            if (raw < 0)
            {
                compass = 360 + raw;
            }

            else
            {
                compass = raw;
            }
        return compass;
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    void Wait(double WaitTime) {
        runtime.reset();
        while (runtime.seconds() < WaitTime) {
            //Comment this out to avoid it overwriting other telemetry
            //telemetry.addData("5", " %2.5f S Elapsed", runtime.seconds());
            //telemetry.update();
        }
    }

    void sR() {
        motorFL.setPower(0);
        motorBL.setPower(0);
        motorFR.setPower(0);
        motorBR.setPower(0);
    }

    void setUpVuforia(){

        VuforiaLocalizer vuforia;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "Ac+mNz7/////AAAAGarZm7vF6EvSku/Zvonu0Dtf199jWYNFXcXymm3KQ4XngzMBntb" +
                    "NeMXt0qCPqACXugivtrYvwDU3VhMDRJwlwdMi4C2F6Su/8LZBrPIFtxUtr7MMagebQM/+4CSUIOQQdKNpdBttrX8yWM" +
                    "SrdyfnkNhh/vhXpQd7pXWwJ02UcnEVT1CiLeyTcl+bJUo1+xNonNaNEs8861zxmtO2TBtf9gyXhunlM6lpBJjC6nYWQ3" +
                    "BM2DOODFNz2EU3F3N1WxnOvCERQ+c934JKPajgCrNs5dquSo1wpcr0Kkf3u29hzK0DornR8s9j03g8Ea7q5cYN8WLn/e" +
                    "q1dUOFznng+6y2/7/fvw9wrzokOP9nP1QujkUN";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();
        int timeRecongnize=0;

        for(int t = 0; t<2e6; t++) {
            timeRecongnize = t;
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){
                break;
            }
        }
        telemetry.addData("VuMark", "%s visible", vuMark);
        telemetry.addData("Time Recognize", "%d", timeRecongnize);
        telemetry.update();
    }

    void distCorrector(double trgDistance) {

        double curDistance = rangeSensor.getDistance(DistanceUnit.CM);
        double fstDistance = curDistance - trgDistance;

        boolean go = true;
        int i = 0;

        // While not our state do ...
        while (go) {

            curDistance = rangeSensor.getDistance(DistanceUnit.CM);
            double chgDistance = curDistance - trgDistance;
            double direction;
            if (chgDistance > 0) {
                direction = 1;
            } else {
                direction = -1;
            }

            if (Math.abs(chgDistance) >= 2) {

                // Slow down
                double adjust = 0.20 * direction;

                //(Math.abs(chgDistance / fstDistance)) * direction;
                //if (adjust > 0.25) { adjust = 0.25; }

                telemetry.addData("current distance", curDistance);
                telemetry.addData("target distance", trgDistance);
                telemetry.addData("direction", direction);
                telemetry.addData("adjust", adjust);
                telemetry.addData("i", i);
                telemetry.update();

                // Power the motors
                  motorFL.setPower(adjust);
                  motorFR.setPower(adjust);
                  motorBL.setPower(adjust);
                  motorBR.setPower(adjust);
            } else
            {
                go = false;
            }
            i++;
        //    if (i > 30) {
        //        go = false;
        //    }
        }
        sR();
    }


    void jewel(){

        // J e w e l s

        boolean iSeeBlue = false;
        boolean iSeeRed = false;


        telemetry.update();

        telemetry.addLine()
                .addData("iSeeRed", "%b", iSeeRed)
                .addData("iSeeBlue", "%b", iSeeBlue)
                .addData ( "iSeeRed && iAmRed", "%b", (iSeeRed && iAmRed))
                .addData ( "iSeeBlue && iAmBlue", "%b", (iSeeBlue && iAmBlue))
                .addData ( "Final Boolean", "%b", ((iSeeRed && iAmRed) || (iSeeBlue && iAmBlue)));

        telemetry.update();

        Wait(1f);


        telemetry.update();

    }
}
