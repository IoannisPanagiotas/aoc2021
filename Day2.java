import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day2 {
	
	
	public static int directions_aim(String fname) throws NumberFormatException, IOException{
		int X=0;
		int Y=0;
		int aim=0;
		BufferedReader  brdr=new BufferedReader(new FileReader(fname));
		String ln;
		while ((ln=brdr.readLine())!=null) {
			String[] command=ln.split(" ");
			int mv=Integer.parseInt(command[1]);
			if (command[0].equals("forward")) 
			{	
				X+=mv;
				Y+=(mv*aim);
			}
			else if (command[0].equals("up"))
				aim-=mv;
			else if (command[0].equals("down"))
				aim+=mv;
		}
		return Y*X;
}
	public static int directions(String fname) throws NumberFormatException, IOException{
			int X=0;
			int Y=0;
			BufferedReader  brdr=new BufferedReader(new FileReader(fname));
			String ln;
			while ((ln=brdr.readLine())!=null) {
				String[] command=ln.split(" ");
				int mv=Integer.parseInt(command[1]);
				if (command[0].equals("forward")) 
					X+=mv;
				else if (command[0].equals("up"))
					Y-=mv;
				else if (command[0].equals("down"))
					Y+=mv;
			}
			return Y*X;
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		System.out.println(directions("input/day2_sample.txt"));
		System.out.println(directions("input/day2.txt"));
		//
		System.out.println(directions_aim("input/day2_sample.txt"));
		System.out.println(directions_aim("input/day2.txt"));

	}

}
