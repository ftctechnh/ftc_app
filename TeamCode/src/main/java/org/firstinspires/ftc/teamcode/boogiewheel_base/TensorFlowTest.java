package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAuton;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAutonNew;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.SamplePosition;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.TensorFlow;
import org.firstinspires.ftc.teamcode.framework.util.State;

@Autonomous(name = "TensorFlow Test", group = "New")
//@Disabled

public class TensorFlowTest extends AbstractAutonNew {

    private TensorFlow tensorFlow;
    private SamplePosition lastPosition = SamplePosition.UNKNOWN;
    private int loop = 0;

    @Override
    public void RegisterStates() {
        addState(new State("run", "start", ()-> {
            tensorFlow.stop();
            telemetry.addData("Position: "+lastPosition.toString());
            telemetry.update();
            return true;
        }));
    }

    @Override
    public void Init() {
        tensorFlow = new TensorFlow(TensorFlow.CameraOrientation.VIRTICAL, "Webcam 1",false);
        //tensorFlow = new TensorFlow(TensorFlow.CameraOrientation.HORIZONTAL, false);
    }

    @Override
    public void InitLoop(){
        if(loop==0) {
            tensorFlow.pause();
            tensorFlow.start();
        }
        SamplePosition currentPosition = tensorFlow.getSamplePosition();
        if(currentPosition!=SamplePosition.UNKNOWN)lastPosition = currentPosition;
        telemetry.addData(currentPosition.toString());
        telemetry.update();
        loop++;
        if(loop>=5) loop = 0;
    }

    @Override
    public void Stop() {
        tensorFlow.stop();
    }
}
