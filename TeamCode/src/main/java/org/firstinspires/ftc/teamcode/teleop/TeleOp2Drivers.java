package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TeleOp2Drivers extends LinearOpMode {
    //  private Gyroscope imu;
    private DcMotor left_front;
    private DcMotor right_front;
    private DcMotor left_back;
    private DcMotor right_back;
    private DcMotor Rack_and_Pinion_Motor;
    private DcMotor lift_Base;
    private DcMotor lift_Extn;
    private CRServo servo0;
    int speed = 2;
    // private DigitalChannel digitalTouch;
    // private DistanceSensor sensorColorRange;
    // private Servo servoTest;


    @Override
    public void runOpMode() {
        //  imu = hardwareMap.get(Gyroscope.class, "imu");
        left_front = hardwareMap.get(DcMotor.class, "left_Front");
        right_front = hardwareMap.get(DcMotor.class, "right_Front");
        left_back = hardwareMap.get(DcMotor.class, "left_Rear");
        right_back = hardwareMap.get(DcMotor.class, "right_Rear");
        lift_Base = hardwareMap.get(DcMotor.class, "lift_Base");
        lift_Extn = hardwareMap.get(DcMotor.class, "lift_Extn");
        servo0 = hardwareMap.get(CRServo.class, "servo0");
        left_front.setDirection(DcMotor.Direction.REVERSE);
        left_back.setDirection(DcMotor.Direction.REVERSE);
        // Set to REVERSE if using AndyMark motors
        Rack_and_Pinion_Motor = hardwareMap.get(DcMotor.class, "rack_pinion");
        //   digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        //   sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        //  servoTest = hardwareMap.get(Servo.class, "servoTest");

        telemetry.addData("Status", "Intialized");
        telemetry.update();
        //Wait for the game to start (driver presses PLAY)
        waitForStart();

        //run until the end of the match (driver presses STOP)
        while (opModeIsActive()){
            servo0.setPower(0);
            lift_Extn.setPower(0);
            Rack_and_Pinion_Motor.setPower(0);
            left_front.setPower(((gamepad1.right_stick_y)/speed));
            left_back.setPower(((gamepad1.right_stick_y)/speed));
            right_front.setPower((gamepad1.left_stick_y)/speed);
            right_back.setPower((gamepad1.left_stick_y)/speed);
            while(gamepad1.right_bumper){
                Rack_and_Pinion_Motor.setPower(0.5);
                telemetry.addData("Rack and Pinion motor","Heading Up");
            }
            while(gamepad1.left_bumper){
                Rack_and_Pinion_Motor.setPower(-0.5);
                telemetry.addData("Rack and Pinion motor","Heading Down");
            }
            if(gamepad2.right_trigger > 0){
                lift_Base.setPower(gamepad1.right_trigger/4);
            }
            if(gamepad2.left_trigger > 0){
                lift_Base.setPower(-(gamepad1.left_trigger)/2);
            }
            while(gamepad2.y){
                lift_Extn.setPower(0.5);
                telemetry.addData("lift_Extn Motor",lift_Extn.getPower());
            }
            while(gamepad2.a){
                lift_Extn.setPower(-0.5);
                telemetry.addData("lift_Extn Motor",lift_Extn.getPower());
            }
            while(gamepad2.x){
                servo0.setPower(2);

            }
            while(gamepad2.b){
                servo0.setPower(-2);

            }
            if(gamepad1.a){
                if(speed == 2){
                    speed = 4;
                } else {
                    if(speed == 4){
                        speed = 2;
                    }
                }
            }
            //  while(gamepad1.left_stick_y==-1 && gamepad1.right_stick_y==1){
            //     right_drive.setPower(-1);
            //     left_drive.setPower(1);

            //  }
            //  while(gamepad1.left_stick_y==1 && gamepad1.right_stick_y==-1){
            //     right_drive.setPower(1);
            //     left_drive.setPower(-1);

            //  }


            telemetry.addData("Speed is",speed);
            telemetry.addData("Power of left front",left_front.getPower());
            telemetry.addData("Power of left back", left_back.getPower());
            telemetry.addData("Power of right front",right_front.getPower());
            telemetry.addData("Power of right back",right_back.getPower());
            telemetry.addData("Status", "Running");
            telemetry.addData("lift_Base Motor",lift_Base.getPower());
            telemetry.update();

        }
    }
}
