package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "ConnorRobot", group = "Linear OpMode")
public class ConnorRobot extends LinearOpMode {

    public static final long MILIS_PER_TICK = 1000L;

    public static ConnorRobot instance; // a static variable holding a reference to the instance in use

    private int currentTargetPosition = 0;

    @Override
    public void runOpMode() {
        waitForStart();
        instance = this; // assigns the current instance to a static variable
        telemetry.addData("Status", "Online");
        telemetry.addData("ArmPosition", HardwareManager.getArmMotorBasePosition());
        telemetry.update();

        while(opModeIsActive()){
            //GamePadInput.update(); // performs update operations based on game pad input
            for (int i = 0; i < 10; i++) {
                currentTargetPosition += HardwareManager.setArmMotorBasePosition(50);
                telemetry.addData("ArmPosition", HardwareManager.getArmMotorBasePosition());
                //telemetry.addData("TotalArmPosition", currentTargetPosition);
                telemetry.update();
                sleep(2000);
            }
            break;
        }
    }

}
