package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.teamcode.seasons.velocityvortex.LinearOpModeBase;

import java.text.DecimalFormat;

/**
 * Created by aburger on 3/5/2017.
 */
@Disabled
@TeleOp(name = "Motor Testing", group = "utilities")
public class MotorTest extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        autonomousInitLoop();
        while (this.opModeIsActive()) {


            telemetry.addData("clr", reading(getColorSensor1()));
            telemetry.addData("clr2", reading(getColorSensor2()));
            telemetry.addData("gyro:", reading(getGyroSensor()));
            getLeftOds().enableLed(true);
            telemetry.addData("ods:", reading(getLeftOds()));
            telemetry.addData("range", reading(getFrontRange()));
            telemetry.addData("blm:" , reading(getBackLeftDrive()));
            telemetry.addData("brm:" , reading(getBackRightDrive()));
            telemetry.addData("flm:" , reading(getFrontLeftDrive()));
            telemetry.addData("frm:" , reading(getFrontRightDrive()));
            telemetry.update();

            runMotor(gamepad1.x,getFrontLeftDrive());
            runMotor(gamepad1.y,getFrontRightDrive());
            runMotor(gamepad1.a,getBackLeftDrive());
            runMotor(gamepad1.b,getBackRightDrive());



        }
    }

    private void runMotor(boolean button, DcMotor motor) {
        if (button)
        {
            motor.setPower(.3);
        }
        else
        {
            motor.setPower(0);
        }
    }

    private String reading(DcMotor m) {
        StringBuilder string = new StringBuilder();
        string.append(": p=");
        string.append(m.getCurrentPosition());
        return string.toString();
    }

    private String reading(ModernRoboticsI2cRangeSensor range) {
        StringBuilder string = new StringBuilder();
        string.append("ld(");
        string.append(range.getLightDetected());
        string.append(")");
        string.append("rld(");
        string.append(range.getRawLightDetected());
        string.append(")");
        string.append("rldm(");
        string.append(range.getRawLightDetectedMax());
        string.append(")");
        string.append("cmO:");
        string.append(range.cmOptical());
        string.append("cmU:");
        string.append(range.cmUltrasonic());
        return string.toString();
    }

    private String reading(OpticalDistanceSensor ods) {
        StringBuilder string = new StringBuilder();
        string.append("ld(");
        string.append(new DecimalFormat("##.###").format(ods.getLightDetected()));
        string.append(")");
        string.append("rld(");
        string.append(new DecimalFormat("##.###").format(ods.getRawLightDetected()));
        string.append(")");
        string.append("rldm(");
        string.append(new DecimalFormat("##.###").format(ods.getRawLightDetectedMax()));
        string.append(")");
        return string.toString();
    }

    private String reading(ModernRoboticsI2cGyro gyroSensor) {
        StringBuilder string = new StringBuilder();
        string.append("x(");
        string.append(gyroSensor.rawX());
        string.append(") ");
        string.append("y(");
        string.append(gyroSensor.rawY());
        string.append(") ");
        string.append("z(");
        string.append(gyroSensor.rawZ());
        string.append(") ");
        return string.toString();

    }

    private String reading(ColorSensor colorSensor) {
       StringBuilder string = new StringBuilder();
        string.append("R(");
        string.append(colorSensor.red());
        string.append(") G(");
        string.append(colorSensor.green());
        string.append(") B(");
        string.append(colorSensor.blue());
        string.append(") A(");
        string.append(colorSensor.alpha());
        string.append(")");

        return string.toString();
    }
}
