package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


/**
 * <p>
 *      Utility class used to manage events on button presses in a runtime loop
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("all")
final class UtilToggle
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
    enum Status
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
    final Status status(boolean buttonStatus)
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
    final boolean isPressed(boolean buttonStatus)
    {
        boolean pressed = false;    // Tells whether a button press (up and down) has occurred

        if(status(buttonStatus) == Status.COMPLETE)
            pressed = true;

        return pressed;
    }
}