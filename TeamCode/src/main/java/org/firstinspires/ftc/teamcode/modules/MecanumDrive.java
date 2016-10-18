package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by billcipher1344 on 10/17/2016.
 */
public final class MecanumDrive {
final static float JOYSTICK_DEADZONE = .1F;

    public static void straight(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);

    }

    /**
     * shifts the robot left or right
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed positive speed will make it move right, negative will make it move left
     */
    public static void shift(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(speed);
        frontRight.setPower(-speed);
        backLeft.setPower(-speed);
        backRight.setPower(speed);

    }

    /**
     * moves the robot diagonally toward the right
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed positive speed makes it move to the front right, negative to the back right
     */
    public static void diagonalRight(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(speed);
        backRight.setPower(speed);
        frontRight.setPower(0);
        backLeft.setPower(0);
    }

    /**
     * moves the robot diagonally to the left
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed pos to the front left, neg to the back left
     */
    public static void diagonalLeft(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
    }

    /**
     * this is concerning...
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed positive speed makes the back right concerned, negative speed concerns the front
     */
    public static void concerningRight(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(speed);
        frontRight.setPower(0);
        backLeft.setPower(speed);
        backRight.setPower(0);

    }

    /**
     * this is concerning...
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed positive speed makes the back right concerned, negative speed concerns the front
     */
    public static void concerningLeft(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(0);
        frontRight.setPower(speed);
        backLeft.setPower(0);
        backRight.setPower(speed);

    }

    /**
     * turn round baby right round
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed pos turns right, neg turns left
     */
    public static void turn(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(speed);
        frontRight.setPower(-speed);
        backLeft.setPower(speed);
        backRight.setPower(-speed);

    }

    /**
     * turns the rear axis
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed pos turns right, neg turns left
     */
    public static void turnRear(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(speed);
        frontRight.setPower(-speed);
        backLeft.setPower(0);
        backRight.setPower(0);

    }

    /**
     * turns the front axis
     * @param frontLeft
     * @param frontRight
     * @param backLeft
     * @param backRight
     * @param speed pos turns right, neg turns left
     */
    public static void turnFront(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double speed){

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(speed);
        backRight.setPower(-speed);

    }

    public static void loop(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, GamepadV2 gamepad){
        gamepad.setJoystickDeadzone(JOYSTICK_DEADZONE);

        if (Math.abs(gamepad.left_stick_x) < JOYSTICK_DEADZONE && Math.abs(gamepad.left_stick_y) < JOYSTICK_DEADZONE && Math.abs(gamepad.right_stick_x) < JOYSTICK_DEADZONE && Math.abs(gamepad.right_stick_y) < JOYSTICK_DEADZONE ){
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }

        else if ( Math.abs(gamepad.left_stick_y) < JOYSTICK_DEADZONE && Math.abs(gamepad.right_stick_y) < JOYSTICK_DEADZONE && gamepad.right_stick_y*gamepad.left_stick_y > 0){
            float power = (gamepad.left_stick_x + gamepad.right_stick_x)/2;
            shift(frontLeft, frontRight, backLeft, backRight, power);
        }

        else if ((gamepad.left_stick_x >= JOYSTICK_DEADZONE && gamepad.left_stick_y >= JOYSTICK_DEADZONE) || (gamepad.right_stick_x <= -JOYSTICK_DEADZONE && gamepad.right_stick_y <= -JOYSTICK_DEADZONE)){
            float power = (float)Math.sqrt(Math.pow(((gamepad.left_stick_x + gamepad.right_stick_x)/2), 2) + Math.pow(((gamepad.left_stick_y + gamepad.right_stick_y) / 2), 2));
            diagonalRight(frontLeft, frontRight, backLeft, backRight, power);
        }

        else if ((gamepad.left_stick_x <= -JOYSTICK_DEADZONE && gamepad.left_stick_y >= JOYSTICK_DEADZONE) || (gamepad.right_stick_x >= JOYSTICK_DEADZONE && gamepad.right_stick_y <= -JOYSTICK_DEADZONE)) {
            float power = (float) Math.sqrt(Math.pow(((gamepad.left_stick_x + gamepad.right_stick_x) / 2), 2) + Math.pow(((gamepad.left_stick_y + gamepad.right_stick_y) / 2), 2));
            diagonalRight(frontLeft, frontRight, backLeft, backRight, power);
        }

        else{
            frontLeft.setPower(gamepad.left_stick_y_exponential(1));
            backLeft.setPower(gamepad.left_stick_y_exponential(1));
            frontRight.setPower(gamepad.right_stick_y_exponential(1));
            backRight.setPower(gamepad.right_stick_y_exponential(1));
        }

    }



}
