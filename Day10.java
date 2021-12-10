import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class Day10 {
	
	public static long function(String fname,boolean alsoComplete) throws IOException{
		HashMap<Character,Integer> mappingStart=new HashMap<Character,Integer>();
		HashMap<Character,Integer> mappingEnd=new HashMap<Character,Integer>();

		int[] corruptScore= {3,57,1197,25137};
		int[] completeScore= {1,2,3,4};
		char[] openings= {'(','[','{','<'};
		char[] ends= {')',']','}','>'};
		for (int i=0;i<4;++i) {
			mappingStart.put(openings[i],i);
			mappingEnd.put(ends[i],i);
		}
		
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln;
		int corruptedAns=0;
		ArrayList<Long> completeScores=new ArrayList<Long>();
		
		while ((ln=brdr.readLine())!=null) {
			Stack<Character> s=new Stack<Character>();
			boolean corrupted=false;
			
			for (int i=0;i<ln.length();++i) {
				char c=ln.charAt(i);
				if (mappingStart.containsKey(c)) 
					s.push(c);
				else {
					if (mappingEnd.containsKey(c) && openings[mappingEnd.get(c)]!=s.peek()) {
						corruptedAns+=corruptScore[mappingEnd.get(c)];
						corrupted=true;
						break;
					}else 
						s.pop();
				}
				
			}
			
			if (alsoComplete && !corrupted) {
				long completeAns=0;
				while (!s.empty())
					completeAns=5*completeAns + completeScore[mappingStart.get(s.pop())];
				completeScores.add(completeAns);
			}
			
		}
		
		if (alsoComplete) {
			Collections.sort(completeScores);
			return  completeScores.get(completeScores.size()/2 );
		}
		return  corruptedAns;

	}
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String SAMPLE="input/day10_sample.txt";
		String REAL="input/day10.txt";

		System.out.println(function(SAMPLE,false));
		System.out.println(function(REAL,false));
		
		System.out.println(function(SAMPLE,true));
		System.out.println(function(REAL,true));

	}

}
