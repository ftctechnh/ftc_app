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
    private DcMotor Shoulder;
    private DcMotor Elbow;
    @Override
    public void runOpMode() {
        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");
        Shoulder = hardwareMap.get(DcMotor.class, "shoulder");
        Elbow = hardwareMap.get(DcMotor.class, "elbow");


        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Left.setPower(-Range.clip(gamepad1.left_stick_y, -1, 1));
            Right.setPower(Range.clip(-gamepad1.right_stick_y, -1, 1));
            Shoulder.setPower(Range.clip(gamepad2.left_stick_y, -1,1));
            Elbow.setPower(Range.clip(-gamepad2.right_stick_y, -1,1));
        }
    }
}
