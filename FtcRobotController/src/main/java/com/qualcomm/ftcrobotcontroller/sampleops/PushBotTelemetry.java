package com.qualcomm.ftcrobotcontroller.sampleops;

//------------------------------------------------------------------------------
//
// PushBotTelemetry
//
/**
 * Extends the PushBotHardware class to provide basic telemetry for the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-02-13-57
 */
public class PushBotTelemetry extends PushBotHardware

{

	// --------------------------------------------------------------------------
	//
	// update_telemetry
	//
	/**
	 * Update the telemetry with current values from the base class.
	 */
	public void update_telemetry()

	{
		//
		// Send telemetry data to the driver station.
		//
		this.telemetry.addData("01",
				"Left Drive: " + this.a_left_drive_power() + ", " + this.a_left_encoder_count());
		this.telemetry.addData("02",
				"Right Drive: " + this.a_right_drive_power() + ", " + this.a_right_encoder_count());
		this.telemetry.addData("03", "Left Arm: " + this.a_left_arm_power());
		this.telemetry.addData("04", "Hand Position: " + this.a_hand_position());
	} // PushBotTelemetry::loop

} // PushBotTelemetry
