package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * Created by darwin on 10/29/16.
 *
 * -----IMPORTANT NOTE-----
 *
 *      ROBOT MUST BE
 *      SET UP SUCH THAT
 *      THE CENTER OF THE ROBOT
 *      IS ALLIGNED TO THE CENTER OF THE
 *      TILE FROM THE CORNOR
 *
 */
@Autonomous(name = "9374_AUTONOMOUS_CORNOR_VOTREX",group = "null")

public class NineK_MainBotA_Cornor extends LinearOpMode {

    DcMotor left_f;
    DcMotor right_f;
    DcMotor left_b;
    DcMotor right_b;

    DcMotor shooter_l;
    DcMotor shooter_r;

    Servo center;

    public ElapsedTime runTime = new ElapsedTime();

    final int tpr = 1120;   //Ticks per Rotation
    final int wheelDiameterInInches = 3;// All of out wheels will be inches this year
    int ticks;  //To Be used for later. Just have to define it here
    // Please note that this needs to be changed for any wheel size that we decide to use

    public void runOpMode() throws InterruptedException  {
        left_f = hardwareMap.dcMotor.get("Eng1-left");
        right_f = hardwareMap.dcMotor.get("Eng1-right");
        left_b = hardwareMap.dcMotor.get("Eng2-left");
        right_b = hardwareMap.dcMotor.get("Eng2-right");

        shooter_r = hardwareMap.dcMotor.get("Eng3-left");
        shooter_l = hardwareMap.dcMotor.get("Eng3-right");

        center = hardwareMap.servo.get("Ser1-center");
        left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left_f.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        right_f.setDirection(DcMotorSimple.Direction.REVERSE);
        right_b.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter_r.setDirection(DcMotorSimple.Direction.REVERSE);

        runTime.reset();

        //left.setDirection(DcMotorSimple.Direction.REVERSE);//Or .FORWARD
        telemetry.addData("I am at runOpMode",null);
        super.waitForStart();

        while(super.opModeIsActive()) {
            //This needs to be called at the begginning of every program.
            while (true) {
                shooter_l.setPower(1);
                shooter_r.setPower(1);
                if (runTime.time() > 5) {
                    center.setPosition(.2);
                    if (runTime.time() > 10) {
                        break;
                    }
                }
            }
            //Moving to the cornor vortex
            int clicks = calcClicksForInches(110);

            setALLpower(.5);

            setALLposition(clicks);

            while (opModeIsActive()) {
                telemetry.addData("Target:", clicks);
                telemetry.addData("Left Position", left_f.getCurrentPosition());
                if (left_f.getCurrentPosition() > clicks) {
                    break;
                }
            }
            //Turning
            Turn(45,.5,false);
            //Moving up the cornor vortex
            clicks = calcClicksForInches(25);

            setALLpower(.5);

            setALLposition(clicks);

            while (opModeIsActive()) {
                telemetry.addData("Target:", clicks);
                telemetry.addData("Left Position", left_f.getCurrentPosition());
                if (left_f.getCurrentPosition() > clicks) {
                    break;
                }
            }

            break;
        }

    }

    public void Turn(int degrees, double speed,boolean direction) {
        /*
        I am acutally really proud of myself for this method.
        This method moves the robot a certain amount of degrees.
        //True  = Counter-Clockwise
        //False = Clockwise
        */
        ticks = (degrees*13);   //In reality is is 13.44, but
        //everything needs to be in integers.

        //This took a lot of time to come up with one number
        //Just saying.


        if (direction){         //Going counter-clockwise
            setALLposition(ticks);

            left_b.setPower(-speed);
            left_f.setPower(-speed);
            right_b.setPower(speed);
            right_f.setPower(speed);

        } else { //Going clockwise
            setALLposition(ticks);

            left_f.setPower(speed);
            left_b.setPower(speed);
            right_f.setPower(-speed);
            right_b.setPower(-speed);
        }
        while (true) {
            telemetry.addData("CurrentPos",left_f.getCurrentPosition());
            if ((left_f.getCurrentPosition() - ticks) < 5){
                break;
            }
        }


    }
    private int calcClicksForInches(double distanceInInches) {
        //Currently there are 1120 different positions on any given wheel
        double revlutions = distanceInInches / (wheelDiameterInInches * Math.PI); //Find out how many revolutations
        int clicks = (int) (revlutions * tpr); //This is a pretty big number, gonna be in the 1,000's
        return clicks; //The position to set the wheels to.
    }
    public void moveToPosition(int distanceInIN,double power){
        setALLposition(calcClicksForInches(distanceInIN));
        setALLpower(power);
        while (true){
            if (left_f.getCurrentPosition() > calcClicksForInches(distanceInIN)){
                break;
            }
        }
    }
    public void setALLpower(double power){
        left_b.setPower(power);
        left_f.setPower(power);
        right_b.setPower(power);
        right_f.setPower(power);
    }
    public void setALLposition(int position) {
        left_b.setTargetPosition(position);
        left_f.setTargetPosition(position);
        right_b.setTargetPosition(position);
        right_f.setTargetPosition(position);

    }
}
