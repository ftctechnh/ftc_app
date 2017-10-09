package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * This class manages the teleop driver controlled period.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends OpMode {

    DriveController driveController;

    @Override
    public void init() {
        DriveController driveController = new DriveController(hardwareMap, null);
    }

    @Override
    public void loop() {

        //GAMEPAD 1

        float game1StickX = gamepad1.right_stick_y;
        float game1StickY = gamepad1.left_stick_y;
        boolean game1A = gamepad1.a;
        boolean game1B = gamepad1.b;
        boolean game1X = gamepad1.x;
        boolean game1Y = gamepad1.y;
        float game1LT = gamepad1.left_trigger;
        float game1RT = gamepad1.right_trigger;
        boolean game1LB = gamepad1.left_bumper;
        boolean game1RB = gamepad1.right_bumper;
        boolean game1Up = gamepad1.dpad_up;
        boolean game1Down = gamepad1.dpad_down;
        boolean game1Right = gamepad1.dpad_right;
        boolean game1Left = gamepad1.dpad_left;

        // GAMEPAD 2

        float game2StickX = gamepad2.right_stick_y;
        float game2StickY = gamepad2.left_stick_y;
        boolean game2A = gamepad2.a;
        boolean game2B = gamepad2.b;
        boolean game2X = gamepad2.x;
        boolean game2Y = gamepad2.y;
        float game2LT = gamepad2.left_trigger;
        float game2RT = gamepad2.right_trigger;
        boolean game2LB = gamepad2.left_bumper;
        boolean game2RB = gamepad2.right_bumper;
        boolean game2Up = gamepad2.dpad_up;
        boolean game2Down = gamepad2.dpad_down;
        boolean game2Right = gamepad2.dpad_right;
        boolean game2Left = gamepad2.dpad_left;

        game1StickX = Range.clip(game1StickX, -1, 1);
        game1StickY = Range.clip(game1StickY, -1, 1);

        // scale the joystick value to make it easier to control at lower speeds
        game1StickX = (float) scaleInput(-game1StickX);
        game1StickY = (float) scaleInput(-game1StickY);

        // MOTORS

        driveController.motorRightF.setPower(game1StickX * 0.4);
        driveController.motorRightB.setPower(game1StickX * 0.4);
        driveController.motorLeftF.setPower(game1StickY * 0.4);
        driveController.motorLeftB.setPower(game1StickY * 0.4);

    }

    //SCALE MOTOR POWERS
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}


