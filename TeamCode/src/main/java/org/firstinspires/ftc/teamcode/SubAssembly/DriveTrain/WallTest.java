package org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubAssembly.Sensors.IMUcontrol;
import org.firstinspires.ftc.teamcode.SubAssembly.Sensors.TofControl;
import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

/* Sub Assembly Test OpMode
 * This TeleOp OpMode is used to test the functionality of the specific sub assembly
 */
// Assign OpMode type (TeleOp or Autonomous), name, and grouping
@TeleOp(name = "Wall Test", group = "Test")
public class WallTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Seek Test: ");
        double speed = 1.0;

        /* initialize sub-assemblies
         */
        DriveControl Drive = new DriveControl();
        TofControl Tof = new TofControl();
        IMUcontrol imu = new IMUcontrol();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        Drive.init(this);
        Tof.init(this);
        imu.init(this);
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();


        while (opModeIsActive()) {
            /*
        while (Tof.getDistance3() > 20) {
                Drive.moveForward(speed / 5.0, 150);
                Tof.getWallAngle();
                Tof.Telemetry();
                if (Tof.wallAngle != 0) {
                    Drive.turnAngle(speed / 5.0, Tof.wallAngle);
                }
            }
        }
        */
            if (Tof.getDistance1() > Tof.getDistance2()) {
                Drive.turnAngle(speed / 3, 15);
            } else if (Tof.getDistance2() > Tof.getDistance1()) {
                Drive.turnAngle(speed / 3, -15);
            }
        }
    }
}
