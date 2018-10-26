package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.R;

@Autonomous(name = "cobaltClawsAutoTest", group = "Linear OpMode")

public class cobaltClawsAutoTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor; //motor 0
    private DcMotor RightDriveMotor; //motor 1

    //private ColorSensor colorSensor;

    //1000 ticks is about 26 inches

    public enum Direction { Forward, Backward, Left, Right }

    @Override
    public void runOpMode() {


        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {

            //GOLD TEST

            //Looks at the center mineral. If the center mineral is gold, goes straight to the
            // depot, then turns and drives into the crater.
            /*if(isGold()) {*/

                move(Direction.Forward,  2200, 0.25);
                move(Direction.Backward, 200,  0.25);
                turn(Direction.Left,     500,  0.25);
                move(Direction.Forward,  800,  0.25);
                turn(Direction.Left,     270,  0.25);
                move(Direction.Forward,  2000, 0.25);

                break;

            /*} else{

                //Turns to face the left mineral. If the left mineral is gold, goes to the mineral,
                // then turns and goes to the depot, then turns and drives into the crater.
                turn(Direction.Left, 250, 0.25);

                if(isGold()) {

                    move(Direction.Forward, 1000, 0.25);
                    turn(Direction.Right,   200,  0.25);
                    move(Direction.Forward, 1200, 0.25);
                    turn(Direction.Left,    1000, 0.25);
                    move(Direction.Forward, 800,  0.25);
                    turn(Direction.Left,    270,  0.25);
                    move(Direction.Forward, 2000, 0.25);

                    requestOpModeStop();


                } else{

                    //Turns to face the right mineral. If the right mineral is gold, goes to the
                    // mineral, then turns and goes to the depot, then turns and drives into the
                    // crater.
                    turn(Direction.Right, 500, 0.25);

                    if(isGold()){

                        move(Direction.Forward, 1000, 0.25);
                        turn(Direction.Left,    200,  0.25);
                        move(Direction.Forward, 1200, 0.25);
                        turn(Direction.Left,    250,  0.25);
                        move(Direction.Forward, 800,  0.25);
                        turn(Direction.Left,    270,  0.25);
                        move(Direction.Forward, 2000, 0.25);

                        requestOpModeStop();

                    }

                }

            }*/


            //SILVER TEST

            //Looks at the center mineral. If the center mineral is gold, goes to the mineral,
            // then drives to the depot, then turns and drives into the crater.
            /*if(isGold()) {

            move(Direction.Forward,  1200, 0.25);
            move(Direction.Backward, 200,  0.25);
            turn(Direction.Left,     300,  0.25);
            move(Direction.Forward,  1500, 0.25);
            turn(Direction.Left,     200,  0.25);
            move(Direction.Forward,  1750, 0.25);
            turn(Direction.Right,    750,  0.25);
            move(Direction.Forward,  3000, 0.25);

            requestOpModeStop();

            } else{

                //Turns to face the left mineral. If the left mineral is gold, goes to the mineral,
                // then drives to the depot, then turns and drives into the crater.
                turn(Direction.Left, 250, 0.25);

                if(isGold()) {

                    move(Direction.Forward,  1200, 0.25);
                    move(Direction.Backward, 200,  0.25);
                    turn(Direction.Left,     500,  0.25);
                    move(Direction.Forward,  1200, 0.25);
                    turn(Direction.Left,     50,   0.25);
                    move(Direction.Forward,  1750, 0.25);
                    turn(Direction.Right,    750,  0.25);
                    move(Direction.Forward,  3000, 0.25);

                    requestOpModeStop();


                } else{

                    //Turns to face the right mineral. If the right mineral is gold, goes to the
                    // mineral, then drives to the depot, then turns and drives into the
                    // crater.
                    turn(Direction.Right, 500, 0.25);

                    if(isGold()){

                        move(Direction.Forward,  1200, 0.25);
                        move(Direction.Backward, 200,  0.25);
                        turn(Direction.Left,     1000, 0.25);
                        move(Direction.Forward,  400,  0.25);
                        turn(Direction.Left,     30,   0.25);
                        move(Direction.Forward,  1300, 0.25);
                        turn(Direction.Left,     300,  0.25);
                        move(Direction.Forward,  1750, 0.25);
                        turn(Direction.Right,    750,  0.25);
                        move(Direction.Forward,  3000, 0.25);

                        requestOpModeStop();

                    }

                }

            }*/

        }


    }

    private void initialize(){

        //giving internal hardware an external name for the app config
        this.LeftDriveMotor = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.RightDriveMotor = hardwareMap.get (DcMotor.class, "RightDriveMotor");

        //this.colorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");


        //Sets correct directions for motors and servos
        LeftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        RightDriveMotor.setDirection(DcMotor.Direction.FORWARD);

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.update();
    }

    public void move(Direction direction, int distance, double speed){


        //Resets encoder and moves the inputted ticks
        RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);


        if(direction == Direction.Forward) {

            LeftDriveMotor.setTargetPosition(distance);
            RightDriveMotor.setTargetPosition(distance);

        } else if(direction == Direction.Backward) {

            LeftDriveMotor.setTargetPosition(-distance);
            RightDriveMotor.setTargetPosition(-distance);

        }

        LeftDriveMotor.setPower    (speed);
        RightDriveMotor.setPower   (speed);

        while(!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower    (0);
        RightDriveMotor.setPower   (0);

    }

    public void turn(Direction direction, int distance, double speed){


        //Resets the encoders and does a left point turn for the inputted degrees
        RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);

        if(direction == Direction.Left){

            RightDriveMotor.setTargetPosition  (distance);
            LeftDriveMotor.setTargetPosition   (-distance);

        }

        if(direction == Direction.Right){

            RightDriveMotor.setTargetPosition  (-distance);
            LeftDriveMotor.setTargetPosition   (distance);

        }

        LeftDriveMotor.setPower    (speed);
        RightDriveMotor.setPower   (speed);

        while(!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower    (0);
        RightDriveMotor.setPower   (0);

    }

    public boolean motorsBusy(){

        return (RightDriveMotor.isBusy() || LeftDriveMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget(){

        int lDif = (LeftDriveMotor.getTargetPosition() - LeftDriveMotor.getCurrentPosition());
        int rDif = (RightDriveMotor.getTargetPosition() - RightDriveMotor.getCurrentPosition());

        return ((Math.abs(lDif) <= 10) & (Math.abs(rDif) <= 10));

    }

    /*public  boolean isGold() {

        if((colorSensor.red() >= 240 && colorSensor.red() <= 255) &&
                (colorSensor.green() >= 200 && colorSensor.green() <= 230) &&
                (colorSensor.blue() >= 0 && colorSensor.blue() <= 15)) {

            return true;

        }

        return false;

    }*/

}

