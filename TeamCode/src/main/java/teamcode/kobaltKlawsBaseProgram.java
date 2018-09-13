package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "kobaltKlawsBaseProgram", group = "Linear OpMode")

public class kobaltKlawsBaseProgram extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private Servo servo1;
    private Servo servo2;
    private Servo servo3;

    @Override
    public void runOpMode() {

        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()){
            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }


    private void initialize(){

        //giving internal hardware an external name for the app config
        //also initializing the hardware?
        this.motor1 = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.motor2 = hardwareMap.get (DcMotor.class, "RightDriveMotor");
        this.motor3 = hardwareMap.get (DcMotor.class, "ArmMotor");
        this.servo1 = hardwareMap.get (Servo.class, "");
        this.servo2 = hardwareMap.get (Servo.class, "");
        this.servo2 = hardwareMap.get (Servo.class, "");

        telemetry.addData("Status", "Online");
        telemetry.update();
    }
}
