package org.firstinspires.ftc.teamcode.Hardware.controller;

import android.os.Handler;

import org.firstinspires.ftc.robotcore.external.Func;

public class Button
{
    public Func<Boolean> isPressed;
    public Handler pressedHandler;
    public Handler releasedHandler;

    private boolean wasPressed;
    public boolean justPressed;
    public boolean justReleased;

    public Button()
    {
    }

    public Button(Func<Boolean> isPressed)
    {
        this.isPressed = isPressed;
    }

    public Button(Func<Boolean> isPressed, Handler pressedHandler)
    {
        this.isPressed = isPressed;
        this.pressedHandler = pressedHandler;
    }

    public Button(Func<Boolean> isPressed, Handler pressedHandler, Handler releasedHandler)
    {
        this.isPressed = isPressed;
        this.pressedHandler = pressedHandler;
        this.releasedHandler = releasedHandler;
    }

    public void testAndHandle()
    {
        Boolean pressed = this.isPressed.value();

        this.justPressed = (pressed && !this.wasPressed);
        this.justReleased   = (!pressed && this.wasPressed);
        this.wasPressed = pressed;

        if (this.justPressed && this.pressedHandler != null)
            try
            {
                //this.pressedHandler.invoke();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        if (this.justReleased && this.releasedHandler != null)
            try
            {
                //this.releasedHandler.invoke();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
}
