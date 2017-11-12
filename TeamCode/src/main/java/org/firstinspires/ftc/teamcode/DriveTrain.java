package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by pston on 10/22/2017
 */

public class DriveTrain {

    //////////////////////////////////// MOVE

    //objects for all the drive train motors
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

    //////////////////////////////////// GYRO

    private IntegratingGyroscope gyro;

    private float goalDegrees;
    private int goalPosition;
    private int gyroRange;
    private static final double countsPerMotorRev = 1440;
    private static final double driveGearReduction = 1.5;
    private static final double wheelDiameterInches = 4.0;
    private static final double countsPerInch = (countsPerMotorRev * driveGearReduction) / (wheelDiameterInches * Math.PI);
    //////////////////////////////////// SETUP

    private Telemetry telemetry;

    private boolean encodersCanRun;
    private double goalEncoderPosition;
    private double goalBackwardPosition;
    private double goalLeftPosition;
    private double goalRightPosition;
    private double goalRightStrafePosition;
    private double goalLeftStrafePosition;

    private double startEncoderValue;

    private double currentGyro;

    private double power;

    private double degreesSetup;
    private int zonedDegrees;
    private float yaw;
    //////////////////////////////////// CONSTRUCT

    //calls the second constructor of DriveTrain and passes a reference to the hardware map, telemetry, the 4 string names of the motors in the order left front, right front, left back, right back and the port reference to the gyro.
    public DriveTrain(DcMotor leftFront, DcMotor rightFront, DcMotor leftRear, DcMotor rightRear, IntegratingGyroscope gyro, Telemetry telemetry) {
        telemetry.addData("DriveTrain Startup", "Beginning");
        telemetry.update();
        //setup for all the motors.
        this.leftFront = leftFront;
        this.rightFront = rightFront;
        this.leftRear = leftRear;
        this.rightRear = rightRear;

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //sets all the motors power to zero, this prevents the motors from spinning at the beginning of the program
        this.leftFront.setPower(0);
        this.rightFront.setPower(0);
        this.leftRear.setPower(0);
        this.rightRear.setPower(0);

        this.gyro = gyro;

        //misc setup

        this.goalDegrees = -1;
        this.goalPosition = -1;
        this.gyroRange = 3;

        this.telemetry = telemetry;
        this.encodersCanRun = true;
        //Starts the goal for the encoders at a neutral value, so no leftover values mess with the program
        this.startEncoderValue = -1;

        this.goalEncoderPosition = startEncoderValue;
        goalBackwardPosition = -1;
        telemetry.addData("DriveTrain Startup", "End");
        telemetry.update();
    }
    //////////////////////////////////// MOVE

    public void setMotorPower(double x, double y, double z){
        /*
        Guide to motor Powers:
        Left Front: - (y + x + Z)
        Right Front: y - x - z
        Left Rear: y + x - z
        Right Rear: - (y - x + z)
         */
        this.leftFront.setPower(Range.clip(-(y+x+z),-1,1));
        this.rightFront.setPower(Range.clip((y-x-z),-1,1));
        this.leftRear.setPower(Range.clip(-(y-x+z),-1,1));
        this.rightRear.setPower(Range.clip((y+x-z), -1, 1));
    }

    //A zoned drive that makes it easier to move in the sixteen different directions listed, using the joystick
    public void zonedDrive(int zone, double x, double y, double z) {
        power = Math.sqrt((x*x) + (y*y));
        switch(zone) {
            case 0:
                setMotorPower(power, 0, z);
                telemetry.addData("Current Movement", Direction.E);
                break;
            case 1:
                setMotorPower(power, (power*.41), z);
                telemetry.addData("Current Movement", Direction.ENE);
                break;
            case 2:
                setMotorPower(power, power, z);
                telemetry.addData("Current Movement", Direction.NE);
                break;
            case 3:
                setMotorPower((power*.41), power, z);
                telemetry.addData("Current Movement", Direction.NNE);
                break;
            case 4:
                setMotorPower(0, power, z);
                telemetry.addData("Current Movement", Direction.N);
                break;
            case 5:
                setMotorPower(-(power*.41), power, z);
                telemetry.addData("Current Movement", Direction.NNW);
                break;
            case 6:
                setMotorPower(-power, power, z);
                telemetry.addData("Current Movement", Direction.NW);
                break;
            case 7:
                setMotorPower(-power, (power*.41), z);
                telemetry.addData("Current Movement", Direction.WNW);
                break;
            case 8:
                setMotorPower(-power, 0, z);
                telemetry.addData("Current Movement", Direction.W);
                break;
            case 9:
                setMotorPower(-power, -(power*.41), z);
                telemetry.addData("Current Movement", Direction.WSW);
                break;
            case 10:
                setMotorPower(-power, -power, z);
                telemetry.addData("Current Movement", Direction.SW);
                break;
            case 11:
                setMotorPower(-(power*.41), -power, z);
                telemetry.addData("Current Movement", Direction.SSW);
                break;
            case 12:
                setMotorPower(0, -power, z);
                telemetry.addData("Current Movement", Direction.S);
                break;
            case 13:
                setMotorPower((power*.41), -power, z);
                telemetry.addData("Current Movement", Direction.SSE);
                break;
            case 14:
                setMotorPower(power, -power, z);
                telemetry.addData("Current Movement", Direction.SE);
                break;
            case 15:
                setMotorPower(power, -(power*.41), z);
                telemetry.addData("Current Movement", Direction.ESE);
                break;
        }
    }

    public void drive(Direction direction, double power){
        switch (direction) {
            case N:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(power);
                break;
            case S:
                this.leftFront.setPower(power);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(-power);
                break;
            case W:
                this.leftFront.setPower(power);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(-power);
                break;
            case E:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(power);
                break;
            case NW:
                this.leftFront.setPower(0);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(0);
                break;
            case SW:
                this.leftFront.setPower(0);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(0);
                break;
            case NE:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(0);
                this.leftRear.setPower(0);
                this.rightRear.setPower(power);
                break;
            case SE:
                this.leftFront.setPower(power);
                this.rightFront.setPower(0);
                this.leftRear.setPower(0);
                this.rightRear.setPower(-power);
                break;
            case TURNLEFT:
                this.leftFront.setPower(power);
                this.rightFront.setPower(power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(power);
                break;
            case TURNRIGHT:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(-power);
                break;
            case ENE:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(-power/2);
                this.leftRear.setPower(power/2);
                this.rightRear.setPower(power);
                break;
            case NNE:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(power/2);
                this.leftRear.setPower(-power/2);
                this.rightRear.setPower(power);
                break;
            case NNW:
                this.leftFront.setPower(-power/2);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(power/2);
                break;
            case WNW:
                this.leftFront.setPower(power/2);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(-power/2);
                break;
            case WSW:
                this.leftFront.setPower(power);
                this.rightFront.setPower(power/2);
                this.leftRear.setPower(-power/2);
                this.rightRear.setPower(-power);
                break;
            case SSW:
                this.leftFront.setPower(power/2);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(-power/2);
                break;
            case SSE:
                this.leftFront.setPower(power);
                this.rightFront.setPower(-power/2);
                this.leftRear.setPower(power/2);
                this.rightRear.setPower(-power);
                break;
            case ESE:
                this.leftFront.setPower(-power/2);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(power/2);
                break;
        }
    }

    public void stop() {
        this.leftFront.setPower(0);
        this.leftRear.setPower(0);
        this.rightFront.setPower(0);
        this.rightRear.setPower(0);
    }

    public boolean encoderDrive(Direction direction, double power, double inches) {

        int combinedEnValue = ((getLeftEncoder() + getRightEncoder()) / 2);
        int leftStrafeValue = ((-getLeftEncoder() + getRightEncoder()) / 2);
        int rightStrafeValue = ((getLeftEncoder() + -getRightEncoder()) / 2);

        if (encodersCanRun){
            currentGyro = getYaw();
            goalEncoderPosition = (combinedEnValue + (inches * countsPerInch));
            goalBackwardPosition = (combinedEnValue - (inches * countsPerInch));
            goalLeftPosition = (getLeftEncoder() + (inches * countsPerInch));
            goalRightPosition = (getRightEncoder() + (inches * countsPerInch));
            goalRightStrafePosition = (rightStrafeValue + (inches * countsPerInch));
            goalLeftStrafePosition = (leftStrafeValue + (inches *countsPerInch));
            encodersCanRun = false;
            return encodersCanRun;
        } else {
            switch (direction) {

                case N:
                    if (combinedEnValue < goalEncoderPosition) {
                        drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                    } else {
                        encodersCanRun = true;
                        //resets the encoders to a neutral value
                        goalEncoderPosition = startEncoderValue;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case S:
                    if (combinedEnValue > goalBackwardPosition) {
                        drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                    } else {
                        encodersCanRun = true;
                        //resets the encoders to a neutral value
                        goalEncoderPosition = startEncoderValue;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case W:
                    if (leftStrafeValue < goalLeftStrafePosition) {
                        drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                        telemetry.addData("Goal Value", goalLeftStrafePosition);
                    } else {
                        encodersCanRun = true;
                        //resets the encoders to a neutral value
                        goalEncoderPosition = startEncoderValue;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case E:
                    if (rightStrafeValue < goalRightStrafePosition) {
                        drive(direction, power);
                        telemetry.addData("Current Combined Value", rightStrafeValue);
                        telemetry.addData("Current Goal Value", goalRightStrafePosition);
                        telemetry.addData("Current Comparison Value", goalEncoderPosition);
                    } else {
                        encodersCanRun = true;
                        //resets the encoders to a neutral value
                        goalEncoderPosition = startEncoderValue;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case NE:
                    if (getLeftEncoder() < goalLeftPosition) {
                        drive(direction, power);
                        if (getYaw() < currentGyro) {
                            rightFront.setPower(-power);
                        } else {
                            leftFront.setPower(0);
                        }
                        telemetry.addData("Current Right Encoder Value", getLeftEncoder());
                        telemetry.addData("Current Yaw", getYaw());
                    } else {
                        encodersCanRun = true;
                        //resets the encoders to a neutral value
                        goalEncoderPosition = startEncoderValue;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case SE:
                    break;
                case NW:
                    if (getRightEncoder() < goalRightPosition) {
                        drive(direction, power);
                        if (getYaw() < currentGyro) {
                            leftFront.setPower(-power);
                        } else {
                            leftFront.setPower(0);
                        }
                        telemetry.addData("Current Right Encoder Value", getRightEncoder());
                        telemetry.addData("Current Yaw", getYaw());
                    } else {
                        encodersCanRun = true;
                        //resets the encoders to a neutral value
                        goalEncoderPosition = startEncoderValue;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case SW:
                    break;
                case TURNLEFT:
                    break;
                case TURNRIGHT:
                    if (getLeftEncoder() < goalLeftPosition) {
                        drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                        telemetry.addData("Goal Value", goalEncoderPosition);
                    } else {
                        encodersCanRun = true;
                        //resets the encoders to a neutral value
                        goalEncoderPosition = startEncoderValue;
                        stop();
                        return encodersCanRun;
                    }
                    break;
            }
            return encodersCanRun;
        }
    }

    //////////////////////////////////// GYRO

    public boolean gyroTurn(Direction direction, double power, float degrees){
        switch(direction) {
            case TURNLEFT:
                if (goalDegrees == -1) {
                    goalDegrees = (this.getYaw() + degrees);
                    if (goalDegrees > 360) {
                        goalDegrees = (goalDegrees - 360);
                    }
                }
                if (!(this.getYaw() > (goalDegrees + gyroRange) && this.getYaw() > (goalDegrees - gyroRange))) {
                    drive(direction, power);

                    telemetry.addData("Goal Degrees", goalDegrees);
                    telemetry.addData("Current Degrees", getYaw());
                    return false;
                } else {
                    this.stop();
                    goalDegrees = -1;
                    return true;
                }
            case TURNRIGHT:
                if (goalDegrees == -1) {
                    goalDegrees = (this.getYaw() - degrees);
                    if (goalDegrees < 0) {
                        goalDegrees = (goalDegrees + 360);
                    }
                }
                if (!(this.getYaw() < (goalDegrees + gyroRange) && this.getYaw() > (goalDegrees - gyroRange))) {
                    drive(direction, power);
                    telemetry.addData("Goal Degrees", goalDegrees);
                    telemetry.addData("Current Degrees", getYaw());
                    return false;
                } else {
                    this.stop();
                    goalDegrees = -1;
                    return true;
                }
        }
        return false;
    }

    public float getYaw(){
        Orientation angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if(AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle) < 0) {
            return (360 + AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle));
        } else {
            return AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle);
        }
    }

    //Experiment with using a field oriented drive instead of a robot oriented drive
    public void experimentalDrive(double x, double y, double z){
        /*
        Guide to motor Powers:
        Left Front: - (y + x + Z)
        Right Front: y - x - z
        Left Rear: y + x - z
        Right Rear: - (y - x + z)
         */

        double forward = x; //Invert stick X axis
        double strafe = y;

        double pi = Math.PI;

        /* Adjust Joystick X/Y inputs by navX MXP yaw angle */

        double gyro_radians = getYaw() * pi/180;
        double temp = forward * cos(gyro_radians) + strafe * sin(gyro_radians);
        strafe = -forward * sin(gyro_radians) + strafe * cos(gyro_radians);
        forward = temp;

        /* At this point, Joystick X/Y (strafe/forward) vectors have been */
        /* rotated by the gyro angle, and can be sent to drive system */

        this.leftFront.setPower(Range.clip(-(forward+strafe+z),-1,1));
        this.rightFront.setPower(Range.clip((forward-strafe-z),-1,1));
        this.leftRear.setPower(Range.clip(-(forward-strafe+z),-1,1));
        this.rightRear.setPower(Range.clip((forward+strafe-z), -1, 1));
    }

    public double currentDegrees(double x, double y) {
        degreesSetup = Math.atan2(y, x);
        if ((degreesSetup * (180/Math.PI)) < 0) {
            return (360 + degreesSetup * (180/Math.PI));
        } else {
            return degreesSetup * (180/Math.PI);
        }
    }

    public double currentZone(double x, double y) {
        zonedDegrees = ((int)(Math.round(currentDegrees(x, y) / 22.5)));
        if (zonedDegrees <= 15) {
            return zonedDegrees;
        } else {
            return 0;
        }
    }

    public void resetEncoders() {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public boolean straighten(double goal) {
        yaw = this.getYaw();
        if (yaw > (goal - 1) && yaw < (goal + 1)) {
            if (yaw > (goal - 1)) {
                drive(Direction.TURNLEFT, 0.2);
            } else if (yaw < (goal + 1)) {
                drive(Direction.TURNRIGHT, 0.2);
            }
            return false;
        } else {
            return true;
        }
    }

    public void displayEncoders() {
        telemetry.addData("Right Front Encoder: ", rightFront.getCurrentPosition());
        telemetry.addData("Left Front Encoder: ", leftFront.getCurrentPosition());
        telemetry.addData("Right Rear Encoder: ", rightRear.getCurrentPosition());
        telemetry.addData("Left Rear Encoder: ", leftRear.getCurrentPosition());
    }

    //////////////////////////////////// ENCODER

    public int getLeftEncoder() {return -this.leftFront.getCurrentPosition();}
    public int getRightEncoder() {return this.rightFront.getCurrentPosition();}
    public double encoderInchesRight() {return (this.getRightEncoder() / (1440 * 1.5) / (4 * Math.PI));}
    public double encoderInchesLeft() {return (this.getLeftEncoder() / (1440 * 1.5) / (4 * Math.PI));}
    //////////////////////////////////// ENUMS

    public enum Direction{
        N,
        S,
        W,
        E,
        NE,
        SE,
        NW,
        SW,
        TURNLEFT,
        TURNRIGHT,
        ENE,
        NNE,
        NNW,
        WNW,
        WSW,
        SSW,
        SSE,
        ESE
    }
}
