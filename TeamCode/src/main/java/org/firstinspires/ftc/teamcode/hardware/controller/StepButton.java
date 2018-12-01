package org.firstinspires.ftc.teamcode.hardware.controller;

import org.firstinspires.ftc.robotcore.external.Func;

public class StepButton<T> extends Button
{
    private int offset;
    private T[] states;
    private Button incrementButton;
    private Button decrementButton;
    public Handler incrementAction;
    public Handler decrementAction;

    private Func<Boolean> isPressedIncrement;
    private Func<Boolean> isPressedDecrement;

    /**
     * Steps the button
     * @param incrementButton the button to increment
     * @param decrementButton the button to decrement
     * @param states the states of the button
     */
    public StepButton(Button incrementButton, Button decrementButton, T... states) {
        this.states = states;
        this.incrementButton = incrementButton;
        this.decrementButton = decrementButton;
        this.isPressedDecrement = decrementButton.isPressed;
        this.isPressedIncrement = incrementButton.isPressed;
        initButtons();
    }

    /**
     * Set the offset
     * @param offset the offset to be set
     */
    public void setOffset(int offset) {
        if (offset >= 0 && offset < states.length) {
            this.offset = offset;
        }
    }

    /**
     * Initialize the buttons
     */
    private void initButtons() {
        incrementButton.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                if (incrementAction != null) {
                    incrementStep();
                    incrementAction.invoke();
                }
            }
        };
        incrementButton.isPressed = this.isPressedIncrement;
        decrementButton.pressedHandler = new Handler()
        {
            @Override
            public void invoke() throws Exception
            {
                if (decrementAction != null) {
                    decrementStep();
                    decrementAction.invoke();
                }
            }
        };
        decrementButton.isPressed = this.isPressedDecrement;
    }

    /**
     * Increment the offset
     */
    private void incrementStep() {
        if (offset <= states.length - 1) {
            offset++;
        }
    }

    /**
     * Decrement the offset
     */
    private void decrementStep() {
        if (offset > 0) {
            offset --;
        }
    }

    /**
     * Gets the current state
     * @return the current state
     */
    public T getCurrentState() {
        return states[offset];
    }
}
