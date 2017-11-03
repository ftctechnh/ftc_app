package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Robot extends OpMode
{
    // Seconds elapsed over last loop()
    private double deltaTime;

    // Used to calculate deltaTime
    private ElapsedTime elapsedTime;


    // Rotation in degrees relative to compassOrigin
    private double rotation;

    // Target rotation in degrees relative to compassOrigin
    private double targetRotation;


    // Absolute x position in inches
    private static double xPosition;

    // Absolute y position in inches
    private static double yPosition;

    // Target absolute x position in inches
    private double targetXPosition = xPosition;

    // Target absolute y position in inches
    private double targetYPosition = yPosition;

    // Direction in degrees of movement
    private double direction;

    // Speed between [0, 1] of movement
    private double speed;




    // Arm rotation constants
    static final double START_STORED_ARM_ROTATION = 0.13;
    static final double START_BLOCK_ARM_ROTATIONS = 0.25;
    static final double BLOCK_1_ARM_ROTATION = 0.3;
    static final double BLOCK_2_ARM_ROTATION = 0.33;
    static final double BLOCK_3_ARM_ROTATION = 0.36;
    static final double BLOCK_4_ARM_ROTATION = 0.39;
    static final double START_RELIC_ARM_ROTATIONS = 0.45;
    static final double RELIC_WALL_CLEARANCE_ARM_ROTATION = 0.6;
    static final double RELIC_ARM_ROTATION = 0.76;
    static final double START_FLIP_ARM_ROTATIONS = 0.83;
    static final double FLIP_ARM_ROTATION = 0.85;

    // Current rotation estimated in degrees
    // 0 equals down, 0.25 equals forward, 0.5 equals up, 0.75 equals back
    static double armRotation = START_STORED_ARM_ROTATION;

    // Target rotation in degrees
    // 0 equals down, 0.25 equals forward, 0.5 equals up, 0.75 equals back
    double targetArmRotation = armRotation;


    // Target rotation in degrees
    // 0 equals open, 1 equals closed
    static double gripperRotation;


    // Mode of calibration
    private static boolean calibratingCompass;

    // Home rotation in degrees relative to north
    private static double compassOrigin;

    // Hardware devices
    private static CompassSensor compass;
    private static DeviceInterfaceModule dim;
    // BEWARE: AndyMark AM-2992 encoder cable 4-pin housing is not keyed for FTC motor controllers. Make sure BLACK cable is towards bottom of motor controller.
    private static DcMotor flMotor;
    private static DcMotor frMotor;
    private static DcMotor blMotor;
    private static DcMotor brMotor;
    private static DcMotor armMotor;
    private static Servo lArmServo;
    private static Servo rArmServo;
    private static Servo lGripperServo;
    private static Servo rGripperServo;


    // Seconds elapsed over last loop()
    double DeltaTime() { return deltaTime; }

    // Rotation in degrees relative to compassOrigin


    // Target mode of calibration
    boolean CalibratingCompass() { return calibratingCompass; };
    void CalibratingCompass(boolean newCalibratingCompass) {
        calibratingCompass = newCalibratingCompass;

        if (calibratingCompass) {
            compass.setMode(CompassSensor.CompassMode.CALIBRATION_MODE);
        } else {
            compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
        }
    }



    @Override
    public void init()
    {
        /// Setup hardware devices

        if (dim == null) {
            dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");
        }

        if (compass == null) {
            compass = hardwareMap.get(CompassSensor.class, "compass");
        }

        rotation = compass.getDirection() - rotationOrigin;
        targetRotation = rotation;

        if (flMotor == null) {
            flMotor = hardwareMap.get(DcMotor.class, "flMotor");
            flMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            flMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (frMotor == null) {
            frMotor = hardwareMap.get(DcMotor.class, "frMotor");
            frMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (blMotor == null) {
            blMotor = hardwareMap.get(DcMotor.class, "blMotor");
            blMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            blMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            blMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (brMotor == null) {
            brMotor = hardwareMap.get(DcMotor.class, "brMotor");
            brMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            brMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            brMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        if (armMotor == null) {
            armMotor = hardwareMap.get(DcMotor.class, "armMotor");
            armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }


    @Override
    public  void  start()
    {
        lGripperServo = hardwareMap.get(Servo.class, "lGripperServo");
        rGripperServo = hardwareMap.get(Servo.class, "rGripperServo");

        lGripperServo.setDirection(Servo.Direction.REVERSE);
        rGripperServo.setDirection(Servo.Direction.FORWARD);

        elapsedTime.reset();
    }


    @Override
    public void loop()
    {
        // Update the compass mode
        compass.setMode(targetCompassMode);

        // Update rotation if compass is in measurement mode
        if(targetCompassMode == CompassSensor.CompassMode.MEASUREMENT_MODE) rotation = compass.getDirection() - rotationOrigin;

        // Unwrap targetRotation
        targetRotation = Math.IEEEremainder(targetRotation, 360.0);

        // Unwrap rotation
        rotation = Math.IEEEremainder(rotation, 360.0);

        // Update position
        xPosition += Math.sin(direction) * (speed * deltaTime);
        yPosition += Math.cos(direction) * (speed * deltaTime);

        // Calculate direction
        direction = Math.atan((targetXPosition - xPosition) / (targetYPosition - yPosition));

        // Calculate speed
        speed = Math.sqrt(Math.pow(targetXPosition - xPosition, 2) + Math.pow(targetYPosition - yPosition, 2)) * 0.1;

        // Constrain speed
        if(speed > 1.0) speed = 1.0;

        // Set drive motor speeds (internally maintained using encoder feedback)
        {
            double rotationAdjustment = targetRotation - rotation;

            if (rotationAdjustment < -180) rotationAdjustment += 360;
            else if (rotationAdjustment > 180) rotationAdjustment -= 360;

            if (rotationAdjustment < -22.5) rotationAdjustment = -1.0;
            else if (rotationAdjustment > 22.5) rotationAdjustment = 1.0;
            else rotationAdjustment = Math.sin((rotationAdjustment) * 4.0);

            flMotor.setPower((Math.cos((rotation + 225.0) - direction) * speed * 0.9) - (rotationAdjustment * 0.1));
            frMotor.setPower((Math.cos((rotation + 315.0) - direction) * speed * 0.9) - (rotationAdjustment * 0.1));
            blMotor.setPower((Math.cos((rotation + 135.0) - direction) * speed * 0.9) - (rotationAdjustment * 0.1));
            brMotor.setPower((Math.cos((rotation + 45.0) - direction) * speed * 0.9) - (rotationAdjustment * 0.1));
        }

        // Constrain targetArmRotation
        if (targetArmRotation < START_STORED_ARM_ROTATION) targetArmRotation = START_STORED_ARM_ROTATION;
        else if (targetArmRotation > FLIP_ARM_ROTATION) targetArmRotation = FLIP_ARM_ROTATION;

        // Update armRotation
        armRotation += armMotor.getPower() * deltaTime * 0.25;

        // Set arm motor speed (internally maintained using encoder feedback)
        armMotor.setPower(targetArmRotation - armRotation < -22.5 ? -1.0 : (targetArmRotation - armRotation > 22.5 ? 1.0 : Math.sin((targetArmRotation - armRotation) * 4.0)));


        if (armRotation >= START_BLOCK_ARM_ROTATIONS && armRotation < START_RELIC_ARM_ROTATIONS) {
            // Create arm servos if they are disabled
            if (lArmServo == null) {
                lArmServo = hardwareMap.get(Servo.class, "lArmServo");
                lArmServo.setDirection(Servo.Direction.FORWARD);
            }

            if (rArmServo == null) {
                rArmServo = hardwareMap.get(Servo.class, "rArmServo");
                rArmServo.setDirection(Servo.Direction.REVERSE);
            }

            // Set arm servo rotations
            lArmServo.setPosition(armRotation);
            rArmServo.setPosition(armRotation);

            // Set gripper servo ranges for gripping blocks
            lGripperServo.scaleRange(0.5, 0.7);
            rGripperServo.scaleRange(0.5, 0.7);
        }
        else if (armRotation > START_FLIP_ARM_ROTATIONS){
            // Create arm servos if they are disabled
            if (lArmServo == null) {
                lArmServo = hardwareMap.get(Servo.class, "lArmServo");
                lArmServo.setDirection(Servo.Direction.FORWARD);
            }
            if (rArmServo == null) {
                rArmServo = hardwareMap.get(Servo.class, "rArmServo");
                rArmServo.setDirection(Servo.Direction.REVERSE);
            }

            // Set arm servo rotations to move grippers over center
            lArmServo.setPosition(0.55);
            rArmServo.setPosition(0.55);

            // Set gripper servo ranges for gripping relics
            lGripperServo.scaleRange(0.75, 0.85);
            rGripperServo.scaleRange(0.75, 0.85);
        }
        else {
            // Disable arm servos if they are enabled
            if (lArmServo != null) {
                lArmServo.close();
                lArmServo = null;
            }

            if (rArmServo != null) {
                rArmServo.close();
                rArmServo = null;
            }

            // Fix gripper servo positions for clearance in storage or while inverting
            lGripperServo.scaleRange(0.85, 0.85);
            rGripperServo.scaleRange(0.85, 0.85);
        }


        // Constrain gripperRotation
        if (gripperRotation < 0.0) gripperRotation = 0.0;
        else if (gripperRotation > 1.0) gripperRotation = 1.0;

        // Set gripper servo rotations
        lGripperServo.setPosition(gripperRotation);
        rGripperServo.setPosition(gripperRotation);

        // Update deltaTime
        deltaTime = elapsedTime.seconds();

        // Reset elapsedTime to 0 to time next loop()
        elapsedTime.reset();

        // Send telemetry feedback
        telemetry.clearAll();
        telemetry.addData("~Robot State Data~", "");
        telemetry.addData("deltaTime:", deltaTime);
        telemetry.addData("targetCompassMode:", targetCompassMode);
        telemetry.addData("rotationOrigin:", rotationOrigin);
        telemetry.addData("targetRotation:", targetRotation);
        telemetry.addData("rotation:", rotation);
        telemetry.addData("targetXPosition:", targetXPosition);
        telemetry.addData("targetYPosition:", targetYPosition);
        telemetry.addData("xPosition:", xPosition);
        telemetry.addData("yPosition:", yPosition);
        telemetry.addData("direction:", direction);
        telemetry.addData("speed:", speed);
        telemetry.addData("targetArmRotation:", targetArmRotation);
        telemetry.addData("armRotation:", armRotation);
        telemetry.addData("gripperRotation:", gripperRotation);
        telemetry.update();
    }


    @Override
    public void stop()
    {
        // Turn off lights
        dim.setLED(0, false);
        dim.setLED(1, false);

        // Stop all motors
        flMotor.setPower(0.0);
        frMotor.setPower(0.0);
        blMotor.setPower(0.0);
        brMotor.setPower(0.0);
        armMotor.setPower(0.0);

        // Deactivate all servos
        lArmServo.close();
        rArmServo.close();
        lGripperServo.close();
        rGripperServo.close();
    }


    // Returns robot position if arm is not rotated to pick up blocks
    public double GetGripperTargetXPosition()
    {
        if (targetArmRotation >= START_BLOCK_ARM_ROTATIONS && targetArmRotation < START_RELIC_ARM_ROTATIONS) {
            return targetXPosition + (Math.sin(targetRotation) * ((Math.sin(targetArmRotation) * 18.0) + 5.0));
        }
        else return targetXPosition;
    }

    // Returns robot position if arm is not rotated to pick up blocks
    public double GetGripperTargetYPosition()
    {
        if (targetArmRotation >= START_BLOCK_ARM_ROTATIONS && targetArmRotation < START_RELIC_ARM_ROTATIONS) {
            return targetYPosition + (Math.cos(targetRotation) * ((Math.sin(targetArmRotation) * 18.0) + 5.0));
        }
        else return targetYPosition;
    }

    // Sets robot position if arm is not rotated to pick up blocks
    public void SetGripperTargetXPosition(double gripperTargetXPosition)
    {
        if (targetArmRotation >= START_BLOCK_ARM_ROTATIONS && targetArmRotation < START_RELIC_ARM_ROTATIONS) {
            targetXPosition = gripperTargetXPosition - (Math.sin(targetRotation) * ((Math.sin(targetArmRotation) * 18.0) + 5.0));
        }
        else targetXPosition = gripperTargetXPosition;
    }

    // Sets robot position if arm is not rotated to pick up blocks
    public void SetGripperTargetYPosition(double gripperTargetYPosition)
    {
        if (targetArmRotation >= START_BLOCK_ARM_ROTATIONS && targetArmRotation < START_RELIC_ARM_ROTATIONS) {
            targetYPosition = gripperTargetYPosition - (Math.cos(targetRotation) * ((Math.sin(targetArmRotation) * 18.0) + 5.0));
        }
        else targetYPosition = gripperTargetYPosition;
    }
}