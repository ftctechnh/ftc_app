package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;
import org.firstinspires.ftc.teamcode.framework.userHardware.PIDController;
import org.firstinspires.ftc.teamcode.framework.util.SubsystemController;

import java.text.DecimalFormat;

import static java.lang.Math.*;
import static org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode.isOpModeActive;
import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Constants.*;
import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.RobotState.*;

public class DriveController extends SubsystemController {

    private Drive drive;

    private PIDController anglePID;

    private PIDController distancePID;

    private double motorPosition, baseHeading = 0;

    private double turnY = 0, turn_z = 0, leftPower = 0, rightPower = 0, Drive_Power = 1.0;

    public ElapsedTime runtime;

    private DecimalFormat DF;

    //Utility Methods
    public DriveController() {
        init();
    }

    public synchronized void init() {

        opModeSetup();

        runtime = new ElapsedTime();

        DF = new DecimalFormat("#.##");

        //Put general setup here
        drive = new Drive(hardwareMap);
        //anglePID = new PIDController(10,1.6,24, 2);
        anglePID = new PIDController(8, 0.5, 10, 1, 0);
        distancePID = new PIDController(0.6, 0.1, 0, 2);
        drive.setSlewSpeed(0.1);
    }

    public synchronized void update() {
        telemetry.addData(DoubleTelemetry.LogMode.TRACE,"Left drive power: "+drive.getLeftPower());
        telemetry.addData(DoubleTelemetry.LogMode.TRACE,"Right drive power: "+drive.getRightPower());
        telemetry.addData(DoubleTelemetry.LogMode.TRACE,"Left drive position: "+drive.getLeftPosition());
        telemetry.addData(DoubleTelemetry.LogMode.TRACE,"Right drive position: "+drive.getRightPosition());
    }

    public synchronized void stop() {
        drive.stop();
    }

    //Autonomous Methods
    public synchronized void turnTo(double angle, double speed, double error, int period) {
        AbstractOpMode.delay(100);

        baseHeading = angle;

        telemetry.addData("starting turn segment---------------------------");
        anglePID.reset();
        anglePID.setMinimumOutput(0);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double power;
        while (isOpModeActive()) {
            //While we are not in the error band keep turning
            while (!atPosition(angle, drive.getHeading(), error) && isOpModeActive()) {
                telemetry.addDataDB("--------------------");
                //Use the PIDController class to calculate power values for the wheels
                if (angle - getHeading() > 180) {
                    power = anglePID.output(angle, 180 + (180 + getHeading()));
                    telemetry.addData("How Far", 180 + (180 + getHeading()));
                } else {
                    power = anglePID.output(angle, getHeading());
                    telemetry.addData("How Far", getHeading());
                }
                setPower(-power * speed, power * speed);
                telemetry.addData("Heading", getHeading());
                telemetry.addData("Power", power);
                telemetry.update();
            }
            runtime.reset();
            while (runtime.milliseconds() < period) {
                if ((abs(getHeading() - angle)) > error && (abs(getHeading() + angle)) > error)
                    break;
            }
            if ((abs(getHeading() - angle)) > error && (abs(getHeading() + angle)) > error)
                continue;
            telemetry.addData("Final heading", getHeading());
            telemetry.update();
            drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            return;
        }
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public synchronized void driveTo(double distance, double speed) {
        driveTo(distance, speed, (int) baseHeading);
    }

    public synchronized void driveTo(double distance, double speed, int angle) {
        baseHeading = angle;

        AbstractOpMode.delay(100);

        telemetry.addData("starting drive segment---------------------------");
        anglePID.reset(); //Resets the PID values in the PID class to make sure we do not have any left over values from the last segment
        distancePID.reset();
        anglePID.setMinimumOutput(0);
        int position = (int) (distance * DRIVE_COUNTS_PER_INCH); //
        telemetry.addData("Encoder counts: " + position);
        double turn;
        speed = range(speed);
        telemetry.addData("Speed: " + speed);
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.setPosisionP(5);
        double startHeading = angle;
        telemetry.addData("Start Heading: " + startHeading);
        telemetry.update();
        double leftPower, rightPower;
        double power;
        while ((!atPosition(position, drive.getLeftPosition(), 15) && !atPosition(position, drive.getRightPosition(), 15)) && isOpModeActive()) {
            power = range(distancePID.output(position, drive.getRightPosition()));

            turn = anglePID.output(startHeading, getHeading());

            if (power > 0) {
                leftPower = range(power * (speed - turn));
                rightPower = range(power * (speed + turn));
            } else {
                leftPower = range(power * (speed + turn));
                rightPower = range(power * (speed - turn));
            }

            drive.setPower(leftPower, rightPower);

            telemetry.addData("Encoder counts: " + position);
            telemetry.addData("Left Position: " + drive.getLeftPosition());
            telemetry.addData("Right Position: " + drive.getRightPosition());
            telemetry.addData("Left Power: " + leftPower);
            telemetry.addData("Right Power: " + rightPower);
            telemetry.addData("Heading: " + getHeading());
            telemetry.addData("PID Output: " + turn);
            telemetry.update();
        }

        for (int i = 0; i < 5; i++) {
            power = range(distancePID.output(position, (drive.getRightPosition() + drive.getLeftPosition()) / 2));

            turn = anglePID.output(startHeading, getHeading());

            if (power > 0) {
                leftPower = range(power * (speed - turn));
                rightPower = range(power * (speed + turn));
            } else {
                leftPower = range(power * (speed + turn));
                rightPower = range(power * (speed - turn));
            }

            drive.setPower(leftPower, rightPower);

            telemetry.addData("Encoder counts: " + position);
            telemetry.addData("Left Position: " + drive.getLeftPosition());
            telemetry.addData("Right Position: " + drive.getRightPosition());
            telemetry.addData("Left Power: " + leftPower);
            telemetry.addData("Right Power: " + rightPower);
            telemetry.addData("Heading: " + getHeading());
            telemetry.update();
        }

        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public synchronized void setPosition(int position, double power) {
        this.motorPosition = position;
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPower(range(power), range(power));
        drive.setTargetPosition(position);
    }

    public synchronized double getHeading() {
        return drive.getHeading();
    }

    public synchronized void resetAngleToZero() {
        drive.resetAngleToZero();
    }

    //TeleOp Methods
    public synchronized void setPower(double left, double right) {

        if ((currentMineralLiftState == MineralLiftState.IN_MOTION ||
                currentMineralLiftState == MineralLiftState.DUMP_POSITION) &&
                currentMatchState == MatchState.TELEOP) {
            left *= DRIVE_MINERAL_LIFT_RAISED_SCALAR;
            right *= DRIVE_MINERAL_LIFT_RAISED_SCALAR;
        }

        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        drive.setPower(range(left), range(right));
    }

    public synchronized void setY(double y) {
        if (currentDriveDirection == DriveDirection.REVERSED && DRIVE_AUTO_INVERT) turnY = -y;
        else turnY = y;
        turnY = (float) scaleInput(turnY);
    }

    public synchronized void setZ(double z) {
        turn_z = z;
        turn_z = (float) scaleInput(turn_z);
    }

    public synchronized void updateYZDrive() {
        if ((currentMineralLiftState == MineralLiftState.IN_MOTION ||
                currentMineralLiftState == MineralLiftState.DUMP_POSITION) &&
                currentMatchState == MatchState.TELEOP) {
            leftPower = range((turnY + turn_z) * (Drive_Power * DRIVE_MINERAL_LIFT_RAISED_SCALAR));
            rightPower = range((turnY - turn_z) * (Drive_Power * DRIVE_MINERAL_LIFT_RAISED_SCALAR));
        } else {
            leftPower = range((turnY + turn_z) * Drive_Power);
            rightPower = range((turnY - turn_z) * Drive_Power);
        }

        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drive.setPower(leftPower, rightPower);
    }

    public synchronized void setInverted(boolean inverted) {
        if (inverted) currentDriveDirection = DriveDirection.REVERSED;
        else currentDriveDirection = DriveDirection.FORWARD;
    }

    public synchronized void toggleInverted() {
        if (currentDriveDirection == DriveDirection.FORWARD)
            currentDriveDirection = DriveDirection.REVERSED;
        else currentDriveDirection = DriveDirection.FORWARD;
    }

    //Util Methods
    public synchronized int[][] recordPath(int numSamples, int timeInterval) {
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int[][] values = new int[2][numSamples];
        runtime.reset();
        for (int i = 0; i < numSamples; i++) {
            while (runtime.milliseconds() < timeInterval && isOpModeActive()) ;
            values[0][i] = drive.getLeftPosition();
            values[1][i] = drive.getRightPosition();
            if (!isOpModeActive()) break;
            runtime.reset();
        }
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return values;
    }

    public synchronized void runPath(int[] left, int[] right, int timeInterval) {
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPower(1, 1);
        runtime.reset();
        for (int i = 0; i < (left.length - right.length <= 0 ? right.length : left.length); i++) {
            while (runtime.milliseconds() < timeInterval && isOpModeActive()) ;
            drive.setTargetPosition(left[i], right[i]);
            if (!isOpModeActive()) break;
            runtime.reset();
        }
    }

    public synchronized int[] recordPathWithHeading(int numSamples, int timeInterval) {
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int[] values = new int[numSamples * 3];
        runtime.reset();
        for (int i = 0; i < numSamples * 3; i += 3) {
            while (runtime.milliseconds() < timeInterval && isOpModeActive()) ;
            telemetry.addData(drive.getHeading());
            telemetry.update();
            values[i] = (int) drive.getHeading();
            values[i + 1] = drive.getLeftPosition();
            values[i + 2] = drive.getRightPosition();
            if (!isOpModeActive()) break;
            runtime.reset();
        }
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return values;
    }

    public synchronized void runPathWithHeading(int[] values, int timeInterval, double speed) {
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drive.setPower(1, 1);
        runtime.reset();
        double turn;
        for (int i = 0; i < values.length; i += 3) {
            while (runtime.milliseconds() < timeInterval && isOpModeActive()) ;
            turn = anglePID.output(values[i], getHeading());
            drive.setPower(speed - turn, speed + turn);
            drive.setTargetPosition(values[i + 1], values[i + 2]);
            if (!isOpModeActive()) break;
            runtime.reset();
        }
    }

    private synchronized double scaleInput(double val) {
        return (range(pow(val, 3)));
    }

    private synchronized double range(double val) {
        if (val < -1) val = -1;
        if (val > 1) val = 1;
        return val;
    }

    private boolean atPosition(double x, double y, double error) {
        double upperRange = x + error;
        double lowerRange = x - error;

        return y >= lowerRange && y <= upperRange;
    }

    public synchronized boolean isGyroCalibrated() {
        return drive.isGyroCalibrated();
    }
}