package org.firstinspires.ftc.teamcode.HardWareMaps;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * @author 4Tune.Kerrea
 * 仅使用 2 轮驱动的机器底盘测试硬件表
 */
public class Hardware_Base_Platform {

    //  创建电机
    public DcMotor Base_L       = null;
    public DcMotor Base_R       = null;

    public void init(HardwareMap hardwareMap) {
        //  初始化电机机组
        Base_L = hardwareMap.dcMotor.get("Base_L");
        Base_R = hardwareMap.dcMotor.get("Base_R");

        //  重置编码器
        Base_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Base_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //  设定电机非工作状态下的模式
        Base_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Base_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //  翻转电机运行方向
        Base_R.setDirection(DcMotor.Direction.REVERSE);

    }
}
