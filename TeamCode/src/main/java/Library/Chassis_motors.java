package Library;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import  com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Uz as a starter
 */

public class Chassis_motors
{
    private DcMotor motorLeft;
    private DcMotor motorRight;

    public Chassis_motors(HardwareMap hardwareMap){    // constructor
        motorLeft = hardwareMap.dcMotor.get("LeftDrive");
        motorRight = hardwareMap.dcMotor.get("RightDrive");

    }

    // set direction forward
    public void set_Direction_Forward () {
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
    }

    // set direction reverse
    public void set_Direction_Reverse () {
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    // To move the chassis forward, power 0-1
    public void move_Forward (double power) {
        set_Direction_Forward();
        run_Motors_no_encoder(power,power);
    }

    // To move the chassis reverse, power 0-1
    public void move_Reverse (double power) {
        set_Direction_Reverse();
        run_Motors_no_encoder(power,power);
    }

    public void run_Motors_no_encoder(double leftPower, double rightPower) {
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
    }

}
