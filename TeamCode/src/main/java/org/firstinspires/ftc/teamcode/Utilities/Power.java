package org.firstinspires.ftc.teamcode.Utilities;

/**
 * Created by 72710 on 11/14/2017.
 */

public class Power {
  
    Telemetry tele;
  
    public Power(Telemetry tele) {
        telemetry = tele;
    }
  
    public void power(HardwareDevice motor, double power) {
        telemetry.addData("motor power", power);
        try {
            motor.setPower(power);
        } catch (Exception opModeException) {
            telemetry.addData("Can't power (not mapped)", "motor");
        }
    }
    
    public void position(HardwareDevice servo, double power) {
        telemetry.addData("servo power", power);
        try {
            motor.setPosition(power);
        } catch (Exception opModeException) {
            telemetry.addData("Can't power (not mapped)", "servo");
        }
    }
}
