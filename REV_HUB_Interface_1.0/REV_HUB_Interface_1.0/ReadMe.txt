The REV Hub Interface (REVHI) is a beta piece of software which allows people to directly connect with their REV Expansion hub and peripherals from their PC.  The goal of this interface is to allow a method for teams to prototype with motors, servos and sensors in a way that is faster and easier than fully setting up their control system. This software is also a good way to isolate problems and identify them as either electrical or software. Lastly you will be able to upgrade your firmware through this interface (firmware may also be updated through the robot controller app).

Version 1.0 - Preliminary Release

Please report bugs or questions to support@revrobotics.com

It is our goal to release this software as opensource in the future, but for now if you are interested in contributing to this project please email us. 

General Notes
* Connect hub via USB to your PC and hit connect. When the hub is connected icons will appear on the different tabs.
* In order to run motors or servos you need to also have a battery connected to the hub.
* There is up to a 2 second delay after a usb disconnect before the last action stops. Be Careful when driving motors.
* There is a known bug when querying the IMU, you need to hit the "Poll" button twice
* Every time you change a tab and go back you will need to hit "poll" or activate things again (we do this to reduce lag due to packet size)
* For motors - you can either slide the bar or enter a specific number into the box. Be careful as there is no "Stop button" besides moving things back to zero or hitting the e-stop (this will be added later)
* When you e-stop the software you need to close the program and re-launch it before it will work again (this is due to the way windows handles comm ports)
* If you can't talk to the hub your PC is likely missing the FTDI drivers needed for the hub, you can download them here http://www.ftdichip.com/FTDrivers.htm
* In order for the firmware update tool to work sflash.exe needs to be in the same folder as the main exe file.


Featues that are on our dev list
* Reading/writing of other i2c devices
* Adding pre-configured sensors and motors
* PID tuning 
* Generating configuration files
* Better start/stop interface
* Versions for other opperating systems








Legal Disclaimer

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Sflash.exe is a product of Texas Instruments and is re-distributed by REV Robotics LLC under grant permission. It is only to be used to program REV Expansion Hubs that feature TI chips.

