package org.firstinspires.ftc.teamcode.utils;

/**
 * Created by ftc6347 on 9/1/17.
 */

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.internal.AppUtil;

import java.io.File;
import java.util.Locale;

/**
 * {@link org.firstinspires.ftc.robotcontroller.external.samples.SensorBNO055IMUCalibration} calibrates the IMU accelerometer per
 * "Section 3.11 Calibration" of the BNO055 specification.
 * <p>
 * <p>Manual calibration of the IMU is definitely NOT necessary: except for the magnetometer
 * (which is not used by the default {@link BNO055IMU.SensorMode#IMU
 * SensorMode#IMU}), the BNO055 is internally self-calibrating and thus can be very successfully
 * used without manual intervention. That said, performing a one-time calibration, saving the
 * results persistently, then loading them again at each run can help reduce the time that automatic
 * calibration requires.</p>
 * <p>
 * <p>This summary of the calibration process, from <a href="http://iotdk.intel.com/docs/master/upm/classupm_1_1_b_n_o055.html">
 * Intel</a>, is informative:</p>
 * <p>
 * <p>"This device requires calibration in order to operate accurately. [...] Calibration data is
 * lost on a power cycle. See one of the examples for a description of how to calibrate the device,
 * but in essence:</p>
 * <p>
 * <p>There is a calibration status register available [...] that returns the calibration status
 * of the accelerometer (ACC), magnetometer (MAG), gyroscope (GYR), and overall system (SYS).
 * Each of these values range from 0 (uncalibrated) to 3 (fully calibrated). Calibration [ideally]
 * involves certain motions to get all 4 values at 3. The motions are as follows (though see the
 * datasheet for more information):</p>
 * <p>
 * <li>
 * <ol>GYR: Simply let the sensor sit flat for a few seconds.</ol>
 * <ol>ACC: Move the sensor in various positions. Start flat, then rotate slowly by 45
 * degrees, hold for a few seconds, then continue rotating another 45 degrees and
 * hold, etc. 6 or more movements of this type may be required. You can move through
 * any axis you desire, but make sure that the device is lying at least once
 * perpendicular to the x, y, and z axis.</ol>
 * <ol>MAG: Move slowly in a figure 8 pattern in the air, until the calibration values reaches 3.</ol>
 * <ol>SYS: This will usually reach 3 when the other items have also reached 3. If not, continue
 * slowly moving the device though various axes until it does."</ol>
 * </li>
 * <p>
 * <p>To calibrate the IMU, run this sample opmode with a gamepad attached to the driver station.
 * Once the IMU has reached sufficient calibration as reported on telemetry, press the 'A'
 * button on the gamepad to write the calibration to a file. That file can then be indicated
 * later when running an opmode which uses the IMU.</p>
 * <p>
 * <p>Note: if your intended uses of the IMU do not include use of all its sensors (for exmaple,
 * you might not use the magnetometer), then it makes little sense for you to wait for full
 * calibration of the sensors you are not using before saving the calibration data. Indeed,
 * it appears that in a SensorMode that doesn't use the magnetometer (for example), the
 * magnetometer cannot actually be calibrated.</p>
 *
 * @see AdafruitBNO055IMU
 * @see BNO055IMU.Parameters#calibrationDataFile
 * @see <a href="https://www.bosch-sensortec.com/bst/products/all_products/bno055">BNO055 product page</a>
 * @see <a href="https://ae-bst.resource.bosch.com/media/_tech/media/datasheets/BST_BNO055_DS000_14.pdf">BNO055 specification</a>
 */
public class BNO055IMUHelper {
    // Our sensors, motors, and other devices go here, along with other long term state
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;
    private final HardwareMap hardwareMap;
    Orientation angles;


    public BNO055IMUHelper(String name, BNO055IMU.Parameters _parameters, LinearOpMode linearOpMode) {
        parameters = _parameters;
        hardwareMap = linearOpMode.hardwareMap;
        imu = hardwareMap.get(BNO055IMU.class, name);
        imu.initialize(parameters);
    }

    public BNO055IMUHelper(String name, BNO055IMU.Parameters _parameters, OpMode opMode) {
        parameters = _parameters;
        hardwareMap = opMode.hardwareMap;
        imu = hardwareMap.get(BNO055IMU.class, name);
        imu.initialize(parameters);
    }

    public void composeTelemetry(Telemetry telemetry) {


        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    public String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    public String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }


    public String  writeCalFile(String filename) {
        // Get the calibration data
        BNO055IMU.CalibrationData calibrationData = imu.readCalibrationData();

        // Save the calibration data to a file. You can choose whatever file
        // name you wish here, but you'll want to indicate the same file name
        // when you initialize the IMU in an opmode in which it is used. If you
        // have more than one IMU on your robot, you'll of course want to use
        // different configuration file names for each.
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, calibrationData.serialize());
        return "saved to " + filename;
    }

    public BNO055IMU getInstance(){
        return this.imu;
    }


}
