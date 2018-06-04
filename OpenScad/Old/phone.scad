
holeDepth = 4;
holeOffset = 80;
holeRadius = 2.1;
holeFacets = 72;

wallThickness=2;
overhang=5;
phoneLength = 143;
phoneWidth = 12;
phoneHeight = 76;

union () {
    union() {

        translate([0,0,-wallThickness/2])
            cube([phoneWidth+wallThickness*2,phoneLength+2*overhang,wallThickness],
                  center=true);
        
        intersection () {
            union() {
                translate([(phoneWidth+wallThickness)/2,0,phoneHeight/2])
                    cube([wallThickness,phoneLength+2*overhang,phoneHeight],
                         center=true);
                translate([-(phoneWidth+wallThickness)/2,0,phoneHeight/2])
                    cube([wallThickness,phoneLength+2*overhang,phoneHeight], center=true);
            }
            
            union() {
                translate([0,76.5,0])
                    scale([1,1.15,1])
                        rotate([0,90,0])
                            cylinder(r=30,h=phoneWidth*2, center=true);
                translate([0,-76.5,0])
                    scale([1,1.15,1])
                        rotate([0,90,0])
                        cylinder(r=30,h=phoneWidth*2, center=true);
            }
        }

        
        translate([-5,-76.5+2.8,14.9])
        EndCap();
        
        translate([5,76.5-2.8,14.9])
        rotate([0,0,180])
            EndCap();

    }
    //    translate([0,0,phoneHeight/2])
     //   color("green")
          //  cube([phoneWidth,phoneLength,phoneHeight], center=true);


  //  color("red")cube([50,88,200], center=true);

    
    translate([0,holeOffset,-1])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,-1])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
            
}

module EndCap()
{    sliceThickness=4;
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
