package teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class VisionManager {
    private static final String VUFORIA_KEY = "AQX8YaP/////AAABmc44m7pQPEMMp8EQwO41NvF6wIRvm5UPUKKqH6QlxfXwBkOeaBpoiE55yTzVauEH70W1o+tMYaYwzVNd4Jw/YsBvwgn0luDO1M1eAUdYpfAA/mP+ug7HAtUO/SMdXpPKvSbZCmSeZ9W06ATDBY56wBgfkHbeolbtyZPPE1gybmA8V+8Ek8Gbfqi5DHW3yI41pcD6XD6AW+tnVZB5QV5BK132W32bdVpPgl6aDD9m8HUlDk9Ew/+DRFBSngY4SYOOtzMgPbee3dzTApXBT7gib+3qB5Qvh2Hzx5MH33o9xXTu1pfo6qsOSclChe3WH2kvn2O4rBx6Jry8kC1MOo8IyilAWdRWsH/bd3yZ1aqE5Mzj";

    private VuforiaLocalizer vuforia;

    public VisionManager() {
    }

    public VuforiaLocalizer getVuforia() { return vuforia; }

    public void initialize() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }
}
