
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name="Blue Right With Key", group="Bacon Autonomous!")
//@Disabled
public class FinalPerfectBlueRightWithKeyMaybe extends LinearOpMode
{
  /* Declare all devices since hardware class isn't working */
    DcMotor                 frontLeftMotor;
    DcMotor                 backLeftMotor;
    DcMotor                 frontRightMotor;
    DcMotor                 backRightMotor;
    DcMotor                 verticalArmMotor;
    ColorSensor             colorSensor;
    Servo                   gemServo;
    BNO055IMU               imu;
    CRServo                 clawServo;

  /* Set up and init all variables */
    Orientation             lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    double xPosUp = 0;
    double xPosDown = .55;
    static double clawClose = .3;
    static double clawOpen = -.5;
    static double clawStill = 0;
    OpenGLMatrix lastLocation = null;

    /*{@link #vuforia} is the variable we will use to store our instance of the Vuforia localization engine.*/
    VuforiaLocalizer vuforia;

    /* Create a "timer" that begins once the OpMode begins */
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException
    {

    /* To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);*/
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

    /* Our vurforia license key */
        parameters.vuforiaLicenseKey = "AbWq5Uv/////AAAAGcM6elMmVUOogS1JXLiPuvxUPJgstfq86yvjnKb87ZG0oftSUkO7FUXo+LPZdGe3ytBqkwQmXV6b0hiAMotK9TAX//BaE8tYQe0cJQzMPk5jjMAmLbZgZ1p3V9EQzp59pYvYvBMYzoNw7YzlpMNC3GzmXd40NyecOmx8Q6lp/tQikXO0yKGQLIoJpKtGfoVkxpmyCx/u4/6FYBAGyvZt8I8mz3UtGl/Yf366XKgNXq26uglpVfeurmB/cV5RzMVdVDTdyE/2yLqjalrAKgL2CZFv3iY/MnxW+pIyJUHbXUQVCUoB8SqULq7u948Vx+5w5ObesVFNzZ3jbBTBHwUWbpaJAZFGjmD1dRaVdS/GK74x";

    /*We also indicate which camera on the RC that we wish to use. Here we chose the back (HiRes) camera (for greater range)*/
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

    /* Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
    * in this data set: all three of the VuMarks in the game were created from this one template,
    * but differ in their instance id information.
    */
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

    /* Find all hardware in configuration */
        frontLeftMotor = hardwareMap.dcMotor.get("FL");
        backLeftMotor = hardwareMap.dcMotor.get("BL");
        frontRightMotor = hardwareMap.dcMotor.get("FR");
        backRightMotor = hardwareMap.dcMotor.get("BR");
        verticalArmMotor = hardwareMap.dcMotor.get("VAM");
        gemServo = hardwareMap.servo.get("gemservo");
        colorSensor = hardwareMap.colorSensor.get("colorsensor");
        clawServo =  hardwareMap.crservo.get("CS");

    /* Reverse the direction of the front right and back right motors */
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

    /* When robot is off remove the brake on the wheel motors */
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    /* Set parameters for the gyro (imu)*/
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode                = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");

    /* Initialize all motors and servoes to their starting positions */
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        gemServo.setPosition(xPosUp);

    /* Initialize the gyro with the parameters above */
        imu.initialize(imuparameters);

    /* Tell driver the gyro is calibrating */
        telemetry.addLine("I'm calibrating...");
        telemetry.update();

    /* Make sure the imu gyro is calibrated before continuing */
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addLine("Um...waiting for start");
        telemetry.addData("Imu calibration?", imu.getCalibrationStatus().toString());
        telemetry.update();

        // wait for start button.
        waitForStart();

        relicTrackables.activate();

        /* Let the team know wassup */
        telemetry.addLine("Ayyy, I'm running");
        telemetry.update();

        /* Power the claw to have a grip on the block */
        clawServo.setPower(clawClose);

        /* Move the claw up so it doesn't dig into the ground coming off the balance board */
        moveclawbytime(1,.5,"Up");

        /* Put the servo color arm down */
        gemServo.setPosition(xPosDown);
        sleep(1500);

        /* Knock of the Red jewel */
        knockjewelRed();

        /* Rotate so the phone can see the Vuforia Key */
        rotate(10,.3);

        /* Read the vuMark */
        /* Tells vuforia to look for relic templates, if it finds something, then it returns
        LEFT, RIGHT, CENTER and stores it into "vuMark", otherwise it only returns UNKNOWN */
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        /* If "vuMark" is something other than UNKNOWN */
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            /* Send telemetry saying what vuforia sees */
            telemetry.addData("Ayyy I see", vuMark);
        }
        telemetry.update();

        /* Return to starting position */
        rotate(-10, .3);

        /* Wait a moment and let vuforia do its work and for the robot to realign properly */
        sleep(500);

        //////////////////* Begin the variance *\\\\\\\\\\\\\\\\\\\\\\\\\\\

        /////////////////* This is the Blue Right Case *\\\\\\\\\\\\\\\\\\\\\\\

        /* Switch case based on what vuMark we see */
        switch (vuMark){
            case LEFT:
                /* Drive forward into the left position */
                movebytime(1,.5,"Forward");
                break;
            case RIGHT:
                /* Drive forward into the right position */
                movebytime(2, .5, "Forward");
                break;
            case CENTER:
                /* Drive forward into the center position */
                movebytime(1.25, .5, "Forward");
                break;
            case UNKNOWN:
                /* Drive forward into the center position just cuz i said so */
                movebytime(1.25, .5, "Forward");
                break;
        }

        /* This is really meant to accomplish  a 90 degree rotation, however, it is set to 87 to
        account for the slight slippage after powering off the wheels */
        rotate(87, .35);

        /* Move forward slightly so the block is in the space */
        movebytime(.2, .3, "Forward");

        /////////////////* End the variance *\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        /* Move the claw down slightly */
        moveclawbytime(1,.5,"Down");

        /* Open up the claw to release the block */
        clawServo.setPower(clawOpen);
    }

/***********************************************************************************************
 * These are all of the methods used in the Autonomous*
 ***********************************************************************************************/


/* This method moves the claw up or down for a certain amount of time either up or down */
    public void clawAbre(double time) {
        /* reset the "timer" to 0 */
        runtime.reset();

        /* Open the claw */
        clawServo.setPower(clawOpen);

        /* If the timer hasn't reached the time that is indicated do nothing and keep opening */
        while (opModeIsActive() && runtime.seconds() < time) {
        }

        /* Once the while loop above finishes turn off claw servo */
        clawServo.setPower(clawStill);
    }


/* This method moves the claw up or down for a certain amount of time either up or down */
    public void moveclawbytime(double time, double power, String direction) {
    /* reset the "timer" to 0 */
        runtime.reset();

    /* This switch case is determined by the String indicated above */
        switch (direction) {
            case "Up":
                verticalArmMotor.setPower(power);
                break;
            case "Down":
                verticalArmMotor.setPower(-power);
                break;
        }

    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {
        }

    /* Once the while loop above finishes turn off claw motor */
        verticalArmMotor.setPower(0);
    }

/* This method moves the robot forward for time and power indicated*/
    public void movebytime (double time, double power, String direction) {
    /* reset the "timer" to 0 */
        runtime.reset();

    /* This switch case is determined by the String direction indicated above */

        switch (direction) {
            case "Forward":
                setWheelPower(power, -power, power, -power);
                break;
            case "Backward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Right":
                setWheelPower(power, power, -power, -power);
                break;
            case "Left":
                setWheelPower(-power, -power, power, power);
                break;
        }
    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {

        }
    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


/* This method simply sets all wheel motors to zero power */
    public void wheelsOff() {
        setWheelPower(0,0,0,0);
    }

/* This method powers each wheel to whichever power is desired */
    public void setWheelPower(double fl, double fr, double bl, double br) {

        /* Create power variables */
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

        double FrontLeftPower;
        double FrontRightPower;
        double BackLeftPower;
        double BackRightPower;

        FrontLeftPower = 0;
        FrontRightPower = 0;
        BackLeftPower = 0;
        BackRightPower = 0;

        /* Initialize the powers with the values input whenever this method is called */
        frontLeft   =   fl;
        frontRight  =   fr;
        backLeft    =   bl;
        backRight   =   br;

        /* set each wheel to the power indicated whenever this method is called */
        if ( FrontLeftPower != frontLeft) {
            frontLeftMotor.setPower(-fl);
            FrontLeftPower = frontLeft;
        }
        if ( FrontRightPower != frontRight) {
            frontRightMotor.setPower(fr);
            FrontRightPower = frontRight;
        }
        if ( BackLeftPower != backLeft) {
            backLeftMotor.setPower(-bl);
            BackLeftPower = backLeft;
        }
        if ( BackRightPower != backRight)
            backRightMotor.setPower(br);
            BackRightPower = backRight;
    }

/* This method is tells the color sensor to read color, then rotate to knock off the blue
jewel and then return the color sensor arm back up */
    public void knockjewelRed() {

        if (colorSensor.red() < colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(-10,.3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(10,.3);
        }
    }

/* This method is tells the color sensor to read color, then rotate to knock off the red
jewel and then return the color sensor arm back up */
    public void knockjewelBlue(){

        if (colorSensor.red() > colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(-10,.3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(10,.3);
        }

    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle()
    {

        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    /**
     * See if we are moving in a straight line and if not return a power correction value.
     * @return Power adjustment, + is adjust left - is adjust right.
     */
    private double checkDirection()
    {
        // The gain value determines how sensitive the correction is to direction changes.
        // You will have to experiment with your robot to get small smooth direction changes
        // to stay on a straight line.
        double correction, angle, gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0;             // no adjustment.
        else
            correction = -angle;        // reverse sign of angle for correction.

        correction = correction * gain;

        return correction;
    }

    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power)
    {
        double  leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0)
        {   // turn right.
            leftPower = -power;
            rightPower = power;
        }
        else if (degrees > 0)
        {   // turn left.
            leftPower = power;
            rightPower = -power;
        }
        else return;

        // set power to rotate.
        frontLeftMotor.setPower(leftPower);
        backLeftMotor.setPower(leftPower);

        frontRightMotor.setPower(rightPower);
        backRightMotor.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {}

            while (opModeIsActive() && getAngle() > degrees) {}
        }
        else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {}

        // turn the motors off.
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }
}