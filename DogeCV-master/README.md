

<div align="center">
    <img src="https://i.imgur.com/sjEURcc.png" width="100%"/>
    <br></br>
  <p>
    <a href="https://discord.gg/qCRpgEY"><img src="https://discordapp.com/api/guilds/345404637374971907/embed.png" alt="Discord server" /></a>
  </p>
  <b>Created by Alex Carter of Disnode Robotics</b>
    <br/>
     <i>Version 1.1.1 Last Updated 1/3/2018</i>

</div>

# DogeCV
A easy to use computer vision library used for FTC Games to detect game objects. Based on Ender CV and OpenCV. 


# DISCLAIMER
### THIS REPO IS STILL UNDER HEAVY DEVELOPMENT. I WILL BE ADDING FURTHER DOCUMENTATION, BUG FIXES AND NEW DETECTORS SOON. MANY OF THE 
However, although many of the detectors are currently pretty basic, I am putting alot of time in effort into this lib, and open sourced it to let the community work or learn from my mistakes. This is the exact code my team will be running so I do have a decent motivation to work on it ;)

## Credits
- Levi 8148 AlephBots (Amazing color filtering and Cryptobox Detector)
- Karter FTC 5975 Cybots (Brainstorming for Jewel Detector)
- Aparna ig (Testing)
- Everyone else on the Programming Discord <3 (Dealing with my bs)

## Known Issues
- Blue in background throws of Crypto checkers. (NO JEANS PLZZZ)
- Gylph Detections pretty buggy

## Planned Features / TODO
- Glyph Color Reading
- Previous frame's results to increase accuracy in detectors
- Motion Tracking for Cryptobox
- Cryptobox line checks follow lines instead of Y-Axis


## Install (Credit to EnderCV)
1. Download this repo, either by cloning from Git or using the zip download. 
2. Open up your FTC Application
3. Navigate to **File** -> **New** -> **Import Module** from the title bar.
4. When the a dialog comes up, asking for the module source directory, navigate to this repo and select the **openCVLibrary320** folder, and then hit **Finish**
5. Repeat steps 3 and 4 except instead of selecting the **openCVLibrary320** folder, select the **DogeCV** folder instead.
6. In the left hand side project explorer in Android Studio, right-click **TeamCode**, and click on **Open Module Settings**.
7. A **Project Struture** dialog should come up. Click the **Dependencies** tab.
8. Click the green plus sign on the right hand side, then **Module dependency**, and then **:openCVLibrary320**, then press OK.
9. Repeat step 8, except substitute **:openCVLibrary320** with **:dogecv**
10. Click **OK** to exit the **Project Structure** dialog.


# Detectors
**See Wiki**

## FAQ
- **If I use dogeCV can I still use Vuforia?**

    Yes, but not at the same time. You must disable Vuforia before using dogeCV and vice versa. Also, the default vuforia localizer doesn't detach from the camera by default. In order to make it detech from the camera, you must use [another vuforia localizer](https://gist.github.com/5484Enderbots/772584ae3fc53fda99c0f8f51dc1a9f9). 

## Changelogs
**1.1.1 HOTFIX**:
 - Fixed Jewel Detector Blue Filter
 

**1.1**:
- New Color Filter API   
- New Generic Detector     
- Fixed Jewel Debug Scores    
- Fixed Imports for DogeLogger inside Cryptobox Detector   
- Ported all detectors to Color Filter API     
- Added Yellow to LeviColorFilter
- Added HSV color filter
- New Relic/Generic Example
 
**1.0**:
 - New Cryptobox Detector
 - Youtube Tutorials
 - Per-Detector Documentation
 - Wiki Start 
 - Added `perfectRatio` tuning for Jewels
 - Optimiziation
 - Removed Multiple Mat returning

**0.5**:
 - Fixed rotated preview on protrait mode.
 - Detectors return an array of images. You can cycle through them by tapping on the preview screen
## Contact
If you have any suggestions or questions feel free to contact me at:    
**VictoryForPhil@gmail.com**
or 
**VictoryForPhil#4759** on Discord

You can also usually spot me on the FTC Discord.
