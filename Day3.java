import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day3 {

	
	public static long  function(String fname,int bits) throws NumberFormatException, IOException {
		BufferedReader  brdr=new BufferedReader(new FileReader(fname));
		String ln;
		int[] ones=new int[bits];
		int n=0;
		while ((ln=brdr.readLine())!=null) {
			int j=0;
			n++;
			for (Character c : ln.toCharArray() ) {
				if (c=='1') 
					ones[j]++;
				j++;
			}
		}
		long gamma=0;
		long epsilon=0;
		long power=1;;
		for (int i=bits-1;i>=0;--i) { 
			if (ones[i] >= n/2 ) 
				gamma+=power;
			else
				epsilon+=power;
			power*=2;
			
		}
		return gamma*epsilon;
		
	}
	private static long typepopular(ArrayList<String> LIST,int bit,int type) {
		ArrayList<String> ONE_LIST=new ArrayList<String>();
		ArrayList<String> ZERO_LIST=new ArrayList<String>();
		if (LIST.size()==1) 
			return Long.parseLong(LIST.get(0),2);
		for (String st : LIST) {
			if (st.charAt(bit)=='1')
				ONE_LIST.add(st);
			else
				ZERO_LIST.add(st);
		}
			if (ONE_LIST.size() >= ZERO_LIST.size())
				return (type==1) ?  typepopular(ONE_LIST,bit+1,type) :
									typepopular(ZERO_LIST,bit+1,type);
				
			return 		(type==1) ?	typepopular(ZERO_LIST,bit+1,type) :
									typepopular(ONE_LIST,bit+1,type);		
		
	}
	public static long  function2(String fname,int bits) throws NumberFormatException, IOException {
		BufferedReader  brdr=new BufferedReader(new FileReader(fname));
		String ln;
		ArrayList<String> LIST=new ArrayList<String>();
		while ((ln=brdr.readLine())!=null)
				LIST.add(ln);
		return typepopular(LIST,0,1) * typepopular(LIST,0,0);
	
	
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		System.out.println(function("input/day3_sample.txt",5));
		System.out.println(function("input/day3.txt",12));
		///
		System.out.println(function2("input/day3_sample.txt",5));
		System.out.println(function2("input/day3.txt",12));

	}

}
