import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

class CountObject{
	public long[] freq;
	public CountObject() {
		freq=new long[26];
	}
	public void add(CountObject co2) {
		for (int i=0;i<26;++i)
			freq[i]+=co2.freq[i];
	}
}
public class Day14 {
	
	public static long function(String fname,int iters) throws IOException{
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String expression=brdr.readLine();
		HashMap<String,Character> rules=new HashMap<String,Character>();
		HashMap<String,Integer>   rulesID=new HashMap<String,Integer>();

		brdr.readLine();
		String ln;
		int nruleid=0;
		while ((ln=brdr.readLine())!=null) {
			String[] lns=ln.split(" -> ");
			rules.put(lns[0], lns[1].charAt(0));
			rulesID.put(lns[0],nruleid++);
		}
		CountObject[][] dp=new CountObject[rules.size()][iters+1];
		for (String rule : rules.keySet())
			dp[rulesID.get(rule)][0]=new CountObject();
		for (int iter=1;iter<=iters;++iter) {
				for (String rule: rules.keySet()) {
					String  part1="";
					part1+=rule.charAt(0);
					part1+=rules.get(rule);
					String part2="";
					part2+=rules.get(rule);
					part2+=rule.charAt(1);
					dp[rulesID.get(rule)][iter]=new CountObject();
					if (rules.containsKey(part1)) 
						dp[rulesID.get(rule)][iter].add(dp[rulesID.get(part1)][iter-1]);
					if (rules.containsKey(part2))
						dp[rulesID.get(rule)][iter].add(dp[rulesID.get(part2)][iter-1]);
					dp[rulesID.get(rule)][iter].freq[rules.get(rule)-'A']++;
				}
		}
		CountObject ans=new CountObject();
		for (int i=0;i<expression.length();++i) {
			ans.freq[expression.charAt(i)-'A']++;
			if (i!=expression.length()-1) {
				String temp="";
				temp+=expression.charAt(i);
				temp+=expression.charAt(i+1);
				if (rules.containsKey(temp)) 
					ans.add(dp[rulesID.get(temp)][iters]);
			}
		}
		long[] freq=ans.freq;
	
		Arrays.sort(freq);
		int k=0;
		while (freq[k]==0) k++;
		return freq[25]-freq[k];
	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String SAMPLE="input/day14_sample.txt";
		String REAL="input/day14.txt";
		System.out.println(function(SAMPLE,10));
		System.out.println(function(SAMPLE,40));
		System.out.println(function(REAL,10));
		System.out.println(function(REAL,40));
	}

}
