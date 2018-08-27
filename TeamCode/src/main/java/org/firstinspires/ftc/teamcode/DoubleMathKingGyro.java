package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Double Math King Gyro", group="Basic OP Mode")
@Disabled

public class DoubleMathKingGyro extends LinearOpMode{
    //Initializes motor variables
    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;
    private DcMotor motor4 = null;
    private GyroSensor gyro = null;

    public void runOpMode(){
        //Telemetry initialized message
        telemetry.addData(  "Status",   "Initialized");
        telemetry.update();
        //Motor definitions
        motor1 = hardwareMap.get(DcMotor.class,"motor1");
        motor2 = hardwareMap.get(DcMotor.class,"motor2");
        motor3 = hardwareMap.get(DcMotor.class,"motor3");
        motor4 = hardwareMap.get(DcMotor.class,"motor4");
        gyro = hardwareMap.get(GyroSensor.class,"gyro");
        DcMotor[] motors = {motor1, motor3, motor2, motor4};
        //Base angle/Zero position angle
        int base_angle = 0;
        //Array zero position
        int base = 0;
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
            //Boolean for distance reset
            double g_angle = gyro.getRotationFraction();
            if ((Math.abs(g_angle)-base_angle-90) > 0){
                base_angle += 90;
                base += 2;
            }
            //x component vector: calls motors 2 and 4
            motors[(2+base_angle)%4].setPower(power*(-left_x+left_t-right_t));
            motors[(3+base_angle)%4].setPower(power*(left_x+left_t-right_t));
            //y vector: calls motors 1 and 3
            motors[(0+base_angle)%4].setPower(power*(left_y+left_t-right_t));
            motors[(1+base_angle)%4].setPower(power*(-left_y+left_t-right_t));
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
