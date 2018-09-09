package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotutil.DriveTrain;
import org.firstinspires.ftc.teamcode.robotutil.Functions;
import org.firstinspires.ftc.teamcode.robotutil.IMU;

/**
 * Created by Howard on 10/15/16.
 */
public class DriveTrainTask extends TaskThread {

    private DcMotor lF, rF, lB, rB;
    private BNO055IMU adaImu;
    private IMU imu;
    DriveTrain driveTrain;
    boolean balance;


    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public double zeroAngle, joyStickAngle, gyroAngle;

    public DriveTrainTask(LinearOpMode opMode) {
        this.opMode = opMode;

        initialize();
    }

    @Override
    public void run() {
        timer.reset();
        while (opMode.opModeIsActive() && running) {
//            balance = opMode.gamepad1.start;
            balance = false;

            double r = Math.hypot(opMode.gamepad1.left_stick_x, opMode.gamepad1.left_stick_y);
            double robotAngle = Math.atan2(opMode.gamepad1.left_stick_y, opMode.gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -opMode.gamepad1.right_stick_x;
            final double frontLeft = r * Math.cos(robotAngle) + rightX;
            final double frontRight = r * Math.sin(robotAngle) - rightX;
            final double backLeft = r * Math.sin(robotAngle) + rightX;
            final double backRight = r * Math.cos(robotAngle) - rightX;

            if(balance){
                selfBalance();
            } else{
                lF.setPower(-frontLeft);
                rF.setPower(-frontRight);
                lB.setPower(-backLeft);
                rB.setPower(-backRight);
            }

        }
    }
    @Override
    public void initialize() {

        lF = opMode.hardwareMap.dcMotor.get("lF");
        rF = opMode.hardwareMap.dcMotor.get("rF");
        lB = opMode.hardwareMap.dcMotor.get("lB");
        rB = opMode.hardwareMap.dcMotor.get("rB");

//        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        lB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rF.setDirection(DcMotorSimple.Direction.REVERSE);
        rB.setDirection(DcMotorSimple.Direction.REVERSE);
        lB.setDirection(DcMotorSimple.Direction.FORWARD);
        lF.setDirection(DcMotorSimple.Direction.FORWARD);
        adaImu = opMode.hardwareMap.get(BNO055IMU.class, "imu");

        imu = new IMU(adaImu);

        driveTrain = new DriveTrain(opMode);


    }
    public void selfBalance() {
        // call when the robot is fully on the platform
        /* also assumes that after 0 degrees to the right starts incrementing positively
         * and before 0 degrees to the left is incrementing negatively
        */
        //using trig you know that the robot will be stable if the angle is less than 10 degrees. Thus, we will have a threshold of 5
        boolean pitchDone = false;
        boolean rollDone = false;
        int checkTimeMS = 1000;

        while (opMode.opModeIsActive() && balance) {
            double pitch = IMU.getOrientation()[1];
            double roll = IMU.getOrientation()[2];
            opMode.telemetry.addData("Initial Pitch", pitch);
            opMode.telemetry.addData("Initial Roll", roll);

            //initialized as false to check again
            while ((!pitchDone || !rollDone)&& opMode.opModeIsActive() && balance) {
                pitch = IMU.getOrientation()[1];
                roll = IMU.getOrientation()[2];
                opMode.telemetry.addData("Pitch", pitch);
                opMode.telemetry.addData("Roll", roll);
                if (pitch > driveTrain.balanceThreshold && !pitchDone) {
                    driveTrain.moveAtSpeed(DriveTrain.Direction.BACKWARD, driveTrain.powerPerDegree(pitch));
                } else if (pitch < -driveTrain.balanceThreshold) {
                    driveTrain.moveAtSpeed(DriveTrain.Direction.FORWARD, driveTrain.powerPerDegree(pitch));
                } else {
                    pitchDone = true;
                    driveTrain.stopAll();
                    opMode.telemetry.addLine("Pitch Done");
                }

                if (roll > driveTrain.balanceThreshold && !rollDone) {
                    driveTrain.moveAtSpeed(DriveTrain.Direction.LEFT, driveTrain.powerPerDegree(roll));
                } else if (roll < -driveTrain.balanceThreshold) {
                    driveTrain.moveAtSpeed(DriveTrain.Direction.RIGHT, driveTrain.powerPerDegree(roll));
                } else {
                    rollDone = true;
                    opMode.telemetry.addLine("Roll Done");
                    driveTrain.stopAll();
                }
//                opMode.telemetry.update();
                //Functions.waitFor(50);
            }
            Functions.waitFor(checkTimeMS);
            //CHECK TO SEE IF OVERSHOT AND IS STILL WITHIN THRESHOLD
            if(Math.abs(pitch)<driveTrain.balanceThreshold && Math.abs(roll)<driveTrain.balanceThreshold){
                opMode.telemetry.addLine("Overall Done");
//                opMode.telemetry.update();
                driveTrain.stopAll();
                break;
            }
            pitchDone=false;
            rollDone=false;
        }

    }
}
