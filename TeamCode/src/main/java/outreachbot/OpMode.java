package outreachbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Caroline Opmode", group = "Linear Opmode")
public class OpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    Hardware robot = new Hardware();

    double forward, rotate, ttSpeed, wormForward, wormBackward;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()){

            forward = gamepad1.left_stick_y * -1;
            rotate = gamepad1.left_stick_x;

            robot.backLeft.setPower(forward + rotate);
            robot.backRight.setPower(forward - rotate);

            ttSpeed = gamepad1.right_stick_y;

            robot.turnTable.setPower(ttSpeed);

            wormForward = gamepad1.right_trigger / 1.5;
            wormBackward = gamepad1.left_trigger / 1.5;

            robot.wormGear.setPower(wormForward - wormBackward);

            if(gamepad1.x){
                robot.grabyboi.setPosition(0);
            }
            else if(gamepad1.y){
                robot.grabyboi.setPosition(1);
            }

        }

    }

}
