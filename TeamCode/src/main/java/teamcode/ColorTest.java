package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.R;

@Autonomous(name = "ColorTest", group = "Linear OpMode")

public class ColorTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor TestMotor; //motor 0

    private ColorSensor colorSensor;


    @Override
    public void runOpMode() {


        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {

            if(isGold()) {

                this.TestMotor.setPower(0.25);

            } else this.TestMotor.setPower(0);

            //Gives stats and updates
            telemetry.addData("Status", "Running");
            telemetry.addData("isGold", isGold());
            telemetry.addData("Red: ", colorSensor.red());
            telemetry.addData("Blue: ", colorSensor.blue());
            telemetry.addData("Green: ", colorSensor.green());
            telemetry.addData("Alpha: ", colorSensor.alpha());
            telemetry.addData("RGB: ", colorSensor.argb());
            telemetry.update();

        }


    }

    private void initialize(){

        //giving internal hardware an external name for the app config
        this.TestMotor = hardwareMap.get (DcMotor.class,"TestMotor");

        this.colorSensor = hardwareMap.get(ColorSensor.class, "ColorSensor");


        //Sets correct directions for motors and servos
        TestMotor.setDirection(DcMotor.Direction.FORWARD);

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.update();
    }





    public  boolean isGold() {

        if((colorSensor.red() >= (2 * colorSensor.blue()))
                && colorSensor.green() >= (2* colorSensor.blue())) {
            return true;

        }

        return false;

    }

}

