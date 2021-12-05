import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Day5 {
	public static int function(String fname, int boardSize,boolean straightLines) throws NumberFormatException, IOException {
		BufferedReader  brdr=new BufferedReader(new FileReader(fname)); 
		int[][] board=new int[boardSize+1][boardSize+1];
		int ans=0;
		String ln;
		while ((ln=brdr.readLine())!=null) {
			String[] numsString=ln.replace(" -> ", ",").split(",");
			int[] nums=Arrays.stream(numsString).mapToInt((s) -> Integer.parseInt(s)).toArray();
			int[] currend= {nums[0],nums[2],nums[1],nums[3]};
			boolean btp=true;
			boolean isStraight= (currend[0]==currend[1]) || (currend[2]==currend[3]);
			if (isStraight && currend[0]==currend[1]) {
					currend[0]=nums[1];
					currend[1]=nums[3];
					currend[2]=nums[0]; //no end2 needed in straight-line only
					btp=false;
			}
			if (!isStraight && straightLines)
				continue;
			boolean metAndSurpassed=false;
			while (!metAndSurpassed) {
				if (currend[0]==currend[1])
					metAndSurpassed=true;
				if (btp) 
					ans += (++board[currend[0]][currend[2]]==2) ? 1 : 0;
				else 
					ans += (++board[currend[2]][currend[0]]==2) ? 1 : 0;
				currend[0]+=  (currend[0] < currend[1]) ? 1 : -1;			
				if (!isStraight) 
					currend[2]+=  (currend[2] < currend[3]) ? 1 : -1;			
			}
		}
		return ans;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		System.out.println(function("input/day5_sample.txt",100,true));
		System.out.println(function("input/day5.txt",1000,true));
		//
		System.out.println(function("input/day5_sample.txt",100,false));
		System.out.println(function("input/day5.txt",1000,false));

	}

}
