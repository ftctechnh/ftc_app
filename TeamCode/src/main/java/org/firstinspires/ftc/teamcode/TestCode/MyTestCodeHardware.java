
package org.firstinspires.ftc.teamcode.TestCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by team on 7/18/2017. During FTC JAVA and robotics software workshop
 */
public class MyTestCodeHardware extends OpMode {
    // after typing extends OpMode remember to implement methods with Alt-Enter

    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor liftMotor;
    DcMotor armMotor;

    double rightPower;
    double leftPower;
    double liftPower;
    double armPower;

    @Override
    public void init() {
        hardwareInit();
    }

    @Override
    public void loop() {

    }

    private void hardwareInit() {
        rightMotor = hardwareMap.dcMotor.get("rm");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor = hardwareMap.dcMotor.get("lm");
        liftMotor = hardwareMap.dcMotor.get("lift");
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor = hardwareMap.dcMotor.get("arm");
    }

}       // end myTestCodeHardware


/* WE MOVED THIS LOOP TO MyTestCodeTeleOp

    @Override
    public void loop() {

        rightPower = gamepad1.right_stick_x; // gamepad right stick for right robot motor
        leftPower = gamepad1.left_stick_x; // gamepad left stick for left robot motor

        if (gamepad1.dpad_up){              //  lift motor controlled by d-pad on controller
            liftPower = 1;
        }
        else if (gamepad1.dpad_down){
            liftPower = -1;
        }
        else {
            liftPower = 0;
        }

        rightMotor.setPower(rightPower);
        leftMotor.setPower(leftPower);
        liftMotor.setPower(liftPower);
        liftMotor.setPower(armPower);

        myTelemetry();

    }   //end loop
*/

/* deleted from MyTestCodeHardware class above.
    moved this to myTestCodeTelemetry

    protected void myTelemetry() {
        telemetry.addData("Right Motor Power", rightPower);
        telemetry.addData("Left Motor Power", leftPower);
        telemetry.addData("Lift Motor Power", liftPower);
        telemetry.addData("Arm Motor Power", armPower);

    }
*/
