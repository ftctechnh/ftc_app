package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;


/** DRIVETRAIN CONFIGURATION
 *      front
 *      C   D
 * left       right
 *      B   A
 *      back
 */



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

    //make sure to match these names when getting values
    private String[] titles = new String[] {"forward", "title1", "title2", "title3", "title4", "title5", "title6", "title7", "title8"};
    private double[] values = new double[] {    0.4,        0,        0,        0,        0,        0,        0,        0,        0   };

//    Tuner tuner = new Tuner(titles, values, gamepad1, telemetry);

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        aDrive = hardwareMap.get(DcMotor.class, "1-0");
        aDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        bDrive = hardwareMap.get(DcMotor.class, "1-1");
        bDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        cDrive = hardwareMap.get(DcMotor.class, "1-2");
        cDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        dDrive = hardwareMap.get(DcMotor.class, "1-3");
        dDrive.setDirection(DcMotorSimple.Direction.REVERSE);

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

        //        tuner.tune();
        //        telemetry.addData("get(forward)", tuner.get("forward"));


        if (gamepad1.dpad_up){
//            forward = tuner.get("forward");
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
            forward = Calculate.sensCurve(-gamepad1.left_stick_y, 2);
            turn = Calculate.sensCurve(-gamepad1.right_stick_x, 2);
        }

        aDrive.setPower(forward+turn);
        bDrive.setPower(forward-turn);
        cDrive.setPower(forward-turn);
        dDrive.setPower(forward+turn);


        if(gamepad2.dpad_up){ aMech.setPower(0.9); }
        else if(gamepad2.dpad_down){ aMech.setPower(-0.9); }
        else{ aMech.setPower(0); }

        if(gamepad2.dpad_right){ bMech.setPower(0.9); }
        else if(gamepad2.dpad_left){ bMech.setPower(-0.9); }
        else{ bMech.setPower(0); }


        if(gamepad2.y){ cMech.setPower(0.9); }
        else if(gamepad2.a){ cMech.setPower(-0.9); }
        else{ cMech.setPower(0); }

        if(gamepad2.b){ dMech.setPower(0.9); }
        else if(gamepad2.x){ dMech.setPower(-0.9); }
        else{ dMech.setPower(0); }

        telemetry.addData("forward",forward);
        telemetry.addData("turn",turn);
        telemetry.update();

    }



}
