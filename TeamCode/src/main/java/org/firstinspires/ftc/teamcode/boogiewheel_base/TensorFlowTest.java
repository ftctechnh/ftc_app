package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAuton;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.TensorFlow;

@Autonomous(name = "TensorFlow Test", group = "New")
//@Disabled

public class TensorFlowTest extends AbstractAuton {

    private TensorFlow tensorFlow;

    @Override
    public void Init() {
        //tensorFlow = new TensorFlow(TensorFlow.CameraOrientation.HORIZONTAL, "Webcam 1",false);
        tensorFlow = new TensorFlow(TensorFlow.CameraOrientation.HORIZONTAL, false);
    }

    @Override
    public void Run() {
        tensorFlow.start();
        while (opModeIsActive()) {
            telemetry.addData(tensorFlow.getSamplePosition().toString());
            telemetry.update();
        }
    }

    @Override
    public void Stop() {
        tensorFlow.stop();
    }
}
