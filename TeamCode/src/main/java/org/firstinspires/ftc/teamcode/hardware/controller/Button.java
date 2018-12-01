package org.firstinspires.ftc.teamcode.hardware.controller;

import org.firstinspires.ftc.robotcore.external.Func;

/**
 * The Button class represents a button on the gamepad and handles all the reactions done on the
 * gamepad.
 */
public class Button
{
    public Func<Boolean> isPressed;
    public Handler pressedHandler;
    public Handler releasedHandler;

    private boolean wasPressed;
    public boolean justPressed;
    public boolean justReleased;

    /**
     * Creates a new Button
     */
    public Button()
    {
    }

    /**
     * Creates a new Button given an isPressed Handler
     * @param isPressed handler when the button is pressed
     */
    public Button(Func<Boolean> isPressed)
    {
        this.isPressed = isPressed;
    }

    /**
     * Creates a new Button given an isPressed Handler, and a pressed handler
     * @param isPressed is pressed check
     * @param pressedHandler pressed handler
     */
    public Button(Func<Boolean> isPressed, Handler pressedHandler)
    {
        this.isPressed = isPressed;
        this.pressedHandler = pressedHandler;
    }

    /**
     * Creates a new button given an is pressed handler, pressed handler, and released handler
     * @param isPressed checks if button is pressed
     * @param pressedHandler handles when the button is pressed
     * @param releasedHandler handles when the button is released
     */
    public Button(Func<Boolean> isPressed, Handler pressedHandler, Handler releasedHandler)
    {
        this.isPressed = isPressed;
        this.pressedHandler = pressedHandler;
        this.releasedHandler = releasedHandler;
    }

    /**
     * Tests the button for the button to be pressed and calls the necessary methods depending
     * on previous actions.
     */
    public void testAndHandle()
    {
        Boolean pressed = this.isPressed.value();

        this.justPressed = (pressed && !this.wasPressed);
        this.justReleased   = (!pressed && this.wasPressed);
        this.wasPressed = pressed;

        if (this.justPressed && this.pressedHandler != null)
            try
            {
                this.pressedHandler.invoke();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        if (this.justReleased && this.releasedHandler != null)
            try
            {
                this.releasedHandler.invoke();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
}
