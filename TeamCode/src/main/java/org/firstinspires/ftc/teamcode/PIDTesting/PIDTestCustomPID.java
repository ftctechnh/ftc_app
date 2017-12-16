package org.firstinspires.ftc.teamcode.PIDTesting;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by guberti on 10/21/2017.
 */

public class PIDTestCustomPID extends PIDTestInterface {
    DcMotor m;
    private final int MaxRPM = 120; // Actual max (no load) is 160
    private final int TicksPerRev = 1120;
    private final double MaxTicksPerMilliSecond = (MaxRPM * TicksPerRev) / (1000.0 * 60.0);

    public double Ts = 1.24;
    public double Kp = 2 ;    // Tuning variable for PID.
    public double Ti = 0.0238;  // Eliminate integral error in 1 sec.
    public double Td = 0.1;  // Account for error in 0.1 sec.

    private int prevEncoderPosition;   // Encoder tick at last call to loop().
    private double prevError;
    private ElapsedTime t;
    private double integralError;
    private Telemetry tel;

    PIDTestCustomPID(DcMotor m, Telemetry tel) {
        this.m = m;
        t = new ElapsedTime();
        this.tel = tel;
        this.m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        prevEncoderPosition = 0;
        integralError = 0;
    }
    @Override
    public void setPower(double d) {
        double deltaTime = t.milliseconds();
        t.reset();

        double speed = (m.getCurrentPosition() - prevEncoderPosition) /
                deltaTime; // Gives speed in encoder ticks per millisecond

        double desiredSpeed = d*MaxTicksPerMilliSecond; // Convert a value into a speed

        double error = desiredSpeed - speed; // Error will be from -2.24 to 2.24

        integralError += error * deltaTime;
        double derivativeError = error - prevError;

        double p = d*Ts + Kp*error + integralError*Ti + Td*derivativeError;

        m.setPower(clamp(p));

        prevError = error;
        tel.addData("Ts", Ts);
        tel.addData("Kp", Kp);
        tel.addData("Ti", Ti);
        tel.addData("Td", Td);

        tel.addData("Delta time", deltaTime);
        tel.addData("Current pos", m.getCurrentPosition());
        tel.addData("Prev pos", prevEncoderPosition);

        tel.addData("Current speed", speed);
        tel.addData("Desired speed", desiredSpeed);
        tel.addData("Current error", error);
        tel.addData("Integral error", integralError);
        tel.addData("Predicted error", derivativeError);
        tel.addData("Output", derivativeError);
        tel.update();
        prevEncoderPosition = m.getCurrentPosition();

    }
    private double clamp(double v) {
        return Math.min(Math.max(v, -1), 1);
    }
}
