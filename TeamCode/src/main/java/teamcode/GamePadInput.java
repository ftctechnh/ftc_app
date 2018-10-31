package teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A static-only class that handles Input from the game pad
 */
public class GamePadInput {

    /**
     * The amount that the motor at the base of the arm moves during an update.
     */
    public static final int ARM_MOTOR_TICKS = 10;
    /**
     * The speed at which the arm extends.
     */
    public static final double ARM_EXTEND_SPEED = 1.0;
    /**
     * Speed of the intake servo.
     */
    public static final double INTAKE_SPEED = 10;

    /**
     * Current instance of this class in use. Singleton design.
     */
    private static GamePadInput instance;

    /**
     * the main game pad used to control the robot
     */
    private Gamepad gamePad;
    /**
     * Value representing how far the arm has extended from 0.0 (fully retracted) to 1.0 (fully extended).
     */
    private double armExtention;

    public static void init() {
        instance = new GamePadInput();
    }

    private GamePadInput() {
        gamePad = ConnorRobot.instance.gamepad1;
        armExtention = 0.0;
    }

    /**
     * A method to be called from a LinearOpMode repeatedly to handle input from the game pad
     */
    public static void update() {
        if (HardwareManager.DRIVE_ENABLED) {
            driveUpdate();
        }
        if (HardwareManager.ARM_ENABLED) {
            armUpdate();
        }
    }

    private static void armUpdate() {
        if (instance.gamePad.a) { // extend
            instance.armExtention += ARM_EXTEND_SPEED * ConnorRobot.MILIS_PER_TICK;
            if (instance.armExtention > 1.0) {
                instance.armExtention = 1.0;
            }
        } else if (instance.gamePad.b) { // retract
            instance.armExtention -= ARM_EXTEND_SPEED * ConnorRobot.MILIS_PER_TICK;
            if (instance.armExtention < -1.0) {
                instance.armExtention = -1.0;
            }
        }
        double motorBaseRot = 0.5 * instance.armExtention;
        double servoBasePos = 0;
        double servoMiddlePos = 0;
        double servoTopPos = 0;
        HardwareManager.setArmMotorBasePosition(motorBaseRot);
        HardwareManager.setArmServoBasePosition(servoBasePos);
        HardwareManager.setArmServoMiddlePosition(servoMiddlePos);
        HardwareManager.setArmServoTopPosition(servoTopPos);
    }

    /**
     * Makes the robot drive based on input.
     */
    private static void driveUpdate() {
        double drive = instance.gamePad.left_stick_y;
        double steer = instance.gamePad.left_stick_x;
        double powerL = drive - steer;
        // clamps the power values from -1 to 1
        if (powerL > 1.0) {
            powerL = 1.0;
        } else if (powerL < -1.0) {
            powerL = -1.0;
        }
        double powerR = drive + steer;
        // clamps the power values from -1 to 1
        if (powerR > 1.0) {
            powerR = 1.0;
        } else if (powerR < -1.0) {
            powerR = -1.0;
        }
        HardwareManager.setDrivePower(powerL, powerR);
    }

}
