package teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A static-only class that handles Input from the game pad
 */
public class GamePadInput {

    private static final double DRIVE_SPEED = 0.5;
    private static final double MINIMUM_DRIVE = 0.1;
    private static final double MINIMUM_STEER = 0.1;
    private static final double WRIST_SPEED = 0.005;
    private static final double LOW_CLIMB_POWER = 0.1;
    private static final double MAX_CLIMB_POWER = 1.0;

    /**
     * Current instance of this class in use. Singleton design.
     */
    private static GamePadInput instance;

    /**
     * the main game pad used to control the robot
     */
    private Gamepad gamePad;
    private double wristPos;

    public static void init() {
        instance = new GamePadInput();
    }

    private GamePadInput() {
        gamePad = ConnorRobot.instance.gamepad1;
        wristPos = 0.0;
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
            intakeUpdate();
        }
        if (HardwareManager.CLIMB_ENABLED) {
            climbUpdate();
        }
    }

    /**
     * Makes the robot drive based on input.
     */
    private static void driveUpdate() {
        double y = instance.gamePad.left_stick_y;
        double drive = Math.abs(y) < MINIMUM_DRIVE ? 0.0 : y;
        double x = instance.gamePad.left_stick_x;
        double steer = Math.abs(x) < MINIMUM_STEER ? 0.0 : x;
        double powerL = drive - steer;
        // clamps the power values from -1 to 1
        if (powerL > 1.0) {
            powerL = 1.0;
        } else if (powerL < -1.0) {
            powerL = -1.0;
        }
        powerL *= DRIVE_SPEED;
        double powerR = drive + steer;
        // clamps the power values from -1 to 1
        if (powerR > 1.0) {
            powerR = 1.0;
        } else if (powerR < -1.0) {
            powerR = -1.0;
        }
        powerR *= DRIVE_SPEED;
        ConnorRobot.instance.telemetry.addData("x", x);
        ConnorRobot.instance.telemetry.addData("y", y);
        ConnorRobot.instance.telemetry.update();
        HardwareManager.setDrivePower(powerL, powerR);
    }

    private static void armUpdate() {
        double armServoBasePos = 0.5;
        if (instance.gamePad.dpad_up) { // extend
            armServoBasePos = 0.0;
        } else if (instance.gamePad.dpad_down) { // retract
            armServoBasePos = 1.0;
        }
        HardwareManager.setArmServoBasePosition(armServoBasePos);
        double y = instance.gamePad.right_stick_y;
        if (y > 0) {
            instance.wristPos -= WRIST_SPEED;
        } else if (y < 0) {
            instance.wristPos += WRIST_SPEED;
        }
        if (instance.wristPos > 1.0) {
            instance.wristPos = 1.0;
        } else if (instance.wristPos < 0.0) {
            instance.wristPos = 0.0;
        }
        HardwareManager.setArmServoTopPosition(instance.wristPos);
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
        HardwareManager.setArmServoIntakePosition(intakePos);
    }

    private static void climbUpdate() {
        double climbPower;
        if (instance.gamePad.a) { // up
            climbPower = MAX_CLIMB_POWER;
        } else if (instance.gamePad.a && instance.gamePad.right_bumper) { // down
            climbPower = MAX_CLIMB_POWER;
        } else {
            climbPower = 0.0;
        }
        HardwareManager.setClimbMotorPower(climbPower);
    }

}
