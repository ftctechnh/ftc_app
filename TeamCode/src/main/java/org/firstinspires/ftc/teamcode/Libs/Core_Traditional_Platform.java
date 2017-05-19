package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardWareMaps.Hardware_Traditional_Platform;

public abstract class Core_Traditional_Platform extends LinearOpMode {
    // ---------------------------------初始化变量--------------------------------------------------
    /**
     * 初始化 硬件表
     */
    protected static Hardware_Traditional_Platform robot = new Hardware_Traditional_Platform();

    /**
     * 初始化 运行时间记录器
     */
    protected static ElapsedTime runtime = new ElapsedTime();

    /**
     * 圆周率常数
     */
    private final static double PI = Math.PI;

    /**
     * 车轮半径
     */
    private final static double Wheel_Radius = 4.8;

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
    private final static int Tick_Per_CM  = (int)((Count_Per_Round * Gear_Ratio) / (Wheel_Radius * PI * 2));

    /**
     * 机器宽度 单位：厘米
     */
    private final static double WidthOfRobot = 44.00;

    //  ---------------------------------------通用-------------------------------------------------
    /**
     *  机器人初始化
     * @param teamColor 队伍颜色
     * @see Enum_Libs.TeamColor
     */
    protected void initRobot(Enum_Libs.TeamColor teamColor, Enum_Libs.OpMode opMode){
        //  读取硬件设备
        robot.init(hardwareMap);
        //  根据模式设定机器
        if(opMode == Enum_Libs.OpMode.Autonomous) {
            //  启动编码器电机
            resetMotors();
            //  显示队伍颜色
            displayTeamColor(teamColor);
            //  检查传感器接线或是否正常工作
            throwIfColorIsZero(robot.Color.red());
            throwIfColorIsZero(robot.Color.blue());
        }else if(opMode == Enum_Libs.OpMode.Manual){
            //  禁用编码器
            disableEncoder();
        }
        //  告知准备完成
        telemetry.addLine("-------------------");
        telemetry.addLine("Robot Init  is  OK!");
        telemetry.addLine("Good Luck in games!");
        telemetry.addLine("-------------------");
        telemetry.addLine(returnWhatToDo());
        telemetry.update();
        runtime.reset();
        //  按下启动开始
        waitForStart();
    }

    /**
     * 计算速度
     * @param motor 设置需要检测的电机
     * @return 电机当前运行速度
     */
    private double velocityCalculation(DcMotor motor) {
        //  记录开始位置
        int Prev_Position = motor.getCurrentPosition();
        //  重置时间以确保准确性
        runtime.reset();
        //  记录开始时间
        double Prev_Time = runtime.seconds();
        //  程序会产生 0.3秒的延迟
        while (runtime.seconds() <= 0.30) {
            telemetry.addLine("Now Calculation");
        }
        //  记录结束位置
        int Cur_Position = motor.getCurrentPosition();
        //  记录结束时间
        double Cur_Time = runtime.seconds();
        //  重置时间
        runtime.reset();
        //  防止除0错
        try {
            //  返回测试的速度
            return ((Cur_Position - Prev_Position) / (Cur_Time - Prev_Time)) / Tick_Per_CM;
        } catch (ArithmeticException e) {
            //  告知出错
            telemetry.addLine("Error To Read Velocity");
            telemetry.addLine("Velocity Will Set 0");
            telemetry.update();
            return 0.00;
        }
    }

    public abstract String returnWhatToDo();
    // --------------------------------------自动阶段-----------------------------------------------
    /**
     *  机器运行一定距离
     * @param distance 设置的距离 单位：厘米
     * @param Power 电机功率 [-1.00 , 1.00]
     * @param Direction 方向 设置Forward 或 Backward ，不正确的设置抛出异常
     * @see Enum_Libs.RunDirection
     */
    protected void runByDistance(double distance, double Power, Enum_Libs.RunDirection Direction){
        //  检查路径的有效性
        if(Direction != Enum_Libs.RunDirection.Forward && Direction != Enum_Libs.RunDirection.Backward){
            throw new IllegalArgumentException("Can't set Turning in Straight run");
        }
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
     * @param degree 与正前方向的角度 [-360 ,360] 过大无意义
     * @param Power 电机功率 [-1.00 , 1.00]
     * @param Direction 方向 设置 Left 或 Right ， 不正确的设置将抛出异常
     * @see Enum_Libs.RunDirection
     */
    protected void turnByDegree(double degree, double Power, Enum_Libs.RunDirection Direction){
        if(Direction != Enum_Libs.RunDirection.Left && Direction != Enum_Libs.RunDirection.Right){
            throw new IllegalArgumentException("Can't set Straight in Turning run");
        }
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
     * 判断是否检测到红色
     * @return true 或 false 反馈颜色比较结果
     */
    public boolean isRed(){
        if(robot.Color.red() > robot.Color.blue()){
            displayCurrentColor(Enum_Libs.LightColor.Red);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断是否检测到蓝色
     * @return true 或 false 反馈颜色比较结果
     */
    public boolean isBlue(){
        if(robot.Color.red() < robot.Color.blue()){
            displayCurrentColor(Enum_Libs.LightColor.Blue);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 自动阶段按灯
     * @param lightColor 需要按的颜色
     */
    protected void clickLight(Enum_Libs.LightColor lightColor){
        switch (lightColor){
            case Red:
                if(isRed()){
                    do {
                        robot.LightClick.setPosition(1.00);
                    }while (robot.LightClick.getPosition() != 1.00);
                }
                break;
            case Blue:
                if(isBlue()){
                    do {
                        robot.LightClick.setPosition(1.00);
                    }while (robot.LightClick.getPosition() != 1.00);
                }
                break;
            default:
                break;
        }
        do {
            robot.LightClick.setPosition(0.00);
        }while(robot.LightClick.getPosition() != 0.00);
    }

    /**
     * 用于在颜色传感器无法读取或接线错误时提示异常
     * @param ColorResult 需要测试的颜色值
     */
    private void throwIfColorIsZero(int ColorResult){
        if(ColorResult == 0){
            throw new IllegalArgumentException("LEDColor Sensor does't work,\n" +
                    "please restart or check your Sensor.");
        }
        if(ColorResult == -1){
            throw new IllegalArgumentException("LEDColor Sensor does't work,\n" +
                    "please check IIC wires, then restart robot.");
        }
    }

    /**
     * 用于转化角度到弧长，单位：厘米
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
        double LeftVelocity = velocityCalculation(robot.Base_L);
        double RightVelocity = velocityCalculation(robot.Base_R);
        if(direction == Enum_Libs.RunDirection.Forward || direction == Enum_Libs.RunDirection.Backward){
            while (robot.Base_L.isBusy() && robot.Base_R.isBusy()){
                telemetry.addData("Current Left Position :",robot.Base_L.getCurrentPosition());
                telemetry.addData("Current Left Velocity :",LeftVelocity);
                telemetry.addData("Current Right Position :",robot.Base_R.getCurrentPosition());
                telemetry.addData("Current Right Velocity :",RightVelocity);
                telemetry.update();
            }
        }else if(direction == Enum_Libs.RunDirection.Left){
            while (robot.Base_R.isBusy()){
                telemetry.addData("Current Right Position :",robot.Base_R.getCurrentPosition());
                telemetry.addData("Current Right Velocity :",RightVelocity);
                telemetry.update();
            }
        }else if(direction == Enum_Libs.RunDirection.Right) {
            while (robot.Base_L.isBusy()) {
                telemetry.addData("Current Left Position :", robot.Base_L.getCurrentPosition());
                telemetry.addData("Current Left Velocity :", LeftVelocity);
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
        int Cur_LeftPosition  = robot.Base_L.getCurrentPosition();
        int Cur_RightPosition = robot.Base_R.getCurrentPosition();
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
                break;
        }
        robot.Base_L.setTargetPosition(Target_LeftPosition);
        robot.Base_R.setTargetPosition(Target_RightPosition);

        robot.Base_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Base_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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

    /**
     * 用于初始化时，显示机器的颜色
     * @param teamColor 队伍颜色
     */
    private void displayTeamColor(Enum_Libs.TeamColor teamColor){
        switch (teamColor){
            case Blue:
                robot.InterfaceModule.setLED(0,false);
                robot.InterfaceModule.setLED(1,true);
                break;
            case Red:
                robot.InterfaceModule.setLED(0,true);
                robot.InterfaceModule.setLED(1,false);
                break;
            default:
                robot.InterfaceModule.setLED(0,false);
                robot.InterfaceModule.setLED(1,false);
        }
    }

    /**
     * 展示当前测试到的颜色
     * @param lightColor 灯的颜色
     */
    private void displayCurrentColor(Enum_Libs.LightColor lightColor){
        switch (lightColor){
            case Red:
                robot.InterfaceModule.setLED(1,false);
                robot.InterfaceModule.setLED(0,true);
                break;
            case Blue:
                robot.InterfaceModule.setLED(0,false);
                robot.InterfaceModule.setLED(1,true);
                break;
            default:
                robot.InterfaceModule.setLED(1,false);
                robot.InterfaceModule.setLED(0,true);
                break;
        }
    }
    //  ------------------------------------手动阶段------------------------------------------------
    /**
     * 用于手柄摇杆控制
     * @param MaxPower 电机运行最大功率，防止重心不稳造成翻车
     */
    protected void controlByJoystick(double MinPower,double MaxPower){
        double LeftPower,RightPower;
        if(!OpModeIsChecked) {
            //  检测最大功率是否小于最小功率
            if (MaxPower < MinPower) {
                throw new IllegalArgumentException("MaxPower less than MinPower " +
                        "In method controlByJoystick");
            }
            //  检测是否有功率设置为 0，防止无法运行机器
            if (MaxPower == 0) {
                throw new IllegalArgumentException("MaxPower is Zero,Can't move the robot");
            }
            if (MinPower == 0) {
                throw new IllegalArgumentException("MinPower is Zero,Can't move the robot");
            }
        }
        //  权限系统
        if(MainPadIsGame1){
            //  计算功率
            LeftPower = maxPowerProtect(-gamepad1.left_stick_y + gamepad1.right_stick_x, MinPower, MaxPower);
            RightPower = maxPowerProtect(-gamepad1.left_stick_y - gamepad1.right_stick_x, MinPower , MaxPower);
            //  检测按键用于安全更换主控手
            if(gamepad2.y) {
                MainPadIsGame1 = false;
            }
        }else {
            //  计算功率
            LeftPower = maxPowerProtect(-gamepad2.left_stick_y + gamepad2.right_stick_x, MinPower,MaxPower);
            RightPower = maxPowerProtect(-gamepad2.left_stick_y - gamepad2.right_stick_x, MinPower,MaxPower);
            //  检测按键用于安全更换主控手
            if(gamepad1.y) {
                MainPadIsGame1 = true;
            }
        }
        robot.Base_L.setPower(LeftPower);
        robot.Base_R.setPower(RightPower);
    }

    /**
     * 抬升大球，必须启动过大球伺服才可以工作
     * @param ReleasePower 举起最大功率
     * @param TakeBackPower 收回最大功率
     */
    protected void liftBall(double TakeBackPower,double ReleasePower){
        if(!OpModeIsChecked) {
            //  收回功率验证
            if (TakeBackPower > 0) {
                throw new IllegalArgumentException("Invalid TakeBack Power,\n" +
                        "Can't take back the claw.");
            }
            //  释放功率验证
            if (ReleasePower < 0) {
                throw new IllegalArgumentException("Invalid Release Power,\n" +
                        "Can't release the claw.");
            }
        }
        if(ServoIsReleased) {
            //  需等待一次循环结束才生效
            //  防止正负同时成立
            if (gamepad2.left_trigger != 0 && gamepad2.right_trigger == 0) {
                //  限制最大功率 [0,ReleasePower]
                robot.LiftBall.setPower(ReleasePower * gamepad2.left_trigger);
            } else if (gamepad2.left_trigger == 0 && gamepad2.right_trigger != 0) {
                /*
                   限制最小功率 [-TakeBackPower,0]
                   确保收回功率为负值
                   */
                TakeBackPower = -Math.abs(TakeBackPower);
                robot.LiftBall.setPower(TakeBackPower * gamepad2.right_trigger);
            }
        }
    }

    /**
     * 大球伺服释放
     */
    protected void ballClawRelease(){
        robot.BallClaw.setPosition(gamepad1.left_trigger);
        //  如果当前未移动过大球伺服 或 已经成功移动过
        if(!ServoIsReleased && robot.BallClaw.getPosition() != Hardware_Traditional_Platform.BALL_CLAW_INIT_POSITION){
            ServoIsReleased = true;
        }
    }

    /**
     * 按灯伺服释放
     */
    protected void manualClickLight(){
        robot.LightClick.setPosition(gamepad1.right_trigger);
    }

    /**
     * 收球电机工作
     * @param PowerRelease  吸入功率
     * @param PowerTakeBack 推出功率
     */
    protected void collectBall(double PowerRelease ,double PowerTakeBack){
        if(gamepad1.b){
            robot.CollectBall.setPower(Math.abs(PowerRelease));
        }else if(gamepad1.a){
            robot.CollectBall.setPower( - Math.abs(PowerTakeBack));
        }
    }

    /**
     * 手动投球
     * @param PowerRelease 投出功率
     * @param PowerTakeBack 蓄力功率
     */
    protected void manualShootBall(double PowerRelease,double PowerTakeBack){
        if(gamepad2.a){
            robot.ThrowBall.setPower(Math.abs(PowerRelease));
        }else if(gamepad2.b){
            robot.ThrowBall.setPower( - Math.abs(PowerTakeBack));
        }
    }

    /**
     * 程序自检变量，当自检通过后，部分异常检测机制将关闭
     *
     * 此语句一定要放置于手动循环体的最末端，否则可能导致手动操作不流畅
     * 设置成功后，短路多数判断语句，以关闭异常检测机制。
     */
    protected void setChecked(){
        if(!OpModeIsChecked) {
            OpModeIsChecked = true;
        }
    }

    /**
     * 用于限制手柄计算到的最大功率
     * @param Power 由手柄移动值设定
     * @return 当前应该设定的功率
     */
    private double maxPowerProtect(double Power,double MinPower,double MaxPower){
        if(!OpModeIsChecked) {
            if (MaxPower <= MinPower) {
                throw new IllegalArgumentException("MaxPower is less than MinPower,\n " +
                        "please Check your code");
            }
            if (MaxPower > 1.00) {
                throw new IllegalArgumentException("MaxPower is greater than 1.00,\n " +
                        "please Check your code");
            }
            if (MinPower < -1.00) {
                throw new IllegalArgumentException("MaxPower is less than -1.00,\n " +
                        "please Check your code");
            }
        }
        if(Power >= MaxPower){
            return MaxPower;
        }else if(Power <= MinPower){
            return MinPower;
        }else {
            return Power;
        }
    }

    protected void stopAllDevice(){
        robot.CollectBall.setPower(0.00);
        robot.LiftBall.setPower(0.00);
        robot.ThrowBall.setPower(0.00);
    }

    /**
     * 手动阶段禁用编码器
     */
    private void disableEncoder() {
        robot.Base_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.Base_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    // ----------------------------------------标记变量---------------------------------------------
    /**
     * 程序检查标记
     */
    private boolean OpModeIsChecked = false;

    /**
     * 标记主要手柄
     */
    private boolean MainPadIsGame1 = true;

    /**
     * 记录是否释放过伺服
     */
    private boolean ServoIsReleased = false;
    // ---------------------------------------------------------------------------------------------
}