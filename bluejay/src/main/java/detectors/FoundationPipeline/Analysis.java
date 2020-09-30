package detectors.FoundationPipeline;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Analysis {	
	
	/*
	 * In order of left to right, return gaps between stones (sorted), 
	 * provided that there is a non negative distance between
	 * the end of one stone and the start of another
	 */
	private static List<gap> gaps(List<Stone> stones) {
		if(stones.size()==0) return new ArrayList<gap>();

		Stone first = stones.get(0).clone();
		Stone last = stones.get(stones.size()-1).clone();

		first.bounds.x-=first.bounds.width*2;
		last.bounds.x+=last.bounds.width*2;
		stones.add(0,first);
		stones.add(last);

		List<gap> ret = new ArrayList<gap>();

		if(stones.size()<=1) return ret;

		//sort stones
		Collections.sort(stones, new Comparator<Stone>() {
			public int compare(Stone one, Stone two) {
				if(one.x < two.x) return -1;
				if(one.x > two.x) return 1;
				return 0;
			}
		});

		//iterate over stones and make gaps
		for(int i=0;i<stones.size()-1;i++) {
			int gap = stones.get(i+1).bounds.x-
					(stones.get(i).bounds.x+stones.get(i).bounds.width);

			if(gap > 0) {
				ret.add(new gap((int)stones.get(i).y,
						stones.get(i).bounds.x+stones.get(i).bounds.width,
						stones.get(i+1).bounds.x));
			}

		}

		return ret;
	}

	public static Point skystonePosition(List<Stone> stones, Mat canvas) {
		List<gap> gaps = gaps(stones);

		for(gap g : gaps) {
			g.draw(canvas);
		}

		if(gaps.size()==0)return null;

		Collections.sort(gaps, new Comparator<gap>() {
			public int compare(gap o1, gap o2) {
				if(o1.width > o2.width)return 1;
				if(o1.width < o2.width)return -1;
				return 0;
			}
		});
		
		return gaps.get(0).center;
	}
}
