import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class SeaMove{
	public int y;
	public int x;
	public SeaMove(int y,int x) {
		this.y=y;
		this.x=x;
	}
}
public class Day25 {
	public static boolean isRightPossible(char[][] grid, int y,int x,int n,int m) {
		return (grid[y][x]=='>' && grid[y][(x+1)%m]=='.') ? true : false;
	}
	public static boolean isDownPossible(char[][] grid, int y,int x,int n,int m) {
		return (grid[y][x]=='v' && grid[(y+1)%n][x]=='.') ? true : false;
	}

	public static void print(char[][] grid,int n,int m) {
		for (int i=0;i<n;++i) {
			for (int j=0;j<m;++j)
				System.out.print(grid[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	public static int function(String fname,int n,int m) throws IOException{
		char[][] grid=new char[n][m];

	//	ArrayList<SeaMove> nextMovesX= new ArrayList<SeaMove>();
	//	ArrayList<SeaMove> nextMovesY= new ArrayList<SeaMove>();

		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		for (int r=0;r<n;++r)
			grid[r]=brdr.readLine().toCharArray();
		int steps=0;
		while (true) {
			steps++;
			int moves=0;
			for (int i=0;i<n;++i) {
				boolean lastMoves=false;
				if (grid[i][0]=='.' && grid[i][m-1]=='>')
					lastMoves=true;
				for (int j=0;j<m-1;++j) {
					if (grid[i][j]=='>' && grid[i][j+1]=='.') {
						grid[i][j]='.';
						grid[i][j+1]='>';
						moves++;
						j++; //otherwise will keep trying to move >
					}
				}
				if (lastMoves) {
					grid[i][0]='>';
					grid[i][m-1]='.';
					moves++;
				}
			}
			 for (int j=0;j<m;++j) {
				boolean lastMoves=false;
				if (grid[0][j]=='.' && grid[n-1][j]=='v')
					lastMoves=true;
				for (int i=0;i<n-1;++i) {
					if (grid[i][j]=='v' && grid[i+1][j]=='.') {
						grid[i][j]='.';
						grid[i+1][j]='v';
						moves++;
						i++;
					}
				}
				if (lastMoves) {
					grid[0][j]='v';
					grid[n-1][j]='.';
					moves++;
				}
			} 
			if (moves==0)
				break;
		}
		return steps;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String SAMPLE="input/day25_sample.txt";
		String MINI="input/day25_mini.txt";

		String REAL="input/day25.txt";
		System.out.println(function(SAMPLE,9,10));
		//System.out.println(function(MINI,2,5));
		System.out.println(function(REAL,137,139));
	}

}

// WHY THIS NO WORK???
/*	for (int i=0;i<n;++i) {
for (int j=0;j<m;++j)
	if (grid[i][j]=='>' && isRightPossible(grid,i,j,n,m))
		nextMovesX.add(new SeaMove(i,j));
	else if (grid[i][j]=='v' && isDownPossible(grid,i,j,n,m))
		nextMovesY.add(new SeaMove(i,j));
}
int steps=0;
while (!nextMovesX.isEmpty() || !nextMovesY.isEmpty() || steps==0) {
ArrayList<SeaMove> nextnextMovesX=new ArrayList<SeaMove>();
ArrayList<SeaMove> nextnextMovesY=new ArrayList<SeaMove>();
print(grid,n,m);
steps++;
int moves=0;
for (SeaMove e : nextMovesX) {
	int y=e.y;
	int x=e.x;
	if (!isRightPossible(grid,y,x,n,m))
		continue;
	grid[y][(x+1)%m]='>';
	grid[y][x]='.';
	moves++;
	nextnextMovesX.add(new SeaMove(y,(x+1)%m)); //probably should be avoided when not feasible but whatever
	int minusx= (x==0) ? m-1 : x-1;
	if ((isRightPossible(grid,y,minusx,n,m)))
		nextnextMovesX.add(new SeaMove(y,minusx));
	int minusy=(y==0) ?  n-1 : y-1;
	if (isDownPossible(grid,minusy,x,n,m))
		nextMovesY.add(new SeaMove(minusy,x));
}
for (SeaMove e : nextMovesY) {
	int y=e.y;
	int x=e.x;
	if (!isDownPossible(grid,y,x,n,m))
		continue;
	moves++;
	grid[y][x]='.';
	grid[(y+1)%n][x]='v';
	nextnextMovesY.add(new SeaMove((y+1)%n,x));
	int minusx= (x==0) ? m-1 : x-1;
	if (isRightPossible(grid,y,minusx,n,m))
		nextnextMovesX.add(new SeaMove(y,minusx));
	int minusy=(y==0) ?  n-1 : y-1;
	if (isDownPossible(grid,minusy,x,n,m))
		nextnextMovesY.add(new SeaMove(minusy,x));
}
nextMovesX.clear();
nextMovesX.addAll(nextnextMovesX);
nextMovesY.clear();
nextMovesY.addAll(nextnextMovesY);
//	System.out.println(steps+" "+moves);
}*/
