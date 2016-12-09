package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
@Disabled
@Autonomous(name = "9374_AUTONOMOUS_CORNOR_VOTREX",group = "null")

public class NineK_MainBotA_Cornor extends LinearOpMode {

    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBack;

    DcMotor shooterLeft;
    DcMotor shooterRight;

    Servo center;

    public ElapsedTime runTime = new ElapsedTime();

    final int tpr = 1120;   //Ticks per Rotation
    final int wheelDiameterInInches = 3;// All of out wheels will be inches this year
    int ticks;  //To Be used for later. Just have to define it here
    // Please note that this needs to be changed for any wheel size that we decide to use

    public void runOpMode() throws InterruptedException  {
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBack = hardwareMap.dcMotor.get("motor-rightBACK");

        shooterRight = hardwareMap.dcMotor.get("shooter-right");
        shooterLeft = hardwareMap.dcMotor.get("shooter-left");

        center = hardwareMap.servo.get("shooter-servo");
        leftFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);

        runTime.reset();

        //left.setDirection(DcMotorSimple.Direction.REVERSE);//Or .FORWARD
        telemetry.addData("I am at runOpMode",null);
        super.waitForStart();

        while(super.opModeIsActive()) {
            //This needs to be called at the begginning of every program.
            while (true) {
                shooterLeft.setPower(1);
                shooterRight.setPower(1);
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
                telemetry.addData("Left Position", leftFRONT.getCurrentPosition());
                if (leftFRONT.getCurrentPosition() > clicks) {
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
                telemetry.addData("Left Position", leftFRONT.getCurrentPosition());
                if (leftFRONT.getCurrentPosition() > clicks) {
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

            leftBACK.setPower(-speed);
            leftFRONT.setPower(-speed);
            rightBack.setPower(speed);
            rightFRONT.setPower(speed);

        } else { //Going clockwise
            setALLposition(ticks);

            leftFRONT.setPower(speed);
            leftBACK.setPower(speed);
            rightFRONT.setPower(-speed);
            rightBack.setPower(-speed);
        }
        while (true) {
            telemetry.addData("CurrentPos",leftFRONT.getCurrentPosition());
            if ((leftFRONT.getCurrentPosition() - ticks) < 5){
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
            if (leftFRONT.getCurrentPosition() > calcClicksForInches(distanceInIN)){
                break;
            }
        }
    }
    public void setALLpower(double power){
        leftBACK.setPower(power);
        leftFRONT.setPower(power);
        rightBack.setPower(power);
        rightFRONT.setPower(power);
    }
    public void setALLposition(int position) {
        leftBACK.setTargetPosition(position);
        leftFRONT.setTargetPosition(position);
        rightBack.setTargetPosition(position);
        rightFRONT.setTargetPosition(position);

    }
}
