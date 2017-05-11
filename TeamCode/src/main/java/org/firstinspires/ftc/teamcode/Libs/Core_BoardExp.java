package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardWareMaps.Hardware_BoardExp;

public abstract class Core_BoardExp extends LinearOpMode {
    protected Hardware_BoardExp robot = new Hardware_BoardExp();
    protected ElapsedTime runtime = new ElapsedTime();
    //  RGB模块对应色针脚
    private final static int LED_R_PIN = 0;             //  红色  接口
    private final static int LED_G_PIN = 1;             //  绿色  接口
    private final static int LED_B_PIN = 2;             //  蓝色  接口
    //  光线传感器针脚
    protected final static int LIGHT_SENSOR_A_PIN = 3;    //  A 光感数字和模拟量  接口
    protected final static int LIGHT_SENSOR_B_PIN = 4;    //  B 光感数字和模拟量  接口
    //  各个数字针脚默认值
    private boolean []PinState ={false,false,false,false,false,false,false,false};

    protected Enum_Libs.LEDColor[] LEDColorDisplay = new Enum_Libs.LEDColor[8];

    private Enum_Libs.LEDColor numToColor(int KeyNum){
        switch (KeyNum){
            case 1:
                return Enum_Libs.LEDColor.Red;
            case 2:
                return Enum_Libs.LEDColor.Blue;
            case 3:
                return Enum_Libs.LEDColor.Green;
            case 4:
                return Enum_Libs.LEDColor.Cyan;
            case 5:
                return Enum_Libs.LEDColor.Purple;
            case 6:
                return Enum_Libs.LEDColor.Yellow;
            case 7:
                return Enum_Libs.LEDColor.White;
            case 8:
                return Enum_Libs.LEDColor.Off;
            case 0:
                return Enum_Libs.LEDColor.Off;
            default:
                return Enum_Libs.LEDColor.Off;
        }
    }

    protected void keyInt(){
        int index = 0;
        do {
            do {
                telemetry.addLine("Wait For Key");
                telemetry.update();
            } while (!isAnyKeyDown());
            LEDColorDisplay[index] = numToColor(keyToNumber());
            do {
                telemetry.addLine("Wait For Next Key,Please Up This Key");
                telemetry.update();
            } while (isAnyKeyDown());
        }while (index++ < 7);
    }

    private boolean isAnyKeyDown(){
        return gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
                gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_right || gamepad1.dpad_left;
    }

    private int keyToNumber(){
        if(gamepad1.a){
            return 1;
        }else if(gamepad1.b){
            return 2;
        }else if(gamepad1.x){
            return 3;
        }else if(gamepad1.y) {
            return 4;
        }else if(gamepad1.dpad_up) {
            return 5;
        }else if(gamepad1.dpad_down) {
            return 6;
        }else if(gamepad1.dpad_left) {
            return 7;
        }else if(gamepad1.dpad_right){
            return 8;
        }else {
            return 0;
        }
    }

    /**
     * 光感是否读取到白色
     * @param Pin 针脚号码
     * @return  白色返回true
     */
    protected boolean isWhite(int Pin){
        return robot.interfaceModule.getDigitalChannelState(Pin);
    }

    /**
     * 光感是否读取到黑色
     * @param Pin 针脚号码
     * @return  黑色返回true
     */
    protected boolean isBlack(int Pin){
        return !isWhite(Pin);
    }

    /**
     * 根据颜色名设置LED
     * @param LEDColor 颜色
     * @see Enum_Libs.LEDColor
     */
    private void LED_Color(Enum_Libs.LEDColor LEDColor){
        switch (LEDColor){
            case Red:
                PinState[LED_R_PIN] = true;
                PinState[LED_G_PIN] = false;
                PinState[LED_B_PIN] = false;
                break;
            case Green:
                PinState[LED_R_PIN] = false;
                PinState[LED_G_PIN] = true;
                PinState[LED_B_PIN] = false;
                break;
            case Blue:
                PinState[LED_R_PIN] = false;
                PinState[LED_G_PIN] = false;
                PinState[LED_B_PIN] = true;
                break;
            case Yellow:
                PinState[LED_R_PIN] = true;
                PinState[LED_G_PIN] = true;
                PinState[LED_B_PIN] = false;
                break;
            case Cyan:
                PinState[LED_R_PIN] = false;
                PinState[LED_G_PIN] = true;
                PinState[LED_B_PIN] = true;
                break;
            case Purple:
                PinState[LED_R_PIN] = true;
                PinState[LED_G_PIN] = false;
                PinState[LED_B_PIN] = true;
                break;
            case White:
                PinState[LED_R_PIN] = true;
                PinState[LED_G_PIN] = true;
                PinState[LED_B_PIN] = true;
                break;
            case Off:
                PinState[LED_R_PIN] = false;
                PinState[LED_G_PIN] = false;
                PinState[LED_B_PIN] = false;
                break;
        }
    }

    /**
     * 设置针脚模式
     * @param Pin 编号
     * @param NewState 新的状态
     */
    protected void setPinMode(int Pin,boolean NewState){
        PinState[Pin] = NewState;
    }

    /**
     * LED闪烁
     * @param Duration 持续时长
     * @param Blink_Period 闪烁周期
     * @param LEDColorA 闪烁的一个颜色
     * @param LEDColorB 闪烁的另一个颜色
     */
    protected void blinkLED(double Duration, double Blink_Period, Enum_Libs.LEDColor LEDColorA, Enum_Libs.LEDColor LEDColorB){
        double EndTDime = runtime.seconds() + Duration;
        while (runtime.seconds() <= EndTDime){
            displayColor(Blink_Period, LEDColorA);
            displayColor(Blink_Period, LEDColorB);
        }
        LED_Color(Enum_Libs.LEDColor.Off);
    }

    /**
     * 将计算后的LED灯色显示
     * @param Blink_Period 闪烁周期
     * @param LEDColor 颜色，由调用方法体提供
     */
    private void displayColor(double Blink_Period, Enum_Libs.LEDColor LEDColor){
        double ColorB_EndTime = runtime.seconds() + Blink_Period;
        LED_Color(LEDColor);
        while (runtime.seconds() <= ColorB_EndTime){
            robot.interfaceModule.setDigitalChannelState(LED_R_PIN,PinState[LED_R_PIN]);
            robot.interfaceModule.setDigitalChannelState(LED_G_PIN,PinState[LED_G_PIN]);
            robot.interfaceModule.setDigitalChannelState(LED_B_PIN,PinState[LED_B_PIN]);
        }
    }

}
