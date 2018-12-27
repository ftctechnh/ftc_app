package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;

public class IMU {

    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    ElapsedTime GyroTimeOut;

    public IMU(HardwareMap hwMap) {
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.mode = BNO055IMU.SensorMode.IMU;

        imu = hwMap.get(BNO055IMU.class, "imu");

        AbstractOpMode.telemetry.addData("IMU initializing: "+imu.toString());

        imu.initialize(parameters);

        GyroTimeOut = new ElapsedTime();
        GyroTimeOut.reset();

        while (!imu.isGyroCalibrated() && GyroTimeOut.milliseconds() <= 1000 && AbstractOpMode.isOpModeActive()) {
            AbstractOpMode.telemetry.addDataPhone(DoubleTelemetry.LogMode.INFO, imu.getCalibrationStatus().toString());
            AbstractOpMode.telemetry.update();
        }

        AbstractOpMode.telemetry.addData("IMU initialized");
    }

    public double getHeading() {
        Orientation angle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angle.firstAngle;
    }

    public void resetAngleToZero() {
        imu.initialize(parameters);

        while (!imu.isGyroCalibrated() && GyroTimeOut.milliseconds() <= 1000) ;
    }

    public boolean isGyroCalibrated() {
        return imu.isGyroCalibrated();
    }

    /*private BNO055IMU imu1, imu2;
    private BNO055IMU.Parameters parameters;

    public IMU(HardwareMap hardwareMap){
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.mode = BNO055IMU.SensorMode.IMU;

        //parameters.mode = BNO055IMU.SensorMode.GYRONLY;

        imu1 = hardwareMap.tryGet(BNO055IMU.class,"imu1");
        imu2 = hardwareMap.tryGet(BNO055IMU.class,"imu2");

        if(imu1!=null) {
            imu1.initialize(parameters);
        }
        if(imu2!=null) {
            imu2.initialize(parameters);
        }

        if(imu1!=null&&imu2!=null) {
            while (!imu1.isGyroCalibrated() || !imu2.isGyroCalibrated());
        } else if(imu1!=null){
            while (!imu1.isGyroCalibrated());
        } else if(imu2!=null){
            while (!imu2.isGyroCalibrated());
        }
    }

    public double getHeading(){
        //Could be an issue at 180/-180 averages out to 0
        if(imu1!=null&&imu2!=null) {
            Orientation angle1 = imu1.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            Orientation angle2 = imu2.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            return (angle1.firstAngle+angle2.firstAngle)/2;
        }
        if(imu1!=null){
            Orientation angle = imu1.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            return angle.firstAngle;
        }
        if(imu2!=null){
            Orientation angle = imu2.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            return angle.firstAngle;
        }
        return 0;
    }

    public void resetAngleToZero() {
        if(imu1!=null) imu1.initialize(parameters);
        if(imu2!=null) imu2.initialize(parameters);

        if(imu1!=null&&imu2!=null) {
            while (!imu1.isGyroCalibrated() || !imu2.isGyroCalibrated());
        } else if(imu1!=null){
            while (!imu1.isGyroCalibrated());
        } else if(imu2!=null){
            while (!imu2.isGyroCalibrated());
        }
    }*/
}
