package org.swerverobotics.library.internal.tests;

import com.qualcomm.ftccommon.Device;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.TelemetryDashboardAndLog;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.IOpModeLoopCounter;
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

        waitForStart();

        double servoPosition = 0;
        servo.setPosition(servoPosition);

        motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        ElapsedTime elapsedTime = new ElapsedTime();
        IOpModeLoopCounter loopCounter = ClassFactory.createLoopCounter(this);
        int spinCount = 0;

        while (this.opModeIsActive())
            {
            servoPosition += 1. / 256.;
            if (servoPosition >= 1)
                servoPosition = 0;
            servo.setPosition(servoPosition);

            motor.setPower(0.1);

            int loopCount = loopCounter.getLoopCount();
            spinCount++;
            double ms = elapsedTime.milliseconds();
            telemetry.addData("position", format(servoPosition));
            telemetry.addData("#spin",    format(spinCount));
            telemetry.addData("ms/spin",  format(ms / spinCount));
            telemetry.addData("ms/loop",  format(ms / loopCount));
            this.updateTelemetry(telemetry);
            }

        loopCounter.close();
        }

    static String format(double d)
        {
        return String.format("%.3f", d);
        }
    static String format(int i)
        {
        return String.format("%d", i);
        }
    }
