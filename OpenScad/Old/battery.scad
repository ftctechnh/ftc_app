

holeDepth = 4;
holeOffset = 64;
holeRadius = 2.1;
holeFacets = 72;

difference() {
    union() {
         translate([-13,-68,-3])
            cube([26,136,2]); 
         translate([-13,-58.25,-3])
            cube([51,116.5,3]);
        
        intersection() {
            translate([-13,-58.25,0])
                cube([2,116.5,30]);
            
            union() {
                translate([-13,58.25,0])
                    scale([1,1.25,1])
                        rotate([0,90,0])
                            cylinder(r=30,h=2);
                translate([-13,-58.25,0])
                    scale([1,1.25,1])
                        rotate([0,90,0])
                        cylinder(r=30,h=2);
            }
        }
        
        intersection() {
            translate([36,-58.25,0])
                cube([2,116.5,30]);
            
            union() {
                translate([36,58.25,0])
                    scale([1,1.25,1])
                        rotate([0,90,0])
                            cylinder(r=30,h=2);
                translate([36,-58.25,0])
                    scale([1,1.25,1])
                        rotate([0,90,0])
                        cylinder(r=30,h=2);
            }
        }
        
        translate([-9.5,-55.45,14.9])
        EndCap();

        translate([34.5,55.45,14.9])
        rotate([0,0,180])
        EndCap();
    }
    
   // translate([-11,-56,-2.5])
       // color("green")
          //  cube([47,112,48]);
    cube([100,50,100], center=true);
    
    translate([0,holeOffset,-2])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,-2])
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
                        cube([sliceThickness,30,44]);
                    translate([-radius,0,0])
                    cylinder(r=radius,h=44,$fn=holeFacets);
                }
                
                translate([-2,-18,0])
                rotate([0,0,45])
                cube([4,4,44]);
            }
            cube([100,30,100], center=true);
        }
 
        translate([-radius-1.5,0,-2])
            cylinder(r=radius,h=48,$fn=holeFacets);
    }
}



/*
rotate([0,180,0])
difference() {
    union() {

        translate([-12.5,0,24])
            cube([26,136,2],center=true);
        translate([0,0,10])
            cube([51,113,30],center=true);

    }
    translate([0,0,-2.5])
        color("green")
            cube([47,112,48], center=true);

    cube([100,50,100], center=true);

    translate([0,0,3])
    cube([47,200,24],center = true);
    
    translate([-12.5,holeOffset,24])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([-12.5,-holeOffset,24])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
            
}


*/
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
