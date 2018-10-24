package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TestOpModeBionicBots")
public class TestOpModeBionicBots extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private RRVHardwarePushbot robot = new RRVHardwarePushbot();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        telemetry.addData("Status","Initialized");
        telemetry.update();

        double powerLeft = 0;
        double powerRight = 0;
        double powerServo = 0;

        waitForStart();
        while (opModeIsActive()){
            powerLeft = this.gamepad1.left_stick_y;
            powerRight = this.gamepad1.right_stick_y;
            powerServo = this.gamepad1.left_trigger;

            telemetry.addData("PowerLeft:",powerLeft);
            telemetry.addData("PowerRight:",powerRight);
            telemetry.addData("powerServo:",powerServo);


            telemetry.update();

            robot.setLeftRight(powerLeft, powerRight, 0, 0);
            robot.pickupServo.setDirection(DcMotorSimple.Direction.REVERSE);
            robot.pickupServo.setPower(powerServo);
        }
    }
}
