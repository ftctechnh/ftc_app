package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
/**
 * Created by djfigs1 on 6/19/16.
 */
public class LineFollowBot extends OpMode {

    public enum ColorSensorsState {LEFT, RIGHT, BOTH, NONE}

    private DcMotor LeftMotor;
    private DcMotor RightMotor;
    private DcMotor SpinHandle;
    public ColorSensor leftColorSensor;
    public ColorSensor rightColorSensor;

    public int threshold = 15;

    @Override
    public void init() {

        //Motors
        try {
            LeftMotor = hardwareMap.dcMotor.get("left_drive");
            LeftMotor.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "left drive");
            LeftMotor = null;
        }

        try {
            RightMotor = hardwareMap.dcMotor.get("right_drive");
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "right drive");
            RightMotor = null;
        }

        //Color Sensors
        try {
            leftColorSensor = hardwareMap.colorSensor.get("leftColorSensor");
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "left color");
            leftColorSensor = null;
        }

        try {
            //The Right Color Sensor's I2C Address has been modified to 62.
            rightColorSensor = hardwareMap.colorSensor.get("rightColorSensor");
            rightColorSensor.setI2cAddress(I2cAddr.create8bit(62));
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "right color");
            rightColorSensor = null;
        }

        try {
            SpinHandle = hardwareMap.dcMotor.get("spinHandle");
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "spin handle");
            SpinHandle = null;
        }
    }

    @Override
    public void loop() {

    }

    //Functions
    public void setDrivePower(double leftPower, double rightPower) {
        if (LeftMotor != null && RightMotor != null) {
            LeftMotor.setPower(leftPower);
            RightMotor.setPower(rightPower);
        }
    }

    public void setSpinHandlePower(double power) {
        if (SpinHandle != null) {
            SpinHandle.setPower(power);
        }
    }

    public boolean leftBlue() {
        if (leftColorSensor != null) {
            if (leftColorSensor.blue() > threshold) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean rightBlue() {
        if (rightColorSensor != null) {
            if (rightColorSensor.blue() > threshold) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public ColorSensorsState getColorSensorsState() {
        if (leftColorSensor != null || rightColorSensor != null) {
            if (leftColorSensor.blue() >= threshold && rightColorSensor.blue() < threshold) {
                return ColorSensorsState.LEFT;
            }
            if (leftColorSensor.blue() < threshold && rightColorSensor.blue() >= threshold) {
                return ColorSensorsState.RIGHT;
            }
            if (leftColorSensor.blue() >= threshold && rightColorSensor.blue() >= threshold) {
                return ColorSensorsState.BOTH;
            }
            if (leftColorSensor.blue() < threshold && rightColorSensor.blue() < threshold) {
                return ColorSensorsState.NONE;
            }
        }else {
            System.out.println("WARNING SENSOR IS NULL");
        }
        return ColorSensorsState.NONE;
    }
}

