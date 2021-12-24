import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Day24 {
	public static long function(String fname,long[] w) throws IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		
		int wmet=0;
		long[] wxyz=new long[4];
		String ln="";
		int inpsread=0;
		while ((ln=brdr.readLine())!=null) {
			String[] lsn=ln.split(" ");
			char lsn2_0='a';
			if (lsn.length==3)
				lsn2_0=lsn[2].charAt(0);
			int lsn1=lsn[1].charAt(0)-'w';
			if (lsn[0].equals("inp")) 
				wxyz[lsn1]=w[wmet++];
			else if (lsn[0].equals("add")) 
				wxyz[lsn1] += (lsn2_0>='w' && lsn2_0<='z' ) ? wxyz[lsn2_0-'w'] : Integer.parseInt(lsn[2]);
			else if (lsn[0].equals("mul")) 
					wxyz[lsn1] *= (lsn2_0>='w' && lsn2_0<='z' ) ? wxyz[lsn2_0-'w'] : Integer.parseInt(lsn[2]);
			else if (lsn[0].equals("div")) 
					wxyz[lsn1] /= (lsn2_0>='w' && lsn2_0<='z' ) ? wxyz[lsn2_0-'w'] : Integer.parseInt(lsn[2]);
			else if (lsn[0].equals("mod")) 
					wxyz[lsn1] %= (lsn2_0>='w' && lsn2_0<='z' ) ? wxyz[lsn2_0-'w'] : Integer.parseInt(lsn[2]);
			else {
				long value=(lsn2_0>='w' && lsn2_0<='z' ) ? wxyz[lsn2_0-'w'] : Integer.parseInt(lsn[2]);
				wxyz[lsn1] = (wxyz[lsn1]==value) ? 1 : 0;
			}
		/*	System.out.print(ln+": ");
			for (int i=0;i<4;++i)
				System.out.print(wxyz[i]+" ");
			System.out.println(); */
		}
		return wxyz[3];
	}
	public static void readInput(String fname,long[] A,long[] B,long[] C) throws IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		for (int w=0;w<14;++w) {
			for (int i=0;i<4;++i)
				brdr.readLine();
			C[w]=Integer.parseInt(brdr.readLine().split(" ")[2]);

			A[w]=Integer.parseInt(brdr.readLine().split(" ")[2]);
			for (int i=0;i<9;++i)
				brdr.readLine();
			B[w]=Integer.parseInt(brdr.readLine().split(" ")[2]);
			brdr.readLine();
			brdr.readLine();
		}
	}
	public static  void recursiveSolution(int w,long[] A,long[] B,long[] C,long Z,String solution,int allowedRises) {
			if (w==14) {
				if (Z==0)
					System.out.println(solution+" "+Z+" "+allowedRises);
			}
			else {
				long OZ=Z;
				Z/=C[w];
				 for (long d=9;d>=1;--d) {
					 if (((OZ % 26) + A[w])==d) 
						 recursiveSolution(w+1,A,B,C,Z,solution+d,allowedRises);
					 else 
						 if (allowedRises>0) 
							 recursiveSolution(w+1,A,B,C,26*Z+B[w]+d,solution+d,allowedRises-1);
				}
			}
	}  
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		 * That was not a nice task
		 * txt just calls  a function 14 times with three possible alterations at each step
		 *  this leads to an update of Z as follows
		 *  		-Z[i] = Z[i-1] / C[i]  if (Z[i-1] % 26) + A[i]==w[i]  
		 *  		-Z[i] = Z[i-1/ C[i]  + 26*Z[i-1] + B[i] +W[i]   otherwise
		 *  
		 *  simulate this procedure cutting when applicable
		 */
		String REAL="input/day24.txt";
		long[] A=new long[14];
		long[] B=new long[14];
		long[] C=new long[14];

		readInput(REAL,A,B,C);
		for (int i=0;i<14;++i)
			System.out.print(C[i]+" ");
		System.out.println();
		recursiveSolution(0,A,B,C,0,"",7); 
		//there only seven divisions by 26 
		//B[i] + W[i] are always positive  so only division can bring Z[i] closer to 0
		//if we multiply by 26 more than 7 times, we can never end up to 0
	}

}
