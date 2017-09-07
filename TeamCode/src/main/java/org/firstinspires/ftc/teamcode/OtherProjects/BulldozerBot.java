package org.firstinspires.ftc.teamcode.OtherProjects;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "Bulldozer")
@SuppressWarnings({"unused" , "WeakerAccess"})
public final class BulldozerBot extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        float drive;
        float rotate;

        DcMotor left = hardwareMap.dcMotor.get("l");
        DcMotor right = hardwareMap.dcMotor.get("r");

        right.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive() && !isStopRequested())
        {
            drive = -gamepad1.left_stick_y;
            rotate = gamepad1.right_stick_x;

            left.setPower(drive + rotate);
            right.setPower(drive - rotate);
        }
    }
}