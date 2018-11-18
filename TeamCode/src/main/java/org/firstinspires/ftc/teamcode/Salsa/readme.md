## Team Salsa Code Repository

Here is the code for Team Salsa
- The goal for this code is to be as object-oriented as possible, so that it can adapt to new situations

For _**any**_ questions, please contact me! (I'm is Aditya, btw)

> Remember to use Google to your advantage, it probably has the answer to your question somewhere!

#### Development Status

You can find the schedule in the team's Google Drive!

| Task        | Status           |
| ------------- |:-------------:|
| Create the underlying Code      | Complete |
| Create relatively specific code for robot      | Complete      |
| Begin vision development | Complete      |
| Have a working TeleOp Code | Complete |
| Have a _working_ vision code | In Progress |
| Have a working Auto | In Progress |
| Code version 1.0 complete | In Progress |
| Begin Code Version 2.0 | Not Started |

___

As you can see, we have three separate folders:
- **Hardware**
- **OpModes**
- **Vision**

We also have a file, titled `Constants.java`. This file has all of the constants, from hardware names to information such as the wheel circumference

#### Hardware

The **_Hardware_** folder contains the compiled `Robot.java` file, and then a folder titled **_Subcomponents_**

#### OpModes

The **_OpModes_** folder is divided into two:
- TeleOp
- Autonomous

We also have two files: `SalsaLinearOpMode.java` and `SalsaOpMode.java`

These two files are made so that we can have the robot specific functions without needing to instantiate anything. It also looks better :)

#### Hardware

The **_Vision_** folder contains the code for any required vision. It interfaces with the DogeCV library, but is made to work easily with our code. It does the same with Vuforia, also.

