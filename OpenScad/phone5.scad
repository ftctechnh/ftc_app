
holeDepth = 4;
holeYOffset = 48;
holeZOffset=12.96;
screwThickness=1;
screwRadius=4;
holeRadius = 2;
holeFacets = 288;
cameraAngle=-10;

wallThickness=2;
innerWallThickness=20;
innerWallOffset=3;
outerWallThickness=2;
overhang=4;
arcShift = -4.3835;
phoneLength = 146;
phoneWidth = 11;
phoneHeight = 76;

difference () {
    union() {
        translate([-(innerWallThickness+phoneWidth/2),
                   -(phoneLength+2*overhang)/2,-wallThickness])
            cube([phoneWidth+innerWallThickness+outerWallThickness,
                  phoneLength+2*overhang, wallThickness]);
        
        intersection () {
            union() {
                translate([(phoneWidth+outerWallThickness)/2,0,phoneHeight/2])
                    cube([outerWallThickness,phoneLength+2*overhang,phoneHeight],
                         center=true);
                translate([-(phoneWidth+innerWallThickness)/2,0,phoneHeight/2])
                    cube([innerWallThickness,phoneLength+2*overhang,phoneHeight],
                         center=true);
            }
            
            union() {
                translate([0,(phoneLength/2+overhang+arcShift),0])
                    scale([1,1.15,1])
                        rotate([0,90,0])
                            cylinder(r=30,h=phoneWidth*5, center=true, $fn=holeFacets);
                translate([0,-(phoneLength/2+overhang+arcShift),0])
                    scale([1,1.15,1])
                        rotate([0,90,0])
                        cylinder(r=30,h=phoneWidth*5, center=true, $fn=holeFacets);
            }
        }

        
        translate([0,-(phoneLength/2+overhang+arcShift),14.9])
            EndCap();
        
        translate([0,(phoneLength/2+overhang+arcShift),14.9])
            rotate([0,0,180])
                EndCap();

    }
    //    translate([0,0,phoneHeight/2])
     //   color("green") cube([phoneWidth,phoneLength,phoneHeight], center=true);
    
    translate([0,-(phoneLength/2+overhang+20),0])
        cube([50,40,50],center=true);
    translate([-(phoneWidth/2+outerWallThickness+25),0,45])
        color("blue") cube([50,300,50],center=true);

  translate([0,100,0])
        color("red")cube([50,280,200], center=true);
    
      rotate([0,cameraAngle,0])  {
    translate([-(innerWallOffset+phoneWidth*6),-phoneLength, -wallThickness])

    cube([phoneWidth*5.5, phoneLength*2, phoneHeight*4]); 

  //  translate([0,holeYOffset,holeZOffset])
    //    Screw();
    translate([0,holeYOffset+8,holeZOffset])
        Screw();
    translate([0,holeYOffset,holeZOffset-8])
        Screw();

 //   translate([0,-holeYOffset,holeZOffset])
      //  Screw();
    translate([0,-(holeYOffset+8),holeZOffset])
        Screw();
    translate([0,-holeYOffset,holeZOffset-8])
        Screw();
      }
                        
}

module Screw()
{
    rotate([0,90,0])
    union() {
        cylinder(r=holeRadius,h=phoneWidth*5,
                 center=true, $fn=holeFacets);
        translate([0,0,-(phoneWidth/2+innerWallOffset-screwThickness)])
        cylinder(r=screwRadius,h=3+phoneWidth/2+innerWallOffset,
                 $fn=holeFacets);
    }
}   

module EndCap()
{
    sliceThickness=6;
    radius = 60;
    tabWidth = phoneWidth - 2;
    translate([-(tabWidth/2),0,0])
    rotate([90,0,90])
    difference () {
        intersection() {
            union() {
                intersection() {
                    translate([-sliceThickness,-15,0])
                        cube([sliceThickness,30,tabWidth]);
                    translate([-radius,0,0])
                    cylinder(r=radius,h=tabWidth,$fn=holeFacets);
                }
                
                translate([-2,-18,0])
                rotate([0,0,45])
                cube([4,4,tabWidth]);
            }
            cube([100,30,100], center=true);
        }
 
        translate([-radius-2.5,0,-2])
            cylinder(r=radius,h=48,$fn=holeFacets);
    }
}

module Holes() {
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
