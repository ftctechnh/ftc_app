package teamcode;

//import these things to make the code work
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//the name of the Op Mode in the Driver's Station
@Autonomous(name= "demoRobotAutoColorBenjamin", group= "Linear Opmode")

//okay now onto the class finally
public class demoRobotAutoColorBenjamin extends LinearOpMode {

    // declaring hardware
    // variable type, Hardware type, variable name
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor0;
    private DcMotor motor1;
    private Servo servo0;

    @Override
    public void runOpMode() {

        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        
    }

    private void initialize(){

        //giving internal hardware an external name for the app config
        //also initializing the hardware?
        this.motor0 = hardwareMap.get (DcMotor.class,"Left Motor");
        this.motor1 = hardwareMap.get (DcMotor.class, "Right Motor");
        this.servo0 = hardwareMap.get (Servo.class, "Arm Servo");

        // makes the motors that face away from each other move the same direction
        this.motor0.setDirection(DcMotor.Direction.FORWARD);
        this.motor1.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Online");
        telemetry.update();
    }

}
