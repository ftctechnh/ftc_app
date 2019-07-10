package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp(name = "MecanumDrive",group="teleop")
public class MechanumWheelDriving extends OpMode {
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;

    HardwareMapOne robot       = new HardwareMapOne();
    double speed=0;


    @Override
    public void init() {

        robot.init(hardwareMap);
    }
    @Override
    public void init_loop(){
    }


    @Override
    public void start() {

    }
    @Override
    public void loop() {


        double analog;


        analog = gamepad1.right_stick_y;
        robot.Andy.setPower(analog);


        if (gamepad1.a) {
            robot.Seral.setPosition(1);
        } else if (gamepad1.b) {
            robot.Seral.setPosition(-1);
        }
    }
    @Override
    public void stop() {
    }
}












































}
