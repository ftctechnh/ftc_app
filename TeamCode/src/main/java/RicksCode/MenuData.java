package RicksCode;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MenuData {

    public String fileName = "8045config.txt" ;
    public String directoryPath = "/sdcard/FIRST/" ;
    public String filePath = directoryPath + fileName ;
    //Menu Variables
    //public int      nitems = 12;    // easier to just include 0 as the first element so there are actually 5
    public String[] menulabel ;

    public String[] teamname ;
    public String[] modename ;
    //                                           -52,58    supermodeturnred 4

    public int[]    menuvalue ;
    public int      programmode,secondstodelay,particles,heading0,distance0,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5,rightaveragecolor,leftaveragecolor, supermodeturnblue,supermodeturnred;
    public boolean  TeamisBlue,PickUpPartnerBall, hitcapball;


    public void initializeValues() {

        String[] menulabel= {"Team","Mode", "Particles","Delay","Heading 0", "Distance 0","Heading 1", "Distance 1","Heading 2","Distance 2","Heading 3","Distance 3","Heading 4","Distance 4","Heading 5","Distance 5","Right Color Threshold","Left Color Threshold","SuperMode Turn Blue","SuperMode Turn Red","Hit Cap Ball","Pick Up Partner Ball"};

        String[] teamname = {"Red", "Blue"};

        String[] modename = {"Beacon Route","Judging Mode","Beacon Route SUPER Mode"," Simple Capball","Simple Ramp", "Defense Mode","Super Defense Mode"};


        int[]    menuvalue = {1,0,2,0, 0,8,  -45,55,   20,-14,   0,13,   0,30   ,130,40, 121,121, -6,-7,1,0};


    }




}
