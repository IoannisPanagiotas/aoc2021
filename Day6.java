import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.IntStream;

public class Day6 {

	public static long function(String fname,int days) throws NumberFormatException, IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		long[][] dp=new long[days+1][9];
		String[] nums=brdr.readLine().split(",");
		for (String st : nums)
			dp[0][Integer.parseInt(st)]++;
		for (int i=1;i<=days;++i) {
			for (int j=0;j<=7;++j) {
				dp[i][j]=dp[i-1][j+1];
			}
			dp[i][6]+=dp[i-1][0];
			dp[i][8]=dp[i-1][0];
		}
		long sum=IntStream.range(0,9).mapToLong(i -> dp[days][i]).sum();
		return sum;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		System.out.println(function("input/day6_sample.txt",18));
		System.out.println(function("input/day6_sample.txt",80));
		System.out.println(function("input/day6.txt",80));
		//
		System.out.println(function("input/day6_sample.txt",256));
		System.out.println(function("input/day6.txt",256));
	}

}
