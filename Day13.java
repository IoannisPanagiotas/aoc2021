import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day13 {

	public static void fold_horizontal(int n,int m,int pos,boolean[][] board) {
		for (int i=0;i<n;++i) 
			for (int j=pos+1;j<m;++j) 
				board[i] [2*pos  - j ] |= board[i][j];
			
	}
	public static void fold_vertical(int n,int m,int pos,boolean[][] board) {
		for (int j=0;j<m;++j) 
			for (int i=pos+1;i<n;++i) 
				board[ 2*pos - i  ] [j] |= board[i][j];

	}
	public static int function(String fname,int numOfOperrations,boolean print) throws IOException {
		String ln;
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		
		int n=0;
		int m=0;
		while (!(ln=brdr.readLine()).equals("")) {
			int[] yx= {Integer.parseInt(ln.split(",")[0]),Integer.parseInt(ln.split(",")[1])};
			n=Math.max(yx[1]+1, n);
			m=Math.max(yx[0]+1, m);
		}
		boolean[][] board=new boolean[n][m];
		brdr.close();
		brdr=new BufferedReader(new FileReader(fname));
		while (!(ln=brdr.readLine()).equals("")) {
			int[] yx= {Integer.parseInt(ln.split(",")[0]),Integer.parseInt(ln.split(",")[1])};
			board[yx[1]][yx[0]]=true;
		}
		for (int iter=0;iter<numOfOperrations;++iter) {	
			ln=brdr.readLine();
			if (ln==null)
				break;
			String[] lsplit= ln.split(" ")[2].split("=");
			int pos=Integer.parseInt(lsplit[1]);
			if (lsplit[0].charAt(0)=='x') {
				//System.out.println(n+" ["+m+"] "+pos);
				fold_horizontal(n,m,pos,board);
				m= pos;
			}else {
				//System.out.println("["+n+"] "+m+" "+pos);

				fold_vertical(n,m,pos,board);
				n=pos;
			}
		}
		brdr.close();
		if (print) {
			for (int i=0;i<n;++i) {
		for (int j=0;j<m;++j) 
			if (board[i][j])
				System.out.print("#");
			else
				System.out.print(".");
		System.out.println();
		}
	} 
	System.out.println();  
		int ans=0;
		for (int i=0;i<n;++i)
			for (int j=0;j<m;++j)
					ans += ( board[i][j]) ? 1 : 0;
		return ans;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String SAMPLE="input/day13_sample.txt";
		String REAL="input/day13.txt";
		System.out.println(function(SAMPLE,1,false));
		System.out.println(function(SAMPLE,10000,true));
		System.out.println(function(REAL,1,false));
		System.out.println(function(REAL,1000,true));

	}

}
