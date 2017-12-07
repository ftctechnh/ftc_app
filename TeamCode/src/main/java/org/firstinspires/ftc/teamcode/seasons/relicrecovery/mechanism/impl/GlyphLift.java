package org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;

import static org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift.RotationMotorPosition.DOWN;
import static org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift.RotationMotorPosition.LEFT;
import static org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift.RotationMotorPosition.RIGHT;
import static org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift.RotationMotorPosition.UNDEFINED;
import static org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift.RotationMotorPosition.UP;

/**
 * The glyph lift mechanism collects glyphs with two grippers and is able to place them in the cryptobox.
 */

public class GlyphLift implements IMechanism {

    private static final double MAX_LIFT_ROTATION_MOTOR_POWER = 0.4;
    private static final double MAX_LIFT_MOTOR_POWER_UP = 0.9;
    private static final double MAX_LIFT_MOTOR_POWER_DOWN = 0.4;

    private static final double ROTATION_MOTOR_GYRO_POWER = 0.1;
    private static final int ROTATION_MOTOR_POSITION_THRESHOLD = 20;

    private OpMode opMode;

    private DcMotor liftMotor;
    public DcMotor rotationMotor;

    private Servo redLeftServo, redRightServo, blueLeftServo, blueRightServo;

    private RotationMotorPosition currentRotationPosition = UNDEFINED;

    /**
     * This enumeration type represents the rotation position of the rotation motor,
     * which can be one of four values:
     * <ul>
     *   <li>UP (with the blue gripper on the top)</li>
     *   <li>DOWN (with the blue gripper on the bottom)</li>
     *   <li>LEFT (with the red gripper to the right)</li>
     *   <li>RIGHT (with the red gripper to the left)</li>
     * </ul>
     *
     * The additional value UNDEFINED is implementation specific and should never be passed to
     * {@link #setRotationMotorPosition(RotationMotorPosition)} and will never be
     * returned by {@link #getCurrentRotationMotorPosition()}.
     *
     */
    public enum RotationMotorPosition {
        UP(0), DOWN(1650), LEFT(825), RIGHT(-825), UNDEFINED(-1);

        private int encoderPosition;

        RotationMotorPosition(int pos) {
            this.encoderPosition = pos;
        }
    }

    /**
     * Construct a new {@link GlyphLift} with a reference to the utilizing robot.
     *
     * @param robot the robot using this glyph lift
     */
    public GlyphLift(Robot robot) {
        this.opMode = robot.getCurrentOpMode();
        HardwareMap hwMap = opMode.hardwareMap;

        this.liftMotor = hwMap.dcMotor.get("lift");
        this.rotationMotor = hwMap.dcMotor.get("rot");

        this.blueLeftServo = hwMap.servo.get("bgl");
        this.blueRightServo = hwMap.servo.get("bgr");
        this.redLeftServo = hwMap.servo.get("rgl");
        this.redRightServo = hwMap.servo.get("rgr");

        // reverse lift motor
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // brake both motors
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rotationMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // run using encoders
        rotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * This method sets the power of the lift motor.
     * The lift motor is the motor that raises and lowers the linear slides.
     *
     * @param power the power to run the lift motor in a range of 1.0 to -1.0.
     *              Negative values lower the lift and, likewise, positive values raise the lift.
     */
    public void setLiftMotorPower(double power) {
        liftMotor.setPower(power * (power < 0 ? MAX_LIFT_MOTOR_POWER_UP : MAX_LIFT_MOTOR_POWER_DOWN));
    }

    /**
     * Set the power of the rotation motor. The rotation motor is the motor that rotates the glyph grippers.
     *
     * @param power the power to run the rotation motor in a range of 1.0 to -1.0.
     *              Negative values run the rotation motor in the counterclockwise direction, and
     *              likewise, positive values run the rotation motor clockwise.
     */
    public void setRotationMotorPower(double power) {
        rotationMotor.setPower(power * MAX_LIFT_ROTATION_MOTOR_POWER);
    }

    /**
     * Get the current position of the rotation motor.
     *
     * @see RotationMotorPosition for what this enumeration type represents
     * @return the current rotation position of the rotation motor
     */
    public RotationMotorPosition getCurrentRotationMotorPosition() {
        int currentEncoderPos = rotationMotor.getCurrentPosition();
        int rotationEncoderPos;

        for (RotationMotorPosition pos : RotationMotorPosition.values()) {
            rotationEncoderPos = pos.encoderPosition;

            if(pos == DOWN) {
                rotationEncoderPos = Math.abs(rotationEncoderPos);
            }

            if (Math.abs(rotationEncoderPos - currentEncoderPos) < ROTATION_MOTOR_POSITION_THRESHOLD) {
                return pos;
            }
        }
        // just return undefined if rotation motor not at any of the positions
        return RotationMotorPosition.UNDEFINED;
    }

    /**
     * Rotate the rotation motor to the requested rotation position.
     * This method is implemented so as not to tangle or over-wrap the I2c cable around the motor shaft.
     *
     * @see RotationMotorPosition
     * @param requestedPosition the desired rotation position
     */
    public void setRotationMotorPosition(RotationMotorPosition requestedPosition) {
        // only update current position if it's not undefined
        RotationMotorPosition tempPos = getCurrentRotationMotorPosition();
        if(tempPos != UNDEFINED) {
            this.currentRotationPosition = tempPos;
        }

        opMode.telemetry.addData("current encoder position", rotationMotor.getCurrentPosition());
        opMode.telemetry.addData("current rotation position", currentRotationPosition);
        opMode.telemetry.update();

        if(currentRotationPosition != requestedPosition) {
            switch (requestedPosition) {
                case UP:
                    // always use negative motor power when requested position is up
                    rotationMotor.setPower(-ROTATION_MOTOR_GYRO_POWER);
                    break;
                case DOWN:
                    // always use positive motor power when requested position is down
                    rotationMotor.setPower(ROTATION_MOTOR_GYRO_POWER);
                    break;
                case LEFT:
                    if (currentRotationPosition == DOWN || currentRotationPosition == RIGHT) {
                        // positive motor power
                        //rotationMotor.setPower(ROTATION_MOTOR_GYRO_POWER);
                    } else if (currentRotationPosition == UP) {
                        // negative motor power
                        rotationMotor.setPower(-ROTATION_MOTOR_GYRO_POWER);
                    }
                    break;
                case RIGHT:
                    if (currentRotationPosition == DOWN || currentRotationPosition == LEFT) {
                        // negative motor power
                        rotationMotor.setPower(-ROTATION_MOTOR_GYRO_POWER);
                    } else if (currentRotationPosition == UP) {
                        // positive motor power
                        rotationMotor.setPower(ROTATION_MOTOR_GYRO_POWER);
                    }
                    break;
            }
        } else {
            rotationMotor.setPower(0);
        }
    }

    /**
     * Set the grippers to their initialized positions.
     * This method should be called during initialization.
     */
    public void initializeGrippers() {
        blueLeftServo.setPosition(1.0);
        blueRightServo.setPosition(0);
        redRightServo.setPosition(1.0);
        redLeftServo.setPosition(0);
    }

    /**
     * Close the red gripper.
     */
    public void closeRedGripper() {
        redLeftServo.setPosition(0.35);
        redRightServo.setPosition(0.65);
    }

    /**
     * Close the blue gripper.
     */
    public void closeBlueGripper() {
        blueLeftServo.setPosition(0.65);
        blueRightServo.setPosition(0.35);
    }

    /**
     * Open the red gripper.
     */
    public void openRedGripper() {
        redRightServo.setPosition(0.8);
        redLeftServo.setPosition(0.2);
    }

    /**
     * Open the blue gripper.
     */
    public void openBlueGripper() {
        blueLeftServo.setPosition(0.8);
        blueRightServo.setPosition(0.2);
    }
}
