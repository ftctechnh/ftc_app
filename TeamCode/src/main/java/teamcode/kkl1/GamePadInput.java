package teamcode.kkl1;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A static-only class that handles Input from the game pad.
 */
public class GamePadInput {

    private static final double DEFAULT_DRIVE_SPEED = 0.5;
    private static final double MAX_DRIVE_SPEED = 1.0;
    private static final double MINIMUM_DRIVE = 0.1;
    private static final double MINIMUM_STEER = 0.1;
    private static final double WRIST_SPEED = 0.005;
    private static final double LOW_LIFT_POWER = 1.0;
    private static final double MAX_LIFT_POWER = 1.0;

    /**
     * Current instance of this class in use. Singleton design.
     */
    private static GamePadInput instance;

    /**
     * the main game pad used to control the robot
     */
    private Gamepad gamePad;
    private double armWristServoPos;

    public static void init() {
        instance = new GamePadInput();
    }

    private GamePadInput() {
        gamePad = KobaltClawsTeleOp.instance.gamepad1;
        armWristServoPos = 0.0;
    }

    /**
     * A method to be called from a LinearOpMode repeatedly to handle input from the game pad
     */
    public static void update() {
        if (KKL1HardwareManager.DRIVE_ENABLED) {
            driveUpdate();
        }
        if (KKL1HardwareManager.ARM_ENABLED) {
            armUpdate();
        }
        if (KKL1HardwareManager.INTAKE_ENABLED) {
            intakeUpdate();
        }
        if (KKL1HardwareManager.LIFT_ENABLED) {
            liftUpdate();
        }
    }

    /**
     * Makes the robot drive based on input.
     */
    private static void driveUpdate() {
        double driveSpeed = instance.gamePad.left_bumper ? MAX_DRIVE_SPEED : DEFAULT_DRIVE_SPEED;
        double y = instance.gamePad.right_stick_y;
        double drive = Math.abs(y) < MINIMUM_DRIVE ? 0.0 : y;
        double x = instance.gamePad.right_stick_x;
        double steer = Math.abs(x) < MINIMUM_STEER ? 0.0 : x;
        double powerL = drive - steer;
        powerL *= driveSpeed;
        // clamps the power values from -1 to 1
        if (powerL > 1.0) {
            powerL = 1.0;
        } else if (powerL < -1.0) {
            powerL = -1.0;
        }
        double powerR = drive + steer;
        powerR *= driveSpeed;
        // clamps the power values from -1 to 1
        if (powerR > 1.0) {
            powerR = 1.0;
        } else if (powerR < -1.0) {
            powerR = -1.0;
        }
        KKL1HardwareManager.setDrivePower(powerL, powerR);
    }

    private static void armUpdate() {
        double armBaseServoPower;
        if (instance.gamePad.dpad_up) { // retract
            armBaseServoPower = 0.0;
        } else if (instance.gamePad.dpad_down) { // extend
            armBaseServoPower = 1.0;
        } else {
            armBaseServoPower = 0.5;
        }
        double y = instance.gamePad.left_stick_y;
        instance.armWristServoPos -= y * WRIST_SPEED;
        if (instance.armWristServoPos > 1.0) {
            instance.armWristServoPos = 1.0;
        } else if (instance.armWristServoPos < 0.0) {
            instance.armWristServoPos = 0.0;
        }
        KKL1HardwareManager.setArmBaseServoPower(armBaseServoPower);
        KKL1HardwareManager.setArmWristServoPosition(instance.armWristServoPos);
    }

    private static void intakeUpdate() {
        double intakePos;
        if (instance.gamePad.left_trigger > 0.5) {
            intakePos = 0.0;
        } else if (instance.gamePad.right_trigger > 0.5) {
            intakePos = 1.0;
        } else {
            intakePos = 0.5;
        }
        KKL1HardwareManager.setIntakeServoPosition(intakePos);
    }

    private static void liftUpdate() {
        double liftPower;
        if (instance.gamePad.right_bumper) {
            if (instance.gamePad.a) {
                liftPower = MAX_LIFT_POWER;
            } else if (instance.gamePad.y) {
                liftPower = -MAX_LIFT_POWER;
            } else {
                liftPower = 0.0;
            }
        } else {
            if (instance.gamePad.a) {
                liftPower = LOW_LIFT_POWER;
            } else if (instance.gamePad.y) {
                liftPower = -LOW_LIFT_POWER;
            } else {
                liftPower = 0.0;
            }
        }
        KKL1HardwareManager.setLiftMotorPower(liftPower);
    }

}
