import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

abstract class Packet{
	public int ID;
	public int version;
	public Packet(int version,int ID) {
		this.version=version;
		this.ID=ID;
	}
	public boolean isLiteral() {
		return ID==4;
	}
	abstract public int getVersionSum();
	abstract public long getValue();

}
class Literal extends Packet{
	long value;
	public Literal(int version,int ID,long value) {
		super(version,ID);
		this.value=value;
	}

	@Override
	public int getVersionSum() {
		return version;
	}

	@Override
	public long getValue() {
		return value;
	}
}
class Composite extends Packet{
	public ArrayList<Packet> packets;
	public Composite(int version, int ID) {
		super(version, ID);
		packets=new ArrayList<Packet>();
	}
	public void addPacket(Packet pid) {
		packets.add(pid);
	}
	@Override
	public int getVersionSum() {
		int sumVersions=version;
		for (Packet p : packets)
			sumVersions+=p.getVersionSum();
		return sumVersions;
	}

	@Override
	public long getValue() {
		if (ID<=3) {
			long ans=packets.get(0).getValue();
			for (int i=1;i<packets.size();++i) {
				if (ID==0) ans+=packets.get(i).getValue();
				if (ID==1) ans*=packets.get(i).getValue();
				if (ID==2) ans=Math.min(ans,packets.get(i).getValue());
				if (ID==3) ans=Math.max(ans,packets.get(i).getValue());
			}
			return ans;
		}else {
			if (ID==5) return (packets.get(0).getValue() > packets.get(1).getValue()) ? 1 : 0;
			if (ID==6) return (packets.get(0).getValue() < packets.get(1).getValue()) ? 1 : 0;
			if (ID==7) return (packets.get(0).getValue() == packets.get(1).getValue()) ? 1 : 0;

		}
				
			
		return 0;
	}
	
}

public class Day17 {
	public static String binaryOfValue(int value,boolean firstIsOne) {
		String sval=Integer.toBinaryString(value);
		String ans="";
		String firstDigit = (firstIsOne) ? "1" : "0";
		while (ans.length()+sval.length()!=4) 
			ans+=  (ans.length()==0) ?  firstDigit : "0";
		return ans+=sval;
	}
	public static HashMap<Character,String> hmap=new HashMap<Character,String>();
	public static void initfill() {
		for (int i=0;i<10;++i) 
			hmap.put(Integer.toString(i).charAt(0),binaryOfValue(i,false));
		for (Character c='A'; c<='F';++c) 
			hmap.put(c,binaryOfValue(c-'A' + 2 ,true));
	}
	public static long readLiteral(String ln, AtomicInteger startingPos) {
		String lstringval="";
		boolean isLast=false;
		while (!isLast) {
			isLast=ln.charAt(startingPos.intValue())=='0';
			String ssub=ln.substring(startingPos.intValue()+1,startingPos.intValue()+5);
			lstringval+=ssub;
			startingPos.addAndGet(5);
		}
		return Long.parseLong(lstringval,2);
	}
	public static Packet parsePacket(String ln,AtomicInteger startingPos) {

		String version=ln.substring(startingPos.intValue(), startingPos.intValue()+3);
		String ID=ln.substring(startingPos.intValue()+3, startingPos.intValue()+6);
		startingPos.addAndGet(6);

		if (Integer.parseInt(ID,2)==4) { //4 (Literal)
			long value=readLiteral(ln,startingPos);
			return new Literal(Integer.parseInt(version,2),Integer.parseInt(ID,2),value); 
		}else {
			Composite cpacket=new Composite(Integer.parseInt(version,2),Integer.parseInt(ID,2));
			char bit=ln.charAt(startingPos.getAndIncrement());
			int nparts=0;
			int nlength=0;
			if (bit=='1') 
				nparts=Integer.parseInt(ln.substring(startingPos.intValue(),startingPos.addAndGet(11)),2);
			else 
				nlength=Integer.parseInt(ln.substring(startingPos.intValue(),startingPos.addAndGet(15)),2);
			while (true) { 
				int curr=startingPos.intValue();
				cpacket.addPacket(parsePacket(ln,startingPos));
				if (bit=='1') {
					nparts--;
					if (nparts==0) 
						break;
				}else {
					nlength-=(startingPos.intValue()-curr);
					curr=startingPos.intValue();
					if (nlength==0)
						break;
				}
			}
			return cpacket;
		}
	}
	public  static long function(String fname,boolean wantValue) throws IOException{
			BufferedReader brdr=new BufferedReader(new FileReader(fname));
			String ln=brdr.readLine();
			String  ln2="";
			for (char c : ln.toCharArray()) 
				ln2+=hmap.get(c);
			
			return  wantValue ? 
								  parsePacket(ln2,new AtomicInteger(0)).getValue()
								: parsePacket(ln2,new AtomicInteger(0)).getVersionSum();
			
	}
	public static void main(String[] args)  throws IOException{
		// TODO Auto-generated method stub
		String SAMPLE="input/day16_sample.txt";
		String REAL="input/day16.txt";

		initfill();
	
		System.out.println(function(SAMPLE,false));
		System.out.println(function(REAL,false));
		System.out.println(function(SAMPLE,true));
		System.out.println(function(REAL,true));
	}

}
