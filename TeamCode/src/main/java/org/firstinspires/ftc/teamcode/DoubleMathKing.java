package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Double Math King", group="Basic OP Mode")
@Disabled

public class DoubleMathKing extends LinearOpMode{
    //Initializes motor variables
    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;
    private DcMotor motor4 = null;

    public void runOpMode(){
        //Telemetry initialized message
        telemetry.addData(  "Status",   "Initialized");
        telemetry.update();

        //Motor definitions
        motor1 = hardwareMap.get(DcMotor.class,"motor1");
        motor2 = hardwareMap.get(DcMotor.class,"motor2");
        motor3 = hardwareMap.get(DcMotor.class,"motor3");
        motor4 = hardwareMap.get(DcMotor.class,"motor4");

        //Wait until phone interrupt
        waitForStart();

        //While loop for robot operation
        while (opModeIsActive()){

            //Power variable (0,1), average drive train motor speed
            double power = 0.1;

            //Gamepad's left stick x and y values
            double left_y = -gamepad1.left_stick_y;
            double left_x = gamepad1.left_stick_x;

            //Gamepad's left and right trigger values
            double left_t = gamepad1.left_trigger;
            double right_t = gamepad1.right_trigger;

            //x component vector
            //The sum of trigger and stick values are added and multiplied by power
            //motor2 and motor4 are paired with motor2 left_x = motor4 -left_x for inline movement
            //left_t-right_t are not multiplied by - since all motors are rotating the same way
            motor2.setPower(power*(-left_x+left_t-right_t));
            motor4.setPower(power*(left_x+left_t-right_t));

            //y vector
            //Same setup as above but with the y direction
            motor1.setPower(power*(left_y+left_t-right_t));
            motor3.setPower(power*(-left_y+left_t-right_t));

            //More telemetry. Adds left stick values and trigger values
            telemetry.addLine()
                    .addData("right_y", left_y)
                    .addData("left_x", left_x );
            telemetry.addLine()
                    .addData("left trigger", left_t)
                    .addData("right trigger", right_t);

            telemetry.update();
            }
        }
}
