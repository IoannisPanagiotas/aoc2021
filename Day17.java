
public class Day17 {
	private static boolean inside(int pos,int low,int high) {
			return low<=pos && pos<=high;
	}

	private static int bruteforce_followTrajectory(int vx,int vy,int lowX,int uppX,int lowY,int uppY,boolean count) {
		
		int ans=0;
		boolean converge=true;
		int x=0;
		int y=0;
		while (!(inside(x,lowX,uppX) && inside(y,lowY,uppY)) ) {
			if (vx==0 && lowY > y) {
				converge=false;
				break;
		}
			x+=vx;
			if (vx>0) vx--;
			if (vx<0) vx++;
			y+=vy;
			vy--;
			ans=Math.max(y, ans);
			
		}
		if (count)
			return converge ? 1 : 0;
		return converge ?  ans : 0;
		
	}
	public static int function(int lowX,int uppX,int lowY,int uppY,boolean count) {
		int ans=0;
		for (int i=-400;i<400;++i) { //expl: cant be that far from target (around sqrt is likely better estimation but anyway)
			for (int j=-400;j<400;++j) {
		
				if (count)
					ans=ans+bruteforce_followTrajectory(i,j,lowX,uppX,lowY,uppY,count);
				else
					ans=Math.max(ans,bruteforce_followTrajectory(i,j,lowX,uppX,lowY,uppY,count)); 
			}
		}
		return ans;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(function(20,30,-10,-5,false));
		System.out.println(function(85,145,-163,-108,false));
		System.out.println(function(20,30,-10,-5,true));
		System.out.println(function(85,145,-163,-108,true));

	}

}
