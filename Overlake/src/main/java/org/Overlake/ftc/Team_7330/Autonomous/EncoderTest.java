package org.Overlake.ftc.Team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

import java.util.EmptyStackException;

@TeleOp(name="EncoderTest")
public class EncoderTest extends SynchronousOpMode
{
    // All hardware variables can only be initialized inside the main() function,
    // not here at their member variable declarations.
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;

    IBNO055IMU imu;
    IBNO055IMU.Parameters parameters = new IBNO055IMU.Parameters();

    @Override protected void main() throws InterruptedException
    {
        this.composeDashboard();

        parameters.angleunit = IBNO055IMU.ANGLEUNIT.DEGREES;
        parameters.accelunit = IBNO055IMU.ACCELUNIT.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "BNO055";
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"), parameters);

        // Enable reporting of position using the naive integrator
        imu.startAccelerationIntegration(new Position(), new Velocity());

        // Initialize our hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names you assigned during the robot configuration
        // step you did in the FTC Robot Controller app on the phone.
        this.motorFrontRight = this.hardwareMap.dcMotor.get("motorFrontRight");
        this.motorBackRight = this.hardwareMap.dcMotor.get("motorBackRight");
        this.motorFrontLeft = this.hardwareMap.dcMotor.get("motorFrontLeft");
        this.motorBackLeft = this.hardwareMap.dcMotor.get("motorBackLeft");
        // telemetry.update();

        // One of the two motors (here, the left) should be set to reversed direction
        // so that it can take the same power level values as the other motor.
        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        try
        {
            turn(90,.5);
        }
        catch (Exception e)
        {
            throw new EmptyStackException();
        }
    }

    // to turn right, degrees and power should both be positive
    // to turn left, degrees - but not power - should be negative
    void turn(double degrees, double power)
    {
        double heading = imu.getAngularOrientation().heading;
        double targetHeading = heading + degrees;

        if (targetHeading > 180)
        {
            targetHeading -= 360;
        }
        else if (targetHeading < 0)
        {
            targetHeading += 360;
        }

        motorBackLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        while (Math.abs(targetHeading - heading) > .5) {
            this.motorFrontRight.setPower(-getPower(power, targetHeading, heading));
            this.motorBackRight.setPower(-getPower(power, targetHeading, heading));
            this.motorFrontLeft.setPower(getPower(power, targetHeading, heading));
            this.motorBackLeft.setPower(getPower(power, targetHeading,  heading));
            heading = imu.getAngularOrientation().heading;
        }

        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }

    double getPower(double power, double targetHeading, double heading) {
        double x = targetHeading - heading;
        if (Math.abs(x) > 180)
        {
            x += (-360 * (x / Math.abs(x)));
        }

        if (x > 0)
        {
            return (Math.log((Math.min(Math.E - 1, (Math.E - 1) * x / 15.0) + 1.0)) * power);
        }
        else {
            return (-(Math.log((Math.min(Math.E - 1, (Math.E - 1) * Math.abs(x) / 15.0) + 1.0)) * power));
        }
    }

    void composeDashboard()
    {
        telemetry.addLine(
                telemetry.item("BL Run mode ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorBackLeft.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("BR Run mode ", new IFunc<Object>() {
                    public Object value() {
                        return motorBackRight.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("FL Run mode ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontLeft.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("FR Run mode ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontRight.getChannelMode().toString();
                    }
                }));

        telemetry.addLine(
                telemetry.item("BL Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorBackLeft.getTargetPosition();
                    }
                }));

        telemetry.addLine(
                telemetry.item("BR Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorBackRight.getTargetPosition();
                    }
                }));

        telemetry.addLine(
                telemetry.item("FL Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontLeft.getTargetPosition();
                    }
                }));

        telemetry.addLine(
                telemetry.item("FR Encoder target ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return motorFrontRight.getTargetPosition();
                    }
                }));
    }

    // Handy functions for formatting data for the dashboard
    String format(double d)
    {
        return String.format("%.1f", d);
    }
}
