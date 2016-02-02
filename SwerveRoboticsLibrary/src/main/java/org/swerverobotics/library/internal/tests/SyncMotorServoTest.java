package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.Autonomous;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * A simple synchronous opmode that exercises a motor and a servo simultaneously
 */
@TeleOp(name="Motor Servo Test (Sync)", group="Swerve Tests")
@Disabled
public class SyncMotorServoTest extends SynchronousOpMode
    {
    DcMotor motor;
    Servo   servo;

    @Override
    protected void main() throws InterruptedException
        {
        motor = this.hardwareMap.dcMotor.get("motor");
        servo = this.hardwareMap.servo.get("servo");

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
            telemetry.update();
            this.idle();
            }
        }
    }
