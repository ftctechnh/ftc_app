package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
//import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;
/**
 * Created by emilyhinds on 9/9/18.
 */
@TeleOp(name="TankDriveRoverRuckus", group="TankDriveRoverRuckus")
public class TankDriveRoverRuckus extends LinearOpMode {
    // Declare OpMode members.
    private DcMotor Left;
    private DcMotor Right;
    @Override
    public void runOpMode() {
        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Left.setPower(Range.clip(gamepad1.left_stick_y, -1, 1));
            Right.setPower(Range.clip(-gamepad1.right_stick_y, -1, 1));
        }
    }
}
