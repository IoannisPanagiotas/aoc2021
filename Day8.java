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
		HashMap<Integer,String> hmap=new HashMap<Integer,String>();
		hmap.put(1,lines[0]);
		hmap.put(7,lines[1]);
		hmap.put(4, lines[2]);
		hmap.put(8, lines[9]);
		String a="";
		String b="";
		String c="";
		String d="";
		String e="";
		String f="";
		String g="";
		c=lines[0];
		f=lines[1];
		for (int i=0;i<3;++i) {
			if (lines[1].charAt(i)!= lines[0].charAt(0) && lines[1].charAt(i)!= lines[0].charAt(1))
				a+=lines[1].charAt(i);
		}
		for (int i=0;i<4;++i) {
			if (lines[2].charAt(i)!=c.charAt(0) && lines[2].charAt(i)!=c.charAt(1)) {
				b+=lines[2].charAt(i);
				d+=lines[2].charAt(i);
			}
			
		}
		for (int i=3;i<=5;++i) {
			int count_bc=0;
			for (Character w : lines[i].toCharArray())
				if (w==b.charAt(0) || w==b.charAt(1))
					count_bc++;
			if (count_bc==2) {
				hmap.put(5, lines[i]);
			}else {
				int count_cf=0;
				for (Character w : lines[i].toCharArray())
					if (w==c.charAt(0) || w==c.charAt(1))
					count_cf++;
				if (count_cf==2) {
					hmap.put(3,lines[i]);
				}else {
					hmap.put(2, lines[i]);
				}
			}
		}
		
		for (int i=6;i<=8;++i) {
			int common_cf=0;
			int common_bd=0;
			for (Character w : lines[i].toCharArray()) {
				if (w ==c.charAt(0) || w==c.charAt(1))
					common_cf++;
				if (w ==b.charAt(0) || w==d.charAt(1))
					common_bd++;
			}
			if (common_cf==2 && common_bd==2) {
				hmap.put(9,lines[i]);
			}else if (common_cf==2) {
				hmap.put(0, lines[i]);
			}else
				hmap.put(6,lines[i]);
	
		}		
		 HashMap<String,Integer> hmap2=new HashMap<String,Integer>();
		 for (int i=0;i<=9;++i) {
			 hmap2.put(sort(hmap.get(i)), i);
			// System.out.println(i+" "+hmap.get(i));
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
		//System.out.println(function(SAMPLE));
		//System.out.println(function(REAL));
		//
		System.out.println(function2(SAMPLE));
		System.out.println(function2(REAL));
	}

}
