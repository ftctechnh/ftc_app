package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardWareMaps.Hardware_Base_Platform;

public abstract class Core_Base_Platform extends LinearOpMode {

    // ---------------------------------初始化变量--------------------------------------------------
    /**
     * 初始化 硬件表
     */
    protected static Hardware_Base_Platform robot = new Hardware_Base_Platform();

    /**
     * 初始化 运行时间记录器
     */
    private static ElapsedTime runtime = new ElapsedTime();

    /**
     * 圆周率常数
     */
    private final static double PI = Math.PI;

    /**
     * 车轮半径
     */
    private final static double Wheel_Radius = 4.3;

    /**
     * 齿轮传动比
     */
    private final static double Gear_Ratio = 1.00;

    /**
     * 脉冲数每圈
     */
    private final static double Count_Per_Round = 1478.4;

    /**
     * 脉冲数每厘米
     */
    private final static int    Tick_Per_CM     =
            (int)((Count_Per_Round * Gear_Ratio) / (Wheel_Radius * PI * 2));

    /**
     * 机器宽度 单位：厘米
     */
    private final static double WidthOfRobot    = 44.00;


    // ---------------------------------------通用--------------------------------------------------

    /**
     *  机器人初始化
     */
    protected void initRobot(){
        robot.init(hardwareMap);
        telemetry.addLine("-------------------");
        telemetry.addLine("Robot Init  is  OK!");
        telemetry.addLine("Good Luck in games!");
        telemetry.addLine("-------------------");
        telemetry.update();
        waitForStart();
    }

    /**
     * 重置编码器电机
     */
    private void resetMotors(){
        robot.Base_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Base_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.Base_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.Base_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * 停止编码器的运行
     */
    private void stopMotors(){
        setMotorsPower(0.00);
        resetMotors();
    }

    /**
     * 设置电机功率
     * @param Power 由主调函数提供功率
     */
    private void setMotorsPower(double Power){
        robot.Base_R.setPower(Power);
        robot.Base_L.setPower(Power);
    }

    // --------------------------------------自动阶段-----------------------------------------------

    /**
     *  机器运行一定距离
     * @param distance 设置的距离 厘米为单位
     * @param Power 电机功率 [-1.00 , 1.00]
     * @param Direction 方向 设置Forward 或 Backward ，不正确的设置抛出异常
     * @see Enum_Libs.RunDirection
     */
    public void runByDistance(double distance, double Power, Enum_Libs.RunDirection Direction){
        //  检查路径的有效性
        throwIfSetInvalidStraightDirection(Direction);
        //  重置电机
        resetMotors();
        //  计算目标位置 并设定
        setRunTarget(distance,Direction);
        //  设置功率
        setMotorsPower(Power);
        //  循环遥测机器人电机
        telemetry_DataForEncoder(Direction);
        //  停止并重置电机
        stopMotors();
    }

    /**
     * 机器转动一定角度
     * @param degree 与正前方向的角度 [0,360] 过大无意义
     * @param Power 电机功率 [-1.00 , 1.00]
     * @param Direction 方向 设置 Left 或 Right ， 不正确的设置将抛出异常
     * @see Enum_Libs.RunDirection
     */
    public void turnByDegree(double degree, double Power, Enum_Libs.RunDirection Direction){
        throwIfSetInvalidTurnDirection(Direction);
        //  重置电机
        resetMotors();
        //  计算目标位置 并设定
        setRunTarget(degreeToDistance(degree),Direction);
        //  设置功率
        setMotorsPower(Power);
        //  循环遥测机器人电机
        telemetry_DataForEncoder(Direction);
        //  停止并重置电机
        stopMotors();
    }

    /**
     * 用于转化角度到弧长
     * @param degree 机器转向角度 ， 由主调函数提供
     * @return 返回角度对应的弧长
     */
    private double degreeToDistance(double degree){
        return degree/180 * PI * WidthOfRobot;
    }

    /**
     * 监测编码器电机的运行状态
     * @param direction 根据设定的方向 监测特定电机
     */
    private void telemetry_DataForEncoder(Enum_Libs.RunDirection direction){
        if(direction == Enum_Libs.RunDirection.Forward || direction == Enum_Libs.RunDirection.Backward){
            while (robot.Base_L.isBusy() && robot.Base_R.isBusy()){
                telemetry.addData("Current Left Position :",robot.Base_L.getCurrentPosition());
                telemetry.addData("Current Left Velocity :",velocityCalculation(robot.Base_L));
                telemetry.addData("Current Right Position :",robot.Base_R.getCurrentPosition());
                telemetry.addData("Current Right Velocity :",velocityCalculation(robot.Base_R));
                telemetry.update();
            }
        }else if(direction == Enum_Libs.RunDirection.Left){
            while (robot.Base_R.isBusy()){
                telemetry.addData("Current Right Position :",robot.Base_R.getCurrentPosition());
                telemetry.addData("Current Right Velocity :",velocityCalculation(robot.Base_R));
                telemetry.update();
            }
        }else if(direction == Enum_Libs.RunDirection.Right) {
            while (robot.Base_L.isBusy()) {
                telemetry.addData("Current Left Position :", robot.Base_L.getCurrentPosition());
                telemetry.addData("Current Left Velocity :", velocityCalculation(robot.Base_L));
                telemetry.update();
            }
        }
    }

    /**
     * 获取机器设定的距离，并计算设为机器运行目标
     * @param distance 机器运行距离
     * @param Direction 机器运行方向
     */
    private void setRunTarget(double distance, Enum_Libs.RunDirection Direction){
        int TargetPosition =(int)(distance * Tick_Per_CM);
        int Cur_LeftPosition = robot.Base_L.getCurrentPosition();
        int Cur_RightPosition= robot.Base_R.getCurrentPosition();
        int Target_LeftPosition,Target_RightPosition;
        switch (Direction){
            case Forward:
                Target_LeftPosition = Cur_LeftPosition + TargetPosition;
                Target_RightPosition = Cur_RightPosition + TargetPosition;
                break;
            case Backward:
                Target_LeftPosition = Cur_LeftPosition - TargetPosition;
                Target_RightPosition = Cur_RightPosition - TargetPosition;
                break;
            case Left:
                Target_LeftPosition = Cur_LeftPosition;
                Target_RightPosition = Cur_RightPosition + TargetPosition;
                break;
            case Right:
                Target_LeftPosition = Cur_LeftPosition + TargetPosition;
                Target_RightPosition = Cur_RightPosition;
                break;
            default:
                Target_LeftPosition = 0;
                Target_RightPosition = 0;
        }
        robot.Base_L.setTargetPosition(Target_LeftPosition);
        robot.Base_R.setTargetPosition(Target_RightPosition);

        robot.Base_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Base_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * 计算速度
     * @param motor 设置需要检测的电机
     * @return 电机当前运行速度
     */
    private double velocityCalculation(DcMotor motor){
        //  记录开始位置
        int Prev_Position = motor.getCurrentPosition();
        //  重置时间以确保准确性
        runtime.reset();
        //  记录开始时间
        double Prev_Time = runtime.seconds();
        //  程序会产生 0.3秒的延迟
        while (runtime.seconds() <= 0.3){
            telemetry.addLine("Now Calculation");
        }
        //  记录结束位置
        int Cur_Position = motor.getCurrentPosition();
        //  记录结束时间
        double Cur_Time  = runtime.seconds();
        //  重置时间
        runtime.reset();
        //  防止除0错
        try {
            //  返回测试的速度
            return ((Cur_Position - Prev_Position) / (Cur_Time - Prev_Time)) / Tick_Per_CM;
        }catch (ArithmeticException e){
            //  告知出错
            telemetry.addLine("Error To Read Velocity");
            telemetry.addLine("Velocity Will Set 0");
            telemetry.update();
            return 0.00;
        }
    }

    /**
     * 用于抛出异常 防止错误设定了机器运行方向
     * @param direction 由主调函数提供的运行方向
     */
    private void throwIfSetInvalidStraightDirection(Enum_Libs.RunDirection direction){
        if(direction != Enum_Libs.RunDirection.Forward && direction != Enum_Libs.RunDirection.Backward){
            throw new IllegalArgumentException("Can't set Turning in Straight run");
        }
    }

    /**
     * 用于抛出异常 防止错误设定了机器运行方向
     * @param direction 由主调函数提供的运行方向
     */
    private void throwIfSetInvalidTurnDirection(Enum_Libs.RunDirection direction){
        if(direction != Enum_Libs.RunDirection.Left && direction != Enum_Libs.RunDirection.Right){
            throw new IllegalArgumentException("Can't set Straight in Turning run");
        }
    }

}
