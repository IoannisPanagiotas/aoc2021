import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Day1 {

	
	static int sliding_window_increasing(String fname,int K) throws NumberFormatException, IOException{
		int answer=0;
		long[] prev=new long[K];
		long prev_sum=0;
		int index_to_rem=0;
		int index=0;
		String ln;
		BufferedReader  brdr=new BufferedReader(new FileReader(fname));
		while ((ln=brdr.readLine())!=null) {
				long lval=Long.parseLong(ln);
				if (index<K) {
					prev[index++]=lval;
					prev_sum+=lval;
				}else {
					long curr_sum=prev_sum - prev[index_to_rem] + lval;
					if (curr_sum >prev_sum)
						answer++;
					prev_sum=curr_sum;
					prev[index_to_rem++]=lval;
					if (index_to_rem==K) index_to_rem=0;
				}
		}
		return answer;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		System.out.println(sliding_window_increasing("input/day1_sample.txt",1));
		System.out.println(sliding_window_increasing("input/day1_sample.txt",3));
		//
		System.out.println(sliding_window_increasing("input/day1.txt",1));
		System.out.println(sliding_window_increasing("input/day1.txt",3));

		
	}

}
