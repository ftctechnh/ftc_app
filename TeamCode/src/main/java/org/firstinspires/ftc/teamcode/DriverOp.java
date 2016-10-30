package org.firstinspires.ftc.robotcontroller.internal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="DriverOp", group ="Driving")
public class DriverOp extends OpMode {

    // Actuators
    DcMotor /*m1, m2,*/ lift;
    Servo button, release;

    // Direction
    boolean forwards;
    boolean backwards;

    // Lift Speed
    double liftSpeed;

    @Override
    public void init() {

        // Drive Motors
        //m1 = hardwareMap.dcMotor.get("1");
        //m2 = hardwareMap.dcMotor.get("2");

        // Change one side direction
        //m2.setDirection(DcMotorSimple.Direction.REVERSE);

        // Lift assembly
        lift = hardwareMap.dcMotor.get("lift");
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        liftSpeed = 0;

        // Button Pusher Assembly
        button = hardwareMap.servo.get("button");
        button.setPosition(0);

        // Release
        release = hardwareMap.servo.get("release");
        release.setPosition(0);

        // Set Direction
        forwards = true;
        backwards = false;

    }

    // Unused setup method
    /*
    @Override
    public void init_loop() {}
    */

    @Override
    public void loop() {

        // Change Direction
        if(backwards && gamepad1.y){
            forwards = true;
            backwards = false;
        }
        if(forwards && gamepad1.a){
            forwards = false;
            backwards = true;
        }

        // Gamepad2 Operations
        if(gamepad2.left_bumper || gamepad1.right_bumper)
            release.setPosition(180);
        else
            release.setPosition(0);

        if(gamepad2.x)
            button.setPosition(0);
        else
            button.setPosition(90);


        lift.setPower(liftSpeed);

        liftSpeed = 0;

        // Coarse
        if(Math.abs(gamepad2.left_stick_y) > .1)
            liftSpeed += gamepad2.left_stick_y;
        // Fine
        if(Math.abs(gamepad2.right_stick_y) > .1)
            liftSpeed += (gamepad2.right_stick_y * .03);

        telemetry.addData("liftSpeed", liftSpeed);
        updateTelemetry(telemetry);
    }
}
