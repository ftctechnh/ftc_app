/*
 * Copyright (C) 2015 Powerstackers
 *
 * Teleop code for Res-Q.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.powerstackers.resq.opmodes.teleop;

import com.powerstackers.resq.common.Robot;
import com.powerstackers.resq.common.RobotConstants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

import static com.powerstackers.resq.common.enums.PublicEnums.AllianceColor;
import static com.powerstackers.resq.common.enums.PublicEnums.DoorSetting;
import static com.powerstackers.resq.common.enums.PublicEnums.MotorSetting;

/**
 * This is the opmode for use on our competition robot during teleop.
 * @author Jonathan Thomas
 */
@TeleOp(name = "Res-Q Tele-op", group = "Powerstackers")
@Disabled
public class ResqTeleop extends OpMode {

    private static final float MINIMUM_JOYSTICK_THRESHOLD = 0.15F;

    AllianceColor allianceColor;

    Robot robot;

    float stickDriveLeft;
    float stickDriveRight;
    float stickWinch;
    MotorSetting settingTapeMeasureServo;
    MotorSetting settingLiftMotor = MotorSetting.STOP;
    MotorSetting settingBrushMotor = MotorSetting.STOP;
    double tapeTiltPosition = RobotConstants.TAPE_FLAT;
    final double servoDelta = 0.005;

    boolean buttonLiftOut;
    boolean buttonLiftIn;
    boolean buttonHangOut;
    boolean buttonHangIn;
    boolean buttonBrushOn;
    boolean buttonBrushRev;

    boolean buttonTapeOut;
    boolean buttonTapeIn;
    boolean buttonTapeUp;
    boolean buttonTapeDown;
    boolean buttonHopperLeft;
    boolean buttonHopperRight;
    boolean buttonClimbers;
    boolean buttonChurros;
    boolean buttonBothHoppers;

    /**
     * Generate a new Teleop program with the given alliance color.
     * @param allianceColor The color that we are playing as this round.
     */
    public ResqTeleop(AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
    }

    /**
     * Initialize the robot.
     */
    @Override
    public void init() {
        robot = new Robot(this);
    }

    /**
     * Called continuously while the robot runs.
     */
    @Override
    public void loop() {

        // Read the joystick and determine what motor setting to use.
        stickDriveLeft  = (float) scaleInput(Range.clip(-gamepad1.left_stick_y, -1, 1));
        stickDriveRight = (float) scaleInput(Range.clip(-gamepad1.right_stick_y, -1, 1));
        stickWinch      = (float) scaleInput(Range.clip(-gamepad2.left_stick_y, -1, 1));

        // Neatly read all the button assignments for clarity purposes.
        buttonLiftOut     = gamepad1.left_bumper;
        buttonLiftIn      = gamepad1.right_bumper;
        buttonHangIn      = gamepad1.dpad_down;
        buttonHangOut     = gamepad1.dpad_up;

        // Everyone wants the manipulator to control the brush
        buttonBrushOn     = gamepad2.x;
        buttonBrushRev    = gamepad2.b;

        buttonTapeOut     = gamepad2.dpad_right;
        buttonTapeIn      = gamepad2.dpad_left;
        buttonTapeUp      = gamepad2.dpad_up;
        buttonTapeDown    = gamepad2.dpad_down;

        buttonHopperLeft  = gamepad2.left_bumper;
        buttonHopperRight = gamepad2.right_bumper;
        buttonClimbers    = gamepad2.right_trigger > 0.5;
        buttonChurros     = gamepad1.right_trigger > 0.5;

        buttonBothHoppers = gamepad2.right_bumper;

        // Set the lift motor value.
        if (buttonLiftOut) {
            settingLiftMotor = MotorSetting.REVERSE;
        } else if (buttonLiftIn) {
            settingLiftMotor = MotorSetting.FORWARD;
        } else {
            settingLiftMotor = MotorSetting.STOP;
        }

        // Set the brush motor value.
        if (buttonBrushOn) {
            settingBrushMotor = MotorSetting.FORWARD;
        } else if (buttonBrushRev) {
            settingBrushMotor = MotorSetting.REVERSE;
        } else {
            settingBrushMotor = MotorSetting.STOP;
        }

        // Set the tape measure value.
        if (buttonTapeOut) {
            settingTapeMeasureServo = MotorSetting.REVERSE;
        } else if (buttonTapeIn) {
            settingTapeMeasureServo = MotorSetting.FORWARD;
        } else {
            settingTapeMeasureServo = MotorSetting.STOP;
        }

        // Increment or decrement the tape measure tipper
        if (buttonTapeUp) {
            tapeTiltPosition += servoDelta;
            Range.clip(tapeTiltPosition, 0.0, 1.0);
            robot.setTapeTilt(tapeTiltPosition);
        } else if (buttonTapeDown) {
            tapeTiltPosition -= servoDelta;
            Range.clip(tapeTiltPosition, 0.0, 1.0);
            robot.setTapeTilt(tapeTiltPosition);
        }

        // Set the hopper doors.
        // Hopper left
        if (buttonHopperLeft) {
            robot.setHopperLeft(DoorSetting.OPEN);
        } else {
            robot.setHopperLeft(DoorSetting.CLOSE);
        }
        // Hopper right
        if (buttonHopperRight) {
            robot.setHopperRight(DoorSetting.OPEN);
        } else {
            robot.setHopperRight(DoorSetting.CLOSE);
        }

//        if (buttonBothHoppers) {
//            robot.setAllianceHopper(DoorSetting.OPEN, allianceColor);
//        } else {
//            robot.setAllianceHopper(DoorSetting.CLOSE, allianceColor);
//        }

        // Set the climber flipper value.
        if (buttonClimbers) {
            robot.setClimberFlipper(DoorSetting.OPEN);
        } else {
            robot.setClimberFlipper(DoorSetting.CLOSE);
        }

        // Set the churro grabber value
        // NOTE: We want the grabbers to go up when the driver holds the button.
        if (buttonChurros) {
            robot.setChurroGrabbers(DoorSetting.OPEN);
        } else {
            robot.setChurroGrabbers(DoorSetting.CLOSE);
        }

        // Last of all, update the motor values.
        // Left drive motors
        // TODO Motors are backwards.
        if (absoluteValue(stickDriveLeft) > MINIMUM_JOYSTICK_THRESHOLD) {
            robot.setPowerRight(-stickDriveLeft);
        } else {
            robot.setPowerRight(0);
        }

        // Right drive motors
        if (absoluteValue(stickDriveRight) > MINIMUM_JOYSTICK_THRESHOLD) {
            robot.setPowerLeft(-stickDriveRight);
        } else {
            robot.setPowerLeft(0);
        }

        // Winch motor control
        if (stickWinch > MINIMUM_JOYSTICK_THRESHOLD) {
            robot.setWinch(MotorSetting.FORWARD);
        } else if (stickWinch < -MINIMUM_JOYSTICK_THRESHOLD) {
            robot.setWinch(MotorSetting.REVERSE);
        } else {
            robot.setWinch(MotorSetting.STOP);
        }

        robot.setTapeMeasure(settingTapeMeasureServo);
        robot.setLift(settingLiftMotor);
        robot.setBrush(settingBrushMotor);

        telemetry.addData("right stick", stickDriveRight);
        telemetry.addData("left stick ", stickDriveLeft);
        telemetry.addData("tape tilt servo", tapeTiltPosition);
//        telemetry.addData("hopper tilt pos", robot.hopperTiltPosition);
    }

    /**
     * Stop the robot and make any final assignments.
     */
    @Override
    public void stop() {
        // Engage the churro grabbers when the match ends.
        robot.setChurroGrabbers(DoorSetting.CLOSE);
    }

    /**
     * Return the absolute value of a float.
     * @param in Floating point number.
     * @return Positive float.
     */
    private float absoluteValue(float in) {
        if (in > 0) {
            return in;
        } else {
            return -in;
        }
    }

    /**
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

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
        double dScale;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
