package com.qualcomm.ftcrobotcontroller.sampleops;

//------------------------------------------------------------------------------
//
// PushBotAuto
//
/**
 * Extends the PushBotTelemetry and PushBotHardware classes to provide a basic autonomous
 * operational mode for the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */
public class PushBotAuto extends PushBotTelemetry

{

	// --------------------------------------------------------------------------
	//
	// v_state
	//
	// --------
	// This class member remembers which state is currently active. When the
	// start method is called, the state will be initialized (0). When the loop
	// starts, the state will change from initialize to state_1. When state_1
	// actions are complete, the state will change to state_2. This implements
	// a state machine for the loop method.
	// --------
	int v_state = 0;

	// --------------------------------------------------------------------------
	//
	// PushBotAuto
	//
	/**
	 * Constructs the class. The system calls this member when the class is instantiated.
	 */
	public PushBotAuto()

	{
		//
		// Initialize base classes.
		//
		// All via self-construction.

		//
		// Initialize class members.
		//
		// All via self-construction.

	} // PushBotAuto::PushBotAuto

	// --------------------------------------------------------------------------
	//
	// start
	//
	/**
	 * Performs any actions that are necessary when the OpMode is enabled. The system calls
	 * this member once when the OpMode is enabled.
	 */
	@Override
	public void start()

	{
		//
		// Call the PushBotHardware (super/base class) start method.
		//
		super.start();

		//
		// Reset the motor encoders on the drive wheels.
		//
		this.reset_drive_encoders();

	} // PushBotAuto::start

	// --------------------------------------------------------------------------
	//
	// loop
	//
	/**
	 * Implement a state machine that controls the robot during auto-operation. The system
	 * calls this member repeatedly while the OpMode is running.
	 */
	static int[] l_times = new int[3];

	@Override
	public void loop()

	{
		// ----------------------------------------------------------------------
		//
		// State: Initialize (i.e. state_0).
		//
		switch (this.v_state) {
			//
			// Synchronoize the state machine and hardware.
			//
			case 0:
				//
				// Reset the encoders to ensure they are at a known good value.
				//
				this.reset_drive_encoders();

				//
				// Transition to the next state when this method is called again.
				//
				PushBotAuto.l_times[0] = 0;
				PushBotAuto.l_times[1] = 0;
				PushBotAuto.l_times[2] = 0;
				this.v_state++;

				break;
			//
			// Drive forward until the encoders exceed the specified values.
			//
			case 1:
				//
				// Tell the system that motor encoders will be used. This call MUST
				// be in this state and NOT the previous or the encoders will not
				// work. It doesn't need to be in subsequent states.
				//
				this.run_using_encoders();

				//
				// Start the drive wheel motors at full power.
				//
				this.set_drive_power(1.0f, 1.0f);

				//
				// Have the motor shafts turned the required amount?
				//
				// If they haven't, then the op-mode remains in this state (i.e this
				// block will be executed the next time this method is called).
				//
				if (this.have_drive_encoders_reached(2880, 2880)) {
					//
					// Reset the encoders to ensure they are at a known good value.
					//
					this.reset_drive_encoders();

					//
					// Stop the motors.
					//
					this.set_drive_power(0.0f, 0.0f);

					//
					// Transition to the next state when this method is called
					// again.
					//
					this.v_state++;
				}
				break;
			//
			// Wait...
			//
			case 2:
				if (this.have_drive_encoders_reset()) {
					this.v_state++;
				} else {
					PushBotAuto.l_times[0]++;
				}
				break;
			//
			// Turn left until the encoders exceed the specified values.
			//
			case 3:
				this.run_using_encoders();
				this.set_drive_power( -1.0f, 1.0f);
				if (this.have_drive_encoders_reached(2880, 2880)) {
					this.reset_drive_encoders();
					this.set_drive_power(0.0f, 0.0f);
					this.v_state++;
				}
				break;
			//
			// Wait...
			//
			case 4:
				if (this.have_drive_encoders_reset()) {
					this.v_state++;
				} else {
					PushBotAuto.l_times[1]++;
				}
				break;
			//
			// Turn right until the encoders exceed the specified values.
			//
			case 5:
				this.run_using_encoders();
				this.set_drive_power(1.0f, -1.0f);
				if (this.have_drive_encoders_reached(2880, 2880)) {
					this.reset_drive_encoders();
					this.set_drive_power(0.0f, 0.0f);
					this.v_state++;
				}
				break;
			//
			// Wait...
			//
			case 6:
				if (this.have_drive_encoders_reset()) {
					this.v_state++;
				} else {
					PushBotAuto.l_times[2]++;
				}
				break;
			//
			// Perform no action - stay in this case until the OpMode is stopped.
			// This method will still be called regardless of the state machine.
			//
			default:
				//
				// The autonomous actions have been accomplished (i.e. the state has
				// transitioned into its final state.
				//
				break;
		}

		//
		// Send telemetry data to the driver station.
		//
		this.update_telemetry(); // Update common telemetry
		this.telemetry.addData("11", "State: " + this.v_state);
		this.telemetry.addData("12", "Times: " + PushBotAuto.l_times[0]);
		this.telemetry.addData("13", "Times: " + PushBotAuto.l_times[1]);
		this.telemetry.addData("14", "Times: " + PushBotAuto.l_times[2]);

	} // PushBotAuto::loop

} // PushBotAuto
