package org.firstinspires.ftc.teamcode;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by pston on 10/22/2017
 */

public class DriveTrain {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MOVE V
    //objects for all the drive train motors
    //objects for controlling the front two motors
    private DcMotor leftFront;
    //object for controlling the left front motor
    private DcMotor rightFront;
    //object for controlling the right front motor

    //objects for controlling the rear two motors
    private DcMotor leftRear;
    //object for controlling the left rear motor
    private DcMotor rightRear;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GYRO V
    private AHRS gyro;
    private AHRS.DimStateTracker gyroReset;
    //gyro sensor

    //variables for gyro sensor
    private float goalDegrees;
    // ???
    private int goalPosition;
    // ???
    private int gyroRange;
    // ???
    private static final double     countsPerMotorRev    = 1440 ;
    private static final double     driveGearReduction    = 1.5 ;
    private static final double     wheelDiameterInches   = 4.0 ;
    private static final double     countsPerInch         = (countsPerMotorRev * driveGearReduction) / (wheelDiameterInches * Math.PI);
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// SETUP V
    private String LeftFront = "leftfront";
    private String RightFront = "rightfront";
    private String LeftRear = "leftrear";
    private String RightRear = "rightrear";
    private final int gyroPort = 3;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// MISS V
    private Telemetry telemetry;        //do we need this?
    //object for reference (telemetry)
    private boolean encodersCanRun;
    private double goalEncoderPosition;
    private double goalBackwardPosition;
    private double goalLeftPosition;
    private double goalRightPosition;
    private double goalRightStrafePosition;
    private double goalLeftStrafePosition;

    private double testLiftPosition = 1;

    private double currentGyro;

    private double power;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCT
    //calls the second constructor of DriveTrain and passes a reference to the hardware map, telemetry, the 4 string names of the motors in the order left front, right front, left back, right back and the port reference to the gyro.
    public DriveTrain(HardwareMap hardwareMap, Telemetry telemetry) {
        telemetry.addData("DriveTrain Startup", "Beginning");
        telemetry.update();
        //setup for all the motors.
        //setup for all the front motors.
        this.leftFront = hardwareMap.dcMotor.get(LeftFront);
        //setup for the left front motor.
        this.rightFront = hardwareMap.dcMotor.get(RightFront);
        //setup for the right front motor.
        //setup for all the back motors.
        this.leftRear = hardwareMap.dcMotor.get(LeftRear);
        //setup for the left back motor.
        this.rightRear = hardwareMap.dcMotor.get(RightRear);
        //setup for the right back motor.

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //sets all the motors power to zero.
        //do we need this?
        this.leftFront.setPower(0);
        this.rightFront.setPower(0);
        this.leftRear.setPower(0);
        this.rightRear.setPower(0);
        //gyro sensor setup.
        this.gyro = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get("dim"), gyroPort, AHRS.DeviceDataType.kProcessedData);
        //sets up the gyro sensor.
        this.gyro.zeroYaw();
        gyroReset = gyro.getDimStateTrackerInstance();

        //sets the gyro sensors position to zero.
        gyroReset.reset();

        //miss setup

        this.goalDegrees = -1;
        // ???
        this.goalPosition = -1;
        // ???
        this.gyroRange = 3;

        this.telemetry = telemetry;
        //do we need this?
        this.encodersCanRun = true;
        this.goalEncoderPosition = -1;
        goalBackwardPosition = -1;
        telemetry.addData("DriveTrain Startup", "End");
        telemetry.update();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//     MOVE
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

    public void zonedDrive(int zone, double x, double y, double z) {
        power = Math.sqrt((x*x) + (y*y));
        switch(zone) {
            case 0:
                setMotorPower(power, 0, z);
                telemetry.addData("Current Movement", Direction.STRAFERIGHT);
                break;
            case 1:
                setMotorPower(power, (power*.41), z);
                telemetry.addData("Current Movement", Direction.DSTRAFERIGHTUP);
                break;
            case 2:
                setMotorPower(power, power, z);
                telemetry.addData("Current Movement", Direction.DRIGHTUP);
                break;
            case 3:
                setMotorPower((power*.41), power, z);
                telemetry.addData("Current Movement", Direction.DUPSTRAFERIGHT);
                break;
            case 4:
                setMotorPower(0, power, z);
                telemetry.addData("Current Movement", Direction.FORWARD);
                break;
            case 5:
                setMotorPower(-(power*.41), power, z);
                telemetry.addData("Current Movement", Direction.DUPSTRAFELEFT);
                break;
            case 6:
                setMotorPower(-power, power, z);
                telemetry.addData("Current Movement", Direction.DLEFTUP);
                break;
            case 7:
                setMotorPower(-power, (power*.41), z);
                telemetry.addData("Current Movement", Direction.DSTRAFELEFTUP);
                break;
            case 8:
                setMotorPower(-power, 0, z);
                telemetry.addData("Current Movement", Direction.STRAFELEFT);
                break;
            case 9:
                setMotorPower(-power, -(power*.41), z);
                telemetry.addData("Current Movement", Direction.DSTRAFELEFTDOWN);
                break;
            case 10:
                setMotorPower(-power, -power, z);
                telemetry.addData("Current Movement", Direction.DLEFTDOWN);
                break;
            case 11:
                setMotorPower(-(power*.41), -power, z);
                telemetry.addData("Current Movement", Direction.DDOWNSTRAFELEFT);
                break;
            case 12:
                setMotorPower(0, -power, z);
                telemetry.addData("Current Movement", Direction.BACKWARD);
                break;
            case 13:
                setMotorPower((power*.41), -power, z);
                telemetry.addData("Current Movement", Direction.DDOWNSTRAFERIGHT);
                break;
            case 14:
                setMotorPower(power, -power, z);
                telemetry.addData("Current Movement", Direction.DRIGHTDOWN);
                break;
            case 15:
                setMotorPower(power, -(power*.41), z);
                telemetry.addData("Current Movement", Direction.DSTRAFERIGHTDOWN);
                break;
        }
    }

    public void Drive(Direction direction, double power){
        switch (direction) {
            case FORWARD:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(power);
                break;
            case BACKWARD:
                this.leftFront.setPower(power);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(-power);
                break;
            case STRAFELEFT:
                this.leftFront.setPower(power);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(-power);
                break;
            case STRAFERIGHT:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(power);
                break;
            case DLEFTUP:
                this.leftFront.setPower(0);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(0);
                break;
            case DLEFTDOWN:
                this.leftFront.setPower(0);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(0);
                break;
            case DRIGHTUP:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(0);
                this.leftRear.setPower(0);
                this.rightRear.setPower(power);
                break;
            case DRIGHTDOWN:
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

            //Possible movement code, not yet tested

            case DSTRAFERIGHTUP:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(-power/2);
                this.leftRear.setPower(power/2);
                this.rightRear.setPower(power);
                break;
            case DUPSTRAFERIGHT:
                this.leftFront.setPower(-power);
                this.rightFront.setPower(power/2);
                this.leftRear.setPower(-power/2);
                this.rightRear.setPower(power);
                break;
            case DUPSTRAFELEFT:
                this.leftFront.setPower(-power/2);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(power/2);
                break;
            case DSTRAFELEFTUP:
                this.leftFront.setPower(power/2);
                this.rightFront.setPower(power);
                this.leftRear.setPower(-power);
                this.rightRear.setPower(-power/2);
                break;
            case DSTRAFELEFTDOWN:
                this.leftFront.setPower(power);
                this.rightFront.setPower(power/2);
                this.leftRear.setPower(-power/2);
                this.rightRear.setPower(-power);
                break;
            case DDOWNSTRAFELEFT:
                this.leftFront.setPower(power/2);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(-power/2);
                break;
            case DDOWNSTRAFERIGHT:
                this.leftFront.setPower(power);
                this.rightFront.setPower(-power/2);
                this.leftRear.setPower(power/2);
                this.rightRear.setPower(-power);
                break;
            case DSTRAFERIGHTDOWN:
                this.leftFront.setPower(-power/2);
                this.rightFront.setPower(-power);
                this.leftRear.setPower(power);
                this.rightRear.setPower(power/2);
                break;

//            case DSTRAFERIGHTUP:
//                this.leftFront.setPower(-power);
//                this.rightFront.setPower(-power/2);
//                this.leftRear.setPower(power/2);
//                this.rightRear.setPower(power);
//                break;
//            case DUPSTRAFERIGHT:
//                this.leftFront.setPower(-power);
//                this.rightFront.setPower(power/2);
//                this.leftRear.setPower(-power/2);
//                this.rightRear.setPower(power);
//                break;
//            case DUPSTRAFELEFT:
//                this.leftFront.setPower(-power/2);
//                this.rightFront.setPower(power);
//                this.leftRear.setPower(-power);
//                this.rightRear.setPower(power/2);
//                break;
//            case DSTRAFELEFTUP:
//                this.leftFront.setPower(power/2);
//                this.rightFront.setPower(power);
//                this.leftRear.setPower(-power);
//                this.rightRear.setPower(-power/2);
//                break;
//            case DSTRAFELEFTDOWN:

        }
    }
    public void stop(){
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

                case FORWARD:
                    if ((combinedEnValue) < goalEncoderPosition) {
                        Drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                    } else {
                        encodersCanRun = true;
                        goalEncoderPosition = -1;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case BACKWARD:
                    if ((combinedEnValue) > goalBackwardPosition) {
                        Drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                    } else {
                        encodersCanRun = true;
                        goalEncoderPosition = -1;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case STRAFELEFT:
                    if ((leftStrafeValue) < goalLeftStrafePosition) {
                        Drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                        telemetry.addData("Goal Value", goalLeftStrafePosition);
                    } else {
                        encodersCanRun = true;
                        goalEncoderPosition = -1;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case STRAFERIGHT:
                    if ((rightStrafeValue) < goalRightStrafePosition) {
                        Drive(direction, power);
                        telemetry.addData("Current Combined Value", rightStrafeValue);
                        telemetry.addData("Current Goal Value", goalRightStrafePosition);
                        telemetry.addData("Current Comparison Value", goalEncoderPosition);
                    } else {
                        encodersCanRun = true;
                        goalEncoderPosition = -1;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case DRIGHTUP:
                    if (getLeftEncoder() < goalLeftPosition) {
                        Drive(direction, power);
                        if (getYaw() < currentGyro) {
                            rightFront.setPower(-power);
                        } else {
                            leftFront.setPower(0);
                        }
                        telemetry.addData("Current Right Encoder Value", getLeftEncoder());
                        telemetry.addData("Current Yaw", getYaw());
                    } else {
                        encodersCanRun = true;
                        goalEncoderPosition = -1;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case DRIGHTDOWN:
                    break;
                case DLEFTUP:
                    if (getRightEncoder() < goalRightPosition) {
                        Drive(direction, power);
                        if (getYaw() < currentGyro) {
                            leftFront.setPower(-power);
                        } else {
                            leftFront.setPower(0);
                        }
                        telemetry.addData("Current Right Encoder Value", getRightEncoder());
                        telemetry.addData("Current Yaw", getYaw());
                    } else {
                        encodersCanRun = true;
                        goalEncoderPosition = -1;
                        stop();
                        return encodersCanRun;
                    }
                    break;
                case DLEFTDOWN:
                    break;
                case TURNLEFT:
                    break;
                case TURNRIGHT:
                    if (getLeftEncoder() < goalLeftPosition) {
                        Drive(direction, power);
                        telemetry.addData("Current Combined Value", combinedEnValue);
                        telemetry.addData("Goal Value", goalEncoderPosition);
                    } else {
                        encodersCanRun = true;
                        goalEncoderPosition = -1;
                        stop();
                        return encodersCanRun;
                    }
                    break;
            }
            return encodersCanRun;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//   GYRO
    public boolean gyroTurn(Direction direction, double power, float degrees){
        switch(direction) {
            case TURNLEFT:
                if (goalDegrees == -1) {
                    goalDegrees = (this.getYaw() - degrees);
                    if (goalDegrees < 0) {
                        goalDegrees = (goalDegrees + 360);
                    }
                }
                if (!(this.getYaw() > (goalDegrees - gyroRange) && this.getYaw() < (goalDegrees + gyroRange))) {
                    Drive(direction, power);
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
                    goalDegrees = (this.getYaw() + degrees);
                    if (goalDegrees > 360) {
                        goalDegrees = (goalDegrees - 360);
                    }
                }
                if (!(this.getYaw() > (goalDegrees - gyroRange) && this.getYaw() < (goalDegrees + gyroRange))) {
                    Drive(direction, power);
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
        if(this.gyro.getYaw() < 0) {
            return (360 + this.gyro.getYaw());
        } else {
            return this.gyro.getYaw();
        }
    }

    public void experimentalDrive(double x, double y, double z){
        /*
        Guide to motor Powers:
        Left Front: - (y + x + Z)
        Right Front: y - x - z
        Left Rear: y + x - z
        Right Rear: - (y - x + z)
         */
//        double LFpower = Range.clip(-(y+x+z),-1,1);
//        double RFpower = Range.clip((y-x-z),-1,1);
//        double LRpower = Range.clip(-(y-x+z),-1,1);
//        double RRpower = Range.clip((y+x-z), -1, 1);

        double forwrd = x; /* Invert stick X axis */
        double strafe = y;

        double pi = Math.PI;

/* Adjust Joystick X/Y inputs by navX MXP yaw angle */

        double gyro_radians = getYaw() * pi/180;
        double temp = forwrd * cos(gyro_radians) +
                strafe * sin(gyro_radians);
        strafe = -forwrd * sin(gyro_radians) +
                strafe * cos(gyro_radians);
        forwrd = temp;

/* At this point, Joystick X/Y (strafe/forwrd) vectors have been */
/* rotated by the gyro angle, and can be sent to drive system */

        this.leftFront.setPower(Range.clip(-(forwrd+strafe+z),-1,1));
        this.rightFront.setPower(Range.clip((forwrd-strafe-z),-1,1));
        this.leftRear.setPower(Range.clip(-(forwrd-strafe+z),-1,1));
        this.rightRear.setPower(Range.clip((forwrd+strafe-z), -1, 1));
    }

    public double currentDegrees(double x,double y) {
        if (((Math.atan2(y, x)) * (180/Math.PI))<0) {
            return (360 + ((Math.atan2(y, x)) * (180/Math.PI)));
        } else {
            return ((Math.atan2(y, x)) * (180/Math.PI));
        }
    }

    public double currentZone(double x,double y) {
        if (((int) (Math.round(currentDegrees(x, y) / 22.5))) <= 15) {
            return (int) (Math.round(currentDegrees(x, y) / 22.5));
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
        if (this.getYaw() > (goal - 1) && this.getYaw() < (goal + 1)) {
            if (this.getYaw() > (goal - 1)) {
                Drive(Direction.TURNLEFT, 0.2);
            } else if (this.getYaw() < (goal + 1)) {
                Drive(Direction.TURNRIGHT, 0.2);
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean checkGyro() {
        return gyro.isCalibrating();
    }
    public double clipRange(double number, double min, double max) {
        if (number < min) {return min;}
        else if (number > max) {return max;}
        else {return number;}
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  ENCODER
    public int getLeftEncoder() {return -this.leftFront.getCurrentPosition();}
    public int getRightEncoder() {return this.rightFront.getCurrentPosition();}
    //    countsPerMotorRev    = 1440 ;
//    driveGearReduction    = 1.5 ;
//    wheelDiameterInches   = 4.0 ;
    public double encoderInchesRight() {return (this.getRightEncoder() / (1440 * 1.5) / (4 * Math.PI));}
    public double encoderInchesLeft() {return (this.getLeftEncoder() / (1440 * 1.5) / (4 * Math.PI));}
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  ENUMS
    public enum Direction{
        FORWARD,
        BACKWARD,
        STRAFELEFT,
        STRAFERIGHT,
        DRIGHTUP,
        DRIGHTDOWN,
        DLEFTUP,
        DLEFTDOWN,
        TURNLEFT,
        TURNRIGHT,
        DSTRAFERIGHTUP,
        DUPSTRAFERIGHT,
        DUPSTRAFELEFT,
        DSTRAFELEFTUP,
        DSTRAFELEFTDOWN,
        DDOWNSTRAFELEFT,
        DDOWNSTRAFERIGHT,
        DSTRAFERIGHTDOWN
    }

}
