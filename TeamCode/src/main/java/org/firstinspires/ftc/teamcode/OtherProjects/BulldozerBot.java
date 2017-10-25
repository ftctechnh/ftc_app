package org.firstinspires.ftc.teamcode.OtherProjects;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilToggle;


@TeleOp(name = "Bulldozer" , group = "Prototypes")
@Disabled
@SuppressWarnings({"unused" , "WeakerAccess"})
public final class BulldozerBot extends LinearOpMode
{
    UtilToggle toggleReverse = new UtilToggle();
    UtilToggle toggleSlow = new UtilToggle();


    @Override
    public void runOpMode()
    {
        float drive;
        float rotate;

        boolean reverse = false;

        boolean slow = false;

        DcMotor left = hardwareMap.dcMotor.get("l");
        DcMotor right = hardwareMap.dcMotor.get("r");

        right.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive() && !isStopRequested())
        {
            if(toggleReverse.isPressed(gamepad1.a))
            {
                reverse = !reverse;
            }


            if(toggleSlow.isPressed(gamepad1.x))
            {
                slow = !slow;
            }


            drive = -gamepad1.left_stick_y;
            rotate = gamepad1.right_stick_x;

            if(reverse)
            {
                drive *= -1;
                rotate *= -1;
            }

            if(slow)
            {
                drive *= .5;
                rotate *= .5;
            }

            left.setPower(drive + rotate);
            right.setPower(drive - rotate);
        }
    }
}