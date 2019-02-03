package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


/**
 * Created by Josie on 10/5/2018.
 */
@Autonomous(name="Preciousss: auto1819", group="Preciousss")

public class auto1819 extends superAuto {

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    int quad = 1;


    public void runOpMode() {
        setUp();
        configVuforiaRoverRuckus();

/*
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        //Lower
       // lowerRobot(7.1, 1);

        //Translate
       // translate(-1,0,.5, 0.5);
        //sR();

        //Determine what quadrant
        //getQuadrant();
        startingQuadrant = 3;

        // Pivot to face pt
        //fancyGyroPivot(180);

        //Drive to row of minerals
        translate( 0, 1, 1.2, .75 );

        //Look for mineral
        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() >= 1) {
                    //int goldMineralX = -1;
                    //int silverMineral1X = -1;
                    //int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            telemetry.addData("Gold Mineral Position", "Visible");
                        } else {
                            telemetry.addData("Gold Mineral Position", "Not Visible");
                        }

                    }
                }
            }
        }
*/
            if (quad == 1){
            pivotToVuforia(45);
            goToPoint(1200,1200);
        }
        else if (quad == 2){
            pivotToVuforia(135);
            goToPoint(-1200,1200);
        }

        else if (quad == 3){
            //pivotToVuforia(-135);
            //goToPoint(-1200,-1200);
        }

        else{
            //goToPoint(1200,-1200);
        }
        //translate(0,-1,3, 0.5);




        /*final double mmPerInch       = 25.4f;
        final double halfFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
        final double squareSize = 23.5*mmPerInch;
        final double robotSize = 18*mmPerInch;
        final double landerDiagonalSize = 69*mmPerInch;
        final double dropPtDist = 0.5*landerDiagonalSize*Math.cos(45);//from origin
        final double centerMineralLocation = Math.sqrt(Math.pow(1.5*squareSize,2)*2);
        final double diagonalHalfField = Math.sqrt(Math.pow(halfFieldWidth,2)*2);
        final double stopInFrontOfMineral= diagonalHalfField-centerMineralLocation-(robotSize/2);

        double xStopinFrontofMineral = stopInFrontOfMineral* Math.cos(45);
        double yStopinFrontofMineral = xStopinFrontofMineral;

        xStopinFrontofMineral =  1200;
        yStopinFrontofMineral = -1200;
        //Triangle from vector and base-- Arc Tangent of y/x (CurrentVector) - Tangent(TargetVector) = pivotAngle
        int thetaTarget = (int)Math.round( Math.toDegrees(
                Math.atan( (yStopinFrontofMineral - location.getY()) /
                           (xStopinFrontofMineral - location.getX()) ) ) );

        pivotToVuforia(thetaTarget);

        goToPoint(xStopinFrontofMineral,yStopinFrontofMineral);

        pivotToVuforia(90);
        goToPoint(1200,1200);

        //ALL AUTOS DO:
        //Drop down
        //Go straight
        goToPoint(714,714);
        //Knock correct mineral
        //Take left
        goToPoint(0,1562);


        if(quadrantOdd) {
            //Turn 270 degrees
            // move close, not a long time
            goToPoint(-1193,1562);
        }


        else {
            //turn 15 degrees
            // move far, very long time
        }
        //ALL AUTOS DO:
        //Deposit Claimer
        //Move backwards full speed into crater
        goToPoint(1193,1562);
        */
    }
}