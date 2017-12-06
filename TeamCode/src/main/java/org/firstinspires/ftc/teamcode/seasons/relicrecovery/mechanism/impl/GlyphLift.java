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
 *
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
     *
     */
    public enum RotationMotorPosition {
        UP(0), DOWN(1650), LEFT(825), RIGHT(-825), UNDEFINED(-1);

        public int pos;

        RotationMotorPosition(int pos) {
            this.pos = pos;
        }
    }

    /**
     *
     * @param robot
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
     *
     * @param power
     */
    public void setLiftMotorPower(double power) {
        if(power < 0) {
            power *= MAX_LIFT_MOTOR_POWER_UP;
        } else {
            power *= MAX_LIFT_MOTOR_POWER_DOWN;
        }

        liftMotor.setPower(power);
    }

    /**
     *
     * @param power
     */
    public void setRotationMotorPower(double power) {
        rotationMotor.setPower(power * MAX_LIFT_ROTATION_MOTOR_POWER);
    }

    /**
     *
     * @return
     */
    public RotationMotorPosition getCurrentRotationMotorPosition() {
        int currentPos = rotationMotor.getCurrentPosition();
        for (RotationMotorPosition pos : RotationMotorPosition.values()) {
            // if rotation motor position is within 5 degrees of pos
            if (Math.abs(pos.pos - currentPos) < ROTATION_MOTOR_POSITION_THRESHOLD) {
                return pos;
            }
        }
        // just return undefined if rotation motor not at any of the positions
        return RotationMotorPosition.UNDEFINED;
    }

    /**
     *
     * @param requestedPosition
     */
    public void setRotationMotorPosition(RotationMotorPosition requestedPosition) {
        int headingDiff = Math.abs(requestedPosition.pos - rotationMotor.getCurrentPosition());

        // only update current position if it's not undefined
        RotationMotorPosition tempPos = getCurrentRotationMotorPosition();
        if(tempPos != UNDEFINED) {
            this.currentRotationPosition = tempPos;
        }

        opMode.telemetry.addData("current encoder pos", rotationMotor.getCurrentPosition());
        opMode.telemetry.addData("pos diff", headingDiff);
        opMode.telemetry.addData("current rotation position", currentRotationPosition);
        opMode.telemetry.update();

        if(headingDiff > ROTATION_MOTOR_POSITION_THRESHOLD
                && currentRotationPosition != requestedPosition) {
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

    public void closeRedGripper() {
        redLeftServo.setPosition(0.35);
        redRightServo.setPosition(0.65);
    }

    public void closeBlueGripper() {
        blueLeftServo.setPosition(0.65);
        blueRightServo.setPosition(0.35);
    }

    public void openRedGripper() {
        redRightServo.setPosition(0.8);
        redLeftServo.setPosition(0.2);
    }

    public void openBlueGripper() {
        blueLeftServo.setPosition(0.8);
        blueRightServo.setPosition(0.2);
    }

    public void initializeGrippers() {
        blueLeftServo.setPosition(1.0);
        blueRightServo.setPosition(0);
        redRightServo.setPosition(1.0);
        redLeftServo.setPosition(0);
    }
}
