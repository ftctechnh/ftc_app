package org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Sensors.IMUcontrol;
import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;
import org.firstinspires.ftc.robotcore.external.Telemetry;



/* Sub Assembly Class
 */
public class DriveControl {
    /* Declare private class object */
    //private Telemetry telemetry;         /* local copy of telemetry object from opmode class */
    HardwareMap hwMap = null;     /* local copy of HardwareMap object from opmode class */
    //private String name = "Drive Train";
    private ElapsedTime runtime = new ElapsedTime();
    public IMUcontrol imu = new IMUcontrol();

    //initializing motors
       private DcMotor FrontRightM = null;
    private DcMotor FrontLeftM = null;
    private DcMotor BackRightM = null;
    private DcMotor BackLeftM = null;

    /* Declare public class object */

    /* Subassembly constructor */
    public DriveControl() {
    }

    public void init(HardwareMap ahwMap) {
        /* Set local copies from opmode class */
        hwMap = ahwMap;

        imu.init(ahwMap);

        //telemetry.addLine(name + " initialize");

        /* Map hardware devices */

        FrontRightM = hwMap.dcMotor.get("FrontRightM");
        FrontLeftM = hwMap.dcMotor.get("FrontLeftM");
        BackRightM = hwMap.dcMotor.get("BackRightM");
        BackLeftM = hwMap.dcMotor.get("BackLeftM");


        //reverses some motors
        BackLeftM.setDirection(DcMotor.Direction.REVERSE);
        FrontLeftM.setDirection(DcMotor.Direction.REVERSE);

        FrontRightM.setPower(0);
        FrontLeftM.setPower(0);
        BackRightM.setPower(0);
        BackLeftM.setPower(0);
    }

    //setting power to move forward
    public void moveForward(double speed) {
        FrontRightM.setPower(speed);
        FrontLeftM.setPower(speed);
        BackRightM.setPower(speed);
        BackLeftM.setPower(speed);
    }

    public void moveForward(double speed,double time){
        moveForward(speed);
        TimeDelay(time);
        stop();
    }

    //setting power to move backward
    public void moveBackward(double speed) {
        FrontRightM.setPower(-speed);
        FrontLeftM.setPower(-speed);
        BackRightM.setPower(-speed);
        BackLeftM.setPower(-speed);
    }

    public void moveBackward(double speed,double time){
        moveBackward(speed);
        TimeDelay(time);
        stop();
    }

    //setting power to turn left
    public void turnLeft(double speed) {
        FrontRightM.setPower(speed);
        FrontLeftM.setPower(-speed);
        BackRightM.setPower(speed);
        BackLeftM.setPower(-speed);
    }

    public void turnLeft(double speed,double time){
        turnLeft(speed);
        TimeDelay(time);
        stop();
    }

    //setting power to turn right
    public void turnRight(double speed) {
        FrontRightM.setPower(-speed);
        FrontLeftM.setPower(speed);
        BackRightM.setPower(-speed);
        BackLeftM.setPower(speed);
    }

    public void turnRight(double speed,double time){
        turnRight(speed);
        TimeDelay(time);
        stop();
    }

    public void turn2angle (double angle){



        do{
            imu.IMUupdate();

            imu.angle2turn = (angle - imu.trueAngle);

            if (imu.angle2turn > 180){
                imu.angle2turn -= 360;
            }
            if (imu.angle2turn < -180){
                imu.angle2turn += 360;
            }

            if (imu.angle2turn > 15) {
                turnRight(imu.turnSpeed);
            }
            else if (imu.angle2turn < -15) {
                turnLeft(imu.turnSpeed);
            }
            else {
                stop();
            }
        } while (imu.angle2turn > 15 || imu.angle2turn < -15);
    }

    public void turnAngle (int angle) {
        imu.angle2turn = (angle + imu.trueAngle);

        do{
            if (imu.angle2turn > 180){
                imu.angle2turn -= 360;
            }
            if (imu.angle2turn < -180){
                imu.angle2turn += 360;
            }

            if (imu.angle2turn > 15) {
                turnRight(imu.turnSpeed);
            }
            else if (imu.angle2turn < -15) {
                turnLeft(imu.turnSpeed);
            }
            else {
                stop();
                imu.startTrueAngle = 180;
            }
        } while (imu.angle2turn > 15 || imu.angle2turn < -15);
    }
    //setting power to 0
    public void stop() {
        FrontRightM.setPower(0);
        FrontLeftM.setPower(0);
        BackRightM.setPower(0);
        BackLeftM.setPower(0);
    }

    public void tankDrive (double leftSpeed, double rightSpeed, double time){
        tankLeftForward(leftSpeed);
        tankRightForward(rightSpeed);
        TimeDelay(time);
        stop();
    }

    public void tankRightForward(double speed) {
        FrontRightM.setPower(speed);
        BackRightM.setPower(speed);
    }

    public void tankRightBackward(double speed) {
        FrontRightM.setPower(-speed);
        BackRightM.setPower(-speed);
    }

    public void tankLeftForward(double speed) {
        FrontLeftM.setPower(speed);
        BackLeftM.setPower(speed);
    }

    public void tankLeftBackward(double speed) {
        FrontLeftM.setPower(-speed);
        BackLeftM.setPower(-speed);
    }

    public void TimeDelay(double time){
        double start = 0;
        double now = 0;
        start = runtime.seconds();
        do {
            now = runtime.seconds() - start;
        }while (now<time);
    }
}