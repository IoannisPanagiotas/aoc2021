import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
class  OnSegment{
	public int x[];
	public int y[];
	public int z[];
	public long getOnes() {
		long lx=x[1]-x[0]+1;
		long ly=y[1]-y[0]+1;
		long lz=z[1]-z[0]+1;
		return lx*ly*lz;
	}
	public OnSegment(int[] x,int[] y,int[] z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
}
public class Day22 {
	public static int[] getCoords(String s) {
		int[] co=new int[2];
		String[] l=s.split("\\.\\.");
		
		co[0]=Integer.parseInt(l[0].substring(2, l[0].length()));
		co[1]=Integer.parseInt(l[1]);
		return co;
	}
	public static int[] intersectionWith(int[] x,int[] y) {
			int[] xy=new int[2];
			if (x[1] < y[0]) xy[0]=Integer.MAX_VALUE;
			else if ( y[1] < x[0]) xy[0]=Integer.MAX_VALUE;
			else {
				xy[0]=Math.max(x[0], y[0]);    
				xy[1]=Math.min(x[1], y[1]);
			}		
			return xy;
	}
	public static int updateGrid(boolean on,int[][][] grid,int[] x,int[]y,int[] z,int LIMITDOWN,int LIMITUP) {
		int diff=0;
		int[] realx=intersectionWith(x,new int[]{LIMITDOWN,LIMITUP});
		int[] realy=intersectionWith(y,new int[]{LIMITDOWN,LIMITUP});
		int[] realz=intersectionWith(z,new int[]{LIMITDOWN,LIMITUP});
			if (realx[0]==Integer.MAX_VALUE)
				return 0;
			if (realy[0]==Integer.MAX_VALUE)
				return 0;
			if (realz[0]==Integer.MAX_VALUE)
				return 0;
		
		for (int i=realx[0];i<=realx[1];++i) {
			for (int j=realy[0];j<=realy[1];++j) {
				for (int w=realz[0];w<=realz[1];++w) {
					int gi=i-LIMITDOWN;
					int gj=j-LIMITDOWN;
					int gz=w-LIMITDOWN;
					if (on && grid[gi][gj][gz]==0) {
						diff++;
						grid[gi][gj][gz]=1;
					}else if (!on && grid[gi][gj][gz]==1) {
						diff--;
						grid[gi][gj][gz]=0;
					}
				}
			}
		}
		return diff;
	}
	public static int function(String fname,int LIMITDOWN,int LIMITUP)  throws IOException{
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		int gridsize=LIMITUP-LIMITDOWN+1;
		int[][][] grid=new int[gridsize][gridsize][gridsize];
		int lit=0;
		String ln;
		while ((ln=brdr.readLine())!=null) {
			String[] lns=ln.split(" ");
			String[] lncomma=lns[1].split(",");
			int[] x=getCoords(lncomma[0]);
			int[] y=getCoords(lncomma[1]);
			int[] z=getCoords(lncomma[2]);
			lit+=updateGrid(lns[0].equals("on"),grid,x,y,z,LIMITDOWN,LIMITUP);
		}
		return lit;
	}
	public static ArrayList<OnSegment> turnOffIntersection(OnSegment existing,int[] x,int[] y,int[] z){
		
		ArrayList<OnSegment> replacements=new ArrayList<OnSegment>();
		int[] interx=intersectionWith(existing.x,x);
		int[] intery=intersectionWith(existing.y,y);
		int[] interz=intersectionWith(existing.z,z);
		//[1,1] ,[1..2], [1...5]  and [0..1] [2..2] [2..4]
		// intersect to  [1..1] [2..2] [2..4]
		if (interx[0]==Integer.MAX_VALUE || intery[0]==Integer.MAX_VALUE || interz[0]==Integer.MAX_VALUE) { //no inetersection at all
			replacements.add(existing);
		}else {
			if (existing.x[0] < interx[0]) 
				replacements.add(new OnSegment(new int[]{existing.x[0],interx[0]-1},existing.y,existing.z));
			if (existing.x[1] > interx[1])
				replacements.add(new OnSegment(new int[]{interx[1]+1,existing.x[1]},existing.y,existing.z));
			if (existing.y[0] < intery[0])
				replacements.add(new OnSegment(interx,new int[]{existing.y[0],intery[0]-1},existing.z));
			if (existing.y[1] > intery[1])
				replacements.add(new OnSegment(interx,new int[]{intery[1]+1,existing.y[1]},existing.z));
			if (existing.z[0] < interz[0])
				replacements.add(new OnSegment(interx,intery,new int[]{existing.z[0],interz[0]-1}));
			if (existing.z[1] > interz[1])
				replacements.add(new OnSegment(interx,intery,new int[]{interz[1]+1,existing.z[1]}));
			
		}
		return replacements;
	}
	public static long function2(String fname)  throws IOException{
		//maybe Kd-trees or R-trees or other comp. geometry classes would make things faster but i dont remember them :P
		BufferedReader brdr=new BufferedReader(new FileReader(fname));

		ArrayList<OnSegment> segments=new ArrayList<OnSegment>();
		String ln="";
		while ((ln=brdr.readLine())!=null) {
			String[] lns=ln.split(" ");
			String[] lncomma=lns[1].split(",");
			int[] x=getCoords(lncomma[0]);
			int[] y=getCoords(lncomma[1]);
			int[] z=getCoords(lncomma[2]);
			if (lns[0].equals("on")){ 
				ArrayList<OnSegment> disjointSegmentsOfNew=new ArrayList<OnSegment>();
				disjointSegmentsOfNew.add(new OnSegment(x,y,z));
				for (OnSegment seg : segments) {
					ArrayList<OnSegment> replaceDisjointSegmentsWith=new ArrayList<OnSegment>();
					for (OnSegment disjointSeg : disjointSegmentsOfNew) {
						ArrayList<OnSegment> tempDisjointList=turnOffIntersection(disjointSeg,seg.x,seg.y,seg.z);
						for (OnSegment tempDisjointSegment: tempDisjointList)
							replaceDisjointSegmentsWith.add(tempDisjointSegment);
					}
					disjointSegmentsOfNew.clear();
					for (OnSegment repDisjointSegment: replaceDisjointSegmentsWith)
						disjointSegmentsOfNew.add(repDisjointSegment);
				}
				for (OnSegment disjointSegment : disjointSegmentsOfNew)
					segments.add(disjointSegment);
			}else { 				    
				ArrayList<OnSegment> replaceSegmentsWith=new ArrayList<OnSegment>();
				for (OnSegment seg : segments) {
					ArrayList<OnSegment> tempList=turnOffIntersection(seg,x,y,z);
					for (OnSegment tempSegment: tempList)
						replaceSegmentsWith.add(tempSegment);
				}
				segments.clear();
				for (OnSegment seg: replaceSegmentsWith)
					segments.add(seg);
			}
		}
		long ans=0;
		for (OnSegment segment: segments) {
			ans+=segment.getOnes();
		}
		return ans;
	}
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String SAMPLE="input/day22_sample.txt";
		String SAMPLE2="input/day22_sample2.txt";

		String REAL="input/day22.txt";

		System.out.println(function(SAMPLE,-50,50));
		System.out.println(function(REAL,-50,50));
		
		System.out.println(function2(SAMPLE2));
		System.out.println(function2(REAL));


	}

}
