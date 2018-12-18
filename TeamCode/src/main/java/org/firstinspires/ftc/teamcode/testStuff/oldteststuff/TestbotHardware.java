package org.firstinspires.ftc.teamcode.testStuff.oldteststuff;


import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TestbotHardware
{
    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    public ColorSensor colorSensor = null;


    private DcMotor.RunMode initialMode = null;

    HardwareMap hwMap = null;

    public TestbotHardware(DcMotor.RunMode enteredMode)
    {
        initialMode = enteredMode;
    }

    public void init(HardwareMap ahwMap)
    {
        hwMap =  ahwMap;

        // *** Set up Motors
        // makes the name that gets associated with phone and REV HUB
        leftDrive = hwMap.dcMotor.get("left_drive"); //needs to be named this on HUB
        rightDrive = hwMap.dcMotor.get("right_drive"); // needs to be named this on HUB

        colorSensor = hwMap.colorSensor.get("color_sensor_1");

        //encoders
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //all
        leftDrive.setMode(initialMode);
        rightDrive.setMode(initialMode);

        //default directions of motors
        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        //sets power to zero so motors don't run during initialization
        leftDrive.setPower(0);
        rightDrive.setPower(0);

        //turns led over during initializaiton
        colorSensor.enableLed(false);


    }


}
