package org.firstinspires.ftc.teamcode.Experience;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Test Servo",group = "Exp")
public final class Exp_Servo extends LinearOpMode {

    private Servo []Servos = new Servo[6];
    private String []ServosNames = {
            "S0","S1","S2","S3","S4","S5","S6"
    };
    @Override
    public void runOpMode() throws InterruptedException{
        initRobot();
        waitForStart();
        //  设置所有伺服至 完全展开
        int index = 0;
        do{
            //  确保完全打开后继续展开下一个
            do {
                Servos[index].setPosition(1.00);
            }while (Servos[index].getPosition() != 1.00);
        }while (index++ < 5);
        //  回复当前所有的设备位置，并等待 a b 按键的启动
        do{
            index = 0;
            do {
                telemetry.addData("Servo" + index + ":",Servos[index].getPosition());
            }while (index++ < 5);
            telemetry.update();
        }while (!gamepad1.b && !gamepad1.a);
        index = 0;
        //  收回所有伺服
        do{
            //  确保所有伺服收回
            do {
                Servos[index].setPosition(0.00);
            }while (Servos[index].getPosition() != 0.00);
        }while (index++ < 5);
    }
    private void initRobot(){
        int index = 0;
        //  逐个读取伺服
        do{
            Servos[index] = hardwareMap.servo.get(ServosNames[index]);
        }while (index++ < 5);
        //  逐个初始化位置
        index = 0;
        do{
            Servos[index].setPosition(0.00);
        }while (index++ < 5);
    }
}
