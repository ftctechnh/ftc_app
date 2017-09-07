package org.firstinspires.ftc.teamcode._Test._Sensors;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;

import org.firstinspires.ftc.teamcode._Libs.SensorLib;


/**
 * Created by phanau on 1/22/16.
 * Test hardware gyro
 */
@Autonomous(name="Test: MR Gyro Test 1", group ="Test")
//@Disabled
public class GyroTestOp extends OpMode {

    private ModernRoboticsI2cGyro mGyro;
    private SensorLib.CorrectedMRGyro mCorrGyro;

    public GyroTestOp() {
    }

    public void init() {
        // get hardware gyro
        mGyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

        // wrap gyro in an object that corrects its output
        mCorrGyro = new SensorLib.CorrectedMRGyro((ModernRoboticsI2cGyro) mGyro);
        mCorrGyro.calibrate();      // calibrate the underlying hardware gyro
    }

    public void loop() {
        boolean calibrating = mGyro.isCalibrating();
        telemetry.addData("status:  ", calibrating ? "calibrating" : "ready");
        telemetry.addData("integrated z: ", mCorrGyro.getIntegratedZValue());
        telemetry.addData("corrected heading: ", mCorrGyro.getHeading());
    }

    public void stop() {
        mCorrGyro.stop();       // release the physical gyro(s) we're using
    }

}
