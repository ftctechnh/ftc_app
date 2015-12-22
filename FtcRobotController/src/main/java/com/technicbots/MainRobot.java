package com.technicbots;import android.hardware.Sensor;import com.qualcomm.robotcore.hardware.DcMotor;import com.qualcomm.robotcore.hardware.DcMotorController;import com.qualcomm.robotcore.hardware.HardwareMap;import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;import com.qualcomm.robotcore.hardware.Servo;public class MainRobot{public static int DEFAULTLINSLIDE=0;public static double WHEEL_DIAMETER=4;public static int ENCODER_CPR=1440;public static double GEAR_RATIO=0.5;private static DcMotor leftWheel;private static DcMotor rightWheel;private static DcMotor linearSlide;private static Servo buttonTouch;private static Sensor lightSensor;private static Sensor colorSensor;private static Sensor gyroSensor;private static OpticalDistanceSensor opticalDistanceSensor;public MainRobot(HardwareMap hardwareMap){rightWheel=hardwareMap.dcMotor.get("rightwheel");leftWheel=hardwareMap.dcMotor.get("leftwheel");rightWheel.setDirection(DcMotor.Direction.REVERSE);}
    public MainRobot(DcMotor left,DcMotor right,DcMotor linearLift,Servo button,Sensor light,Sensor color,Sensor gyro){leftWheel=left;rightWheel=right;linearSlide=linearLift;buttonTouch=button;lightSensor=light;colorSensor=color;gyroSensor=gyro;}
    public void releaseClimbers(){}
    public void pressRescueBeacon(){}
    public static void moveStraight(double distance,double power,boolean reverse){final double CIRCUMFERENCE=Math.PI*WHEEL_DIAMETER;final double ROTATIONS=distance/CIRCUMFERENCE;final double COUNTS=ENCODER_CPR*ROTATIONS*GEAR_RATIO;rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);rightWheel.setTargetPosition((int)COUNTS);rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);if(reverse){leftWheel.setPower(-1*power);rightWheel.setPower(-1*power);}else{leftWheel.setPower(power);rightWheel.setPower(power);}
        while(rightWheel.getCurrentPosition()<rightWheel.getTargetPosition()){}
        leftWheel.setPower(0);rightWheel.setPower(0);}
    public static void turn(float degrees){}
    public static void straightTillLine(float targetIntensity){}
    public static void lineFollower(double distance){}
    public static void scoreLowGoal(double linSlideDistance){}
    public static void scoreMediumGoal(double linSlideDistance){}
    public static void scoreHighGoal(double linSlideDistance){}}