/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;


/**
 * Utility class used to manage events on button presses in a runtime loop
 */
@SuppressWarnings({"unused" , "WeakerAccess"})
public final class UtilToggle
{
    /**
     * Holds all the states that a toggle can be in. When pressing a button, there are 3 states:
     * 1. Not begun
     * 2. In progress
     * 3. Complete
     *
     * If you're checking a button press using a conventional on/off check and using it to
     * flip a boolean, then you'll flip once for every time the button is held and the
     * loop iterates.
     */
    public enum Status
    {
        NOT_BEGUN ,
        IN_PROGRESS ,
        COMPLETE
    }


    private Status _status = Status.NOT_BEGUN;      // Current status of the toggle


    /**
     * Monitors and adjusts the toggle value based on previous toggle values and the
     * state of the boolean passed int.
     *
     * @param buttonStatus Current status of the button in question (true or false)
     *
     * @return The status of the button press in question
     */
    public final Status status(boolean buttonStatus)
    {
        // If the button is being held
        if(buttonStatus && _status == Status.NOT_BEGUN)
            _status = Status.IN_PROGRESS;

        // If the button is not being pressed and the toggle was in progress
        else if(!buttonStatus && _status == Status.IN_PROGRESS)
            _status = Status.COMPLETE;

        // If the toggle is finished
        else if(_status == Status.COMPLETE)
            _status = Status.NOT_BEGUN;

        return _status;
    }


    /**
     * Determines whether a button press (up and down) has occurred or not
     *
     * @param buttonStatus Current status of the button in question (true or false)
     *
     * @return Whether a full button press has occurred or not
     */
    public final boolean isPressed(boolean buttonStatus)
    {
        boolean pressed = false;    // Tells whether a button press (up and down) has occurred

        if(status(buttonStatus) == Status.COMPLETE)
            pressed = true;

        return pressed;
    }
}