/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-11-02

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.core.gamecontroller


import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilToggle


/**
 * Wrapper class for the game pads in OpMode, packs in a bunch of additional functionality
 * such as detecting button presses
 */
@Suppress("unused")
class Controller
{
    private var _gamePad: Gamepad? = null

    // Button toggle variables
    private var _aToggle = UtilToggle()
    private var _bToggle = UtilToggle()
    private var _xToggle = UtilToggle()
    private var _yToggle = UtilToggle()

    private var _leftBumperToggle = UtilToggle()
    private var _rightBumperToggle = UtilToggle()

    private var _leftStickButtonToggle = UtilToggle()
    private var _rightStickButtonToggle = UtilToggle()

    private var _dUpToggle = UtilToggle()
    private var _dDownToggle = UtilToggle()
    private var _dLeftToggle = UtilToggle()
    private var _dRightToggle = UtilToggle()


    /**
     * Reads input from a game pad, call this in every iteration of your OpMode loop.
     */
    fun read(GAME_PAD: Gamepad)
    {
        _gamePad = GAME_PAD
    }


    /**
     * Returns whether the "a" button is currently pressed or not
     */
    fun a(): Boolean?
    {
        return _gamePad?.a
    }


    /**
     * Returns whether the "a" button has been fully clicked (up and down) or not
     */
    fun aClicked(): Boolean?
    {
        return _aToggle.isPressed(_gamePad!!.a)
    }


    /**
     * Returns whether the "b" button is currently pressed or not
     */
    fun b(): Boolean?
    {
        return _gamePad?.b
    }


    /**
     * Returns whether the "b" button has been fully clicked (up and down) or not
     */
    fun bClicked(): Boolean?
    {
        return _bToggle.isPressed(_gamePad!!.b)
    }


    /**
     * Returns whether the "b" button is currently pressed or not
     */
    fun x(): Boolean?
    {
        return _gamePad?.x
    }


    /**
     * Returns whether the "x" button has been fully clicked (up and down) or not
     */
    fun xClicked(): Boolean?
    {
        return _xToggle.isPressed(_gamePad!!.x)
    }


    /**
     * Returns whether the "b" button is currently pressed or not
     */
    fun y(): Boolean?
    {
        return _gamePad?.y
    }


    /**
     * Returns whether the "y" button has been fully clicked (up and down) or not
     */
    fun yClicked(): Boolean?
    {
        return _yToggle.isPressed(_gamePad!!.y)
    }


    /**
     * Returns whether the left bumper is currently pressed or not
     */
    fun leftBumper(): Boolean?
    {
        return _gamePad?.left_bumper
    }


    /**
     * Returns whether the left bumper has been fully clicked (up and down) or not
     */
    fun leftBumperClicked(): Boolean?
    {
        return _leftBumperToggle.isPressed(_gamePad!!.left_bumper)
    }


    /**
     * Returns whether the right bumper is currently pressed or not
     */
    fun rightBumper(): Boolean?
    {
        return _gamePad?.right_bumper
    }


    /**
     * Returns whether the right bumper has been fully clicked (up and down) or not
     */
    fun rightBumperClicked(): Boolean?
    {
        return _rightBumperToggle.isPressed(_gamePad!!.right_bumper)
    }


    /**
     * Returns whether the left stick button is currently pressed or not
     */
    fun leftStickButton(): Boolean?
    {
        return _gamePad?.left_stick_button
    }


    /**
     * Returns whether the left stick button has been fully clicked (up and down) or not
     */
    fun leftStickButtonClicked(): Boolean?
    {
        return _leftStickButtonToggle.isPressed(_gamePad!!.left_stick_button)
    }


    /**
     * Returns whether the right stick button is currently pressed or not
     */
    fun rightStickButton(): Boolean?
    {
        return _gamePad?.right_stick_button
    }


    /**
     * Returns whether the right stick button has been fully clicked (up and down) or not
     */
    fun rightStickButtonClicked(): Boolean?
    {
        return _rightStickButtonToggle.isPressed(_gamePad!!.right_stick_button)
    }


    /**
     * Returns whether the dpad up button is currently pressed or not
     */
    fun dUp(): Boolean?
    {
        return _gamePad!!.dpad_up
    }


    /**
     * Returns whether the dpad up button has been fully clicked (up and down) or not
     */
    fun dUpClicked(): Boolean?
    {
        return _dUpToggle.isPressed(_gamePad!!.dpad_up)
    }


    /**
     * Returns whether the dpad down button is currently pressed or not
     */
    fun dDown(): Boolean?
    {
        return _gamePad!!.dpad_down
    }


    /**
     * Returns whether the dpad down button has been fully clicked (up and down) or not
     */
    fun dDownClicked(): Boolean?
    {
        return _dDownToggle.isPressed(_gamePad!!.dpad_down)
    }


    /**
     * Returns whether the dpad left button is currently pressed or not
     */
    fun dLeft(): Boolean?
    {
        return _gamePad!!.dpad_left
    }


    /**
     * Returns whether the dpad left button has been fully clicked (up and down) or not
     */
    fun dLeftClicked(): Boolean?
    {
        return _dLeftToggle.isPressed(_gamePad!!.dpad_left)
    }


    /**
     * Returns whether the dpad right button is currently pressed or not
     */
    fun dRight(): Boolean?
    {
        return _gamePad!!.dpad_right
    }


    /**
     * Returns whether the dpad right button has been fully clicked (up and down) or not
     */
    fun dRightClicked(): Boolean?
    {
        return _dRightToggle.isPressed(_gamePad!!.dpad_right)
    }


    /**
     * Returns the value of the left stick, x value
     */
    fun leftX(): Double
    {
        return _gamePad?.left_stick_x!!.toDouble()
    }


    /**
     * Returns the value of the left stick, y value
     */
    fun leftY(): Double
    {
        return _gamePad?.left_stick_y!!.toDouble()
    }


    /**
     * Returns the value of the right stick, x value
     */
    fun rightX(): Double
    {
        return _gamePad?.right_stick_x!!.toDouble()
    }


    /**
     * Returns the value of the right stick, y value
     */
    fun rightY(): Double
    {
        return _gamePad?.right_stick_y!!.toDouble()
    }


    /**
     * Returns the value of the left trigger
     */
    fun leftTrigger(): Double
    {
        return _gamePad?.left_trigger!!.toDouble()
    }


    /**
     * Returns the value of the right trigger
     */
    fun rightTrigger(): Double
    {
        return _gamePad?.right_trigger!!.toDouble()
    }
}