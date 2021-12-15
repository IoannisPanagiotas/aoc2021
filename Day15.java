import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
class Coord implements Comparable<Coord>{
	public int x;
	public int y;
	public int score;
	public Coord(int y,int x,int score) {
		this.y=y;
		this.x=x;
		this.score=score;
	}
	@Override
	public int compareTo(Coord arg0) {
		if (score < arg0.score) return -1;
		if (score > arg0.score) return 1;
		if (y 	  < arg0.y) return -1;
		if (y 	  > arg0.y) return 1;
		if (x 	  < arg0.x) return -1;
		if (x	  > arg0.x) return -1;
		return 0;
	}
}
public class Day15 {
	
	static int[] X= {-1,0,0,1};
	static int[] Y= {0,-1,1,0};
	private static int actualValue(String ln, int position,int posy,int posx) {
		int ans=posy+posx+Integer.parseInt(String.valueOf(ln.charAt(position)));
		return (ans <=9) ? ans :  ans-9;
	}
	private static void iteration_write(String ln,int[][] board,int m,int posy,int iters,int y) {
		for (int i=0;i<iters*m;++i) 
			board[y][i]=actualValue(ln, i % m,posy,i / m);	
	}
	private static int[][] createGraph(String fname,int iters) throws IOException{
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln=brdr.readLine();
		int m=ln.length();
		int n=1;
		while ((ln=brdr.readLine())!=null)
			n++;
		brdr.close();
		brdr=new BufferedReader(new FileReader(fname));
		int[][] board=new int[iters*n][iters*m];

		int posy=0;
		int y=0;
		for (int iter=0;iter<iters;++iter) {
			while ((ln=brdr.readLine())!=null) {
				iteration_write(ln,board,m,posy,iters,y);
				y++;
			}
			
			posy++;
			brdr.close();
			brdr=new BufferedReader(new FileReader(fname));
		}
		
		return board;
	}
	
	public static int function(String fname,int iters) throws IOException {
		int[][] board=createGraph(fname,iters);
		int n=board.length;
		int m=board[0].length;
		boolean[][] vis=new boolean[n][m];
		int[][] score=new int[n][m];
		PriorityQueue<Coord> pq=new PriorityQueue<Coord>();
		pq.add(new Coord(0,0,0));
		while (!pq.isEmpty()) {
			Coord co=pq.poll();
			while (vis[co.y][co.x]) 
				co=pq.poll();
		
			vis[co.y][co.x]=true;
			score[co.y][co.x]=co.score;
			if (co.x==m-1 && co.y==n-1)
			{
				break;
			}
			for (int i=0;i<4;++i) {
				int nx=co.x + X[i];
				int ny=co.y + Y[i];
				if (nx >=0 && nx<m && ny>=0 && ny<n) {
					if (vis[ny][nx])
						continue;
					int score2 = board[ny][nx]+score[co.y][co.x];
					pq.add(new Coord(ny,nx,score2));
				}
			}
		}
		
		return score[n-1][m-1];
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String SAMPLE="input/day15_sample.txt";
		String REAL="input/day15.txt";
		System.out.println(function(SAMPLE,1));
		System.out.println(function(SAMPLE,5));
		//
		System.out.println(function(REAL,1));
		System.out.println(function(REAL,5));

	}

}
