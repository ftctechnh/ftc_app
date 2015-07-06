package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * An empty op mode serving as a template for custom OpModes
 */
public class TestOpSwerveDualMotor extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    SwerveLRMotors motor1 = new SwerveLRMotors("LRmotor", null, null);

    //state variables for debouncing the controller buttons
    boolean a1IsDown=false;
    boolean b1IsDown=false;
    boolean x1IsDown=false;
    boolean y1IsDown=false;

    int actioncounter = 0; //this is for creating debugging names when creating multiple actions at runtime

    /*
    * Constructor
    */
    public TestOpSwerveDualMotor()
    {
        //queue autonomous commands here
        motor1.AddTimedMotion(DcMotor.Direction.FORWARD, 1.0, 3.0);
        motor1.AddDelay(3.0);
        motor1.AddTimedMotion(DcMotor.Direction.FORWARD, 1.0, 3.0);
        motor1.AddAcceleration(DcMotor.Direction.FORWARD, 0.0, 1.0, 5.0);

    }

    private boolean debounceA1()
    {
        if ((!a1IsDown) && (gamepad1.a))
        {
            a1IsDown = true;
            return true;
        }
        else return false;
    }
    private boolean debounceB1()
    {
        if ((!b1IsDown) && (gamepad1.b))
        {
            b1IsDown = true;
            return true;
        }
        else return false;
    }
    private boolean debounceX1()
    {
        if ((!x1IsDown) && (gamepad1.x))
        {
            x1IsDown = true;
            return true;
        }
        else return false;
    }
    private boolean debounceY1()
    {
        if ((!y1IsDown) && (gamepad1.y))
        {
            y1IsDown = true;
            return true;
        }
        else return false;
    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void start() { runtime.reset(); }


    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        telemetry.addData("Timer", "Running for " + runtime.toString());

        //clear flags used in debouncing
        if (!gamepad1.a) a1IsDown = false;
        if (!gamepad1.b) b1IsDown = false;
        if (!gamepad1.x) x1IsDown = false;
        if (!gamepad1.y) y1IsDown = false;


        //handle new input because it may preempt existing items in the queue
        if (debounceA1())
        {
            motor1.AddTimedMotion(DcMotor.Direction.FORWARD, 1.0, 5.0);
        }
        if (debounceX1())
        {
            motor1.AddDelay(3.0);
        }
        if (debounceY1())
        {
            motor1.AddTimedMotion(DcMotor.Direction.FORWARD, 1.0, 2.0);
            motor1.AddDelay(3.0);
            motor1.AddTimedMotion(DcMotor.Direction.FORWARD, 1.0, 2.0);
        }
        if (gamepad1.b)
        {
            motor1.ClearQueue();
        }

        //process the queue
        motor1.Update(runtime);

        //stop motors? Decision: No!
        //if (motor1.GetQueueLength()==0)
        //{
            //to do: stop motor 1 here?
            //instead, I think we should consider using an explicit stop action,
            //and like lego, allowing an optional param to motor moves that decides if they should brake at end.
        //}

        telemetry.addData("Count", "Queue length: " + motor1.GetQueueLength());
        String message = motor1.ToString();
        if (message.length() > 128) //cover unlikely case that the queue is too big to display in a telemetry message.
        {
            message = message.substring(0,128) + "...";
        }
        telemetry.addData("Message", "motor1.queue:\n" + message);

    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {

    }
}
