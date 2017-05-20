package org.firstinspires.ftc.teamcode.Experience;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

@Autonomous(name = "Color",group = "Exp")
public class ColorSensor_A extends LinearOpMode {

    // TCS34725 A 的新地址
    private final static I2cAddr CSA_NewAddress = I2cAddr.create7bit(0x52);
    // TCS34725 B 的新地址
    private final static I2cAddr CSB_NewAddress = I2cAddr.create7bit(0x54);

    // 定义传感器
    private final ColorSensor colorSensor_A = hardwareMap.colorSensor.get("C1");
    private final ColorSensor colorSensor_B = hardwareMap.colorSensor.get("C2");

    @Override
    public void runOpMode(){
        /*
        由于6个IIC的端口是在同一根总线上，如果不更新地址，读取到的IIC数据是不能确定哪一个返回值的
        通常是IIC - 0 (最近，返回ACK收取早）
         */
        colorSensor_A.setI2cAddress(CSA_NewAddress);
        colorSensor_B.setI2cAddress(CSB_NewAddress);
        waitForStart();
        boolean isA = true;
        while (opModeIsActive()) {
            if (isA) {
                telemetry.addData("C1 Red:", colorSensor_A.red());
                telemetry.addData("C1 Green:", colorSensor_A.green());
                telemetry.addData("C1 Blue:", colorSensor_A.blue());
                isA = false;
                sleep(3000);
            } else {
                telemetry.addData("C2 Red:", colorSensor_B.red());
                telemetry.addData("C2 Green:", colorSensor_B.green());
                telemetry.addData("C2 Blue:", colorSensor_B.blue());
                isA = true;
                sleep(3000);
            }
            telemetry.update();
        }
    }
}
