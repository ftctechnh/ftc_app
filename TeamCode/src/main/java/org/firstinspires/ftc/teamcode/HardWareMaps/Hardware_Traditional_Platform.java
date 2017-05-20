package org.firstinspires.ftc.teamcode.HardWareMaps;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * @author 4Tune.Kerrea
 * 双电机机构的硬件表
 */
public class Hardware_Traditional_Platform {

    //  大球伺服初始位置
    public final static double BALL_CLAW_INIT_POSITION = 0.05;

    //  创建电机
    public DcMotor Base_L       = null;
    public DcMotor Base_R       = null;
    public DcMotor LiftBall     = null;
    public DcMotor CollectBall  = null;
    public DcMotor ThrowBall    = null;
    //  创建伺服
    public Servo   BallClaw     = null;
    public Servo   LightClick   = null;
    //  创建外围控制设备
    public ColorSensor Color    = null;
    public DeviceInterfaceModule InterfaceModule = null;

    public void init(HardwareMap hardwareMap){
        //  初始化电机机组
        Base_L           = hardwareMap.dcMotor.get("Base_L");
        Base_R           = hardwareMap.dcMotor.get("Base_R");
        LiftBall         = hardwareMap.dcMotor.get("LiftBall");
        CollectBall      = hardwareMap.dcMotor.get("Collector");
        ThrowBall        = hardwareMap.dcMotor.get("ThrowBall");
        //  初始化伺服机组
        BallClaw         = hardwareMap.servo.get("BallClaw");
        LightClick       = hardwareMap.servo.get("LightClick");
        //  初始化外围设备
        Color            = hardwareMap.colorSensor.get("ColorSensor");
        InterfaceModule  = hardwareMap.deviceInterfaceModule.get("Board");

        //  设定电机非工作状态下的模式
        Base_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Base_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LiftBall.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        CollectBall.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        ThrowBall.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //  翻转电机运行方向
        Base_R.setDirection(DcMotor.Direction.REVERSE);

        //  功率初始化
        Base_L.setPower(0.00);
        Base_R.setPower(0.00);
        LiftBall.setPower(0.00);
        CollectBall.setPower(0.00);
        ThrowBall.setPower(0.00);

        //  各设备初始化位置调整
        BallClaw.setPosition(BALL_CLAW_INIT_POSITION);
        LightClick.setPosition(0.10);
    }
}
