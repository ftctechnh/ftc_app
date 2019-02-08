import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class TestOPmode extends LinearOpMode {

    private DcMotor LeftMotor;
    private  DcMotor RightMotor;

    @Override
    public void runOpMode() {
         LeftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        RightMotor = hardwareMap.get(DcMotor.class, "RightMotor");

        telemetry.addData("sycka blyat", "urmom");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            LeftMotor.setPower(-this.gamepad1.left_stick_y);
        }
    }
}
