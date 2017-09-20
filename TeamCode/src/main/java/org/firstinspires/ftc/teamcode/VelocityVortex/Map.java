package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by spmce on 11/4/2016.
 */
public class Map extends VelocityVortexHardware {
    // Warning
    Warning warning = new Warning();
    //------------Mapping Method------------
    DcMotor map(DcMotor motor, double initPower) {
        try {
            motor = hardwareMap.dcMotor.get(motor.toString());
            motor.setPower(initPower);
        } catch (Exception opModeException) {
            warning.setDriveWarning(motor.toString());
            DbgLog.msg(opModeException.getLocalizedMessage());
        }
        return motor;
    }
    DcMotor map(DcMotor motor, double initPower, String s) {
        try {
            motor = hardwareMap.dcMotor.get(s);
            motor.setPower(initPower);
        } catch (Exception opModeException) {
            warning.setDriveWarning(s);
            DbgLog.msg("" + opModeException.getLocalizedMessage());
        }
        return motor;
    }
    DcMotor map(DcMotor motor, double initPower, boolean reverse) {
        try {
            motor = hardwareMap.dcMotor.get(motor.toString());
            if (reverse)
                motor.setDirection(DcMotorSimple.Direction.REVERSE);
            motor.setPower(initPower);
        } catch (Exception opModeException) {
            warning.setDriveWarning(motor.toString());
            DbgLog.msg(opModeException.getLocalizedMessage());
        }
        return motor;
    }
    DcMotor map(DcMotor motor, double initPower, String s, boolean reverse) {
        try {
            motor = hardwareMap.dcMotor.get(s);
            if (reverse)
                motor.setDirection(DcMotorSimple.Direction.REVERSE);
            motor.setPower(initPower);
        } catch (Exception opModeException) {
            warning.setDriveWarning(s);
            DbgLog.msg("" + opModeException.getLocalizedMessage());
        }
        return motor;
    }
    Servo map(Servo servo, double initPosition) {
        try {
            servo = hardwareMap.servo.get(servo.getDeviceName());
            servo.setPosition(initPosition);
        } catch (Exception opModeException) {
            //warning.setServoWarningMessage(servo.getDeviceName());
            DbgLog.msg("" + opModeException.getLocalizedMessage());
            //servo = null;
        }
        return servo;
    }
    Servo map(Servo servo, double initPosition,String s) {
        try {
            servo = hardwareMap.servo.get(s);
            servo.setPosition(initPosition);
        } catch (Exception opModeException) {
            //warning.setServoWarningMessage(s);
            DbgLog.msg("" + opModeException.getLocalizedMessage());
            //servo = null;
        }
        return servo;
    }
    void map(CRServo servo, double initPosition) {
        try {
            servo = hardwareMap.crservo.get(servo.getDeviceName());
            servo.setPower(initPosition);
        } catch (Exception opModeException) {
            warning.setServoWarningMessage(servo.getDeviceName());
            DbgLog.msg(opModeException.getLocalizedMessage());
            //servo = null;
        }
    }
    void map (AccelerationSensor acceleration) {
        try {
            acceleration = hardwareMap.accelerationSensor.get("acceleration");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("acceleration");
            DbgLog.msg(opModeException.getLocalizedMessage());
            acceleration = null;
        }
    }
    void map(ColorSensor color) {
        try {
            color = hardwareMap.colorSensor.get(color.getDeviceName());
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage(color.getDeviceName());
            DbgLog.msg(opModeException.getLocalizedMessage());
            color = null;
        }
    }
    void map(CompassSensor compass) {
        try {
            compass = hardwareMap.compassSensor.get("compass");
            //compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
            // calculate how long we should hold the current position
            //pauseTime = time + HOLD_POSITION;
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("compass");
            DbgLog.msg(opModeException.getLocalizedMessage());
            compass = null;
        }
    }
    void map(DcMotorController motorController) {
        try {
            motorController = hardwareMap.dcMotorController.get("motorController");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("motorController");
            DbgLog.msg(opModeException.getLocalizedMessage());
            motorController = null;
        }
    }
    void map(GyroSensor gyro) {
        try {
            gyro = hardwareMap.gyroSensor.get("gyro");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("gyro");
            DbgLog.msg(opModeException.getLocalizedMessage());
            gyro = null;
        }
    }
    void map(IrSeekerSensor ir) {
        try {
            ir = hardwareMap.irSeekerSensor.get("ir");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("ir");
            DbgLog.msg(opModeException.getLocalizedMessage());
            ir = null;
        }
    }
    void map(LightSensor light) {
        try {
            light = hardwareMap.lightSensor.get("light");
            light.enableLed(true);
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("light");
            DbgLog.msg(opModeException.getLocalizedMessage());
            light = null;
        }
    }
    void map(ServoController servoController) {
        try {
            servoController = hardwareMap.servoController.get("servoController");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("servoController");
            DbgLog.msg(opModeException.getLocalizedMessage());
            servoController = null;
        }
    }
    void map(TouchSensor touch) {
        try {
            touch = hardwareMap.touchSensor.get("touch");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("touch");
            DbgLog.msg(opModeException.getLocalizedMessage());
            touch = null;
        }
    }
    void map(TouchSensorMultiplexer multi) {
        try {
            multi = hardwareMap.touchSensorMultiplexer.get("multi");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("multi");
            DbgLog.msg(opModeException.getLocalizedMessage());
            multi = null;
        }
    }
    void map(UltrasonicSensor sonar) {
        try {
            sonar = hardwareMap.ultrasonicSensor.get("sonar");
        } catch (Exception opModeException) {
            warning.setSensorWarningMessage("sonar");
            DbgLog.msg(opModeException.getLocalizedMessage());
            sonar = null;
        }
    }
}
