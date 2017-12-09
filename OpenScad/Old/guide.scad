sliderWidth = 30;
sliderHeight = 30;
sliderDepth = 5;

tabWidth = 8;
tabGapWidth = 3;
tabRadius = 300.0;
tabDepth = 4;

lowerWidth = 24;
lowerDepth = 2;

upperWidth=24;
upperDepth=1;


holeOffset = 8;
holeRadius = 2;

bigHoleDepth = 1.5;
bigHoleRadius = 4.75;

tabFacets = 256;
holeFacets = 24;
blockFacets = 32;




rotate([0,90,0])
union() {
    difference() {
        Guide();
translate([0,0,1])
rotate([0,90,0])
    rotate_extrude(angle=180, convexity=10)
           translate([6.5, 0]) circle(4, $fn=144);
           
translate([0,10,-5.75])
    rotate([90,0,0])
        cylinder(r=4,h=20, $fn=144);
translate([0,10,7.75])
    rotate([90,0,0])
        cylinder(r=4,h=20, $fn=144);
    }

}
module Guide() {
    difference() {
        union() {
            translate([0,-8,2.75])
            cube([30,16,2], center=true);
            
            difference() {
                union() {
                    intersection() {
                        tapered();
                        translate([0,16,0])
                            cube([32,32,32], center=true);
                    }
                    intersection () {
                        scale([1,0.25,1])
                            tapered();
                        translate([0,-16,0])
                            cube([32,32,32], center=true);
                    }
                }                 translate([0,-16,0])
                cube([36,32,3.5], center=true);
            }

            /*
            translate([0,14.75,1.5])
            rotate([180,0,0])
            intersection() {
                rotate([0,90,0])
                    translate([0.25,0,-15])
                        tapered();
                
                translate([0,-2.5,0])
                    cube([30,5.5,5.5], center=true);
            }*/
        }
        translate([0,-8,0])
            Holes();
    }    
}

module tapered()
{
    rotate([0,90,0])
    translate([-1,0,-15])
    scale([2.5,1,1])
    union() {
        linear_extrude(height=10, scale=[2,2])
            circle(r=2.75,$fn=144);
        translate([0,0,10])
                cylinder(r=5.5,h=10,$fn=144);
        translate([0,0,20])
            linear_extrude(height=10, scale=[0.5,0.5])
                circle(r=5.5,$fn=144);
    }
}


module miniGuide1() {
    translate([0,-6.5,11])
    intersection () {
        difference() {
            translate([0,12,-5])
            rotate([-45,0,0])
                difference() {
                        cylinder(r=16,h=2,center=true,
                                 $fn=32);
                    translate([0,4,0])
                        cylinder(r=11, h=4,
                                 center=true, $fn=32);
            }
        }
        translate([0,0,-30])
            cube([40,80,40],center=true);
    }
}

module miniGuide2() {
    rotate([0,180,0])
        miniGuide1();
}

module Slider()
{
    rotate([90,0,0])
    difference()
    {
        union() {
            translate([sliderWidth/2,0,0])
                Tab();
            translate([-sliderWidth/2,0,0])
                Tab();
            translate([0,0,sliderDepth/2])
                cube([sliderWidth-tabWidth,sliderHeight,sliderDepth],
                     center=true);

        }
        Holes();

        
  /*      translate([0,0,sliderDepth])
            RoundedBlock(upperWidth, 2*sliderHeight, 2*upperDepth);*/
        translate([0,0,0])
            RoundedBlock(lowerWidth, 2*sliderHeight, 2*lowerDepth);
    }
}

module Tab() {
    union() {
        translate([0,0,sliderDepth/2])
            intersection() {
                cube([tabWidth,sliderHeight,sliderDepth],
                     center = true);
                union() {
                    translate([tabRadius + tabGapWidth/2,0,0])
                        cylinder(h = sliderDepth*2, r = tabRadius, 
                                 center=true, $fn=tabFacets);
                    translate([-(tabRadius + tabGapWidth/2),0,0])
                        cylinder(h = sliderDepth*2, r = tabRadius, 
                                 center=true, $fn=tabFacets);

               }
           }
        translate([0,0,(sliderDepth-tabDepth)/2])
           cube([tabWidth,sliderHeight,(sliderDepth-tabDepth)], center=true);
   }
}

module Holes() {
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);

}

module BigHoles() {/*
    rotate([0,180])
    translate([holeOffset,0,0])
        linear_extrude(height = 2*bigHoleDepth, center=true, scale=1.5)
        circle(r=bigHoleRadius,$fn=6);
    
    rotate([0,180])
    translate([-holeOffset,0,0])
        linear_extrude(height = 2*bigHoleDepth, center=true, scale=1.5)
        circle(r=bigHoleRadius,$fn=6);
    
    rotate([0,180])
    translate([0,holeOffset,0])
        linear_extrude(height = 2*bigHoleDepth, center=true, scale=1.5)
        circle(r=bigHoleRadius,$fn=6);
    
    rotate([0,180])
    translate([0,-holeOffset,0])
        linear_extrude(height = 2*bigHoleDepth, center=true, scale=1.5)
        circle(r=bigHoleRadius,$fn=6);
    */
    translate([holeOffset,0,0]) 
        rotate([0,0,30])   
            cylinder(r=bigHoleRadius,h=2*bigHoleDepth,
                     center=true, $fn=6);
    translate([-holeOffset,0,0])
        rotate([0,0,30])
            cylinder(r=bigHoleRadius,h=2*bigHoleDepth,
                     center=true, $fn=6);
    translate([0,holeOffset,0])
        rotate([0,0,30])
            cylinder(r=bigHoleRadius,h=2*bigHoleDepth,
                     center=true, $fn=6);
    translate([0,-holeOffset,0])
        rotate([0,0,30])
            cylinder(r=bigHoleRadius,h=2*bigHoleDepth,
                     center=true, $fn=6);
}

module RoundedBlock(width, length, height, radius)
{
    cube([width, length, height], center=true);
}
