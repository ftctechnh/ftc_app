package org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubAssembly.Sensors.IMUcontrol;
import org.firstinspires.ftc.teamcode.SubAssembly.Sensors.TofControl;


/* Sub Assembly Class
 */
public class DriveControl {
    /* Declare private class object */
    private LinearOpMode opmode = null;     /* local copy of opmode class */
    private ElapsedTime runtime = new ElapsedTime();

    //initializing motors
    private DcMotor FrontRightM = null;
    private  DcMotor FrontLeftM = null;
    private  DcMotor BackRightM = null;
    private  DcMotor BackLeftM = null;

    /* Declare public class object */
    public IMUcontrol imu = new IMUcontrol();
    public TofControl Tof = new TofControl();


    /* Subassembly constructor */
    public DriveControl() {
    }

    public void init(LinearOpMode opMode) {
        HardwareMap hwMap;

        opMode.telemetry.addLine("Drive Control" + " initialize");
        opMode.telemetry.update();

        /* Set local copies from opmode class */
        opmode = opMode;
        hwMap = opMode.hardwareMap;

        imu.init(opMode);
        Tof.init(opMode);


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

    public void moveForward(double speed, double time) {
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

    public void moveBackward(double speed, double time) {
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

    public void turnLeft(double speed, double time) {
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

    public void turnRight(double speed, double time) {
        turnRight(speed);
        TimeDelay(time);
        stop();
    }

    public void turn2Angle(double speed, double angle) {
        double angle2turn;

        double start = 0;
        double now = 0;
        double interval = 0;
        start = runtime.seconds();

        do {
            imu.update();

            angle2turn = (angle - imu.trueAngle);

            if (angle2turn > 180) {
                angle2turn -= 360;
            }
            if (angle2turn < -180) {
                angle2turn += 360;
            }

            if (angle2turn > 15) {
                turnRight(speed);
            } else if (angle2turn < -15) {
                turnLeft(speed);
            }

        } while ( (angle2turn > 12 || angle2turn < -12) && !opmode.isStopRequested() );

        stop();

        TimeDelay(0.05);

        now = 0;

        do {
            imu.update();

            //
            now = runtime.seconds() - start;

            if (now >= interval){
                interval += 0.1;
                opmode.telemetry.addData("trueAngle", imu.trueAngle);
                opmode.telemetry.update();
            }
            //

            angle2turn = (angle - imu.trueAngle);

            if (angle2turn > 0.0625) {
                turnRight(speed*angle2turn/17 + speed/3);
            } else if (angle2turn < -0.0625) {
                turnLeft(speed*angle2turn/17 + speed/3);
            } else {
                turnLeft(angle2turn);
            }
        } while ( now < 1.5 && !opmode.isStopRequested() );

        stop();
    }

    public void turnAngle(double speed, double angle) {
        imu.update();
        turn2Angle(speed, angle + imu.trueAngle);
    }

    //setting power to 0
    public void stop() {
        FrontRightM.setPower(0);
        FrontLeftM.setPower(0);
        BackRightM.setPower(0);
        BackLeftM.setPower(0);
    }

    public void tankDrive(double leftSpeed, double rightSpeed, double time) {
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

    public void forwardUntilDistance(double speed, double distance) {
        double distance2drive;

        double start = 0;
        double now = 0;
        double interval = 0;
        start = runtime.seconds();

        do {
            distance2drive = (distance - Tof.getDistance3());

            moveForward(speed);

        } while (distance2drive > 12 && !opmode.isStopRequested() );

        stop();

        TimeDelay(0.05);

        now = 0;

        do {
            //
            now = runtime.seconds() - start;

            if (now >= interval){
                interval += 0.1;
                opmode.telemetry.addData("trueAngle", imu.trueAngle);
                opmode.telemetry.update();
            }
            //

            distance2drive = (distance - Tof.getDistance3());

            if (distance2drive > 0.0625) {
                moveForward(speed*distance2drive/17 + speed/3);
            } else if (distance2drive < -0.0625) {
                moveBackward(speed*distance2drive/17 + speed/3);
            } else {
                moveBackward(distance2drive);
            }
        } while ( now < 1.5 && !opmode.isStopRequested() );

    }



    public void TimeDelay(double time) {
        double start = 0;
        double now = 0;
        start = runtime.seconds();
        do {
            now = runtime.seconds() - start;

            imu.update();

        } while ((now < time) && !opmode.isStopRequested() );

        opmode.telemetry.addData("trueAngle", imu.trueAngle);
        opmode.telemetry.update();


    }
}
