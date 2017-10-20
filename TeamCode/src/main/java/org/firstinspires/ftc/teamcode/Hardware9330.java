package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;


/**
 * Created by robot on 9/25/2017.
 */

public class Hardware9330 {
    /* Public OpMode members. */

    //public static Servo  lowClamp;
    //public static Servo  highClamp;
    //public static DcMotor leftMotor;
    //public static DcMotor liftMotor;
    public static TouchSensor touch;
    public static ColorSensor cs;
    public static DistanceSensor ds;
    public static DcMotor rightMotor;
    public static BNO055IMU gyro;


    HardwareMap hwMap                 = null;

    private ElapsedTime period  = new ElapsedTime();

    public Hardware9330() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors

        //lowClamp = hwMap.servo.get("lowClamp");
        //lowClamp.setDirection(Servo.Direction.REVERSE);
        //highClamp = hwMap.servo.get("highClamp");
        //highClamp.setDirection(Servo.Direction.REVERSE);
        //leftMotor = hwMap.dcMotor.get("leftMotor ");
        //liftMotor = hwMap.dcMotor.get("liftMotor");
        touch = hwMap.touchSensor.get("touch");
        cs = hwMap.get(ColorSensor.class, "cs");
        ds = hwMap.get(DistanceSensor.class, "cs");
        rightMotor = hwMap.dcMotor.get("rightMotor");
        gyro = hwMap.get(BNO055IMU.class, "imu");

    }
}
