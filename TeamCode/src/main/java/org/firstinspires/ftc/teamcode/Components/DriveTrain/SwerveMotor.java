package org.firstinspires.ftc.teamcode.Components.DriveTrain;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utilities.Map;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by spmce on 11/26/2017.
 *
 * This drive train is used for robots with a four motor drive train
 */
public class SwerveMotor extends DriveTrainHardware {
    // ---------------------- Hardware Devices ----------------------
    /**
     * back left motor
     */
    private DcMotor mBackLeft;
    /**
     * back right motor
     */
    private DcMotor mBackRight;
    /**
     * motor that rotates left wheel
     */
    private DcMotor mSwerveLeft;
    /**
     * motor that rotates right wheel
     */
    private DcMotor mSwerveRight;
    /**
     * motor that rotates back left wheel
     */
    private DcMotor mSwerveBackLeft;
    /**
     * motor that rotates back right wheel
     */
    private DcMotor mSwerveBackRight;
    // --------------------- Hardware Variables ---------------------
    /**
     * this variable is used to set power the back left motor
     */
    public double backLeftPower;
    /**
     * this variable is used to set power the back right motor
     */
    public double backRightPower;
    /**
     * this variable is used to set power the swerve left motor
     */
    public double swerveLeftPower;
    /**
     * this variable is used to set power the swerve right motor
     */
    public double swerveRightPower;
    /**
     * this variable is used to set power the swerve back left motor
     */
    public double swerveBackLeftPower;
    /**
     * this variable is used to set power the swerve back right motor
     */
    public double swerveBackRightPower;
    // ------------------------ Constructor -------------------------
    /**
     * Constructs a four motor drive train
     *
     * @param map object that is used to map the drive train
     * @param setRobot object that is used to set the power to the drive train
     */
    public SwerveMotor(Map map, SetRobot setRobot) {
        this.map = map;
        this.setRobot = setRobot;
        mBackLeft        = null;
        mBackRight       = null;
        mSwerveLeft      = null;
        mSwerveRight     = null;
        mSwerveBackLeft  = null;
        mSwerveBackRight = null;
        backLeftPower        = 0;
        backRightPower       = 0;
        swerveLeftPower      = 0;
        swerveRightPower     = 0;
        swerveBackLeftPower  = 0;
        swerveBackRightPower = 0;
    }
    // -------------------------- Mapping ---------------------------
    /**
     * Maps left and right motors of drive train
     */
    @Override
    public void initHardware() {
        mLeft  = map.revMotor("l");
        mRight = map.motor("r");
        mBackLeft  = map.revMotor("bl");
        mBackRight = map.motor("br");
        mSwerveLeft  = map.revMotor("sl");
        mSwerveRight = map.motor("sr");
        mSwerveBackLeft  = map.revMotor("sbl");
        mSwerveBackRight = map.motor("sbr");
    }
    // --------------------- Set Hardware Power ---------------------
    /**
     * Sets power to left and right motors of drive train
     */
    @Override
    public void runHardware() {
        setRobot.power(mLeft,leftPower,"left motor");
        setRobot.power(mRight,rightPower,"right motor");
        setRobot.power(mBackLeft,backLeftPower,"back left motor");
        setRobot.power(mBackRight,backRightPower,"back right motor");
        setRobot.power(mSwerveLeft,swerveLeftPower,"swerve left motor");
        setRobot.power(mSwerveRight,swerveRightPower,"swerve right motor");
        setRobot.power(mSwerveBackLeft,swerveBackLeftPower,"swerve back left motor");
        setRobot.power(mSwerveBackRight,swerveBackRightPower,"swerve back right motor");
    }
}
