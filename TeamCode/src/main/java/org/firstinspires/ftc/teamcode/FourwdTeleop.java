package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "FourwdTeleop", group = "Joystick Opmode")
public class FourwdTeleop extends OpMode {

    private DcMotor aDrive = null;
    private DcMotor bDrive = null;
    private DcMotor cDrive = null;
    private DcMotor dDrive = null;
    private DcMotor aMech = null;
    private DcMotor bMech = null;
    private DcMotor cMech = null;
    private DcMotor dMech = null;

    private double forward = 0;
    private double turn = 0;


    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        aDrive = hardwareMap.get(DcMotor.class, "1-0");
        aDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        bDrive = hardwareMap.get(DcMotor.class, "1-1");
        bDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        cDrive = hardwareMap.get(DcMotor.class, "1-2");
        cDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        dDrive = hardwareMap.get(DcMotor.class, "1-3");
        dDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        aMech = hardwareMap.get(DcMotor.class, "2-0");
        bMech = hardwareMap.get(DcMotor.class, "2-1");
        cMech = hardwareMap.get(DcMotor.class, "2-2");
        dMech = hardwareMap.get(DcMotor.class, "2-3");

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

        if (gamepad1.dpad_up){
            forward = 0.9;
            turn = 0;
        }else if (gamepad1.dpad_down){
            forward = -0.9;
            turn = 0;}
        else if (gamepad1.dpad_right){
            forward = 0;
            turn = 0.9;
        }else if (gamepad1.dpad_left){
            forward = 0;
            turn = -0.9;
        }else{
            forward = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x;
        }

        aDrive.setPower(Range.clip(forward+turn,-1,1));
        bDrive.setPower(Range.clip(forward-turn,-1,1));
        cDrive.setPower(Range.clip(forward-turn,-1,1));
        dDrive.setPower(Range.clip(forward+turn,-1,1));


        if(gamepad2.dpad_up){ aMech.setPower(0.9); }
        else if(gamepad2.dpad_down){ aMech.setPower(-0.9); }

        if(gamepad2.dpad_right){ bMech.setPower(0.9); }
        else if(gamepad2.dpad_left){ bMech.setPower(-0.9); }

        if(gamepad2.y){ cMech.setPower(0.9); }
        else if(gamepad2.a){ cMech.setPower(-0.9); }

        if(gamepad2.b){ dMech.setPower(0.9); }
        else if(gamepad2.x){ dMech.setPower(-0.9); }

        telemetry.addData("forward",forward);
        telemetry.addData("turn",turn);
        telemetry.update();

    }

}
