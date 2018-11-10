package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "spinMotors", group = "Joystick Opmode")
public class spinMotors extends OpMode {

    private DcMotor aDrive = null;
    private DcMotor bDrive = null;
    private DcMotor cDrive = null;
    private DcMotor dDrive = null;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        aDrive = hardwareMap.get(DcMotor.class, "aDrive");
        bDrive = hardwareMap.get(DcMotor.class, "bDrive");
        cDrive = hardwareMap.get(DcMotor.class, "cDrive");
        dDrive = hardwareMap.get(DcMotor.class, "dDrive");
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
    }

    @Override
    public void loop() {

        telemetry.addData("cDrive", cDrive.getCurrentPosition());


        if (gamepad1.y){
            aDrive.setPower(0.9);
            bDrive.setPower(-0.9);
        }else if (gamepad1.a){
            aDrive.setPower(-0.9);
            bDrive.setPower(0.9);
        }
            aDrive.setPower(gamepad1.left_stick_y);
            bDrive.setPower(-gamepad1.right_stick_y);


        if (gamepad1.dpad_up) {
            cDrive.setPower(0.5);
        }else if (gamepad1.dpad_down){
            cDrive.setPower(-0.5);
        }else{
            cDrive.setPower(0);
        }

    }

}
