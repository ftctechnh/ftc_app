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

            if (angle2turn > 12) {
                turnRight(speed);
            } else if (angle2turn < -12) {
                turnLeft(speed);
            }

        } while ( (angle2turn > 12 || angle2turn < -12) && !opmode.isStopRequested() && opmode.opModeIsActive() );

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
                turnLeft(-speed*angle2turn/17 + speed/3);
            } else {
                turnLeft(angle2turn);
            }
        } while ( now < 1.2 && !opmode.isStopRequested() && opmode.opModeIsActive() );

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
            //
            now = runtime.seconds() - start;

            if (now >= interval){
                interval += 0.1;
                opmode.telemetry.addLine("Distance3: " + Tof.getDistance3());
                opmode.telemetry.update();
            }
            //

            distance2drive = (Tof.getDistance3() - distance);

            moveForward(speed);

        } while (distance2drive > 20 && !opmode.isStopRequested() && opmode.opModeIsActive() );

        stop();

        TimeDelay(0.1);

        now = 0;

        do {
            //
            now = runtime.seconds() - start;

            if (now >= interval){
                interval += 0.1;
                opmode.telemetry.addLine("Distance3: " + Tof.getDistance3());
                opmode.telemetry.update();
            }
            //

            distance2drive = (Tof.getDistance3() - distance);

            if (distance2drive > 1.0) {
                moveForward(speed*distance2drive/20 + speed/6);
            } else if (distance2drive < -1.0) {
                moveBackward(-speed*distance2drive/20 + speed/6);
            } else {
                moveBackward(distance2drive/100);
            }
        } while ( now < 1.5 && !opmode.isStopRequested() && opmode.opModeIsActive() );

        stop();

    }


    public void forwardUntilDistance4Time(double speed, double distance, double time) {
        double distance2drive;

        double start = 0;
        double now = 0;
        double interval = 0;
        start = runtime.seconds();

        do {
            //
            now = runtime.seconds() - start;

            if (now >= interval){
                interval += 0.1;
                opmode.telemetry.addLine("Distance3: " + Tof.getDistance3());
                opmode.telemetry.update();
            }
            //

            distance2drive = (Tof.getDistance3() - distance);

            moveForward(speed);

        } while (distance2drive > 20 && now < time && !opmode.isStopRequested() && opmode.opModeIsActive() );

        stop();

        TimeDelay(0.1);

        if (now < time) {
            now = 0;
        }

        do {
            //
            now = runtime.seconds() - start;

            if (now >= interval){
                interval += 0.1;
                opmode.telemetry.addLine("Distance3: " + Tof.getDistance3());
                opmode.telemetry.update();
            }
            //

            distance2drive = (Tof.getDistance3() - distance);

            if (distance2drive > 1.0 && now < time) {
                moveForward(speed*distance2drive/20 + speed/6);
            } else if (distance2drive < -1.0 && now < time) {
                moveBackward(-speed*distance2drive/20 + speed/6);
            } else {
                moveBackward(distance2drive/100);
            }
        } while ( now < 1.5 && !opmode.isStopRequested() && opmode.opModeIsActive() );

        stop();

    }



    public void TimeDelay(double time) {
        double start = 0;
        double now = 0;
        start = runtime.seconds();
        do {
            now = runtime.seconds() - start;

            imu.update();

        } while ((now < time) && !opmode.isStopRequested() );

        opmode.telemetry.addLine("trueAngle: " + imu.trueAngle);
        opmode.telemetry.addLine("Distance: " + Tof.getDistance3());
        opmode.telemetry.update();


    }
}
