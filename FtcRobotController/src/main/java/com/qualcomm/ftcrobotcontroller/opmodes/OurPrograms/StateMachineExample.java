/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes.OurPrograms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.lang.reflect.Constructor;

//------------------------------------------------------------------------------
// A2818_StateMachine.java
//------------------------------------------------------------------------------
// Extends the OpMode class to provide a Example Autonomous code
//------------------------------------------------------------------------------
/* This opMode does the following steps:
 * 0) Wait till the encoders show reset to zero.
 * 1) Drives to the vicinity of the beacon using encoder counts
 * 2) Use the Legacy light sensor to locate the white line
 * 3) Tracks the line until the wall is reached
 * 4) Pushes up against wall to get square using constant power an time.
 * 5) Deploys the Climbers using the servo
 * 6) Drives to the Mountain using encoder counts
 * 7) Climbs the Mountain using constant speed and time
 * 8) Stops and waits for end of Auto
 *
 * The code is executed as a state machine.  Each "State" performs a specific task which takes time to execute.
 * An "Event" can cause a change in the state.  One or more "Actions" are performed when moving on to next state
 */

public class StateMachineExample extends OpMode
{
    // A list of system States.
    private enum State
    {
        STATE_INITIAL,
        STATE_DRIVE_TO_BEACON,
        STATE_LOCATE_LINE,
        STATE_FOLLOW_LINE,
        STATE_SQUARE_TO_WALL,
        STATE_DEPLOY_CLIMBERS,
        STATE_DRIVE_TO_MOUNTAIN,
        STATE_CLIMB_MOUNTAIN,
        STATE_STOP,
    }

    // Define driving paths as pairs of relative wheel movements in inches (left,right) plus speed %
    // Note: this is a dummy path, and is NOT likely to actually work with YOUR robot.
    final PathSeg[] mBeaconPath = {
            new PathSeg(  0.0,  3.0, 0.2),  // Left
            new PathSeg( 60.0, 60.0, 0.9),  // Forward
            new PathSeg(  1.0,  0.0, 0.2),  // Left
    };

    final PathSeg[] mMountainPath = {
            new PathSeg(  0.0, -3.0, 0.2),  // Left Rev
            new PathSeg(-30.0,-30.0, 0.9),  // Backup
            new PathSeg(-16.0,  0.0, 0.7),  // Right Rev
            new PathSeg( 10.0, 10.0, 0.3),  // Forward
    };

    final double COUNTS_PER_INCH = 240 ;    // Number of encoder counts per inch of wheel travel.

    final double WHITE_THRESHOLD = 0.5 ;
    final double RANGE_THRESHOLD = 0.5 ;
    final double CLIMBER_RETRACT = 0.1 ;
    final double CLIMBER_DEPLOY  = 1.0 ;

    //--------------------------------------------------------------------------
    // Robot device Objects
    //--------------------------------------------------------------------------
    public DcMotor      mLeftMotor;
    public DcMotor      mRightMotor;
    public Servo        mServo;
    public LightSensor  mLight;
    public OpticalDistanceSensor mDistance;

    private int         mLeftEncoderTarget;
    private int         mRightEncoderTarget;

    // Loop cycle time stats variables
    public ElapsedTime  mRuntime = new ElapsedTime();   // Time into round.

    private ElapsedTime mStateTime = new ElapsedTime();  // Time into current state

    private State       mCurrentState;    // Current State Machine State.
    private PathSeg[]   mCurrentPath;     // Array to hold current path
    private int         mCurrentSeg;      // Index of the current leg in the current path


    //--------------------------------------------------------------------------
    // Demo Hardware
    //--------------------------------------------------------------------------
    public void A2818_StateMachine()
    {
    }

    //--------------------------------------------------------------------------
    // init
    //--------------------------------------------------------------------------
    @Override
    public void init()
    {
        // Initialize class members.
        mLeftMotor  = hardwareMap.dcMotor.get("motor left");
        mRightMotor = hardwareMap.dcMotor.get("motor right");
        mRightMotor.setDirection(DcMotor.Direction.REVERSE);

        mLight      = hardwareMap.lightSensor.get("light");
        mDistance   = hardwareMap.opticalDistanceSensor.get("range");

        mServo      = hardwareMap.servo.get("servo");
        mServo.setPosition(CLIMBER_RETRACT);    // Put servo into "starting position"

        setDrivePower(0, 0);        // Ensure motors are off
        resetDriveEncoders();       // Reset Encoders to Zero
    }

    //--------------------------------------------------------------------------
    // loop
    //--------------------------------------------------------------------------
    // @Override
    public void init_loop()
    {
        // Keep resetting encoders and show the current values
        resetDriveEncoders();        // Reset Encoders to Zero
        telemetry.addData("ENC", String.format("L:R %d:%d", getLeftPosition(), getRightPosition()));
    }

    //--------------------------------------------------------------------------
    // start
    //--------------------------------------------------------------------------
    @Override
    public void start()
    {
        // Setup Robot devices, set initial state and start game clock
        setDriveSpeed(0, 0);        // Set target speed to zero
        runToPosition();            // Run to Position set by encoder targets
        mRuntime.reset();           // Zero game clock
        newState(State.STATE_INITIAL);
    }

    //--------------------------------------------------------------------------
    // loop
    //--------------------------------------------------------------------------
    @Override
    public void loop()
    {
        // Send the current state info (state and time) back to first line of driver station telemetry.
        telemetry.addData("0", String.format("%4.1f ", mStateTime.time()) + mCurrentState.toString());

        // Execute the current state.  Each STATE's case code does the following:
        // 1: Look for an EVENT that will cause a STATE change
        // 2: If an EVENT is found, take any required ACTION, and then set the next STATE
        //   else
        // 3: If no EVENT is found, do processing for the current STATE and send TELEMETRY data for STATE.
        //
        switch (mCurrentState)
        {
            case STATE_INITIAL:         // Stay in this state until encoders are both Zero.
                if (encodersAtZero())
                {
                    startPath(mBeaconPath);                 // Action: Load path to beacon
                    newState(State.STATE_DRIVE_TO_BEACON);  // Next State:
                }
                else
                {
                    // Display Diagnostic data for this state.
                    telemetry.addData("1", String.format("L %5d - R %5d ", getLeftPosition(),
                            getRightPosition() ));
                }

                break;

            case STATE_DRIVE_TO_BEACON: // Follow path until last segment is completed
                if (pathComplete())
                {
                    mLight.enableLed(true);                 // Action: Enable Light Sensor
                    setDriveSpeed(-0.1, 0.1);               // Action: Start rotating left
                    newState(State.STATE_LOCATE_LINE);      // Next State:
                }
                else
                {
                    // Display Diagnostic data for this state.
                    telemetry.addData("1", String.format("%d of %d. L %5d:%5s - R %5d:%5d ",
                            mCurrentSeg, mCurrentPath.length,
                            mLeftEncoderTarget, getLeftPosition(),
                            mRightEncoderTarget, getRightPosition()));
                }
                break;

            case STATE_LOCATE_LINE:     // Rotate until white tape is detected
                if (mLight.getLightDetected() > WHITE_THRESHOLD)
                {
                    setDriveSpeed(0.0, 0.0);                // Action: Stop rotation
                    newState(State.STATE_FOLLOW_LINE);      // Next State:
                }
                else
                {
                    // Display Diagnostic data for this state.
                    telemetry.addData("1", String.format("%4.2f of %4.2f ",
                            mLight.getLightDetected(),
                            WHITE_THRESHOLD ));
                }

                break;

            case STATE_FOLLOW_LINE:     // Track line until wall is reached
                if (mDistance.getLightDetected() > RANGE_THRESHOLD)
                {
                    useConstantPower();                     // Apply constant power to motors
                    setDriveSpeed(0.2, 0.2);                // Action: Drive Forward
                    newState(State.STATE_SQUARE_TO_WALL);   // Next State:
                }
                else
                {
                    // Steer left ot right
                    if (mLight.getLightDetected() > WHITE_THRESHOLD)
                    {
                        setDriveSpeed(0.2, 0.0);            // Scan Right
                        telemetry.addData("1", String.format("%4.2f --> %7d : %7d (%4.2f)" ,
                                mLight.getLightDetected(),
                                getLeftPosition(), getRightPosition(),
                                mDistance.getLightDetected() ));
                    }
                    else
                    {
                        setDriveSpeed(0.0, 0.2);            // Scan Left
                        telemetry.addData("1", String.format("%4.2f <-- %7d : %7d (%4.2f)",
                                mLight.getLightDetected(),
                                getLeftPosition(), getRightPosition(),
                                mDistance.getLightDetected() ));
                    }
                }
                break;

            case STATE_SQUARE_TO_WALL:     // Push up against wall for 1 second
                if (mStateTime.time() > 1.0)
                {
                    setDriveSpeed(0.0, 0.0);                // Action: Stop pushing
                    mServo.setPosition(CLIMBER_DEPLOY);     // Action:  Start deploying climbers.
                    newState(State.STATE_DEPLOY_CLIMBERS);  // Next State:
                }
                break;

            case STATE_DEPLOY_CLIMBERS:     // wait 2 seconds while servos move and deposit climbers
                if (mStateTime.time() > 2.0)
                {
                    mServo.setPosition(CLIMBER_RETRACT);    // Put servo into "starting position"
                    startPath(mMountainPath);               // Action: Load path to Mountain
                    newState(State.STATE_DRIVE_TO_MOUNTAIN);// Next State:
                }
                break;

            case STATE_DRIVE_TO_MOUNTAIN: // Follow path until last segment is completed
                if (pathComplete())
                {
                    useConstantPower();                     // Action: Switch to constant Speed
                    setDrivePower(0.5, 0.5);                // Action: Start Driving forward at 50 Speed
                    newState(State.STATE_CLIMB_MOUNTAIN);   // Next State:
                }
                else
                {
                    // Display Diagnostic data for this state.
                    telemetry.addData("1", String.format("%d of %d. L %5d:%5d - R %5d:%5d ",
                            mCurrentSeg, mCurrentPath.length,
                            mLeftEncoderTarget, getLeftPosition(),
                            mRightEncoderTarget, getRightPosition()));
                }
                break;

            case STATE_CLIMB_MOUNTAIN:   // Drive up mountain for 5 seconds
                if (mStateTime.time() > 5.0)
                {
                    useConstantPower();                     // Switch to constant Power
                    setDrivePower(0, 0);                    // Set target speed to zero
                    newState(State.STATE_STOP);             // Next State:
                }
                else
                {
                    // Display Diagnostic data for this state.
                    telemetry.addData("1", String.format("L %5d - R %5d ", getLeftPosition(),
                            getRightPosition() ));
                }
                break;

            case STATE_STOP:
                break;
        }
    }

    //--------------------------------------------------------------------------
    // stop
    //--------------------------------------------------------------------------
    @Override
    public void stop()
    {
        // Ensure that the motors are turned off.
        useConstantPower();
        setDrivePower(0, 0);
    }

    //--------------------------------------------------------------------------
    // User Defined Utility functions here....
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    //  Transition to a new state.
    //--------------------------------------------------------------------------
    private void newState(State newState)
    {
        // Reset the state time, and then change to next state.
        mStateTime.reset();
        mCurrentState = newState;
    }


    //--------------------------------------------------------------------------
    // setEncoderTarget( LeftEncoder, RightEncoder);
    // Sets Absolute Encoder Position
    //--------------------------------------------------------------------------
    void setEncoderTarget(int leftEncoder, int rightEncoder)
    {
        mLeftMotor.setTargetPosition(mLeftEncoderTarget = leftEncoder);
        mRightMotor.setTargetPosition(mRightEncoderTarget = rightEncoder);
    }

    //--------------------------------------------------------------------------
    // addEncoderTarget( LeftEncoder, RightEncoder);
    // Sets relative Encoder Position.  Offset current targets with passed data
    //--------------------------------------------------------------------------
    void addEncoderTarget(int leftEncoder, int rightEncoder)
    {
        mLeftMotor.setTargetPosition(mLeftEncoderTarget += leftEncoder);
        mRightMotor.setTargetPosition(mRightEncoderTarget += rightEncoder);
    }

    //--------------------------------------------------------------------------
    // setDrivePower( LeftPower, RightPower);
    //--------------------------------------------------------------------------
    void setDrivePower(double leftPower, double rightPower)
    {
        mLeftMotor.setPower(Range.clip(leftPower, -1, 1));
        mRightMotor.setPower(Range.clip(rightPower, -1, 1));
    }

    //--------------------------------------------------------------------------
    // setDriveSpeed( LeftSpeed, RightSpeed);
    //--------------------------------------------------------------------------
    void setDriveSpeed(double leftSpeed, double rightSpeed)
    {
        setDrivePower(leftSpeed, rightSpeed);
    }

    //--------------------------------------------------------------------------
    // runToPosition ()
    // Set both drive motors to encoder servo mode (requires encoders)
    //--------------------------------------------------------------------------
    public void runToPosition()
    {
        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    //--------------------------------------------------------------------------
    // useConstantSpeed ()
    // Set both drive motors to constant speed (requires encoders)
    //--------------------------------------------------------------------------
    public void useConstantSpeed()
    {
        setDriveMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // useConstantPower ()
    // Set both drive motors to constant power (encoders NOT required)
    //--------------------------------------------------------------------------
    public void useConstantPower()
    {
        setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // resetDriveEncoders()
    // Reset both drive motor encoders, and clear current encoder targets.
    //--------------------------------------------------------------------------
    public void resetDriveEncoders()
    {
        setEncoderTarget(0, 0);
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // syncEncoders()
    // Load the current encoder values into the Target Values
    // Essentially synch's the software with the hardware
    //--------------------------------------------------------------------------
    void synchEncoders()
    {
        //	get and set the encoder targets
        mLeftEncoderTarget = mLeftMotor.getCurrentPosition();
        mRightEncoderTarget = mRightMotor.getCurrentPosition();
    }

    //--------------------------------------------------------------------------
    // setDriveMode ()
    // Set both drive motors to new mode if they need changing.
    //--------------------------------------------------------------------------
    public void setDriveMode(DcMotorController.RunMode mode)
    {
        // Ensure the motors are in the correct mode.
        if (mLeftMotor.getChannelMode() != mode)
            mLeftMotor.setChannelMode(mode);

        if (mRightMotor.getChannelMode() != mode)
            mRightMotor.setChannelMode(mode);
    }

    //--------------------------------------------------------------------------
    // getLeftPosition ()
    // Return Left Encoder count
    //--------------------------------------------------------------------------
    int getLeftPosition()
    {
        return mLeftMotor.getCurrentPosition();
    }

    //--------------------------------------------------------------------------
    // getRightPosition ()
    // Return Right Encoder count
    //--------------------------------------------------------------------------
    int getRightPosition()
    {
        return mRightMotor.getCurrentPosition();
    }

    //--------------------------------------------------------------------------
    // moveComplete()
    // Return true if motors have both reached the desired encoder target
    //--------------------------------------------------------------------------
    boolean moveComplete()
    {
        //  return (!mLeftMotor.isBusy() && !mRightMotor.isBusy());
        return ((Math.abs(getLeftPosition() - mLeftEncoderTarget) < 10) &&
                (Math.abs(getRightPosition() - mRightEncoderTarget) < 10));
    }

    //--------------------------------------------------------------------------
    // encodersAtZero()
    // Return true if both encoders read zero (or close)
    //--------------------------------------------------------------------------
    boolean encodersAtZero()
    {
        return ((Math.abs(getLeftPosition()) < 5) && (Math.abs(getRightPosition()) < 5));
    }

    /*
        Begin the first leg of the path array that is passed in.
        Calls startSeg() to actually load the encoder targets.
     */
    private void startPath(PathSeg[] path)
    {
        mCurrentPath = path;    // Initialize path array
        mCurrentSeg = 0;
        synchEncoders();        // Lock in the current position
        runToPosition();        // Enable RunToPosition mode
        startSeg();             // Execute the current (first) Leg
    }

    /*
        Starts the current leg of the current path.
        Must call startPath() once before calling this
        Each leg adds the new relative movement onto the running encoder totals.
        By not reading and using the actual encoder values, this avoids accumulating errors.
        Increments the leg number after loading the current encoder targets
     */
    private void startSeg()
    {
        int Left;
        int Right;

        if (mCurrentPath != null)
        {
            // Load up the next motion based on the current segemnt.
            Left  = (int)(mCurrentPath[mCurrentSeg].mLeft * COUNTS_PER_INCH);
            Right = (int)(mCurrentPath[mCurrentSeg].mRight * COUNTS_PER_INCH);
            addEncoderTarget(Left, Right);
            setDriveSpeed(mCurrentPath[mCurrentSeg].mSpeed, mCurrentPath[mCurrentSeg].mSpeed);

            mCurrentSeg++;  // Move index to next segment of path
        }
    }

    /*
        Determines if the current path is complete
        As each segment completes, the next segment is started unless there are no more.
        Returns true if the last leg has completed and the robot is stopped.
     */
    private boolean pathComplete()
    {
        // Wait for this Segement to end and then see what's next.
        if (moveComplete())
        {
            // Start next Segement if there is one.
            if (mCurrentSeg < mCurrentPath.length)
            {
                startSeg();
            }
            else  // Otherwise, stop and return done
            {
                mCurrentPath = null;
                mCurrentSeg = 0;
                setDriveSpeed(0, 0);
                useConstantSpeed();
                return true;
            }
        }
        return false;
    }
}

/**
 * Define a "PathSegment" object, used for building a path for the robot to follow.
 */
class PathSeg
{
    public double mLeft;
    public double mRight;
    public double mSpeed;

    // Constructor
    public PathSeg(double Left, double Right, double Speed)
    {
        mLeft = Left;
        mRight = Right;
        mSpeed = Speed;
    }
}