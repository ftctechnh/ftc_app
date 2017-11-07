package RicksCode.Bill_Adapted;

/**
 * Created by ftc8045 on 10/22/2017.
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="AUTOTEST")  // @Autonomous(...) is the other common choice
@Disabled
public class Auto extends LinearOpMode {

    Gromit gromit;

    private ElapsedTime runtime = new ElapsedTime();



    @Override
    public void runOpMode() {
        gromit = new Gromit();
        gromit.init(hardwareMap);


        waitForStart();

//           gromit.shooter.turnOn();
//           gromit.sweeper.sweepIn();

        gromit.driveTrain.drive(0,0,0);


    }
}
    

