/**
 * Created by spmce on 12/1/2015.
 */
package org.firstinspires.ftc.teamcode.ResQ;

import org.firstinspires.ftc.teamcode.ResQ.BigBerthaHardware;

/**
 * Provide telemetry provided by the BigBerthaHardware class.
 * @author SSI Robotics and revised by Shane McEnaney
 * @version 2015-08-02-13-57-----2015-12-01
 */
public class BigBerthaTelemetry extends BigBerthaHardware//omg no u
{
    /**
     * Construct the class.
     * The system calls this member when the class is instantiated.
     */
    public BigBerthaTelemetry () {
        // Initialize base classes and class members.
        // All via self-construction.
    } //--------------------------------------------------------------------------BigBerthaTelemetry
    public void init_loop () {
        /*telemetry.addData("01" , "Init Bucket Door Servo Position: "+ getBucketDoorPosition());
        telemetry.addData("02" , "Init Hook Servo Position: "       +  getHookPosition());
        telemetry.addData("03" , "Init Man Servo Position: "        + getManPosition());
        telemetry.addData("04" , "Init Left Drive Power: " + getLeftDrivePower());
        telemetry.addData("05" , "Init Right Drive Power: "+ getRightDrivePower());
        telemetry.addData("06" , "Init Lift Arm Power: "   + getLiftArmPower());
        telemetry.addData("07" , "Init Lift Power: "       + getLiftPower());
        telemetry.addData("08" , "Init Chain Hooks Power: "+ getChainHooksPower());
        telemetry.addData("09" , "Init Sweeper Power: "    + getSweeperPower());
        telemetry.addData("10" , "Init Bucket Power: "     + getBucketPower());
        telemetry.addData("11" , "Init Spinner Power: "    + getSpinnerPower());*/
        if (acceleration != null) {
            telemetry.addData("01" , "Init Acceleration: " + acceleration.getAcceleration());
            telemetry.addData("02" , "Init Acceleration Status: " + acceleration.status());
         }
        if (color != null) {
            telemetry.addData("02" , "Init Color alpha: " + color.alpha());
            telemetry.addData("03" , "Init Color argb: " + color.argb());
            telemetry.addData("04" , "Init Color blue: " + color.blue());
            telemetry.addData("05" , "Init Color green: " + color.green());
            telemetry.addData("06" , "Init Color red: " + color.red());
            telemetry.addData("07" , "Init Color I2cAddress: " + color.getI2cAddress());
        }
        /*if (color2 != null) {
            telemetry.addData("19" , "Color2 alpha: " + color2.alpha());
            telemetry.addData("20" , "Color2 argb: " + color2.argb());
            telemetry.addData("21" , "Color2 blue: " + color2.blue());
            telemetry.addData("22" , "Color2 green: " + color2.green());
            telemetry.addData("23" , "Color2 red: " + color2.red());
        }*/
        if (compass != null) {
            telemetry.addData("07" , "Init Compass Calibration Failed: " + compass.calibrationFailed());
            telemetry.addData("08" , "Init Compass Direction: " + compass.getDirection());
        }
        if (motorController!= null)
            telemetry.addData("09", "Init Motor Controller: " + motorController.getConnectionInfo());
        //if (gyro != null)
            //telemetry.addData("10", "Init Gyro: " + gyro.getRotation());
        if (ir != null)
            telemetry.addData("11", "Init IR Seeker: " + ir.getAngle());
        if (light != null)
            telemetry.addData("12" , "Init Light: " + light.getLightDetected());
        if (servoController != null)
            telemetry.addData("13" , "Init Servo Controller: " + servoController.getConnectionInfo());
        if (touch != null)
            telemetry.addData("14" , "Init Touch: " + touch.isPressed());
        if (multi != null) {
            telemetry.addData("15", "Init Touch Multiplexer: " + multi.getSwitches());
            telemetry.addData("16", "Init Touch Port 1: " + multi.isTouchSensorPressed(1));
            telemetry.addData("17", "Init Touch Port 2: " + multi.isTouchSensorPressed(2));
            telemetry.addData("18", "Init Touch Port 2: " + multi.isTouchSensorPressed(3));
            telemetry.addData("19", "Init Touch Port 4: " + multi.isTouchSensorPressed(4));
        }
        if (sonar != null)
            telemetry.addData("20" , "Init Sonar: " + sonar.getUltrasonicLevel());
    }
    /**
     * Update the telemetry with current gamepad readings.
     */
    public double zero (double value) {
        if (value == -0.0)
            return 0.0;
        return value;
    }
    /**
     * Update the telemetry with current values from the base class.
     */
    public void updateTelemetry () {
        if (getDriveWarningGenerated())
            setDriveFirstMessage(getDriveWarningMessage());
        if (getWarningGenerated())
            setFirstMessage(getWarningMessage());
        String game1 = gamepad1.toString();
        String game2 = gamepad2.toString();
        telemetry.addData("01" , "Robot:");
        if (acceleration != null) {
            telemetry.addData("02" , "Loop Acceleration: " + acceleration.getAcceleration());
            telemetry.addData("03" , "Loop Acceleration Status: " + acceleration.status());
        }
        if (color != null) {
            telemetry.addData("04" , "Loop Color alpha: " + color.alpha());
            telemetry.addData("05" , "Loop Color argb: " + color.argb());
            telemetry.addData("06" , "Loop Color blue: " + color.blue());
            telemetry.addData("07" , "Loop Color green: " + color.green());
            telemetry.addData("08" , "Loop Color red: " + color.red());
            telemetry.addData("09" , "Loop Color I2cAddress: " + color.getI2cAddress());
        }
        /*if (color2 != null) {
            telemetry.addData("19" , "Color2 alpha: " + color2.alpha());
            telemetry.addData("20" , "Color2 argb: " + color2.argb());
            telemetry.addData("21" , "Color2 blue: " + color2.blue());
            telemetry.addData("22" , "Color2 green: " + color2.green());
            telemetry.addData("23" , "Color2 red: " + color2.red());
        }*/
        if (compass != null) {
            telemetry.addData("08" , "Loop Compass Direction: " + compass.getDirection());
        }
        if (motorController!= null)
            telemetry.addData("09", "Loop Motor Controller: " + motorController.getConnectionInfo());
        //if (gyro != null)
            //telemetry.addData("10", "Loop Gyro: " + gyro.getRotation());
        if (ir != null)
            telemetry.addData("11", "Loop IR Seeker: " + ir.getAngle());
        if (light != null)
            telemetry.addData("12" , "Loop Light: " + light.getLightDetected());
        if (servoController != null)
            telemetry.addData("13" , "Loop Servo Controller: " + servoController.getConnectionInfo());
        if (touch != null)
            telemetry.addData("14" , "Loop Touch: " + touch.isPressed());
        if (multi != null) {
            telemetry.addData("15", "Loop Touch Multiplexer: " + multi.getSwitches());
            telemetry.addData("16", "Loop Touch Port 1: " + multi.isTouchSensorPressed(1));
            telemetry.addData("17", "Loop Touch Port 2: " + multi.isTouchSensorPressed(2));
            telemetry.addData("18", "Loop Touch Port 2: " + multi.isTouchSensorPressed(3));
            telemetry.addData("19", "Loop Touch Port 4: " + multi.isTouchSensorPressed(4));
        }
        if (sonar != null)
            telemetry.addData("20" , "Loop Sonar: " + sonar.getUltrasonicLevel());

        telemetry.addData("21" , "Gamepad 1 Configuration: " + game1config);
        telemetry.addData("22" ,  game1);
        telemetry.addData("23" , "Gamepad 2 Configuration: " + game2config);
        telemetry.addData("24" ,  game2);
        telemetry.addData("25" , " ");
        //telemetry.addData("07" , "Servo Position:");
        //telemetry.addData("08" , "Bucket Door Servo Position: "+ getBucketDoorPosition());
        //telemetry.addData("08" , "Climbers Servo Position: "   + getRightClimberPosition() + ", " + getLeftClimberPosition());
        //telemetry.addData("09" , "Hook Servo Position: "       + getHookPosition());
        //telemetry.addData("10" , "Man Servo Position: " + getManPosition());
        //telemetry.addData("11" , "Flag Servo Position: "       + getRightFlagPosition() + ", " + getLeftFlagPosition());
        //telemetry.addData("26" , "Motor Power:");
        //telemetry.addData("27" , "Right Drive Power: "+ getRightDrivePower()+ ", " + getRightEncoderCount());
        //telemetry.addData("28" , "Left Drive Power: " + getLeftDrivePower() + ", " + getLeftEncoderCount() + ", " + leftDrivePower);
        //telemetry.addData("29" , "Back Right Power: " + getBackRightPower() + ", " + getRightEncoderCount());
        //telemetry.addData("30" , "Back Left Power: "  + getBackLeftPower()  + ", " + getLeftEncoderCount());
        /*telemetry.addData("17" , "Right Arm Power: "  + getRightArmPower()  + ", " + getLiftArmEncoderCount());
        telemetry.addData("18" , "Left Arm Power: "   + getLeftArmPower()   + ", " + getLiftArmEncoderCount());
        telemetry.addData("19" , "Right Lift Power: " + getRightLiftPower() + ", " + getLiftEncoderCount());
        telemetry.addData("20" , "Left Lift Power: "  + getLeftLiftPower()  + ", " + getLiftEncoderCount());
        telemetry.addData("21" , "Chain Hooks Power: "+ getChainHooksPower()+ ", " + getChainHooksEncoderCount());
        telemetry.addData("22" , "Sweeper Power: "    + getSweeperPower()   + ", " + getSweeperEncoderCount());
        telemetry.addData("23" , "Bucket Power: "     + getBucketPower()    + ", " + getBucketEncoderCount());
        telemetry.addData("24" , "Spinner Power: "    + getSpinnerPower()   + ", " + getSpinnerEncoderCount());*/
        /*telemetry.addData("2401" , "Sweeper Off: "+ sweeperOff);
        telemetry.addData("2402" , "Aux 1 Scale: "+ aux1ScaleOff);
        telemetry.addData("2403" , "Left Drive Off: "+ leftDriveOff);
        telemetry.addData("2404" , "Right Drive Off: "+ rightDriveOff);
        telemetry.addData("2405" , "Back Left Drive Off: "+ backLeftOff);
        telemetry.addData("2406" , "Back Right Drive Off: "+ backRightOff);
        telemetry.addData("2407" , "Full Drive Off: "+ fullDriveOff);
        telemetry.addData("2408" , "Front Drive Off: "+ driveOff);
        telemetry.addData("2409" , "Back Drive Off: "+ backDriveOff);
        telemetry.addData("2410" , "Left Fast Drive Off: "+ leftFastDriveOff);
        telemetry.addData("2411" , "Right Fast Drive Off: "+ rightFastDriveOff);
        telemetry.addData("2412" , "Fast Drive Off: "+ fastDriveOff);
        telemetry.addData("2410" , "Left Slow Drive Off: " + leftSlowDriveOff);
        telemetry.addData("2411" , "Right Slow Drive Off: "+ rightSlowDriveOff);
        telemetry.addData("2412" , "Slow Drive Off: "      + slowDriveOff);
        telemetry.addData("2413" , "Left Climber Off: "    + leftClimberOff);
        telemetry.addData("2414" , "Right Climber Off: "   + rightClimberOff);
        telemetry.addData("2415" , "Left Arm Off: "        + leftArmOff);
        telemetry.addData("2416" , "Right Arm Off: "       + rightArmOff);
        telemetry.addData("2417" , "Left Lift Off: "       + leftLiftOff);
        telemetry.addData("2418" , "Right Lift Off: "      + rightLiftOff);*/
        //telemetry.addData("2419" , ": "+ Off);
        //telemetry.addData("24" , "Bucket Off: " + BigBerthaTeleOp.isBucketOff());
        //telemetry.addData("25" , "Aux 2 Scale: "+ BigBerthaTeleOp.isAux2ScaleOff());
    } //--------------------------------------------------------------------------updateTelemetry

    public void updateGamepadTelemetry () {
        // Send telemetry data concerning gamepads to the driver station.
        telemetry.addData("25" , " ");
        telemetry.addData("26" , " ");
        telemetry.addData("27" , "Robot Controllers: ");
        telemetry.addData("28" , "Gamepad 1: ");
        telemetry.addData("29" , "Servos:");
        telemetry.addData("30" , "Flag A: "       + gamepad1.a);
        telemetry.addData("31" , "Flag B: "       + gamepad1.b);
        telemetry.addData("32" , "Bucket Door Y: "+ gamepad1.y);
        telemetry.addData("33" , "Bucket Door X: "+ gamepad1.x);
        telemetry.addData("34" , "Motors:");
        telemetry.addData("35" , "Right Drive Y Stick: "+zero(-gamepad1.right_stick_y));
        telemetry.addData("36" , "Left Drive Y Stick: " +zero(-gamepad1.left_stick_y));
        telemetry.addData("37" , "Right X Stick: "           + gamepad1.right_stick_x);
        telemetry.addData("38" , "Left X Stick: "            + gamepad1.left_stick_x);
        telemetry.addData("39" , "Dpad Up: "                 + gamepad1.dpad_up);
        telemetry.addData("40" , "Dpad Down: "               + gamepad1.dpad_down);
        telemetry.addData("41" , "Dpad Right: "              + gamepad1.dpad_right);
        telemetry.addData("42" , "Dpad Left: "               + gamepad1.dpad_left);
        telemetry.addData("43" , "Chain Right Bumper: "      + gamepad1.right_bumper);
        telemetry.addData("44" , "Rev Chain Left Bumper: "   + gamepad1.left_bumper);
        telemetry.addData("45" , "Sweeper Right Trigger: "   + gamepad1.right_trigger);
        telemetry.addData("46" , "Rev Sweeper Left Trigger: "+-gamepad1.left_trigger);
        telemetry.addData("47" , "Scale/Sweeper Off Start: " + gamepad1.start);
        telemetry.addData("48" , "Scale/Sweeper Res Guide: " + gamepad1.guide);
        telemetry.addData("49" , "Back: "                    + gamepad1.back);
        telemetry.addData("50" , "Gamepad 2:");
        telemetry.addData("51" , " ");
        telemetry.addData("52" , "Servos:");
        telemetry.addData("53" , "Man A: " + gamepad2.a);
        telemetry.addData("54" , "Man B: " + gamepad2.b);
        telemetry.addData("55" , "Hook Y: "+ gamepad2.y);
        telemetry.addData("56" , "Hook X: "+ gamepad2.x);
        telemetry.addData("57" , "Motors:");
        telemetry.addData("58" , "Right Arm Y Stick: "+zero(-gamepad2.right_stick_y));
        telemetry.addData("59" , "Left Arm Y Stick: " +zero(-gamepad2.left_stick_y));
        telemetry.addData("60" , "Right X Stick: "         + gamepad2.right_stick_x);
        telemetry.addData("61" , "Left X Stick: "          + gamepad2.left_stick_x);
        telemetry.addData("62" , "Bucket Dpad Up: "        + gamepad2.dpad_up);
        telemetry.addData("63" , "Bucket Dpad Down: "      + gamepad2.dpad_down);
        telemetry.addData("64" , "Dpad Right: "            + gamepad2.dpad_right);
        telemetry.addData("65" , "Dpad Left: "             + gamepad2.dpad_left);
        telemetry.addData("66" , "Rev Right Lift Bumper: " + gamepad2.right_bumper);
        telemetry.addData("67" , "Rev Left Lift Bumper: "  + gamepad2.left_bumper);
        telemetry.addData("68" , "Right Lift Trigger: "    + gamepad2.right_trigger);
        telemetry.addData("69" , "Left Lift Trigger: "     +-gamepad2.left_trigger);
        telemetry.addData("70" , "Start: "                 + gamepad2.start);
        telemetry.addData("71" , "Guide: "                 + gamepad2.guide);
        telemetry.addData("72" , "Back: "                  + gamepad2.back);
    } //--------------------------------------------------------------------------updateGamepadTelemetry
    /**
     * Update the telemetry's first message with the specified message.
     */
    public void setFirstMessage (String pMessage) {telemetry.addData("00", pMessage);}
    public void setDriveFirstMessage (String pMessage) {telemetry.addData("001", pMessage);}
    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void setErrorMessage (String pMessage) {setFirstMessage("ERROR: " + pMessage);}
} //------------------------------------------------------------------------------BigBerthaTelemetry
