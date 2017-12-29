package org.firstinspires.ftc.teamcode.SwagsterWagster_UltimateCode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.SwagsterWagster_UltimateCode.Constants;

/**
 * Created by Swagster_Wagster on 12/16/17.
 */
@Autonomous(name = "NewAuto    ",group="We Love Pi")
public class NewAuto extends LinearOpMode {

    ElapsedTime timer = new ElapsedTime();

    protected DcMotor F_L = null;
    protected DcMotor F_R = null;
    protected DcMotor R_L = null;
    protected DcMotor R_R = null;

    public DcMotor clamp = null;

    protected Servo dropper = null;

    com.qualcomm.robotcore.hardware.ColorSensor colorSensor = null;

    String jewelColor = null;

    float hsvValues[] = {0F,0F,0F};

    IntegratingGyroscope gyro;
    ModernRoboticsI2cGyro modernRoboticsI2cGyro;

    ModernRoboticsI2cRangeSensor rangeSensor;

    HardwareMap hwMap = null;

    public void init(HardwareMap ahwMap) {

        hwMap = ahwMap;

        F_L = hwMap.get(DcMotor.class, "F_L");
        F_R = hwMap.get(DcMotor.class, "F_R");
        R_L = hwMap.get(DcMotor.class, "R_L");
        R_R = hwMap.get(DcMotor.class, "R_R");

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);

        F_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        F_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /*
        dropper = hwMap.get(Servo.class, "dropper");

        clamp = hwMap.get(DcMotor.class, "clamp");

        colorSensor = hwMap.get(ColorSensor.class, "colorSensor");

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");

        modernRoboticsI2cGyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)modernRoboticsI2cGyro;
        */
    }

    protected void mysleep(long time) {

        try {

            Thread.sleep(time);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public void stopMotor(long time) {

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);
        mysleep(time);

    }

    public void stopMotor() {

        stopMotor(0);

    }

   public void moveMotor(double power, String direction){

       if(direction == Constants.forward) {

           F_L.setDirection(DcMotor.Direction.FORWARD);
           F_R.setDirection(DcMotor.Direction.FORWARD);
           R_L.setDirection(DcMotor.Direction.REVERSE);
           R_R.setDirection(DcMotor.Direction.REVERSE);

           F_L.setPower(power);
           F_R.setPower(power);
           R_L.setPower(power);
           R_R.setPower(power);
       }

       if(direction == Constants.backward) {

           F_L.setDirection(DcMotor.Direction.REVERSE);
           F_R.setDirection(DcMotor.Direction.REVERSE);
           R_L.setDirection(DcMotor.Direction.FORWARD);
           R_R.setDirection(DcMotor.Direction.FORWARD);

           F_L.setPower(power);
           F_R.setPower(power);
           R_L.setPower(power);
           R_R.setPower(power);

       }

       if(direction == Constants.left) {

           F_L.setDirection(DcMotor.Direction.FORWARD);
           F_R.setDirection(DcMotor.Direction.FORWARD);
           R_L.setDirection(DcMotor.Direction.FORWARD);
           R_R.setDirection(DcMotor.Direction.FORWARD);

           F_L.setPower(power);
           F_R.setPower(power);
           R_L.setPower(power);
           R_R.setPower(power);
       }

       if(direction == Constants.right) {

           F_L.setDirection(DcMotor.Direction.REVERSE);
           F_R.setDirection(DcMotor.Direction.REVERSE);
           R_L.setDirection(DcMotor.Direction.REVERSE);
           R_R.setDirection(DcMotor.Direction.REVERSE);

           F_L.setPower(power);
           F_R.setPower(power);
           R_L.setPower(power);
           R_R.setPower(power);

       }
   }

   public void moveMotorWithTime(double power, long time, String direction) {

       if(direction == Constants.forward) {

           moveMotor(power, direction);
           mysleep(time);
       }

       if(direction == Constants.backward) {

           moveMotor(power, direction);
           mysleep(time);
       }

       if(direction == Constants.left) {

           moveMotor(power, direction);
           mysleep(time);
       }

       if(direction == Constants.right) {

           moveMotor(power, direction);
           mysleep(time);
       }
   }

   public void moveMotorWithEncoder(double power, int distance,String direction) {

       if(direction == Constants.forward) {

       }

       if(direction == Constants.backward) {

       }

       if(direction == Constants.left) {


       }

       if(direction == Constants.right) {


       }
   }

    public void turnMotorUsingGyro (double power, float degrees, String direction) {

        modernRoboticsI2cGyro.calibrate();

        modernRoboticsI2cGyro.resetZAxisIntegrator();

        float zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

        if (direction == Constants.spinRight) {

            while (zAngle <= degrees) {

                F_L.setDirection(DcMotor.Direction.FORWARD);
                F_R.setDirection(DcMotor.Direction.FORWARD);
                R_L.setDirection(DcMotor.Direction.FORWARD);
                R_R.setDirection(DcMotor.Direction.FORWARD);

                F_L.setPower(power);
                F_R.setPower(power);
                R_L.setPower(power);
                R_R.setPower(power);

                zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

            }  if (zAngle >= degrees) {

                F_L.setPower(0);
                F_R.setPower(0);
                R_L.setPower(0);
                R_R.setPower(0);
            }
        }

        if (direction == Constants.spinLeft) {

            while (zAngle <= degrees) {

                F_L.setDirection(DcMotor.Direction.REVERSE);
                F_R.setDirection(DcMotor.Direction.REVERSE);
                R_L.setDirection(DcMotor.Direction.REVERSE);
                R_R.setDirection(DcMotor.Direction.REVERSE);

                F_L.setPower(power);
                F_R.setPower(power);
                R_L.setPower(power);
                R_R.setPower(power);

                zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

            } if (zAngle >= degrees) {

                F_L.setPower(0);
                F_R.setPower(0);
                R_L.setPower(0);
                R_R.setPower(0);
            }
        }
    }


    @Override
    public void runOpMode() throws InterruptedException {

        /*
        init(hardwareMap);

        moveMotorWithTime(.2, 1000, Constants.forward);
        moveMotorWithTime(.2, 1000, Constants.backward);
        moveMotorWithTime(.2, 1500, Constants.left);
        moveMotorWithTime(.2, 1500, Constants.right);
        */

    }
}
