package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class RobotHardware extends OpMode

{
    @Override public void init ()

    {

        v_warning_generated = false;
        v_warning_message = "Can't map; ";

        //
        try {
            v_motor_left_drive_front = hardwareMap.dcMotor.get("left_drive_front");
            v_motor_left_drive_front.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception p_exeception) {
            m_warning_message("left_drive_front");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            v_motor_left_drive_front = null;
        }

        try {
            v_motor_left_drive_back = hardwareMap.dcMotor.get("left_drive_back");
            v_motor_left_drive_back.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception p_exeception) {
            m_warning_message("left_drive_back");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            v_motor_left_drive_back = null;
        }


        try {
            v_motor_right_drive_front = hardwareMap.dcMotor.get("right_drive_front");
        } catch (Exception p_exeception) {
            m_warning_message("right_drive_front");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            v_motor_right_drive_front = null;
        }

        try {
            v_motor_right_drive_back = hardwareMap.dcMotor.get("right_drive_back");
        } catch (Exception p_exeception) {
            m_warning_message("right_drive_back");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            v_motor_right_drive_back = null;
        }

        try {
            arm_servo_1 = hardwareMap.servo.get("arm_1");
        } catch (Exception p_exeception){
            m_warning_message("arm_1");
            DbgLog.msg(p_exeception.getLocalizedMessage());
            //this comment was made during an awkward situation.
            //1-3-16
            //3:09 PM
            arm_servo_1 = null;

        }

        try {
            arm_servo_2 = hardwareMap.servo.get("arm_2");
        } catch (Exception p_exeception){
            m_warning_message("arm_2");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            arm_servo_2 = null;

        }

        try {
            arm_servo_3 = hardwareMap.servo.get("arm_3");
        } catch (Exception p_exeception){
            m_warning_message("arm_3");
            DbgLog.msg(p_exeception.getLocalizedMessage());
            arm_servo_3 = null;

        }

        //Sensorz
        //Color Sensors
        try {
            leftColorSensor = hardwareMap.colorSensor.get("leftColorSensor");
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "left color");
            leftColorSensor = null;
        }

        try {
            //The Right Color Sensor's I2C Address has been modified to 62.
            rightColorSensor = hardwareMap.colorSensor.get("rightColorSensor");
            rightColorSensor.setI2cAddress(I2cAddr.create8bit(62));
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "right color");
            rightColorSensor = null;
        }

        try {
            rightColorSensor = hardwareMap.colorSensor.get("rightColorSensor");
            rightColorSensor.setI2cAddress(I2cAddr.create8bit(62));
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "right color");
            rightColorSensor = null;
        }

        try {
            rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");
        } catch (Exception p_exeception) {
            DbgLog.msg(p_exeception.getLocalizedMessage());
            telemetry.addData("error: ", "range sensor");
            rangeSensor = null;
        }
    }


    //
    // Connect the servo motors.
    //
    // Indicate the initial position of both the left and right servos.  The
    // hand should be halfway opened/closed.
    //
    // ini
    private Servo arm_servo_1;
    private Servo arm_servo_2;
    private Servo arm_servo_3;



    //-------------------------------------------------
    //Get Servo Postitions
    double get_arm_1_position ()
    {
        double l_return = 0.0;

        if (arm_servo_1 != null)
        {
            l_return = arm_servo_1.getPosition ();
        }

        return l_return;

    }

    double get_arm_2_position ()
    {
        double l_return = 0.0;

        if (arm_servo_2 != null)
        {
            l_return = arm_servo_2.getPosition ();
        }

        return l_return;

    }

    double get_arm_3_position ()
    {
        double l_return = 0.0;

        if (arm_servo_3 != null)
        {
            l_return = arm_servo_3.getPosition ();
        }

        return l_return;

    }
    //-------------------------------------------------
    //Set servo positions
    void arm_1_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , 0.25D //0.25D
                        , 0.8D
                );

        //
        // Set the value.  The right hand value must be opposite of the left
        // value.
        //
        if (arm_servo_1 != null)
        {
            arm_servo_1.setPosition (l_position);
        }

    }

    void arm_2_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , 0.45D, //0.15D
                        0.85D //0.6D
                );

        if (arm_servo_2 != null)
        {
            arm_servo_2.setPosition(l_position);
        }

    }

    void arm_3_position (double p_position)
        {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , 0.0D,
                        0.1D
                );

        if (arm_servo_3 != null)
        {
            arm_servo_3.setPosition (l_position);
        }

    }

    void arm_home_position(){
        arm_1_position(0.0D);
        arm_2_position(0.0D);
    }


    //--------------------------------------------------------------------------
    //
    // a_warning_generated
    //
    /**
     * Access whether a warning has been generated.
     */
    boolean a_warning_generated ()

    {
        return v_warning_generated;

    } // a_warning_generated

    //--------------------------------------------------------------------------
    //
    // a_warning_message
    //
    /**
     * Access the warning message.
     */
    String a_warning_message ()

    {
        return v_warning_message;

    } // a_warning_message

    //--------------------------------------------------------------------------
    //
    // m_warning_message
    //
    /**
     * Mutate the warning message by ADDING the specified message to the current
     * message; set the warning indicator to true.
     *
     * A comma will be added before the specified message if the message isn't
     * empty.
     */
    void m_warning_message (String p_exception_message)

    {
        if (v_warning_generated)
        {
            v_warning_message += ", ";
        }
        v_warning_generated = true;
        v_warning_message += p_exception_message;

    } // m_warning_message

    //--------------------------------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start ()

    {
        //
        // Only actions that are common to all Op-Modes (i.e. both automatic and
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Perform any actions that are necessary while the OpMode is running.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //
        // Only actions that are common to all OpModes (i.e. both auto and\
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // loop

    //--------------------------------------------------------------------------
    //
    // stop
    //
    /**
     * Perform any actions that are necessary when the OpMode is disabled.
     *
     * The system calls this member once when the OpMode is disabled.
     */

    //--------------------------------------------------------------------------
    //
    // scale_motor_power
    //
    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    float scale_motor_power (float p_power)
    {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip (p_power, -1, 1);

        float[] l_array =
                { 0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }

        if (l_power < 0)
        {
            l_scale = -l_array[l_index];
        }
        else
        {
            l_scale = l_array[l_index];
        }

        return l_scale;

    } // scale_motor_power

    //--------------------------------------------------------------------------
    //
    // a_left_drive_power
    //
    /**
     * Access the left drive motor's power level.
     */
    double a_left_drive_power ()
    {
        double l_return = 0.0;

        if (v_motor_left_drive_front != null)
        {
            l_return = v_motor_left_drive_front.getPower ();
        }

        return l_return;

    } // a_left_drive_power

    //--------------------------------------------------------------------------
    //
    // a_right_drive_power
    //
    /**
     * Access the right drive motor's power level.
     */
    double a_right_drive_power ()
    {
        double l_return = 0.0;

        if (v_motor_right_drive_front != null)
        {
            l_return = v_motor_right_drive_front.getPower();
        }

        return l_return;

    } // a_right_drive_power

    //--------------------------------------------------------------------------
    //
    // set_drive_power
    //
    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    void set_drive_power (double p_left_power, double p_right_power)

    {
        if (v_motor_left_drive_front != null && v_motor_left_drive_back != null)
        {
            v_motor_left_drive_front.setPower(p_left_power);
            v_motor_left_drive_back.setPower(p_left_power);
        }
        if (v_motor_right_drive_front != null && v_motor_right_drive_back != null)
        {
            v_motor_right_drive_front.setPower (p_right_power);
            v_motor_right_drive_back.setPower(p_right_power);
        }

    }

    void stopdrive() {
        set_drive_power(0, 0);
    }

    void set_rear_drive_power (double p_left_power, double p_right_power)

    {
        if (v_motor_left_drive_back != null)
        {
            v_motor_left_drive_front.setPower (0D);
            v_motor_left_drive_back.setPower(p_left_power);
        }
        if (v_motor_right_drive_back != null)
        {
            v_motor_right_drive_front.setPower (0D);
            v_motor_right_drive_back.setPower(p_right_power);
        }

    }

    private boolean v_warning_generated = false;

    //--------------------------------------------------------------------------
    //
    // v_warning_message
    //
    /**
     * Store a message to the user if one has been generated.
     */
    private String v_warning_message;

    //--------------------------------------------------------------------------
    //
    // v_motor_left_drive
    //
    /**
     * Manage the aspects of the left drive motor.
     */
    private DcMotor v_motor_left_drive_front;
    private DcMotor v_motor_left_drive_back;
    private DcMotor v_motor_right_drive_front;
    private DcMotor v_motor_right_drive_back;

    public ColorSensor leftColorSensor;
    public ColorSensor rightColorSensor;
    public ColorSensor beaconColorSensor;
    public ModernRoboticsI2cRangeSensor rangeSensor;


}