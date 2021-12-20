import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class GridPoint{
	public int x;
	public int y;
	@Override
	public boolean equals(Object o) {
		 if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        GridPoint gp = (GridPoint) o;

	        return  x==gp.x && y==gp.y;
	    }

	    public int hashCode() {
	        return 31 * y + 13*x;
	    }
	    public GridPoint(int y,int x) {
	    	this.y=y;
	    	this.x=x;
	    }
}
public class Day20 {
	public static int getPositionInFilter(int[][] grid,int i,int j,int defaultValue) {
		String binaryRepr="";
		for (int y=-1;y<=1;++y) {
			for (int x=-1;x<=1;++x) {
				int  ni=i+y;
				int  nj=j+x;
				if (ni<0 || nj<0 || ni>=grid.length || nj>=grid[i].length)
					binaryRepr+=defaultValue;
				else
					binaryRepr+=grid[ni][nj];
			}
		}
		return Integer.parseInt(binaryRepr,2);
		
	}
	public static void print(int[][] grid) {
		for (int i=0;i<grid.length;++i) {
			for (int j=0;j<grid[i].length;++j) {
				if (grid[i][j]==0)
					System.out.print(".");
				else
					System.out.print("#");
			}
			System.out.println();
		}
		System.out.println();

	}
	
	public static int function(String fname,int iterations,int n,int m) throws IOException{
		BufferedReader brdr=new BufferedReader(new FileReader(fname));	
		String filter=brdr.readLine();
		//at each new iteration four extra lines can start having non-default values 
		//default is the value of a vertex in [-inf,-inf] i.e., that's surrounded by 9 default values
		int[][] grid=new int[n+2*iterations][m+2*iterations];
		brdr.readLine();
		String ln="";
		int lit=0;
		int y=iterations;
		int x=iterations;
		while ((ln=brdr.readLine())!=null) {
			for (int j=0;j<ln.length();++j) {
				if (ln.charAt(j)=='#') {
					grid[y][x+j]=1;
					lit++;
				}else 
					grid[y][x+j]=0;
			}
			y++;
		}
		int defaultValue=0;
		for (int iter=0;iter<iterations;++iter) {
			ArrayList<GridPoint> gridChanges=new ArrayList<GridPoint>();
			//
			for (int i=0;i<grid.length;++i) {
				for (int j=0;j<grid[i].length;++j) {
					int position=getPositionInFilter(grid, i, j,defaultValue);
					if  ( (filter.charAt(position)=='#' && grid[i][j]==0) ||  (filter.charAt(position)=='.' && grid[i][j]==1) )
						gridChanges.add(new GridPoint(i,j));
				}
			}
			for (GridPoint gp : gridChanges) {
				grid[gp.y][gp.x] = (grid[gp.y][gp.x] +1) %2;
				lit+=  (grid[gp.y][gp.x]==1) ? 1 : -1;
			}
			if (defaultValue==0) 
				defaultValue= ( filter.charAt(0)=='#') ? 1 : 0;
			else
				defaultValue= ( filter.charAt(511)=='#') ? 1 : 0;
			}
		return lit;
	}
	public static void main(String[] args)  throws IOException{
		// TODO Auto-generated method stub
		String SAMPLE="input/day20_sample.txt";
		String REAL="input/day20.txt";
	
		System.out.println(function(SAMPLE,2,5,5));
		System.out.println(function(REAL,2,100,100));
		
		System.out.println(function(SAMPLE,50,5,5));
		System.out.println(function(REAL,50,100,100));
		
	}
}
