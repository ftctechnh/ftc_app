package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.GoldPosition;

import java.util.List;

public class TFODUtil {
    final String VUFORIA_KEY = "ARdpTSz/////AAABmQ7KRGisnUoRnab3MRG7YtwixzwiqRsIjjkqY7tkci5tbijyA" +
            "9KkQWQTxmWXvKii7VZmacpaiTk0dKCy73Q1VngkUCG9cn7OPOHFIzeIWSGQEsR8IfcR7qmGEVFaU9PvNyUcH" +
            "PjWTnV/RD6egsUShXGGWiU/ZvUm2CyIx7O5bxJYuGLha9WsKj0JVkNTaKr/JdKDs/+bEla8V7Se9Eo2C0PTv" +
            "qjkOlHpiG/4M55j2HgYLJzt3yz9tMgT5620G1pGgdEBHDar00+Pl1f3p0rymswy8bVeFuBZgvksqNEeliKHQz" +
            "boYuDprMp/dkqGIC57A6kYDKGie8XVirBGa07PhhuVtgtywwqxGNVlKFQ5ta5T";

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    // Initializes TFODUtil

    public TFODUtil(CameraName camera, HardwareMap hwMap) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = camera;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        }
    }

    public GoldPosition getUpdate() {
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions.size() == 3) {
            int goldMineralX = -1;
            int silverMineral1X = -1;
            int silverMineral2X = -1;
            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineralX = (int) recognition.getLeft();
                } else if (silverMineral1X == -1) {
                    silverMineral1X = (int) recognition.getLeft();
                } else {
                    silverMineral2X = (int) recognition.getLeft();
                }
            }
            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                    return GoldPosition.LEFT;
                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                    return GoldPosition.CENTER;
                } else {
                    return GoldPosition.RIGHT;
                }
            }
        }
        // If we have no new updates
        return null;
    }

    public void activate() {
        tfod.activate();
    }

    public void shutdown() {
        tfod.shutdown();
    }
}
