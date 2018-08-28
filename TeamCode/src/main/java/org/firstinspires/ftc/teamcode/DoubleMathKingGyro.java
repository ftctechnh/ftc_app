package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Double Math King Gyro", group="Basic OP Mode")
@Disabled

public class DoubleMathKingGyro extends LinearOpMode{
    //Initializes motor variables
    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;
    private DcMotor motor4 = null;
    private GyroSensor gyro = null;
    public ElapsedTime timer = new ElapsedTime();

    private static double sigmoid(long time, boolean derivative, boolean inverse){
        //Sigmoid function is NOT log base (*)
        double y = 1/(1+Math.exp(-time));
        if (inverse){
            //Derivative of logistic function
            if (derivative){
                y = Math.log(time/(1-time));
            } else{
                y = 1/(time-time*time);
            }
        } else if (derivative){
            y = y*(1-y);
        }
        return y;
    }


    public void runOpMode(){
        //Telemetry initialized message
        telemetry.addData(  "Status",   "Initialized");
        telemetry.update();

        //Hardware definitions
        motor1 = hardwareMap.get(DcMotor.class,"motor1");
        motor2 = hardwareMap.get(DcMotor.class,"motor2");
        motor3 = hardwareMap.get(DcMotor.class,"motor3");
        motor4 = hardwareMap.get(DcMotor.class,"motor4");
        gyro = hardwareMap.get(GyroSensor.class,"gyro");
        //Motor List
        DcMotor[] motors = {motor1, motor3, motor2, motor4};

        //Base angle/Zero position angle
        int base_angle = 0;

        //Array zero position
        int base = 0;

        boolean press = false;
        long base_time = 0;

        //Wait until phone interrupt
        waitForStart();
        //While loop for robot operation
        while (opModeIsActive()){
            //Gamepad's left stick x and y values
            double left_y = -gamepad1.left_stick_y;
            double left_x = gamepad1.left_stick_x;

            //Gamepad's left and right trigger values
            double left_t = gamepad1.left_trigger;
            double right_t = gamepad1.right_trigger;

            //Boolean for distance reset
            double g_angle = gyro.getRotationFraction();
            if ((Math.abs(g_angle)-base_angle-90) > 0){
                base_angle += 90;
                base += 2;
            }

            //Power variable (0,1), average drive train motor speed
            if (gamepad1.atRest()){
                base_time = timer.nanoseconds();
            } else {
                double power = sigmoid((timer.nanoseconds()- base_time)/timer.SECOND_IN_NANO,false, false);
                /*
                The motors are paired and power based on being the x or y component
                of the vector.

                The - on the left_x or left_y ensures that the "paired" motors run in
                tandem.

                The difference left_t-right_t calculates the delta between the right
                and left triggers. They are not multiplied as the motors are supposed
                to run in a circle.

                The sum of the left_(x/y) and the trigger difference allows for movement
                on the x y plane with added rotation. Think drifting.

                All of this is multiplied by the power variable allowing fine power
                control.
                */

                //x component vector
                //motor 2
                motors[(2+base_angle)%4].setPower(power*(-left_x+left_t-right_t));
                //motor4
                motors[(3+base_angle)%4].setPower(power*(left_x+left_t-right_t));

                //y vector
                //motor1
                motors[(0+base_angle)%4].setPower(power*(left_y+left_t-right_t));
                //motor3
                motors[(1+base_angle)%4].setPower(power*(-left_y+left_t-right_t));

            }

            //More telemetry. Adds left stick values and trigger values
            telemetry.addLine()
                    .addData("right_y", left_y)
                    .addData("left_x", left_x );
            telemetry.addLine()
                    .addData("left trigger", left_t)
                    .addData("right trigger", right_t);
            telemetry.addLine()
                    .addData("angle", g_angle);

            telemetry.update();
        }
    }
}
