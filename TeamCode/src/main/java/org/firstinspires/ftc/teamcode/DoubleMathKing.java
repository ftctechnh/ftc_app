package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.internal.ui.GamepadUser;

public class DoubleMathKing extends LinearOpMode{

    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;
    private DcMotor motor4 = null;

    public void runOpMode(){
        telemetry.addData(  "Status",   "Initialized");
        telemetry.update();

        motor1 = hardwareMap.get(DcMotor.class,                "motor1");
        motor2 = hardwareMap.get(DcMotor.class,                "motor2");
        motor3 = hardwareMap.get(DcMotor.class,                "motor3");
        motor4 = hardwareMap.get(DcMotor.class,                "motor4");
        waitForStart();

        while (opModeIsActive()){
            double power = 0.1;
            double left_y = -gamepad1.left_stick_y;
            double left_x = gamepad1.left_stick_x;
            double left_t = gamepad1.left_trigger;
            double right_t = gamepad1.right_trigger;
            //x vector
            motor2.setPower(power*(-left_x+left_t-right_t));
            motor4.setPower(power*(left_x+left_t-right_t));
            //y vector
            motor1.setPower(power*(left_y+left_t-right_t));
            motor3.setPower(power*(-left_y+left_t-right_t));
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
