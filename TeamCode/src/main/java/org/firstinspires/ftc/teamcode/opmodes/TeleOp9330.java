package org.firstinspires.ftc.teamcode.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.Clamps9330;

/**
 * Created by robot on 9/25/2017.
 */

@TeleOp(name="TeleOp9330", group="Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class TeleOp9330 extends OpMode {
    Hardware9330 robotMap = new Hardware9330();
    Clamps9330 clamps = new Clamps9330(robotMap);

    float yPower = 0;
    float spinPower = 0;
    float liftSpeed = 50;
    boolean aBtnHeld = false;
    boolean bBtnHeld = false;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robotMap.init(hardwareMap);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop()
    {
        yPower = gamepad1.left_stick_y;
        spinPower = gamepad1.right_stick_x;

//        Hardware9330.leftMotor.setPower(-yPower - spinPower);
        Hardware9330.rightMotor.setPower(yPower - spinPower);



        if(gamepad2.a) {
            if (aBtnHeld == true) return;
            clamps.toggleLowClamp();
            telemetry.addData("Program", "Low clamp toggled!");
            telemetry.update();
            aBtnHeld = true;
        } else aBtnHeld = false;

        if(gamepad2.b) {
            if (bBtnHeld == true) return;
            clamps.toggleHighClamp();
            telemetry.addData("Program", "High clamp toggled!");
            bBtnHeld = true;
        } else bBtnHeld = false;

        if(gamepad2.dpad_up) { telemetry.addData("Program", "Lift rising!"); }//Hardware9330.liftMotor.setPower(liftSpeed);  }
        else if (gamepad2.dpad_down) { telemetry.addData("Program", "Lift falling!"); } //Hardware9330.liftMotor.setPower(-liftSpeed);  }
//        else Hardware9330.liftMotor.setPower(0);

        telemetry.update();
}

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
//        Hardware9330.leftMotor.setPower(0);
//        Hardware9330.rightMotor.setPower(0);
    }
}