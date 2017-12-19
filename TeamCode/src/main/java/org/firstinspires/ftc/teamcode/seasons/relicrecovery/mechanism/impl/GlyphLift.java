package org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;

/**
 * The glyph lift mechanism collects glyphs with two grippers and is able to place them in the cryptobox.
 */

public class GlyphLift implements IMechanism {

    private static final double MAX_LIFT_ROTATION_MOTOR_POWER = 0.4;
    private static final double MAX_LIFT_MOTOR_POWER_UP = 0.4;
    private static final double MAX_LIFT_MOTOR_POWER_DOWN = 0.9;


    private static final double ROTATION_MOTOR_POWER_AUTOMATIC = 0.8;
    private static final double ROTATION_MOTOR_POWER_MANUAL = 0.4;

    private static final double ROTATION_MOTOR_GYRO_POWER = 0.8;

    private static final int ROTATION_MOTOR_POSITION_THRESHOLD = 20;

    public static final int LIFT_RAISE_TARGET_POSITION = 850;
    public static final int LIFT_LOWER_TARGET_POSITION = -850;

    private OpMode opMode;

    private DcMotor liftMotor;
    public DcMotor rotationMotor;

    private Servo redLeftServo, redRightServo, blueLeftServo, blueRightServo;

    private boolean isRunningToPositionRotationMotor;
    private boolean isRunningToPositionLiftMotor;

    private RotationMotorPosition previousPosition;

//    private RotationMotorPosition currentRotationPosition = UNDEFINED;

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
     */
    public enum RotationMotorPosition {
        UP(0), DOWN(1700), LEFT(850), RIGHT(-850);

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
        rotationMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
        liftMotor.setPower(-power * (power < 0 ? MAX_LIFT_MOTOR_POWER_UP : MAX_LIFT_MOTOR_POWER_DOWN));
    }

    /**
     * Set the power of the rotation motor. The rotation motor is the motor that rotates the glyph grippers.
     *
     * @param power the power to run the rotation motor in a range of 1.0 to -1.0.
     *              Negative values run the rotation motor in the counterclockwise direction, and
     *              likewise, positive values run the rotation motor clockwise.
     */
    public void setRotationMotorPower(double power) {
        opMode.telemetry.addData("current position", rotationMotor.getCurrentPosition());
        opMode.telemetry.update();

        rotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        isRunningToPositionRotationMotor = false;

        rotationMotor.setPower(0);
        rotationMotor.setPower(power * ROTATION_MOTOR_POWER_MANUAL);
    }

    /**
     * Rotate the rotation motor to the requested rotation position.
     * This method is implemented so as not to tangle or over-wrap the I2c cable around the motor shaft.
     *
     * @see RotationMotorPosition
     * @param requestedPosition the desired rotation position
     */
      public void setRotationMotorPosition(RotationMotorPosition requestedPosition) {
          opMode.telemetry.addData("previous Position", previousPosition);
          opMode.telemetry.addData("current position", rotationMotor.getCurrentPosition());
          opMode.telemetry.update();

          if(previousPosition != requestedPosition) {
              if (!isRunningToPositionRotationMotor) {
                  rotationMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                  rotationMotor.setTargetPosition(requestedPosition.encoderPosition);
                  rotationMotor.setPower(ROTATION_MOTOR_POWER_AUTOMATIC);
                  isRunningToPositionRotationMotor = true;
              } else if (!rotationMotor.isBusy()) {
                  this.previousPosition = requestedPosition;
                  isRunningToPositionRotationMotor = false;
                  rotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                  rotationMotor.setPower(0);
              }
          }
      }

      public boolean setLiftPosition(int targetPosition) {
          if (!isRunningToPositionLiftMotor) {
              liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
              liftMotor.setTargetPosition(targetPosition);
              liftMotor.setPower(MAX_LIFT_MOTOR_POWER_DOWN );
              isRunningToPositionLiftMotor = true;
          } else if (!liftMotor.isBusy()) {
              isRunningToPositionLiftMotor = false;
              liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
              liftMotor.setPower(0);
          }
          return liftMotor.isBusy();
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
        blueRightServo.setPosition(0.4);
    }

    /**
     * Open the red gripper.
     */
    public void openRedGripper() {
        redRightServo.setPosition(0.9);
        redLeftServo.setPosition(0.1);
    }

    /**
     * Open the blue gripper.
     */
    public void openBlueGripper() {
        blueLeftServo.setPosition(0.9);
        blueRightServo.setPosition(0.1);
    }
}
