
holeDepth = 4;
holeYOffset = 19; //48
holeZOffset=16;
screwThickness=2;
screwRadius=4;
holeRadius = 2;
holeFacets = 288;

wallThickness=2;
sideWallThickness=3;
overhang=2.5;
arcShift = -2.764;
phoneLength = 76;//143;  //-67 -- 33.5
phoneWidth = 12;
phoneHeight = 76;

difference () {
    union() {

        translate([0,0,-wallThickness/2])
            cube([phoneWidth+sideWallThickness*2,phoneLength+2*overhang,wallThickness],
                  center=true);
        
        intersection () {
            union() {
                translate([(phoneWidth+sideWallThickness)/2,0,phoneHeight/2])
                    cube([sideWallThickness,phoneLength+2*overhang,phoneHeight],
                         center=true);
                translate([-(phoneWidth+sideWallThickness)/2,0,phoneHeight/2])
                    cube([sideWallThickness,phoneLength+2*overhang,phoneHeight],
                         center=true);
            }
            
            union() {
                translate([0,(phoneLength/2+overhang+arcShift),0])
                    scale([1,1.15,1])
                        rotate([0,90,0])
                            cylinder(r=30,h=phoneWidth*2, center=true, $fn=holeFacets);
                translate([0,-(phoneLength/2+overhang+arcShift),0])
                    scale([1,1.15,1])
                        rotate([0,90,0])
                        cylinder(r=30,h=phoneWidth*2, center=true, $fn=holeFacets);
            }
        }

        
        translate([-5,-(phoneLength/2+overhang+arcShift),14.9])
        EndCap();
        
        translate([5,(phoneLength/2+overhang+arcShift),14.9])
        rotate([0,0,180])
            EndCap();

    }
      //  translate([0,0,phoneHeight/2])
      //  color("green")
         //   cube([phoneWidth,phoneLength,phoneHeight], center=true);

translate([0,100,0])

    color("red")cube([50,213,200], center=true);

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

module Screw()
{
    rotate([0,90,0])
    union() {
        cylinder(r=holeRadius,h=phoneWidth+8,
                 center=true, $fn=holeFacets);
        cylinder(r=screwRadius,h=phoneWidth+screwThickness*2,
                 center=true, $fn=holeFacets);
    }
}   

module EndCap()
{
    sliceThickness=4;
    radius = 90;
    rotate([90,0,90])
    difference () {
        intersection() {
            union() {
                intersection() {
                    translate([-sliceThickness,-15,0])
                        cube([sliceThickness,30,10]);
                    translate([-radius,0,0])
                    cylinder(r=radius,h=10,$fn=holeFacets);
                }
                
                translate([-2,-18,0])
                rotate([0,0,45])
                cube([4,4,10]);
            }
            cube([100,30,100], center=true);
        }
 
        translate([-radius-1.5,0,-2])
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
