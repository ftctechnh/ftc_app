package com.qualcomm.ftcrobotcontroller.opmodes.ui;

/**
 * Created by tdoylend on 2016-01-03.
 *
 * This class provides a button which 'toggles' a boolean when pressed.
 */
public class ToggleButton extends UIBase {

    boolean state;
    boolean btnReady;
    boolean processEvent;

    public ToggleButton() {
        this.state = false;
        this.btnReady = true;
    }

    public void toggle() { //Toggle the state without a button press.
        this.state = ! this.state;
    }

    public void update(boolean btnState) {
        //Update the ToggleButton. Takes a boolean indicating the current state of the button.
        if (btnState) {
            if (this.btnReady) {
                this.btnReady = false;
                this.state = ! this.state;
                this.processEvent = true;
            }
        } else {
            this.btnReady = true;
        }
    }

    public boolean isEvent() {
        //Check if an 'event' (a toggle) has occured.
        boolean processEventTemp = this.processEvent;
        this.processEvent = false;
        return processEventTemp;
    }

    public boolean getState() {
        //Get the state of the ToggleButton.
        return this.state;
    }

}
