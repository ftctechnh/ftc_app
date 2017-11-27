package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecoveryAlpha;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Components.DriveTrain.DriveTrainHardware;
import org.firstinspires.ftc.teamcode.Components.DriveTrain.TwoMotor;
import org.firstinspires.ftc.teamcode.Utilities.Map;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by Shane on 26-11-2017.
 */
public abstract class Robot {
    // ---------------------- Hardware Objects ----------------------
    protected Map      map      = null;
    protected SetRobot setRobot = null;
    DriveTrainHardware driveTrain = null;
    // ---------------------- Hardware Devices ----------------------
    //private DcMotor mLeft       = null;
    //private DcMotor mRight      = null;
    // --------------------- Hardware Variables ---------------------
    //public double leftPower;
    //public double rightPower;
    // ------------------------ Constructor -------------------------
    Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        map = new Map(hardwareMap,telemetry);
        setRobot = new SetRobot(telemetry);
        driveTrain = new TwoMotor(map,setRobot);
        //leftPower  = 0;
        //rightPower = 0;
    }
    // ---------------------- Private Methods -----------------------
    // ----------------------- Init -----------------------
    public void init() {
        //mRight = map.motor("r");
        //mLeft  = map.revMotor("l");
        driveTrain.initHardware();
        mapMotors();
        mapServos();
        mapCRServos();
    }
    public void setHardwarePower() {
        //setRobot.power(mRight,rightPower,"right motor");
        //setRobot.power(mLeft,leftPower,"left motor");
        driveTrain.runHardware();
        setMotorPowers();
        setServoPositions();
        setCRServoPowers();
    }
    // ---------------------- Abstract Methods ----------------------
    // --------------------- Mapping ----------------------
    abstract void mapMotors();
    abstract void mapServos();
    abstract void mapCRServos();
    // ---------------- Set Hardware Power ----------------
    abstract void setMotorPowers();
    abstract void setServoPositions();
    abstract void setCRServoPowers();
}
