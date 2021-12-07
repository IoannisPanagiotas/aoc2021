import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Day7 {
	
	public static int function(String fname) throws NumberFormatException, IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String[] numsInString=brdr.readLine().split(",");	
		int[] nums=Arrays.stream(numsInString).mapToInt((s) -> Integer.parseInt(s)).toArray();
		int sum=Arrays.stream(nums).reduce(0,((s, e)-> s + e)); //answer for position 0 btw
		int ans=sum;
		int right=sum;
		int nright=nums.length;
		int left=0;
		int nleft=0;
		Arrays.sort(nums);
		for (int i=0;i<nums.length;++i) {
			int prev= (i==0) ? 0 : nums[i-1];
			sum+=(nums[i]-prev) * (nleft-nright);
			nright--;
			nleft++;
			ans = (ans > sum) ? sum : ans;
		}
		return ans;
	}
	public static long function2(String fname) throws NumberFormatException, IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String[] numsInString=brdr.readLine().split(",");	
		long[] nums=Arrays.stream(numsInString).mapToLong((s) -> Integer.parseInt(s)).toArray();
		long sumSq=Arrays.stream(nums).reduce(0,((s, e)-> s + (e*(e+1)/2))); //answer for position 0 btw
		long sum=Arrays.stream(nums).reduce(0, ((s, e)-> s+e));
		long ans=sumSq;
		long curr=ans;
		long right=sum; //  keeps sum (d(a_i ,  pos)) for all i such that a_i > pos 
		int nright=nums.length;
		long max=Arrays.stream(nums).max().getAsLong();
		long left=0; //defined as right 
		int nleft=0;
		int n=nums.length;
		Arrays.sort(nums);
	
		
		while (nums[nleft]==0) { nleft++; nright--;}
		for (long i=1;i<=max;++i) {
			int eq=0;
			while (nums[nleft] <= i) {
				nleft++;
				nright--;
				right--;
				curr--;
				eq++;
				if (nleft==n) break;
			}
			
			curr-= (right);
			curr+= (left+(nleft-eq));
			right-= nright;
			left+=(nleft-eq);

			if (curr < ans)
				ans=curr;
			
		}
	
		
		
		return ans;
	}
	
	public static long part2_bruteforce(String fname)  throws NumberFormatException, IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String[] numsInString=brdr.readLine().split(",");	
		long[] nums=Arrays.stream(numsInString).mapToLong((s) -> Integer.parseInt(s)).toArray();
		long n=nums.length;
		long ans=Long.MAX_VALUE;
		long min=0;
		long max=Arrays.stream(nums).max().getAsLong();
		for (long i=min;i<=max;++i) {
			int curr=0;
			for (int j=0;j<n;++j) {
					if (nums[j] > i)
						curr += ((nums[j] -i)*(nums[j]-i+1))/2;
					else
						curr += ((i -nums[j])*(i-nums[j]+1))/2;
			}
			if (curr < ans)
				ans=curr;
		}
		return ans;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		String SAMPLE="input/day7_sample.txt";
		String REAL="input/day7.txt";
		System.out.println(function(SAMPLE));
		System.out.println(function(REAL));
	System.out.println(function2(SAMPLE)+" "+(function2(SAMPLE)-part2_bruteforce(SAMPLE)));
	System.out.println(function2(REAL) +" "+ (function2(REAL)-part2_bruteforce(REAL)));

	}
}
