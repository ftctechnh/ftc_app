package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * Created by darwin on 10/29/16.
 */
@Autonomous(name = "9KA",group = "null")
@Disabled
public class NineK_cornor_vortexA extends LinearOpMode {

    DcMotor left;
    DcMotor right;
    //ColorSensor sensor;
    Servo slapper;
    public ElapsedTime runTime = new ElapsedTime();

    final int tpr = 1120;   //Ticks per Rotation
    final int wheelDiameterInInches = 4;    // All of out wheels will be inches this year
    int ticks;  //To Be used for later. Just have to define it here
    // Please note that this needs to be changed for any wheel size that we decide to use


    public void runOpMode() throws InterruptedException  {
        left = hardwareMap.dcMotor.get("Eng1-left");
        right = hardwareMap.dcMotor.get("Eng1-right");
        slapper = hardwareMap.servo.get("Ser1-front");

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setDirection(DcMotorSimple.Direction.REVERSE);

        left.setMode(DcMotor.RunMode.RESET_ENCODERS);
        right.setMode(DcMotor.RunMode.RESET_ENCODERS);


        runTime.reset();
        //left.setDirection(DcMotorSimple.Direction.REVERSE);//Or .FORWARD
        super.waitForStart();
        while(super.opModeIsActive()){

            int clicks = calcClicksForInches(65);
            left.setPower(.5);
            right.setPower(.5);
            left.setTargetPosition(clicks);
            right.setTargetPosition(clicks);
            while(opModeIsActive()){        //This is moving 65 inches
                telemetry.addData("Target:", clicks);
                telemetry.addData("Left Position", left.getCurrentPosition());
                if (left.getCurrentPosition() > clicks){
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
            left.setTargetPosition(ticks);
            right.setTargetPosition(ticks);

            left.setPower(-speed);
            right.setPower(speed);
        } else { //Going clockwise
            left.setTargetPosition(ticks);
            right.setTargetPosition(ticks);

            left.setPower(speed);
            right.setPower(-speed);
        }
        while (true) {
            telemetry.addData("CurrentPos",left.getCurrentPosition());
            if ((left.getCurrentPosition() - ticks) < 5){
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
}
