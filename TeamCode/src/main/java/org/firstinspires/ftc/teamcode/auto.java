package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * Created by Josie on 10/5/2018.
 */
@Autonomous(name="Preciousss: auto", group="Preciousss")

public class auto extends superAuto {

    public void runOpMode() {
        setUp();
        configVuforiaRoverRuckus();
        //Lower
        lowerRobot(6.9, 1);
        sR();

        getQuadrant();

        final double mmPerInch       = 25.4f;
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
        /*
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