package teamcode.connor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A static-only class that handles Input from the game pad
 */
public class GamePadInput {
    /**
     * the standard drive speed multiplier when turbo mod is not active
     */
    private static final double DEFAULT_DRIVE_SPEED_MULTIPLIER = 0.5;

    public static final double ARM_DELTA = 0.1;

    /**
     * the main game pad used to control the robot
     */
    private static Gamepad gamePad;
    /**
     * the current multiplier which limits the speed of the drive motors so as to prevent the robot from accelerating too fast
     */
    private static double driveSpeedModifier;
    /**
     * if the left bumper is pressed down
     */
    private static boolean leftBumperDown;
    /**
     * if turbo mode has been activated to remove speed handicaps on the motors
     */
    private static boolean turboActive;

    static {
        gamePad = ConnorRobot.instance.gamepad1;
        driveSpeedModifier = DEFAULT_DRIVE_SPEED_MULTIPLIER;
    }

    private GamePadInput() {
        // not to be instantiated
    }

    /**
     * A method to be called from a LinearOpMode repeatedly to handle game pad input
     */
    public static void update() {

        // determines if the left bumper was pressed down this "frame" and handles it accordingly
        if (gamePad.left_bumper) {
            if (!leftBumperDown) {
                // activates turbo mode if left bumper was just pressed down
                turboActive = true;
                driveSpeedModifier = (turboActive ? DEFAULT_DRIVE_SPEED_MULTIPLIER : 1);
                turboActive = !turboActive;
            }
        } else {
            if (leftBumperDown) {
                // disables turbo mode if left bumper was just released
                leftBumperDown = false;
            }
        }
        double drive = gamePad.left_stick_y;
        double steer = gamePad.left_stick_x;
        double powerL = drive - steer;

        // clamps the power values from -1 to 1
        if (powerL > 1) {
            powerL = 1;
        } else if (powerL < -1) {
            powerL = -1;
        }
        double powerR = drive + steer;
        if (powerR > 1) {
            powerR = 1;
        } else if (powerR < -1) {
            powerR = -1;
        }
        powerL *= driveSpeedModifier;
        powerR *= driveSpeedModifier;
        HardwareManager.setDrivePower(powerL, powerR);
/*

        if(gamePad.dpad_up){
            HardwareManager.extendArm(ARM_DELTA);
        }
        else if (gamePad.dpad_down) {
            HardwareManager.extendArm(-ARM_DELTA);
        }

*/

    }

}
