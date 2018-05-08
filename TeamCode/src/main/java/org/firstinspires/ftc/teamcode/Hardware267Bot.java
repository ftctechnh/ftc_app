
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class Hardware267Bot
{
    /* Public OpMode members. */
    // Hardware:
    public DcMotor  leftMotor     = null;
    public DcMotor  rightMotor    = null;
    public DcMotor  belts         = null;
    public DcMotor  spinnerMotor  = null;
    public Servo    beltOpener    = null;
    /*
    public ColorSensor color = null;
    public DcMotor armMotor = null;
    public Servo hopperServo = null;
    public OpticalDistanceSensor lineveiwer = null;
    public Servo buttonPusher = null;
    public DcMotor belt = null;

    // Other state:
    public boolean servosEnabled;
    public boolean gateOpen;
    public ButtonPusherState buttonPusherState;
    */
    // Constants:
    public static final double HOPPER_OPEN = 0; //TODO: Find value
    public static final double HOPPER_CLOSED = 1; //TODO: Find value
    public static final double BUTTON_LEFT = 0.5; //TODO: Find value
    public static final double BUTTON_RIGHT = 0.5; //TODO: Find value
    public static final double BUTTON_CENTER = 0.5; //TODO: Find value
    public static final double RAMP_CLOSED = 0;
    public static final double RAMP_OPEN = 1.0;
    public enum ButtonPusherState { LEFT , RIGHT , CENTER }

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Hardware267Bot(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        //lineviewer = hwMap.opticalDistanceSensor.get("lightveiwer");
        leftMotor   = hwMap.dcMotor.get("leftMotor");
        rightMotor  = hwMap.dcMotor.get("rightMotor");
        belts = hwMap.dcMotor.get("belts");
        //rightBelt  = hwMap.dcMotor.get("rightBelt");
        spinnerMotor  = hwMap.dcMotor.get("spinnerMotor");
        beltOpener = hwMap.servo.get("beltOpener");
        //color = hwMap.colorSensor.get("color");
        //color.enableLed(false);
        //armMotor = hwMap.dcMotor.get("arm");
        //hopperServo = hwMap.servo.get("hopper");
        //hopperServo.setPosition(0);
        //hopperServo.setPosition(hopperServo.getPosition());
        //buttonPusher = hwMap.servo.get("pusher");
        //buttonPusher.setPosition(0);
        //buttonPusher.setPosition(buttonPusher.getPosition());
        //armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        belts.setDirection(DcMotor.Direction.REVERSE);
        //rightBelt.setDirection(DcMotor.Direction.FORWARD);
        //belt = hwMap.dcMotor.get("belt");

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        belts.setPower(0);
       // rightBelt.setPower(0);
        spinnerMotor.setPower(0);

        //leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //servosEnabled = true;
        //gateOpen = false;
        //buttonPusherState = ButtonPusherState.CENTER;
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    /*
    public void openGate(boolean enableServos)
    {
        enableServos();
        hopperServo.setPosition(HOPPER_OPEN);
        setServoState(enableServos);
        gateOpen = true;
    }
    public void openGate()
    {
        openGate(servosEnabled);
    }
    public void closeGate(boolean enableServos)
    {
        enableServos();
        hopperServo.setPosition(HOPPER_CLOSED);
        setServoState(enableServos);
        gateOpen = false;
    }
    public void closeGate()
    {
        closeGate(servosEnabled);
    }
    public void toggleGate()
    {
        if (gateOpen)
            closeGate();
        else
            openGate();
    }
    public void pushLeft()
    {
        enableServos();
        if (buttonPusherState != ButtonPusherState.LEFT)
        {
            buttonPusher.setPosition(BUTTON_LEFT);
            buttonPusherState = ButtonPusherState.LEFT;
        }
    }
    public void pushRight()
    {
        enableServos();
        if (buttonPusherState != ButtonPusherState.RIGHT) {
            buttonPusher.setPosition(BUTTON_RIGHT);
            buttonPusherState = ButtonPusherState.RIGHT;
        }
    }
    public void centerPusher()
    {
        if (buttonPusherState != ButtonPusherState.CENTER) {
            buttonPusher.setPosition(BUTTON_CENTER);
            buttonPusherState = ButtonPusherState.CENTER;
        }
    }
    public void moveButtonPusher(ButtonPusherState position)
    {
        if (position == ButtonPusherState.CENTER)
            centerPusher();
        else if (position == ButtonPusherState.LEFT)
            pushLeft();
        else if (position == ButtonPusherState.RIGHT)
            pushRight();
    }
    public void disableServos()
    {
        if (servosEnabled) {
            buttonPusher.getController().pwmDisable();
            servosEnabled = false;
        }
    }
    public void enableServos()
    {
        if (!servosEnabled) {
            buttonPusher.getController().pwmEnable();
            servosEnabled = true;
        }
    }
    public void setServoState(boolean enableServos)
    {
        if (enableServos)
            enableServos();
        else
            disableServos();
    }
    */
}

