package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by FTCGearedUP on 2/15/2016.
 */
public class VermontBlue2 extends LinearOpMode {
    HardwareMapper hardwareMapper;

    @Override
    public void runOpMode() throws InterruptedException {
        hardwareMapper = new HardwareMapper(hardwareMap);
        /////////////////////////////Servos////////////////////////////

        hardwareMapper.red.setPosition(0.91); //initialize red arm
        hardwareMapper.blue.setPosition(0.02); //initialize blue arm
        hardwareMapper.arm1.setPosition(0.85); //initialize hang arm1, higher servo
        hardwareMapper.climberExtend.setPosition(0.6); //initialize climber extender
        hardwareMapper.climberDump.setPosition(0.82); //initialize climbers to be facing up

        //////////////////////////////Sensors///////////////////////////
        hardwareMapper.reflectedLight.enableLed(true);
        hardwareMapper.xAxis = hardwareMap.gyroSensor.get("g2");//I2c 1
        hardwareMapper.zAxis = hardwareMap.gyroSensor.get("g1");//I2c 0
        hardwareMapper.xAxis.calibrate(); //calibrate both sensors
        hardwareMapper.zAxis.calibrate();

        ////////////////////////////Variables//////////////////////////
        double climberExtendVar = 0.6;
        double climberDumpVar = 0.82;
        int toggle = 0;
        /////////////////////////////////////Start////////////////////////////////
        waitForStart();

        ultrasonicAlign(20);

    }
    public void ultrasonicAlign (int distance) throws InterruptedException{

        while(hardwareMapper.ultrasonicLeft.getUltrasonicLevel() > distance || hardwareMapper.ultrasonicRight.getUltrasonicLevel() > distance ){

            if(hardwareMapper.ultrasonicLeft.getUltrasonicLevel() < hardwareMapper.ultrasonicRight.getUltrasonicLevel()){ //left larger than right

                hardwareMapper.motor1.setPower(-0.3);//turn left
                hardwareMapper.motor2.setPower(0.3);
            }
            else if(hardwareMapper.ultrasonicRight.getUltrasonicLevel() < hardwareMapper.ultrasonicLeft.getUltrasonicLevel()){ //right larger than left

                hardwareMapper.motor1.setPower(0.3);//turn right
                hardwareMapper.motor2.setPower(-0.3);
            }
            else if(hardwareMapper.ultrasonicRight.getUltrasonicLevel() == hardwareMapper.ultrasonicLeft.getUltrasonicLevel()){ //right equal to left

                hardwareMapper.motor1.setPower(-0.3);//go forward
                hardwareMapper.motor2.setPower(-0.3);
            }
            else{
                hardwareMapper.motor1.setPower(0);//stop
                hardwareMapper.motor2.setPower(0);
            }
            sleep(50);
        }
    }
}
