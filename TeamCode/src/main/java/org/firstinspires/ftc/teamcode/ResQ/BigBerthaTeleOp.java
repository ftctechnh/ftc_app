/**
 * Created by spmce on 12/1/2015.
 */
package org.firstinspires.ftc.teamcode.ResQ;
/**
 *TeleOp for Big Bertha
 * @author SSI Robotics and revised by Shane McEnaney
 * @version 2015-08-01-06-01-----2015-12-01
 */
public class BigBerthaTeleOp extends BigBerthaTelemetry {
    /**
     * Construct the class.
     * The system calls this member when the class is instantiated.
     */
    public BigBerthaTeleOp () {
        // Initialize base classes and class members.
        // All via self-construction.
    } //--------------------------------------------------------------------------BigBerthaTeleOp
    private static boolean sweeperOff = false;
    private static boolean bucketOff = false;
    private static boolean aux1ScaleOff = false;
    private static boolean aux2ScaleOff = false;
    public static boolean isSweeperOff () {return sweeperOff;}
    public static boolean isBucketOff () {return bucketOff;}
    public static boolean isAux1ScaleOff () {return aux1ScaleOff;}
    public static boolean isAux2ScaleOff () {return aux2ScaleOff;}
    /**
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop () {
        // ------------DC Motors------------
        if (gamepad1.guide && !gamepad1.start) {
            sweeperOff = true;
            aux1ScaleOff = false;
        }
        if (gamepad1.right_stick_button && gamepad1.left_stick_button) {
            sweeperOff = false;
            aux1ScaleOff = false;
        }
        if (!gamepad1.guide && gamepad1.start) {
            sweeperOff = false;
            aux1ScaleOff = true;
        }
        if (gamepad2.guide && !gamepad2.start) {
            bucketOff = true;
            aux2ScaleOff = false;
        }
        if (gamepad2.right_stick_button && gamepad2.left_stick_button) {
            bucketOff = false;
            aux2ScaleOff = false;
        }
        if (!gamepad2.guide && gamepad2.start) {
            bucketOff = false;
            aux2ScaleOff = true;
        }
        double chainHooksPower = 0.80;
        double liftPower = 0.8;
        float bucketPower = 0.0f;
        float backBucketPower = 0.0f;
        float sweeperPower = 0.0f;
        float backSweeperPower = 0.0f;
        // Obtain the current values of the joystick controllers.
        // The DC motors are scaled to make it easier to control them at slower speeds.
        // Note that x and y equal -1 when the joystick is pushed all of the way forward.
        float leftDrivePower = scaleMotorPower(-gamepad1.left_stick_y);
        float rightDrivePower = scaleMotorPower(-gamepad1.right_stick_y);
        float liftArmPower = scaleMotorPower(-gamepad2.right_stick_y);
        if ((!(gamepad1.right_bumper || gamepad1.left_bumper)) || aux1ScaleOff) {
            if (!sweeperOff) {
                sweeperPower = scaleMotorPower(gamepad1.right_trigger);
                backSweeperPower = scaleMotorPower(-gamepad1.left_trigger);
            }
        }
        if (!(gamepad2.dpad_down || gamepad2.dpad_up) || aux2ScaleOff) {
            if (!bucketOff) {
                bucketPower = scaleMotorPower(gamepad2.right_trigger);
                backBucketPower = scaleMotorPower(-gamepad2.left_trigger);
            }
            }
        float spinnerPower = scaleMotorPower(-gamepad2.left_stick_y);
        if (gamepad1.left_stick_button)
            leftDrivePower = leftDrivePower / 2;
        if (gamepad1.right_stick_button)
            rightDrivePower = rightDrivePower / 2;
        if (gamepad2.right_stick_button)
            liftArmPower = liftArmPower / 2;
        if (gamepad2.left_stick_button)
            spinnerPower = spinnerPower / 2;
        // The setPower methods write the motor power values to the DcMotor
        // class, but the power levels aren't applied until this method ends.
        setDrivePower (leftDrivePower, rightDrivePower);
        setLiftArmPower(liftArmPower);
        setSweeperPower(sweeperPower, backSweeperPower);
        setBucketPower(bucketPower, backBucketPower);
        setSpinnerPower(spinnerPower);

        float liftUpScale = 0.0f;
        float liftDownScale = 0.0f;
        float chainHooksUpScale = 0.0f;
        float chainHooksDownScale = 0.0f;
        if (!aux2ScaleOff) {
            if ((gamepad2.dpad_up || gamepad2.dpad_down)) {
                liftUpScale = scaleMotorPower(gamepad2.right_trigger);
                clipMotorPositive(liftUpScale = liftUpScale / 4);
            }
            if ((gamepad2.dpad_up || gamepad2.dpad_down)) {
                liftDownScale = scaleMotorPower(-gamepad2.left_trigger);
                clipMotorNegative(liftDownScale = (liftDownScale / 5) * 4);
            }
        }
        if (gamepad2.dpad_up)
            liftPower = clipMotorPositive(liftPower + liftUpScale + liftDownScale);
        if (gamepad2.dpad_down)
            liftPower = clipMotorPositive(liftPower + liftUpScale + liftDownScale);

        if (gamepad2.dpad_up)
            setLiftPower(liftPower);
        else if (gamepad2.dpad_down)
            setLiftPower(-liftPower);
        else
            setLiftPower(0);
        if (!aux1ScaleOff) {
            if (gamepad1.right_bumper || gamepad1.left_bumper) {
                chainHooksUpScale = scaleMotorPower(gamepad1.right_trigger);
                clipMotorPositive(chainHooksUpScale = chainHooksUpScale / 4);
            }
            if (gamepad1.right_bumper || gamepad1.left_bumper) {
                chainHooksDownScale = scaleMotorPower(-gamepad1.left_trigger);
                clipMotorNegative(chainHooksDownScale = (chainHooksDownScale / 5) * 4);
            }
        }
        if (gamepad1.right_bumper)
            chainHooksPower = clipMotorPositive(chainHooksPower + chainHooksUpScale + chainHooksDownScale);
        if (gamepad1.left_bumper)
            chainHooksPower = clipMotorPositive(chainHooksPower + chainHooksUpScale + chainHooksDownScale);

        if (gamepad1.right_bumper)
            setChainHooksPower(chainHooksPower);
        else if (gamepad1.left_bumper)
            setChainHooksPower(-chainHooksPower);
        else
            setChainHooksPower (0);
        //------------Servo Motors------------
        // The mPosition methods write the motor power values to the Servo
        // class, but the positions aren't applied until this method ends.
        if (gamepad2.y)
            setBucketDoorPosition (1.0); //1.0 is forward at full speed
        else if (gamepad2.x)
            setBucketDoorPosition (0.0); //0.0 is backward at full speed
        else
            setBucketDoorPosition (0.5); //0.5 is stopped

        if (gamepad1.y)
            setHookPosition (1.0);
        else if (gamepad1.x)
            setHookPosition (0.0);
        else
            setHookPosition(0.5);

        if (gamepad2.b)
            setManPosition (1.0);
        else if (gamepad2.a)
            setManPosition (0.0);
        else
            setManPosition(0.5);
            ////////////////////////////---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------setHookPosition (0.5

        //------------Telemetry------------
        // Send telemetry data to the driver station.
        updateTelemetry(); // Update common telemetry
        updateGamepadTelemetry ();
    } //--------------------------------------------------------------------------loop
} //------------------------------------------------------------------------------BigBerthaTeleOp
