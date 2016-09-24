package com.lasarobotics.library.monkeyc;

import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.util.Timers;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The MonkeyC (MonkeySee) library that handles recording and storing driver controls
 * These controls can be inserted during runtime (when the robot is moving)
 * or can be created prior to a match.  MonkeyDo can then execute these commands.
 */
public class MonkeyC {

    private ArrayList<MonkeyData> commands;             //Array of commands to be written as JSON
    private Controller previous1 = new Controller();    //Previous state of controller 1
    private Controller previous2 = new Controller();    //Previous state of controller 2

    private Timers t;                                   //Timer instance - we use "global"
    private boolean ended = false;                      //True if the end() method has been called
    private boolean isWaiting = false;                          //True if we're pausing the clock until the next press

    /**
     * Initialize the MonkeyC instance
     */
    public MonkeyC() {
        this.commands = new ArrayList<MonkeyData>();
        ended = false;

        //Create a PAUSED clock
        t = new Timers();
        t.createClock("global");
    }

    /**
     * Add an instruction using two controllers
     *
     * @param c1 Controller 1
     * @param c2 Controller 2
     */
    public void add(Controller c1, Controller c2) {
        add(c1, c2, false);
    }

    /**
     * Add an instruction using two controllers
     *
     * @param c1        Controller 1
     * @param c2        Controller 2
     * @param allowNull Allow a null controller state? Required to ensure end of method at a specified time.
     */
    private void add(Controller c1, Controller c2, boolean allowNull) {
        if (ended) {
            return;
        }

        //Make copy of controller
        Controller local1 = new Controller(c1);
        Controller local2 = new Controller(c2);

        //Get current time stamp
        long time = t.getClockValue("global");

        //Get controller deltas
        MonkeyData data = MonkeyUtil.createDeltas(local1, previous1, local2, previous2, time, allowNull);

        //Update previous values to current values
        this.previous1 = new Controller(local1);
        this.previous2 = new Controller(local2);

        //Write to the instruction array for writing to disk later
        if (data.getDeltasGamepad1() != null || data.getDeltasGamepad2() != null) {
            commands.add(data);
            if (!t.isRunning("global")) {
                if (isWaiting) {
                    //Keep the clock paused if we need to wait - at least until the next press
                    isWaiting = false;
                } else {
                    //Resume clock if it is NOT running
                    t.startClock("global");
                }
            }
        }
    }

    /**
     * Add an instruction using two gamepads (FIRST native, not recommended)
     *
     * @param instruction  Gamepad 1
     * @param instruction2 Gamepad 2
     */
    public void add(Gamepad instruction, Gamepad instruction2) {
        //TODO test this - status on update() must remain either just pressed or just unpressed
        Controller one = new Controller(instruction);
        Controller two = new Controller(instruction2);
        add(one, two);
    }

    /**
     * Returns true if the MonkeyC timer is paused.
     * <p/>
     * Please note that time continues running during a custom function, but
     * controller input will NOT be recorded until the function completes.
     * <p/>
     * To resume time, press any controller button or joystick
     * when NOT currently executing a custom function.
     *
     * @return True if paused, false if running
     */
    public boolean isPaused() {
        return !t.isRunning("global");
    }

    /**
     * Gets the time of the MonkeyC clock
     *
     * @return The time of the clock, in seconds.
     */
    public float getTime() {
        return (float) t.getClockValue("global", TimeUnit.MILLISECONDS) / 1000.0f;
    }

    public void pauseTime() {
        t.pauseClock("global");
    }

    public void resumeTime() {
        if (ended) {
            return;
        }
        t.startClock("global");
    }

    /**
     * Wait for a controller key update to continue the clock
     */
    public void waitForController(Controller controller1, Controller controller2) {
        pauseTime();
        //reset controllers to zero while we're waiting to prevent a repress, resuming the clock
        controller1.reset();
        controller2.reset();
        isWaiting = true;
    }

    /**
     * Clears the entire command list. All commands will be deleted.
     */
    public void clear() {
        commands.clear();
    }

    /**
     * End the command stream. After this, no further commands can be written.
     */
    public void end() {
        if (ended) {
            return;
        }
        pauseTime();

        //ensure that the final command written resets all of the controllers to zero
        previous1 = Controller.getPressedController();
        previous2 = Controller.getPressedController();
        add(Controller.getZeroController(), Controller.getZeroController(), true);

        ended = true;
    }

    /**
     * Write the final JSON to a file
     * <p/>
     * If the file exists, a new file with appended ".1", ".2", etc. will be created
     * It is recommended to use the other implementation and set overwrite to true if you're viewing the file
     * from a manager that does not show hidden files.
     *
     * @param filename The filename to write to
     */
    public void write(String filename) {
        write(filename, false);
    }

    /**
     * Write the final JSON to a file
     *
     * @param filename  The filename to write to
     * @param overwrite True to overwrite file if exists, false to append ".1", ".2", etc. to the end if exists
     */
    public void write(String filename, boolean overwrite) {
        end();
        MonkeyUtil.writeFile(filename, commands, overwrite);
    }

    /**
     * Returns the count of items in the command array
     *
     * @return The count of items in the command array
     */
    public int getCommandsWritten() {
        return commands.size();
    }
}
