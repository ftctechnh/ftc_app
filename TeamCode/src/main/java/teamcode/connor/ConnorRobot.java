package teamcode.connor;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "ConnorRobot", group = "Linear OpMode")
public class ConnorRobot extends LinearOpMode {

    public static ConnorRobot instance; // a static variable holding a reference to the instance in use

    @Override
    public void runOpMode() {
        instance = this; // assigns a static variable to the current instance
        telemetry.addData("Status", "Online");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            GamePadInput.update(); // performs update operations based on game pad input
        }
    }

}

