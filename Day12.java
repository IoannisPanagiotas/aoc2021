import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class SearchState{
	public  int position;
	public 	int bitmap;
	public int bitmap2;
	public int steps;
	public SearchState(int position,int bitmap,int bitmap2,int steps) {
		this.position=position;
		this.bitmap=bitmap;
		this.steps=steps;
		this.bitmap2=bitmap2;
	}
	 public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        SearchState ssobj = (SearchState) o;

	        return position == ssobj.position && bitmap == ssobj.bitmap  && steps==ssobj.steps;
	    }

	    public int hashCode() {
	        int result = position;
	        result = 31 * position + 13*bitmap + 7*bitmap2  + steps;
	        return result;
	    }

}
public class Day12 {
	
	private static ArrayList<ArrayList<Integer>> edges;
	private static HashMap<String, Integer> map;
	private static HashSet<Integer> small;
	private static HashMap<SearchState,Integer> stateCounts;
	private static  int start;
	private static int end;
	private static SearchState nextSearchSteap(boolean part1and2,int u,SearchState old) {
		if (!small.contains(u))
			return new SearchState(u,old.bitmap,old.bitmap2,old.steps+1);
		if (part1and2) 
			if ((old.bitmap & (1<<u))==0) 
				return new SearchState(u,old.bitmap | (1<< u), old.bitmap2,old.steps+1);
			else 
				return new SearchState(u,old.bitmap, old.bitmap2  | (1<< u),old.steps+1);
		else 
			return new SearchState(u,old.bitmap | (1<< u), old.bitmap2,old.steps+1);
		
	}
	private static boolean isMoveValid(boolean part1and2,SearchState old,int u) {
		if (!small.contains(u))
			return true;
		if (part1and2) 
			return ((old.bitmap & (1 << u)) ==0) || (old.bitmap2==0);
		else 
			return ((old.bitmap & (1<< u)) ==0);
	}
	public static void readGraph(String fname)  throws IOException{
		edges=new ArrayList<ArrayList<Integer>>();
		map=new HashMap<String,Integer>();
		small=new HashSet<Integer>();
		stateCounts=new HashMap<SearchState,Integer>();
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln;
		while ((ln=brdr.readLine())!=null) {
			String[] args=ln.split("-");
			int key0;
			int key1;
			if (!map.containsKey(args[0])) {
				map.put(args[0], edges.size());
				if (args[0].toLowerCase().equals(args[0]))
					small.add(edges.size());
				if (args[0].equals("start"))
						start=edges.size();
				if (args[0].equals("end"))
					end=edges.size();
				edges.add(new ArrayList<Integer>());
			
			}
			if (!map.containsKey(args[1])) {
				map.put(args[1], edges.size());
				if (args[1].toLowerCase().equals(args[1]))
					small.add(edges.size());
				if (args[1].equals("start"))
					start=edges.size();
				if(args[1].equals("end"))
					end=edges.size();
				edges.add(new ArrayList<Integer>());
			}
			key0=map.get(args[0]);
			key1=map.get(args[1]);
			edges.get(key0).add(key1);
			edges.get(key1).add(key0);
			
		}
	}

	private static void clear() {
		stateCounts.clear();
	}
	public static int function(boolean part1and2) {
		int ans=0;
		ArrayList<SearchState> queue=new ArrayList<SearchState>();
		int cpos=0;
		int pos=1;
		clear();
		queue.add(new SearchState(start,1 << start,0 ,1));
		stateCounts.put(new SearchState(start,1 << start,0,1),1);
		while (cpos<pos) {
			SearchState currState=queue.get(cpos++);
			int u=currState.position;
			if (u!=end) {
				for (Integer  v : edges.get(u)) {
						if (v==start)
							continue;
						if  (isMoveValid(part1and2,currState,v)) {
							SearchState state2=nextSearchSteap(part1and2,v,currState);
							if (stateCounts.containsKey(state2)) {
								int incr=stateCounts.get(state2) + stateCounts.get(currState);
								stateCounts.put(state2,incr);
							}else {
								stateCounts.put(state2,stateCounts.get(currState));
								queue.add(state2);
								pos++;
								
							}
						}
		
				}
			}else {
				ans+=stateCounts.get(currState);
			}
		}
		
		return ans;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String SAMPLE="input/day12_sample.txt";
		String REAL="input/day12.txt";
		readGraph(SAMPLE);
		System.out.println(function(false));
		System.out.println(function(true));
		
		readGraph(REAL);
		System.out.println(function(false));
		System.out.println(function(true));

		
	}

}
