package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Autonomous(name = "TensorFlowDecisionTree")
public class TensorFlowDecisionTree extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        VuforiaFunctions vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        waitForStart();
        while (opModeIsActive())
        {

            List<Recognition> updatedRecognitions = vuforiaFunctions.getUpdatedRecognitions();

            if (updatedRecognitions.size() != 0)
            {
                for (Recognition recognition : updatedRecognitions)
                {
                    if (recognition.getLabel().equals(VuforiaFunctions.LABEL_GOLD_MINERAL))
                    {
                        telemetry.addData("I can see a block", null);
                        telemetry.addData("getLeft", recognition.getLeft());
                        telemetry.addData("getRight", recognition.getRight());
                        telemetry.addData("getTop", recognition.getTop());
                        telemetry.addData("getWidth", recognition.getWidth());
                        telemetry.addData("getHeight", recognition.getHeight());
                        telemetry.addData("getBottom", recognition.getBottom());
                    }
                }
            }
            telemetry.update();
        }
    }
}
