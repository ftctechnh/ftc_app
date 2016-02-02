package org.swerverobotics.library.internal.tests;

import com.qualcomm.ftccommon.Device;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * A simple linear opmode that exercises a motor and a servo simultaneously
 */
@TeleOp(name="Motor Servo Test (Linear)", group="Swerve Tests")
@Disabled
public class LinearMotorServoTest extends LinearOpMode
    {
    DcMotor motor;
    Servo servo;
    DcMotorController motorController;

    @Override
    public void runOpMode() throws InterruptedException
        {
        motor = this.hardwareMap.dcMotor.get("motor");
        servo = this.hardwareMap.servo.get("servo");

        motorController = motor.getController();
        motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        for (;;)
            {
            DcMotorController.DeviceMode mode = motorController.getMotorControllerDeviceMode();
            if (mode == DcMotorController.DeviceMode.READ_WRITE || mode== DcMotorController.DeviceMode.WRITE_ONLY)
                break;
            Thread.yield();
            }

        waitForStart();

        double servoPosition = 0;
        servo.setPosition(servoPosition);

        motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        while (this.opModeIsActive())
            {
            servoPosition += 1. / 256.;
            if (servoPosition >= 1)
                servoPosition = 0;
            servo.setPosition(servoPosition);

            motor.setPower(0.1);

            telemetry.addData("position", servoPosition);
            waitOneFullHardwareCycle();
            }
        }
    }
