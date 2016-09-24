package com.lasarobotics.library.controller;

/**
 * Contains button state variables
 */
public class ButtonState {
    public final static int NOT_PRESSED = 0;    //NOT pressed (called after RELEASED)
    public final static int PRESSED = 1;        //JUST pressed
    public final static int RELEASED = 2;       //JUST released
    public final static int HELD = 3;           //Held down (called after PRESSED)
}
