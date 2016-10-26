package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This is a test class.
 */

@TeleOp(name="FTCTestOpMode", group="Iterative OpMode")
public class FTCTestOpMode extends OpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    /* Declare Motors. */
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;

    /*
     * Code to run when FTCTestOpMode object is initialized
     */
    public FTCTestOpMode() {
        this.status("FTCTestOpMode initialization");
    }

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        this.motorLeft = hardwareMap.dcMotor.get(FTCInterface.LEFT_MOTOR); // get left motor
        this.motorRight = hardwareMap.dcMotor.get(FTCInterface.RIGHT_MOTOR); // get right motor

        // Report status for debugging purposes
        this.status("init()");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        // Report status for debugging purposes
        this.status("init_loop()");
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();

        // Report status for debugging purposes
        this.status("start()");
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Running: " + runtime.toString());

        // Turn left
        motorLeft.setPower(0.5);
        motorRight.setPower(1.0);

        // Report status for debugging purposes
        this.status("loop()");
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        // Stop motors
        motorLeft.setPower(0);
        motorRight.setPower(0);

        // Report status for debugging purposes
        this.status("stop()");
    }

    public void status(String status) {
        Log.i("FTC Status", status);
    }

}
