import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Day9 {
	public static int n;
	public static int m;
	public static int[] X= {-1,0,0,1};
	public static int[] Y= {0,-1,1,0};
	public static int[][] board;
	public static boolean[] vis;

	public static int[][] readBoard(String fname,int n2,int m2) throws IOException{
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		board=new int[n2][m2];
		n=n2;
		m=m2;
		for (int i=0;i<n;++i) {
			String l=brdr.readLine();
			for (int j=0;j<m;++j)
				board[i][j]=Integer.parseInt(String.valueOf(l.charAt(j)));
		}
		return board;
	}
	public static ArrayList<Integer> getLowPoints() throws IOException {
			ArrayList<Integer> low=new ArrayList<Integer>();
			for (int i=0;i<n;++i) {
				for (int j=0;j<m;++j) {
					boolean add=true;
					for (int z=0;z<4;++z) {
						int dj=X[z] + j;
						int di= Y[z] + i;
						if (di>=0 && dj>=0 && di<n && dj<m) {
							if  (board[i][j] >= board[di][dj])
								add=false;
						}
					}
					if (add)
						low.add(i*m+j);
				}
			}
			return low;
	}	
	public static long getBasinOf(int i,int j) {
		long size=0;;
		int[] myQueue=new int[n*m];
		myQueue[0]=i*m+j;
		vis[i*m+j]=true;
		int cpos=0;
		int pos=1;
		while (cpos<pos) {
			int vi=myQueue[cpos] / m;
			int vj=myQueue[cpos++] % m;
			size++;
			for (int z=0;z<4;++z) {
				int dj= X[z] + vj;
				int di= Y[z] + vi;
				if (di>=0 && dj>=0 && di<n && dj<m && !vis[di*m+dj]) {
					if (board[di][dj] > board[vi][vj] && board[di][dj]!=9) {
						vis[di*m+dj]=true;
						myQueue[pos++]=di*m + dj;
					}
				}
			}
		}
		return size;
	}
	public static int part1(ArrayList<Integer> lowPoints) {
		return  lowPoints.stream().mapToInt(v -> 1+board[v /m][v % m]).sum();
	}
	public static long part2(ArrayList<Integer> lowPoints) {
		long[] top3=new long[3];
		int ADDED=0;
		for (Integer v : lowPoints) {
			int vi=v / m;
			int vj=v % m;
			long  sBasin=getBasinOf(vi,vj);
			if (ADDED!=3) {
				top3[ADDED++]=sBasin;
			}else {
				Arrays.sort(top3);
				if (top3[0] < sBasin)
					top3[0]=sBasin;
			}
		}
		return top3[0]*top3[1]*top3[2];
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		String SAMPLE="input/day9_sample.txt";
		readBoard(SAMPLE,5,10);
		vis=new boolean[n*m];
		ArrayList<Integer> lowPoints=getLowPoints();
		System.out.println(part1(lowPoints));
		System.out.println(part2(lowPoints));
		String REAL="input/day9.txt";
		//
		readBoard(REAL,100,100);
		vis=new boolean[n*m];
		lowPoints=getLowPoints();
		System.out.println(part1(lowPoints));
		System.out.println(part2(lowPoints));
		
	}

}
