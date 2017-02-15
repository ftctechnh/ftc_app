package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by 292486 on 10/26/2016.
 */

public class PIDTest {

    private HardwareMap map;
    private DcMotor leftMotor;  //Get the front left and right motors when instantiating a controller
    private DcMotor rightMotor;

    private ElapsedTime timeController;

    private double error, lastError, integral, derivative;  //Controller variables
    private double kp, ki, kd, kf;  //Tuning variables/coefficients TODO: kf might have been intended for a Kalman Filter
    private double correction, maxIntegral, maxCorrection ,tolerance;  //Limits and output
    private double lastTime, deltaTime;

    public PIDTest(HardwareMap map, DcMotor motor1, DcMotor motor2){
        this.map = map;
        leftMotor = motor1;
        rightMotor = motor2;

        maxIntegral = 500;
        maxCorrection = .25;
        tolerance = 50;

        timeController = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);  //Keep track of time in miliseconds. When instantiated, timeController.time() is 0

        setTuning(0.0005, 0.00005, 0.001, 0.0001);  //Default tuning. We can re-parameterize it later
    }

    public void setTuning(double kp, double ki, double kd, double kf){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kf = kf;
    }

    public void setMaxCorrection(double correction) {
        this.maxCorrection = correction;
    }

    /*
    Proportional: main correction (make changes based on an error)
    Integral: eliminates steady state error/offset and contributes to effect of proportional term
    Derivative: reacts to changes... less overshoot and faster settling time
     */
    public double getCorrection(){
        deltaTime = timeController.time() - lastTime;    //Find how much time has passed since last one. In nanoseconds
        error = Math.abs(leftMotor.getCurrentPosition()) - Math.abs(rightMotor.getCurrentPosition());   //Difference in encoders is the process variable. Setpoint is 0 (we want encoders to be equal), but we don't need to type that
        if(error < tolerance) error = 0;

        integral = (1/deltaTime)*(integral + error);    //Assume time interval is near 0, so the integral is sum of errors times delta(time)
        if(integral > maxIntegral) integral = maxIntegral;
        else if(integral < -maxIntegral) integral = -maxIntegral;

        derivative = deltaTime *(error - lastError);    //Assume time interval is near 0, so the derivative is delta(error)/delta(time)
        //To predict how much the error will change in the future, dy = e'(t)dt where dy=how much it will change & dt=how much time later (ex// dt=10 means 10 miliseconds later)
        //We assume the change in time from the last correction is equal to the next change in time
        //Low pass filter (for what?): Esubt = r(Esub(t-1) + derivative)

        correction = (kp*error) + (ki*integral) - (kd*derivative);
        if(correction > maxCorrection) correction = maxCorrection;
        else if(correction < -maxCorrection) correction = -maxCorrection;

        lastError = error;
        lastTime += deltaTime;  //Should we set it to timeController.time() or add the difference? Because .time() may have changed at this point
        return correction;
    }

    //Reset the PID before starting to use it again
    public void resetPID()
    {
        error = 0;
        integral = 0;
        derivative = 0;
        correction = 0;
        deltaTime = 0;
        lastTime = timeController.time();
    }
}
