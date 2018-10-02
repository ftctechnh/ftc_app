package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "REVTrix: Autonomous Test", group = "REVTrix")
public class REVTrixAutonomousTest extends OpMode {

    /* Declare OpMode members. */
    private REVTrix robot = new REVTrix();  // Class created to define a Trainerbot's hardware

    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {


        robot.FrontLeftDrive.setPower(.25);
        robot.FrontRightDrive.setPower(.25);
        robot.RearLeftDrive.setPower(.25);
        robot.RearRightDrive.setPower(.25);


    }








}
