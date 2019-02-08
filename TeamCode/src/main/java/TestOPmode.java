import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class TestOPmode extends LinearOpMode {

    //motor declaration
    private DcMotor leftMotor;
    private  DcMotor rightMotor;
    private double expectedLeft = 0f;
    private double expectedRight = 0f;

    @Override
    public void runOpMode() {
        //motor initialization
        leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "RightMotor");

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("cyka blyat", "cyka blyat");  //motto

            //getting input from controller for setting motor power
            expectedLeft = -this.gamepad1.left_stick_y;
            expectedRight = this.gamepad1.right_stick_y;

            //set motorpower
            leftMotor.setPower(expectedLeft);
            rightMotor.setPower(expectedRight);

            //print expected and actual values
            telemetry.addData("Left motor expected", expectedLeft);
            telemetry.addData("Left motor actual", leftMotor.getPower());
            telemetry.addData("Right motor expected", expectedRight);
            telemetry.addData("Right motor actual", rightMotor.getPower());

            telemetry.update(); //sent out telemetry values to driver station
        }
    }
}
