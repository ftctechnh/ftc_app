gripperLength=90;
gripperThickness = 150;

gripperDepth=20;


gripperMax=4;
gripperMin=-4;

gripperBase= 10;

guideDepth=24;

mountThickness = 2;

gridSpacingX = 32;
gridThickness = 1;
gridYOffset = 16;

grid1Length = length(gridSpacingX, gripperLength-gripperBase-gridYOffset-gridThickness/2);
gridAngle1 = atan2(gridSpacingX,gripperLength-gripperBase-gridYOffset-gridThickness/2);

grid2Length = length(gridSpacingX, gripperBase+gridThickness/2);
gridAngle2 = atan2(gridSpacingX,gripperBase+gridThickness/2);

gripperY = guideDepth*guideDepth/(4*gripperLength);
gripperX = sqrt(guideDepth*guideDepth/4 - gripperY*gripperY);

cutterAngle = atan2(gripperX, gripperLength-gripperBase-gripperY);

holePadding = 2;
holeDepth = 140;
holeOffset = 8;
holeRadius = 2;
bigHoleRadius=2.5;


holeFacets = 144;

union() {
    difference() {
        intersection() {
            Grid();
             
            GripperOutline();
        }
        Cutter();

        translate([gridSpacingX,0,0])
            Cutter();

        translate([-gridSpacingX,0,0])
            Cutter();
    }
   intersection() {
        translate([-gridSpacingX, gripperLength-gripperBase-gridThickness, -guideDepth])
            color("red")
                cube([gridSpacingX*3, gridThickness, guideDepth*2]);  
        GripperOutline();
    }
}

function length(x,y) = sqrt(x*x+y*y);

module Cutter() {
    union() {

   translate([gridSpacingX/2,gripperLength,
              -guideDepth/2+gripperDepth + (gripperMax-gripperMin)/8])
        rotate([90,0,0])
            scale([1,.2,1])
            cylinder(r=(gridSpacingX-mountThickness)/2,h=30,center=true,$fn=holeFacets);
        
        translate([gridSpacingX/2,(gripperLength-gripperBase-gridThickness/2),0])
        rotate([90-cutterAngle,0,0])
            scale([1,.2,1])
                    cylinder(r=(gridSpacingX-mountThickness)/2,h=gripperLength*1.5,
                             $fn=holeFacets);
       translate([gridSpacingX/2,(gripperLength-gripperBase-gridThickness/2)+.66,0])
        rotate([315,0,0])
            scale([1,.2,1])
        translate([0,0,-2.1])
                    cylinder(r=(gridSpacingX-mountThickness)/2,h=gripperLength*1.5,
                             $fn=holeFacets);   
    }
}
module Grid() {
    union() {
    translate([-mountThickness/2 - gridSpacingX, gridYOffset-gridThickness/2, -guideDepth])
        cube([mountThickness, gripperLength*2, guideDepth*2]);
    
    translate([-mountThickness/2, -guideDepth/2, -guideDepth])
        cube([mountThickness, gripperLength*2, guideDepth*2]);

    translate([-mountThickness/2 + gridSpacingX, gridYOffset-gridThickness/2, -guideDepth])
        cube([mountThickness, gripperLength*2, guideDepth*2]);

    translate([-mountThickness/2+ 2*gridSpacingX, -guideDepth/2, -guideDepth])
        cube([mountThickness, gripperLength*2, guideDepth*2]);   
    
    translate([-gridSpacingX, gridYOffset-gridThickness/2, -guideDepth])
        cube([gridSpacingX*3, gridThickness, guideDepth*2+5]);
    
    translate([-gridSpacingX, gripperLength-gridThickness/2, -guideDepth])
        color("blue")
        cube([gridSpacingX*3, gridThickness, guideDepth*2]);
    

    
    translate([0,gridYOffset,0])
        rotate([0,0,gridAngle1])
            translate([-gridThickness/2,0,-guideDepth])

            cube([gridThickness,grid1Length,guideDepth*2+5]);
    translate([gridSpacingX,gridYOffset,0])
        rotate([0,0,gridAngle1])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid1Length,guideDepth*2+5]);
    translate([gridSpacingX*2,gridYOffset,0])
        rotate([0,0,gridAngle1])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid1Length,guideDepth*2+3]);

    translate([-gridSpacingX,gridYOffset,0])
        rotate([0,0,-gridAngle1])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid1Length,guideDepth*2+3]);
            
    translate([0,gridYOffset,0])
        rotate([0,0,-gridAngle1])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid1Length,guideDepth*2+3]);
            
    translate([gridSpacingX,gridYOffset,0])
        rotate([0,0,-gridAngle1])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid1Length,guideDepth*2+3]);
            
    translate([0,gripperLength-gripperBase-gridThickness/2,0])
        rotate([0,0,gridAngle2])
            translate([-gridThickness/2,0,-guideDepth])
            color("green")
            cube([gridThickness,grid2Length,guideDepth*2+3]);
            
    translate([gridSpacingX,gripperLength-gripperBase-gridThickness/2,0])
        rotate([0,0,gridAngle2])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid2Length,guideDepth*2+3]);

    translate([gridSpacingX*2,gripperLength-gripperBase-gridThickness/2,0])
        rotate([0,0,gridAngle2])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid2Length,guideDepth*2+3]);     
            
    translate([0,gripperLength-gripperBase-gridThickness/2,0])
        rotate([0,0,-gridAngle2])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid2Length,guideDepth*2+3]);
            
    translate([gridSpacingX,gripperLength-gripperBase-gridThickness/2,0])
        rotate([0,0,-gridAngle2])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid2Length,guideDepth*2+3]);

    translate([-gridSpacingX,gripperLength-gripperBase-gridThickness/2,0])
        rotate([0,0,-gridAngle2])
            translate([-gridThickness/2,0,-guideDepth])
            cube([gridThickness,grid2Length,guideDepth*2+3]);           
}
}


module GripperOutline() {
    rotate([0,-90,0])
    difference() {
        union() {
            translate([0,0,-gripperThickness/2]) {
                linear_extrude(height=gripperThickness)
                    polygon(points=[[-guideDepth/2,0],
                                    [-guideDepth/2,gripperLength+gridThickness/2],
                                    [-guideDepth/2+gripperDepth,gripperLength+gripperMax],
                                    [-guideDepth/2+gripperDepth+0.14,
                                     gripperLength+gripperMin+0.038],
                                    [0, gripperLength-gripperBase],
                                    [gripperX,gripperY]]);               

                
                cylinder(r=guideDepth/2,h=gripperThickness,$fn=holeFacets);
                
                translate([-guideDepth/2+gripperDepth,gripperLength,0])
                    scale([0.25,1,1])
                        cylinder(r=(gripperMax-gripperMin)/2,h=gripperThickness, $fn=144);
            }
        }
        rotate([0,0,45])
            Holes();
    }
}

module Guide() {
    
}


module Holes() {
    cylinder(r=bigHoleRadius,h=holeDepth,
             center=true, $fn=holeFacets);
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
}
