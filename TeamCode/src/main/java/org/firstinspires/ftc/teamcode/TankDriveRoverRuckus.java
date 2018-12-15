package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RoboticsUtils.PID;
//import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;
/**
 * Created by emilyhinds on 9/9/18.
 */
@TeleOp(name="TankDriveRoverRuckus", group="TankDriveRoverRuckus")
public class TankDriveRoverRuckus extends LinearOpMode {
    // Declare OpMode members.
    private DcMotor Left;
    private DcMotor Right;
    private DcMotor Shoulder;
    private DcMotor Elbow;
    //private DcMotor Wrist;
   // private Servo LGrabber;
    //private Servo RGrabber;
    //private DcMotor Winch;
    //private DcMotor Sideways;
    @Override
    public void runOpMode() {
        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");
        Shoulder = hardwareMap.get(DcMotor.class, "shoulder");
        Elbow = hardwareMap.get(DcMotor.class, "elbow");
        //Wrist = hardwareMap.get(DcMotor.class,"wrist");
        //LGrabber = hardwareMap.get(Servo.class, "lgrab");
       // RGrabber = hardwareMap.get(Servo.class, "rgrab");
        //Winch = hardwareMap.get(DcMotor.class,"winch");
        //Sideways = hardwareMap.get(DcMotor.class,"sideways");
        //boolean bumper = false;
        //int wristdir = 1;
        waitForStart();
      /*  boolean b = true;
        boolean x = true;
        double loffset = 0;
        double roffset = 0;
        double prevTime= 0;
        double currentTime = 0;
        double elbow = Elbow.getCurrentPosition();
        double shoulder = Shoulder.getCurrentPosition();
        //PID elbowPID = new PID(1,.0000000001,.00001);
        //PID shoulderPID = new PID(1,.0000000001,.00001);
        // run until the end of the match (driver presses STOP) */
        while (opModeIsActive()) {
            Left.setPower(Range.clip(gamepad1.left_stick_y, -1, 1));
            Right.setPower(Range.clip(-gamepad1.right_stick_y, -1, 1));
            //Shoulder.setPower(Range.clip(shoulderPID.getPID(), -1,1));
            //Elbow.setPower(Range.clip(elbowPID.getPID(), -1,1));
            Shoulder.setPower(.5*Range.clip(gamepad2.left_stick_y, -1,1));
            Elbow.setPower(.5*Range.clip(gamepad2.right_stick_y, -1,1));
            /*if(gamepad2.right_bumper&&!bumper){
                wristdir = wristdir*-1;
            }
            bumper = gamepad2.right_bumper;
            //Wrist.setPower(Range.clip(.5*gamepad2.right_trigger*wristdir,-1,1)); */
            /*if(gamepad2.b&&!b){
                roffset = roffset*-1;
            }*/
           /* if(gamepad2.b&&!b&&loffset==0){
                roffset=.6;
            }else if(gamepad2.b&&!b){
                roffset=0;
            }
            b = gamepad2.b;
            RGrabber.setPosition(Range.clip(RGrabber.MAX_POSITION-roffset,RGrabber.MIN_POSITION,RGrabber.MAX_POSITION));
            if(gamepad2.x&&!x&&loffset==0){
                loffset=.6;
            }else if(gamepad2.x&&!x){
                loffset=0;
            }
            x = gamepad2.x;
            LGrabber.setPosition(Range.clip(LGrabber.MIN_POSITION+loffset,LGrabber.MIN_POSITION,LGrabber.MAX_POSITION));
            prevTime = currentTime;
            currentTime =getRuntime(); */
            //elbow-=gamepad1.right_stick_y*(currentTime-prevTime)*4/100;
            //shoulder-=gamepad1.left_stick_y*(currentTime-prevTime)*4/100;
            //elbowPID.iteratePID(elbow-Elbow.getCurrentPosition(), currentTime-prevTime);
            //shoulderPID.iteratePID(shoulder-Shoulder.getCurrentPosition(), currentTime-prevTime);
            //Winch code- up: right trigger, down: left trigger
            //Winch.setPower(Range.clip(gamepad1.right_trigger-gamepad1.left_trigger,-1,1));
            //Sideways- front wheel, push both sticks to control
            // both gamepads are inversed, so need to minus
            //Sideways.setPower(Range.clip(-gamepad1.right_stick_y+gamepad1.left_stick_y,-1,1));
            //telemetry.addData("bools",liftmode+" "+a);
            //telemetry.addData("lift pos", Lift.getCurrentPosition());
            //telemetry.addData("loffset",loffset);
            //telemetry.addData("roffset", roffset);
            telemetry.update();
        }
    }
}
