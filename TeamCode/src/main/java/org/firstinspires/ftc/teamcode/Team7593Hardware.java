package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


public class Team7593Hardware {

    //declare motors

    public DcMotor motorFrontRight;
    public DcMotor motorFrontLeft;
    public DcMotor motorRearRight;
    public DcMotor motorRearLeft;


    //declare servos


    //declare sensors


    //declare internal imu and the parameters
    public BNO055IMU imu;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    //get the initial angle from the imu
    float initAngle;

    //local OpMode members
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();


    //Constructor (mostly for IMU (all classes need a constructor though))
    public Team7593Hardware(){
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
    }


    //Initialize standard Hardware interfaces
    public void init(HardwareMap ahwMap){

        //save reference to Hardware map
        hwMap = ahwMap;

        //Define and initialize motors
        motorFrontRight = hwMap.get(DcMotor.class, "mfr");
        motorFrontLeft = hwMap.get(DcMotor.class, "mfl");
        motorRearRight = hwMap.get(DcMotor.class, "mrr");
        motorRearLeft = hwMap.get(DcMotor.class, "mrl");

        //Define and initialize servos


        //Define and initialize sensors


        imu = hwMap.get(BNO055IMU.class, "imu 1");
        imu.initialize(parameters);



        //set motor encoders
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //powers the wheels
    public void powerTheWheels(WheelSpeeds wheelSpeeds){
        motorFrontLeft.setPower(wheelSpeeds.v_lf);
        motorFrontRight.setPower(wheelSpeeds.v_rf);
        motorRearLeft.setPower(wheelSpeeds.v_lr);
        motorRearRight.setPower(wheelSpeeds.v_rr);
    }

    //stops the wheels
    public void stopTheWheels(){
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorRearLeft.setPower(0);
        motorRearRight.setPower(0);
    }

    public float getCurrentAngle(){
        Orientation angles;

        float currentAngle;
        angles   = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        currentAngle = angles.firstAngle - initAngle;

        // Keep current angle between 0 and 360
        while(currentAngle < 0) {
            currentAngle = currentAngle+360;
        }

        while(currentAngle > 360) {
            currentAngle = currentAngle - 360;
        }

        return currentAngle;
    }

    /***
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs){

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    public void resetInitAngle() {
        this.initAngle = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
}
