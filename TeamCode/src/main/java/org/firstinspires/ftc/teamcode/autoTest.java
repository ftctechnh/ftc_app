    package org.firstinspires.ftc.teamcode;

    import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

    import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
    import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
    import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

    /**
    * Created by Josie on 10/5/2018.
    */
    @Autonomous(name="Preciousss: autoTest", group="Preciousss")

    public class autoTest extends superAuto {

    public void runOpMode() {

        iAmRed = false;
        iAmBlue = true;

        setUp();
       /*lowerRobot(6.9, 1);
        translate(-1,0,.5, 0.5);
        pivotToVuforia(-45);
        goToPoint(1200,-1200);
       for(int x=0; x<10000; x++){
            angles =(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
            telemetry.addData("Z: ", angles.firstAngle);
            telemetry.addData("Y: ", angles.secondAngle);
            telemetry.addData("X: ", angles.thirdAngle);
            telemetry.update();
       }*/


        followHeading(0,2.2, -.5f,.4f);
        tensorFlowTest();
        /*followHeading(0,1.4, .8f,0);
        fancyGyroPivot(45);
        followHeading(45,0.5, .8f,0);
        fancyGyroPivot(90);
        tensorFlowTest();*/
    }
    }