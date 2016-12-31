package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by ftc6347 on 9/25/16.
 */
public class ProgrammingRobotHardware {

    public static double COUNTS_PER_INCH = 0;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private ModernRoboticsI2cGyro gyro;
    private ColorSensor colorSensor;

    public ProgrammingRobotHardware(HardwareMap hardwareMap, Telemetry telemetry) {
        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        backLeft = hardwareMap.dcMotor.get("bl");
        backRight = hardwareMap.dcMotor.get("br");

        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");

        colorSensor = hardwareMap.colorSensor.get("clrs");
        colorSensor.enableLed(true);

        // set all motors to run using encoders
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        gyro.calibrate();
    }

    public ModernRoboticsI2cGyro getGyroSensor() {
        return gyro;
    }

    public ColorSensor getColorSensor() {
        return colorSensor;
    }

    public DcMotor getFrontLeft() {
        return frontLeft;
    }

    public DcMotor getFrontRight() {
        return frontRight;
    }

    public DcMotor getBackLeft() {
        return backLeft;
    }

    public DcMotor getBackRight() {
        return backRight;
    }
}
