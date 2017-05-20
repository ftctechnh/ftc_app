package org.firstinspires.ftc.teamcode.HardWareMaps;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware_Omnidirectional_Platform {
    public DcMotor Base_FL = null;
    public DcMotor Base_FR = null;
    public DcMotor Base_BL = null;
    public DcMotor Base_BR = null;

    public Servo LightClick_A = null;
    public Servo LightClick_B = null;

    public ColorSensor ColorSensor_A = null;
    public ColorSensor ColorSensor_B = null;

    public DeviceInterfaceModule InterfaceModule = null;

    public void init(HardwareMap hardwareMap) {
        Base_BL = hardwareMap.dcMotor.get("BL");
        Base_BR = hardwareMap.dcMotor.get("BR");

        Base_FL = hardwareMap.dcMotor.get("FL");
        Base_FR = hardwareMap.dcMotor.get("FR");

        LightClick_A = hardwareMap.servo.get("LA");
        LightClick_B = hardwareMap.servo.get("LB");

        ColorSensor_A = hardwareMap.colorSensor.get("CA");
        ColorSensor_B = hardwareMap.colorSensor.get("CB");

        InterfaceModule = hardwareMap.deviceInterfaceModule.get("SB");
        InterfaceModule.setLED(0,false);
        InterfaceModule.setLED(1,false);

        Base_BR.setPower(0.00);
        Base_BL.setPower(0.00);

        Base_FR.setPower(0.00);
        Base_FL.setPower(0.00);

        Base_BR.setDirection(DcMotor.Direction.REVERSE);
        Base_FR.setDirection(DcMotor.Direction.REVERSE);

        ColorSensor_A.setI2cAddress(I2cAddr.create7bit(0x54));
        ColorSensor_B.setI2cAddress(I2cAddr.create7bit(0x56));

        LightClick_A.setPosition(0.00);
        LightClick_B.setDirection(Servo.Direction.REVERSE);
        LightClick_B.setPosition(0.00);

        int index = 0;
        do {
            InterfaceModule.setDigitalChannelState(index, false);
        }while (index ++ < 7);
    }
}
