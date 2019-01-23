package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


public abstract class StandardChassis extends OpMode {

    protected ChassisConfig config;

    // Elapsed time since the opmode started.
    protected ElapsedTime runtime = new ElapsedTime();
    protected ElapsedTime dropTime = new ElapsedTime();

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor extender;
    private DcMotor shoulder;

    // Team Marker Servo
    private Servo flagHolder;
    private Servo bull;
    private Servo dozer;
    private double angleHand;

    // Walle state management
    int wasteAllocationLoadLifterEarthBegin;
    private DcMotor wasteAllocationLoadLifterEarth;

    //gyroscope built into hub
    private BNO055IMU bosch;

    // Hack stuff.
    protected boolean useBulldozer =false;
    protected boolean useMotors = true;
    protected boolean useTeamMarker = true;
    protected boolean hackTimeouts = true;
    protected boolean useArm = true;


    protected StandardChassis(ChassisConfig config) {
        this.config = config;
    }

    protected void initMotors() {

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorBackLeft.setDirection(config.isLeftMotorReversed() ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
            motorBackRight.setDirection(config.isRightMotorReversed() ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

            // initilize the encoder
            motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if (config.getUseFourWheelDrive()) {
                motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
                motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");

                motorFrontLeft.setDirection(config.isLeftMotorReversed() ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
                motorFrontRight.setDirection(config.isRightMotorReversed() ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

                motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }

        // Team marker servo
        if (useTeamMarker) {
            flagHolder = hardwareMap.get(Servo.class, "servo1");
            resetFlag();
        }

        // init the lifter arm,
        if (config.getHasWalle()) {
            wasteAllocationLoadLifterEarth = hardwareMap.get(DcMotor.class, "motor6");
            wasteAllocationLoadLifterEarth.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wasteAllocationLoadLifterEarth.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            wasteAllocationLoadLifterEarthBegin = wasteAllocationLoadLifterEarth.getCurrentPosition();
        }
    }

    protected void initTimeouts() {
        // This code prevents the OpMode from freaking out if you go to sleep for more than a second.
        if (hackTimeouts) {
            this.msStuckDetectInit = 30000;
            this.msStuckDetectInitLoop = 30000;
            this.msStuckDetectStart = 30000;
            this.msStuckDetectLoop = 30000;
            this.msStuckDetectStop = 30000;
        }
    }

     protected void initBulldDozer() {
         if(useBulldozer){
             bull = hardwareMap.get(Servo.class, "servo3");
             dozer = hardwareMap.get(Servo.class, "servo2");
         }
     }


    protected void bullDozerUp() {
        if (useBulldozer) {
            //control Bulldozer
            bull.setPosition(0);
            dozer.setPosition(1.0);
        }
    }

    protected void initArm() {
        if (useArm) {
            shoulder = hardwareMap.get(DcMotor.class, "motor4");
            extender = hardwareMap.get(DcMotor.class, "motor5");
        }
    }

    protected boolean initGyroscope() {
        bosch = hardwareMap.get(BNO055IMU.class, "imu0");
        telemetry.addData("Gyro", "class:" + bosch.getClass().getName());

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.loggingTag = "bosch";
        //parameters.calibrationDataFile = "MonsieurMallahCalibration.json"; // see the calibration sample opmode
        boolean boschInit = bosch.initialize(parameters);
        return boschInit;
    }



    public void dropFlag() {
        if (useTeamMarker) {
            if(config.isTeamMarkerReversed()) {
                angleHand = 1.0;
            } else {
                angleHand = 0.0;
            }
            flagHolder.setPosition(angleHand);
        }
    }

    public void resetFlag() {
        if (useTeamMarker) {
            angleHand = 0.5;
            flagHolder.setPosition(angleHand);
        }
    }


    protected void encoderDrive(double leftInches, double rightInches) {
        double speed = config.getMoveSpeed();
        double countsPerInch = config.getRearWheelSpeed() / (config.getRearWheelDiameter() * Math.PI);

        // Get the current position.
        int leftBackStart = motorBackLeft.getCurrentPosition();
        int rightBackStart = motorBackRight.getCurrentPosition();
        int leftFrontStart = 0;
        int rightFrontStart = 0;
        if (config.getUseFourWheelDrive()) {
             leftFrontStart = motorFrontLeft.getCurrentPosition();
             rightFrontStart = motorFrontRight.getCurrentPosition();
        }
        telemetry.addData("encoderDrive", "Starting %7d, %7d, %7d, %7d",
                leftBackStart, rightBackStart, leftFrontStart, rightFrontStart);

        // Determine new target position, and pass to motor controller
        int leftBackTarget = leftBackStart + (int) (leftInches * countsPerInch);
        int rightBackTarget = rightBackStart + (int) (rightInches * countsPerInch);
        int leftFrontTarget = 0;
        int rightFrontTarget = 0;
        if (config.getUseFourWheelDrive()) {
             leftFrontTarget = leftFrontStart + (int) (leftInches * countsPerInch);
             rightFrontTarget = rightFrontStart + (int) (rightInches * countsPerInch);
        }

        motorBackLeft.setTargetPosition(leftBackTarget);
        motorBackRight.setTargetPosition(rightBackTarget);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setTargetPosition(leftFrontTarget);
            motorFrontRight.setTargetPosition(rightFrontTarget);
        }
        telemetry.addData("encoderDrive", "Target %7d, %7d, %7d, %7d",
                leftBackTarget, rightBackTarget, leftFrontTarget, rightFrontTarget);

        // Turn On RUN_TO_POSITION
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setPower(Math.abs(speed));
        motorBackRight.setPower(Math.abs(speed));
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorFrontLeft.setPower(Math.abs(speed));
            motorFrontRight.setPower(Math.abs(speed));
        }

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        ElapsedTime motorOnTime = new ElapsedTime();
        boolean keepGoing = true;
        while ((motorOnTime.seconds() < 30) && keepGoing) {

            if (config.getUseFourWheelDrive()) {
                telemetry.addData("encoderDrive1", "Running at %7d, %7d, %7d, %7d",
                        motorBackLeft.getCurrentPosition(),
                        motorBackRight.getCurrentPosition(),
                        motorFrontLeft.getCurrentPosition(),
                        motorFrontRight.getCurrentPosition());
                telemetry.addData("encoderDrive2", "Running to %7d, %7d, %7d, %7d",
                        leftBackTarget,
                        rightBackTarget,
                        leftFrontTarget,
                        rightFrontTarget);
                keepGoing = motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontLeft.isBusy() && motorFrontRight.isBusy();
            } else {
                telemetry.addData("encoderDrive1", "Running at %7d, %7d",
                        motorBackLeft.getCurrentPosition(),
                        motorBackRight.getCurrentPosition());
                telemetry.addData("encoderDrive2", "Running to %7d, %7d",
                        leftBackTarget,
                        rightBackTarget);
                keepGoing = motorBackRight.isBusy() && motorBackLeft.isBusy();
            }

            telemetry.update();
            sleep(100);
        }

        // Turn off RUN_TO_POSITION
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        /*telemetry.addData("encoderDrive", "Finished (%s) at %7d,%7d,%7d,%7d to [%7d,%7d,%7d,%7d] (%7d,%7d,%7d,%7d)",
                motorOnTime.toString(),
                leftBackStart,
                rightBackStart,
                leftFrontStart,
                rightFrontStart,
                motorBackLeft.getCurrentPosition(),
                motorBackRight.getCurrentPosition(),
                motorFrontLeft.getCurrentPosition(),
                motorFrontRight.getCurrentPosition(),
                leftBackTarget,
                rightBackTarget,
                leftFrontTarget,
                rightFrontTarget); */
        sleep(1000);
    }


    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
    }

    // Always returns a number from 0-359.9999
    protected float getGyroscopeAngle() {
        Orientation exangles = bosch.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        float gyroAngle = exangles.thirdAngle + 50;
        return CrazyAngle.normalizeAngle(CrazyAngle.reverseAngle(gyroAngle));
    }



    /**
     *
     * @param deltaAngle
     */
    protected void turnLeft(float deltaAngle) {
        assert(deltaAngle > 0.0);
        assert(deltaAngle <= 360.0);

        // does it wrap at all? (go around the zero mark?)
        float currentAngle = getGyroscopeAngle();
        float destinationAngle = currentAngle - deltaAngle;
        boolean doesItWrapAtAll = (destinationAngle < 0.0);
        destinationAngle = CrazyAngle.normalizeAngle(destinationAngle);

        // Get it past the zero mark.
        if (doesItWrapAtAll) {
            boolean keepGoing = true;
            while (keepGoing) {
                float oldAngle = currentAngle;
                nudgeLeft();
                currentAngle = getGyroscopeAngle();

                float justMoved = oldAngle - currentAngle;
                float stillNeed = currentAngle;
                telemetry.addData("turnLeft1", "current=%.0f, old=%.0f, dst=%.0f, moved=%.0f, need=%.0f", currentAngle, oldAngle, destinationAngle, justMoved, stillNeed);
                telemetry.update();

                keepGoing = (justMoved > -50.0);
            }
        }

        // turn the last part
        while ((currentAngle - destinationAngle) > 5.0) {

            float oldAngle = currentAngle;
            nudgeLeft();
            currentAngle = getGyroscopeAngle();

            float justMoved = oldAngle - currentAngle;
            float stillNeed = currentAngle - destinationAngle;
            telemetry.addData("turnLeft2", "current = %.0f, destination = %.0f, moved=%.0f, need=%.0f", currentAngle, destinationAngle, justMoved, stillNeed);
            telemetry.update();
        }
    }

    /**
     *
     * @param deltaAngle
     */
    protected void turnRight(float deltaAngle) {
        assert(deltaAngle > 0.0);
        assert(deltaAngle <= 360.0);

        // does it wrap at all?
        float currentAngle = getGyroscopeAngle();
        float destinationAngle = currentAngle + deltaAngle;
        boolean doesItWrapAtAll = (destinationAngle > 360.0);
        destinationAngle = CrazyAngle.normalizeAngle(destinationAngle);

        // Get it past the zero mark.
        if (doesItWrapAtAll) {
            boolean keepGoing = true;
            while (keepGoing) {
                float oldAngle = currentAngle;
                nudgeRight();
                currentAngle = getGyroscopeAngle();

                float justMoved = currentAngle - oldAngle;
                float stillNeed = 360.0f - currentAngle;
                telemetry.addData("turRight1", "current=%.0f, old=%.0f, dst=%.0f, moved=%.0f, need=%.0f", currentAngle, oldAngle, destinationAngle, justMoved, stillNeed);
                telemetry.update();

                keepGoing = (justMoved > -50.0);
            }
        }

        // turn the last part
        while ((destinationAngle - currentAngle) > 5.0) {

            float oldAngle = currentAngle;
            nudgeRight();
            currentAngle = getGyroscopeAngle();

            float justMoved = currentAngle - oldAngle;
            float stillNeed = destinationAngle - currentAngle;
            telemetry.addData("turnRight2", "current = %.0f, destination = %.0f, moved=%.0f, need=%.0f", currentAngle, destinationAngle, justMoved, stillNeed);
            telemetry.update();
        }
    }

    // This nudges over about 2 degrees.
    protected void nudgeRight() {
        float power = config.getTurnSpeed();

        motorBackLeft.setPower(power);
        motorBackRight.setPower(-power);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(power);
            motorFrontRight.setPower(-power);
        }
        sleep(2);

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }
    }

    // This nudges over about 2 degrees.
    protected void nudgeLeft() {
        float power = config.getTurnSpeed();

        motorBackLeft.setPower(-power);
        motorBackRight.setPower(power);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(-power);
            motorFrontRight.setPower(power);
        }
        sleep(2);

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }
    }



    protected void nudgeBack() {
        float power = config.getTurnSpeed();

        motorBackLeft.setPower(-power);
        motorBackRight.setPower(-power);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(-power);
            motorFrontRight.setPower(-power);
        }
        sleep(500);

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }
    }


    protected void pointToZero() {

        float currentAngle = getGyroscopeAngle();
        float destinationAngle = 0;
        boolean keepGoing = true;
        while (keepGoing) {
            float oldAngle = currentAngle;
            nudgeRight();
            currentAngle = getGyroscopeAngle();

            float justMoved = currentAngle - oldAngle;
            float stillNeed = 360.0f - currentAngle;
            telemetry.addData("turRight1", "current=%.0f, old=%.0f, dst=%.0f, moved=%.0f, need=%.0f", currentAngle, oldAngle, destinationAngle, justMoved, stillNeed);
            telemetry.update();

            keepGoing = (justMoved > -50.0);
        }
    }

    protected void slideUpExtender(int extenderCounts) {
        double speed = 0.25;

        // Get the current position.
        int extenderStart = extender.getCurrentPosition();
        telemetry.addData("encoderDrive", "Starting %7d", extenderStart);

        // Determine new target position, and pass to motor controller
        int extenderTarget = extenderStart + extenderCounts;
        extender.setTargetPosition(extenderTarget);
        telemetry.addData("encoderDrive", "Target %7d", extenderTarget);

        // Turn On RUN_TO_POSITION
        extender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extender.setPower(speed);

        ElapsedTime motorOnTime = new ElapsedTime();
        while ((motorOnTime.seconds() < 30) && extender.isBusy()) {
            telemetry.addData("slideUpExtender", "Running at %7d to %7d", extender.getCurrentPosition(), extenderTarget);
            telemetry.update();
            sleep(10);
        }

        // Turn off RUN_TO_POSITION
        extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extender.setPower(0);
    }

    protected void shiftShoulderDown(int shoulderCounts) {
        double speed = 0.5;

        // Get the current position.
        int shoulderStart = shoulder.getCurrentPosition();
        telemetry.addData("shoulderShifting", "Starting %7d", shoulderStart);

        // Determine new target position, and pass to motor controller
        int shoulderTarget = shoulderStart + shoulderCounts;
        shoulder.setTargetPosition(shoulderTarget);
        telemetry.addData("shoulderShifting", "Target %7d", shoulderTarget);

        // Turn On RUN_TO_POSITION
        shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shoulder.setPower(speed);

        ElapsedTime motorOnTime = new ElapsedTime();
        while ((motorOnTime.seconds() < 30) && shoulder.isBusy()) {
            telemetry.addData("shiftShoulderDown", "Running at %7d to %7d", shoulder.getCurrentPosition(), shoulderTarget);
            telemetry.update();
            telemetry.update();
            sleep(10);
        }

        // Turn off RUN_TO_POSITION
        shoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoulder.setPower(0);
    }

    protected void strafeLeft(int numberOfMillis) {
        float power = config.getTurnSpeed();

        motorBackLeft.setPower(power);
        motorBackRight.setPower(-power);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(-power);
            motorFrontRight.setPower(power);
        }
        sleep(numberOfMillis);

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }
    }


    protected void strafeRight(int numberOfMillis) {
        float power = config.getTurnSpeed();

        motorBackLeft.setPower(-power);
        motorBackRight.setPower(power);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(power);
            motorFrontRight.setPower(-power);
        }
        sleep(numberOfMillis);

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        if (config.getUseFourWheelDrive()) {
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }
    }

    protected void descendFromLander() {
        if(!config.getlyftStrategy()) {
            // go down.
            lyftDownWalle(-4956);
            //existing Quiksilver descend stragety
            strafeRight(450);
        } else {
            lyftDownWalle(-1449);
            //write new phatswipe descend strategy
            turnLeft(25);
            encoderDrive(2,2);
            turnRight(25);
        }
    }

    protected void lyftDownWalle(int howManySpins) {
        double speed = 0.5f;

        // Get the current position.
        int lyftBegin = wasteAllocationLoadLifterEarth.getCurrentPosition();
        telemetry.addData("lyftDownWalle", "Starting %7d", lyftBegin);

        // Determine new target position, and pass to motor controller
        int lyftTarget = lyftBegin + howManySpins;
        wasteAllocationLoadLifterEarth.setTargetPosition(lyftTarget);
        telemetry.addData("lyftDownWalle", "Target %7d", lyftTarget);

        // Turn On RUN_TO_POSITION
        wasteAllocationLoadLifterEarth.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wasteAllocationLoadLifterEarth.setPower(speed);

        ElapsedTime motorOnTime = new ElapsedTime();
        while ((motorOnTime.seconds() < 30) && wasteAllocationLoadLifterEarth.isBusy()) {
            telemetry.addData("lyftDownWalle", "Running at %7d to %7d", wasteAllocationLoadLifterEarth.getCurrentPosition(), lyftTarget);
            telemetry.update();
            sleep(10);
        }

        // Turn off RUN_TO_POSITION
        wasteAllocationLoadLifterEarth.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wasteAllocationLoadLifterEarth.setPower(0);

        //sleep(5000);
    }




}
