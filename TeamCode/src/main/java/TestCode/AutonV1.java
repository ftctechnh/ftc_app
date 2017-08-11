package TestCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Jake Mueller on 8/10/2017.
 */

public class AutonV1 extends LinearOpMode {

    private DcMotor motorLeft;  //software names for motors
    private DcMotor motorRight;

    public AutonV1(){

    }

    @Override
    public void runOpMode() throws InterruptedException {
        motorLeft = hardwareMap.dcMotor.get("LeftDrive");  //tells program what the motor is called on hardware
        motorRight = hardwareMap.dcMotor.get("RightDrive");  //also name this on the config

        motorRight.setPower(1.0);  //Moves Forward for 2 seconds and stops
        motorLeft.setPower(-1.0);

        sleep(2000);

        motorRight.setPower(0);  // Stops motor
        motorLeft.setPower(0);

        motorRight.setPower(-1.0); //Moves Backwards
        motorLeft.setPower(1.0);

        sleep(2000);  //Continues for 2 seconds

        motorRight.setPower(0);  // Stops motor
        motorLeft.setPower(0);
    }
}
