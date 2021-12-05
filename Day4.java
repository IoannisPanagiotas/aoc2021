import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day4 {
	
	private static int readBoard(BufferedReader brdr,int[] numY,int[] numX)throws NumberFormatException, IOException  {
		String[] board=new String[5];
		brdr.readLine();
		board[0]=brdr.readLine();
		int sum=0;
		if (board[0]==null)
			return -1;
		for (int i=1;i<5;++i) 
			board[i]=brdr.readLine();
		for (int i=0;i<5;++i) {
			String[] numsInString = board[i].split(",");
			for (int j=0;j<5;++j) {
				int vij=Integer.parseInt(numsInString[j]);
				sum+=vij;
				numY[vij]=i;
				numX[vij]=j;
			}
		}
		return sum;
	}
	public static int  function(String fname,boolean boolSlowFast) throws NumberFormatException, IOException {
		BufferedReader  brdr=new BufferedReader(new FileReader(fname));
		String[] numsInString=brdr.readLine().split(",");
		int[] nums=new int[numsInString.length];
		for (int iter=0;iter<numsInString.length;++iter) 
			nums[iter]=Integer.parseInt(numsInString[iter]);
		int fastestOrSlowest=-1;
		int best_score=0;
		int[] numX=new int[100];
		int[] numY=new int[100];
		while (true) {
			for (int i=0;i<100;++i) {
				numX[i]=-1;
				numY[i]=-1;
			}
			int sum=readBoard(brdr,numY,numX);
			if (sum==-1)
				break;
			int[] sX=new int[5];
			int[] sY=new int[5];
			int unsum=0;
			int turn=0;
			for (Integer i : nums) {
				turn++;
				if  (numX[i]!=-1) {
					sX[numX[i]]++;
					sY[numY[i]]++;
					if (sX[numX[i]]==5 || sY[numY[i]]==5) {
						if (fastestOrSlowest==-1) {
							best_score=(sum-unsum-i)*i;
							fastestOrSlowest=turn;
						}else if (fastestOrSlowest > turn && boolSlowFast) {
							best_score=(sum-unsum-i)*i;
							fastestOrSlowest=turn;
						}else if (fastestOrSlowest < turn && !boolSlowFast) {
							best_score=(sum-unsum-i)*i;
							fastestOrSlowest=turn;
						}
						break;
					}
					unsum+=i;
				}
			}
				
			
		}
			return best_score;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		System.out.println(function("input/day4_sample.txt",true));
		System.out.println(function("input/day4.txt",true));
		///
		System.out.println(function("input/day4_sample.txt",false));
		System.out.println(function("input/day4.txt",false));


	}

}
