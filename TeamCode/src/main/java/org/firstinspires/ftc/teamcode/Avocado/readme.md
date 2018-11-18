# Avocado

This subfolder contains the files created by Team Avocado. **IMPORTANT:** To avoid merge conflicts, please do not commit until you have made a major change to the program. Please remember to write a detailed commit message before pushing.

## Project Structure

The project has been restructured so that is easy for non-developers to modify methods and their corresponding controls. Hardware and methods are stored in a file under the folder "Robot."
In order to call a method, simply create an object of the corresponding class under the "Robot" folder, and use it to call the method. Enter the desired controls into the method.

**Ex:**
`Robot robot = new Robot();`
`robot.TankDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);`

### List of methods
*Note: Methods that are listed as analog will only take analog inputs (joysticks, triggers, etc), while the methods that are listed as binary will only take binary inputs (buttons). Some methods will have analog and binary equivalents.

* TankDrive - *Analog* - controls: (leftdrive, rightdrive)
* strafe - *Binary* - parameters: (left, right, up, down)
* lift_a - *Analog* - parameters: (float lift)
* lift_b - *Binary* - parameters: (boolean lift)