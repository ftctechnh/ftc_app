package org.firstinspires.ftc.teamcode.HolonomicOpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/*
- Name: Holonomic Hardware Map
- Creator[s]: Talon
- Date Created: 6/16/17
- Objective: To create a class that sets up the hardware map for our holonomic robot and has basic
             functions to reduce redundancies in other programs.
 */

public class Holonomic_Hardware_Map {

    //Declaring variables
    public DcMotor fleft, fright, bleft, bright;
    public BNO055IMU gyro;
    public float heading;
    public float dp = .2f; //Drive Power (range = 0-1)
    private HardwareMap hwMap;
    private Telemetry telemetry;

    //Constructor; Put program's hardwaremap first, then telemetry,  then put true if gyro will be used or false if it won't
    public Holonomic_Hardware_Map(HardwareMap hwmap, Telemetry telem, boolean usesGyro){
        hwMap = hwmap;
        telemetry = telem;

        //Setting up drive motors
        fleft = hwMap.dcMotor.get("fleft");
        fright = hwMap.dcMotor.get("fright");
        bleft = hwMap.dcMotor.get("bleft");
        bright = hwMap.dcMotor.get("bright");
        fright.setDirection(DcMotor.Direction.REVERSE);
        bright.setDirection(DcMotor.Direction.REVERSE);

        //Setting up gyro sensor if necessary
        if(usesGyro) {
            gyro = hwMap.get(BNO055IMU.class, "imu");
            //Setting up data for gyro sensors
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
            gyro.initialize(parameters); //this could also be causingissues but is probably important
        }

        //Alerts user that initialization is done
        telemetry.addData("Ready to go", true);
        //telemetry.update(); //This line is giving us issues, so we may just have to cut the telemetry during init
    }

    public void drive(float fl, float fr, float bl, float br) {
        fleft.setPower(ClipValue(fl));
        fright.setPower(ClipValue(fr));
        bleft.setPower(ClipValue(bl));
        bright.setPower(ClipValue(br));
    }

    public void updateGyro() {
        //May not have to make negative? Make it so that turning is CCW
        heading = - gyro.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX).firstAngle;
        //Changes the range to -180 to 180 instead of 0 to 360
        if(heading > 180)
            heading = heading - 360;
    }

    float ClipValue(float value) {
        if(value > dp || value < - dp)
            return ((Math.abs(value) / value) * dp);
        else
            return value;
    }

    public void doTelemetry() {
        telemetry.addData("Ultra Turbo Mode Activated:", dp == 1);
        telemetry.addData("Gyro Heading:", heading);
        telemetry.update();
    }

}
