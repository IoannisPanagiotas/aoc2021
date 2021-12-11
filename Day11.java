import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Day11 {
	static int[] X= {-1 ,-1,-1 ,0 , 0  ,1, 1,  1};
	static int[] Y= {-1 , 0, 1, 1 ,-1, -1, 0,  1};
	static int[][] board; 

	static void readBoard(String fname,int n,int m) throws IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln;
		board=new int[n][m];
		for (int i=0;i<n;++i) {
			ln=brdr.readLine();
			for (int j=0;j<m;++j)
				board[i][j]=Integer.parseInt(Character.toString(ln.charAt(j)));
		}
	}
	static int doIteration(int[][] board,int n,int m) {
		Stack<Integer> flash_stack=new Stack<Integer>();
		int iterationFlashes=0;
		for (int i=0;i<n;++i) {
			for (int j=0;j<m;++j) {
				if (board[i][j]<0) 
					board[i][j]=0;
				board[i][j]++;
				if (board[i][j]==10)
					flash_stack.push(i*m+j);
			}
		}
		while (!flash_stack.empty()) {
			int v=flash_stack.pop();
			int vi=v / m;
			int vj = v % m;
			iterationFlashes++;
			board[vi][vj]=Integer.MIN_VALUE;
			for (int i=0;i<X.length;++i) {
				int nvi=vi + Y[i];
				int nvj=vj + X[i];
				if (nvi>=0 && nvj>=0 && nvi<n && nvj<m) {
					board[nvi][nvj]++;
					if ((board[nvi][nvj]==10)) {
						flash_stack.push(nvi*m+nvj);
					}
				}
			}
		}
		return iterationFlashes;
	}
	public static int function(int[][] board,int n,int m,int iterationLimit) throws NumberFormatException, IOException {
		
		int flashes=0;
		for (int iteration=0;iteration<iterationLimit;++iteration)
			flashes+=doIteration(board,n,m);

		return flashes;
	}
	public static int function2(int[][] board,int n,int m) throws NumberFormatException, IOException {
		int iteration=0;
		while (true) {
			iteration++;
			int flashes=doIteration(board,n,m);
			if  (flashes==n*m)
				return iteration;
		}
	}
		public static void main(String[] args) throws NumberFormatException, IOException {
			// TODO Auto-generated method stub
			String SAMPLE="input/day11_sample.txt";
			String REAL="input/day11.txt";
			
			readBoard(SAMPLE,10,10);
			System.out.println(function(board,10,10,100));
			readBoard(SAMPLE,10,10);
			System.out.println(function2(board,10,10));
			//
			readBoard(REAL,10,10);
			System.out.println(function(board.clone(),10,10,100));
			readBoard(REAL,10,10);
			System.out.println(function2(board,10,10));
		}
}
