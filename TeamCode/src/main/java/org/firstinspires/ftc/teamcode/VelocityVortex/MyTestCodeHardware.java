package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by team on 7/18/2017. During FTC JAVA and robotics software workshop
 */
public class MyTestCodeHardware extends OpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    double rightPower;
    double leftPower;

    @Override
    public void init() {
        hardwareInit();
    }

    @Override
    public void loop() {

    }

    private void hardwareInit() {
        rightMotor = hardwareMap.dcMotor.get("rm");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor = hardwareMap.dcMotor.get("lm");
    }
}

