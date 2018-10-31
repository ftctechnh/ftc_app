package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "ConnorRobotAutonomous")
public class ConnorRobotAutonomous extends LinearOpMode {

    private static final long TIME_1 = 4000L;
    private static final long TIME_2 = 500L;
    private static final long TIME_3 = 1000L;
    private static final long TIME_4 = 1000L;
    private static final long TIME_5 = 6000L;

    @Override
    public void runOpMode() {
        waitForStart();
        HardwareManager.setDrivePower(1.0, 1.0);
        sleep(TIME_1);
        HardwareManager.setDrivePower(0, 0);
        sleep(TIME_2);
        HardwareManager.setDrivePower(1.0, 1.0);
        sleep(TIME_3);
        HardwareManager.setDrivePower(0.5, -0.5);
        sleep(TIME_4);
        HardwareManager.setDrivePower(1.0, 1.0);
        sleep(TIME_5);
    }

}
