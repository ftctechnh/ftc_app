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
        while (opModeIsActive()) {
            lowerRobot(7.2, 1);
            translate(-1, 0, .75, 0.5);
            followHeading(0,1.3, 0f,1f);


            //followHeading(0, 2.2, -.5f, .4f);// Translate on side to in front of first mineral
            //tensorFlowJeffrey();

            //tensorFlowCase();





        /*followHeading(0,1.4, .8f,0);
        fancyGyroPivot(45);
        followHeading(45,0.5, .8f,0);
        fancyGyroPivot(90);
        tensorFlowCase();
        distCorrector(15);
        followHeading(45,0.5, .8f,0);???????????????????????????????
        //Deposit
        //Back
        */
        }
    }
    }