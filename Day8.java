import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Day8 {
	public static String sort(String s) {
		char charArray[] = s.toCharArray();
	      Arrays.sort(charArray);
	      return new String(charArray);
	}
	private static String exclude(String a, String b) {
		String diff="";
		for (int i=0;i<b.length();++i) {
			boolean add=true;
			for (int j=0;j<a.length();++j) {
				if (b.charAt(i)==a.charAt(j)) {
					add=false;
					break;
				}
			}
			if (add)
				diff+=b.charAt(i);
		}
		return diff;
	}
	private  static int intersect(String a, String b) {
		int ans=0;
		for (int i=0;i<b.length();++i) {
			boolean add=false;
			for (int j=0;j<a.length();++j) {
				if (b.charAt(i)==a.charAt(j)) {
					add=true;
					break;
				}
			}
			if (add)
				ans++;
		}
		return ans;
	}
	public static HashMap<String,Integer> decode(String[] lines) {
		
		/*
		 * 0 : 6
	1 : 2 
	2 : 5
	3 : 5
	4 : 4

	5 : 5
6 : 6
	7 : 3
	8 : 7
9 : 6
		 */
		/*
		 * 1 gives c,f
	7 gives a
	4 gives b,d
	5 is the only one that uses  both b,d with 5 lenth
	3 is the only one that uses 	c,f with 5 length
	2 is the rest
	9 has cf and bd
		 */
		Arrays.sort(lines,Comparator.comparingDouble(s-> s.length()));
		String[] mymap=new String[10];
		mymap[1]=lines[0];
		mymap[7]=lines[1];
		mymap[4]=lines[2];
		mymap[8]=lines[9];	
		String bd="";
		String cf="";

		cf=lines[0];
		bd=exclude(cf,lines[2]);
	
		for (int i=3;i<=5;++i) {
			int count_bd=intersect(bd,lines[i]);
			if (count_bd==2) {
				mymap[5]=lines[i];
			}else {
				int count_cf=intersect(cf,lines[i]);
				if (count_cf==2) {
					mymap[3]=lines[i];
				}else {
					mymap[2]=lines[i];
				}
			}
		}
		
		for (int i=6;i<=8;++i) {
			int common_cf=intersect(cf,lines[i]);
			int common_bd=intersect(bd,lines[i]);
			if (common_cf==2 && common_bd==2) {
				mymap[9]=lines[i];
			}else if (common_cf==2) {
				mymap[0]=lines[i];
			}else
				mymap[6]=lines[i];
	
		}		
		 HashMap<String,Integer> hmap2=new HashMap<String,Integer>();
		 for (int i=0;i<=9;++i) {
			 hmap2.put(sort(mymap[i]), i);
		 }
		 return hmap2;
		}
	public static int function(String fname) throws NumberFormatException, IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln;
		int ans=0;
		while ((ln=brdr.readLine())!=null) {
		
			String[] RIGHT=ln.split(" @ ")[1].split(" ");
			ans+=Arrays.stream(RIGHT).filter((s) -> (s.length() ==2 || s.length()==3 || s.length()==4 || s.length()==7)).count();
		}
		return ans;
	}
	public static long function2(String fname) throws NumberFormatException, IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln;
		long ans=0;
		while ((ln=brdr.readLine())!=null) {
			String[] LEFT=ln.split(" @ ")[0].split(" ");
			 HashMap<String,Integer> hmap=decode(LEFT);
			String[] RIGHT=ln.split(" @ ")[1].split(" ");
			long num=0;
			for (String st : RIGHT) {
				num*=10;
				num+=hmap.get(sort(st));
			}
			ans+=num;
		}
		return ans;
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		String SAMPLE="input/day8_sample.txt";
		String REAL="input/day8.txt";
		System.out.println(function(SAMPLE));
		System.out.println(function(REAL));
		//
		System.out.println(function2(SAMPLE));
		System.out.println(function2(REAL));
	}

}
