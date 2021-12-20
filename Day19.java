import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class Beacon {
	
	public int[] xyz;
	@Override
	public boolean equals(Object o) {
		 if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        Beacon bo = (Beacon) o;

	        return xyz[0]==bo.xyz[0] && xyz[1]==bo.xyz[1] && xyz[2]==bo.xyz[2];
	    }

	    public int hashCode() {
	        return 31 * xyz[0] + 13*xyz[1] + 7*xyz[2];
	    }
	    public Beacon() {
			xyz=new int[3];
	    }
	    public Beacon(int[] xyz) {
	    	this.xyz=new int[3];
	    	for (int i=0;i<3;++i)
	    		this.xyz[i]=xyz[i];
	    }
	
}
class Scanner{
	public ArrayList<Beacon> beacons;
	public int[] rxyz;
	public int[] permutation;
	public int[] orientation;
	public Scanner() {
		beacons=new ArrayList<Beacon>();
	}
	public void addBeacon(Beacon beacon) {
		beacons.add(beacon);
	}
	public void finalizePermutation(int[] permutation) {
		this.permutation=new int[3];
		for (int i=0;i<3;++i)
			this.permutation[i]=permutation[i];
	}
	public void finalizeOrientation(int[] orientation) {
		this.orientation=new int[3];
		for (int i=0;i<3;++i)
			this.orientation[i]=orientation[i];
	}
	public void finalizeScannerPosition(int[] rxyz) {
		this.rxyz=new int[3];
		for (int i=0;i<3;++i)
			this.rxyz[i]=rxyz[i];
	
	}
	public Beacon getBeacon(int i) {
		return beacons.get(i);
	}
	public Beacon getActualBeacon(int j) {
		Beacon beacon=new Beacon(); 
		for (int i=0;i<3;++i)
			beacon.xyz[i]= rxyz[i] + beacons.get(j).xyz[permutation[i]]*orientation[i];
		return beacon;
	}
	
}

public class Day19 {
	public static void getAllPermsAndOrientasions(int[][] permutation,int[][] orientation) {
		int[] ptrs=new int[6];
		int permId=0;
		for ( ptrs[0]=0;ptrs[0]<=2;++ptrs[0]) {
			for ( ptrs[1]=0;ptrs[1]<=2;++ptrs[1]) {
				for ( ptrs[2]=0;ptrs[2]<=2;++ptrs[2]) {
					if (ptrs[0]!=ptrs[1] && ptrs[1]!=ptrs[2] && ptrs[0]!=ptrs[2]) {
						for ( ptrs[3]=-1;ptrs[3]<=1;ptrs[3]+=2) {
							for (ptrs[4]=-1;ptrs[4]<=1;ptrs[4]+=2) {
								for (ptrs[5]=-1;ptrs[5]<=1;ptrs[5]+=2) {
									for (int j=0;j<6;++j)
										if (j<3)
											permutation[permId][j]=ptrs[j];
										else
											orientation[permId][j-3]=ptrs[j];
									permId++;
								}
							}
						}
					}
				}
			}
		}
	}
	public static Beacon applyBeaconTransformation(Beacon beacon,int[] rxyz,int[] permutation,int[] orientation) {
		Beacon beacon2=new Beacon();
		for (int i=0;i<3;++i)
			beacon2.xyz[i]= rxyz[i] + beacon.xyz[permutation[i]]*orientation[i];
		return beacon2;
	}
	public static int[] getStartingPosition(Beacon bknown,Beacon bunknown,int[] perm,int[] orientation) {
		int[] sxyz=new int[3];
		for (int i=0;i<3;++i)
			sxyz[i]= bknown.xyz[i] - bunknown.xyz[perm[i]]*orientation[i];
		return sxyz;
	}
	public static int countOlaps(Scanner sknown,Scanner sunknown,HashSet<Beacon> hashOfKnown,Beacon xyz,int[] perm,int[] orientation) {
		/*
		 * idea to improve on super brute force maybe be something like
		 *   pick some points form one side, pick some from the other side
		 *   solve a linear system to find startingposition and orientation 
		 *   but i am not going to dwell that much :P 
		 */
		for (int i=0;i<sknown.beacons.size();++i) {
			for (int j=0;j<sunknown.beacons.size();++j) { //assume i-th of known and j-th of unknown are same
				int[] sxyz=getStartingPosition(sknown.getActualBeacon(i),sunknown.getBeacon(j),perm,orientation);
				int overlaps=0;
				for (int z=0;z<sunknown.beacons.size();++z) {
					Beacon transformedBeacon=applyBeaconTransformation(sunknown.getBeacon(z),sxyz,perm,orientation);
					if (hashOfKnown.contains(transformedBeacon))
						overlaps++;
					if(overlaps + sunknown.beacons.size()-z < 13)
						break;
					if (overlaps==12) {
						for (int w=0;w<3;++w)
							xyz.xyz[w]=sxyz[w];
						return overlaps;
					}
				}
				
			}
		}
		return 0;
	} 
	public static ArrayList<Scanner> readScannerData(String fname) throws IOException{
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln="";
		ArrayList<Scanner> scanners=new ArrayList<Scanner>();
		int id=-1;
		while ((ln=brdr.readLine())!=null) {
			if (ln.equals(""))
				continue;
			else if (ln.charAt(0)=='-' && ln.charAt(1)=='-') {
				scanners.add(new Scanner());
				id++;
			}
			else {
				String[] lns=ln.split(",");
				int[] bxyz= {Integer.parseInt(lns[0]),Integer.parseInt(lns[1]),Integer.parseInt(lns[2])};
				scanners.get(id).addBeacon(new Beacon(bxyz));
			}
		}
		return scanners;
	}
	public static boolean distanceIsValid(int a,int b) {
		return Math.abs(a-b) <=1000;
	}
	public static boolean ensureValidityOfScanner(Scanner scn) {
		for (int i=0;i<scn.beacons.size();++i) {
			Beacon nextBeacon=scn.getActualBeacon(i);
			for (int j=0;j<3;++j) {
				if (!distanceIsValid(scn.rxyz[j],nextBeacon.xyz[j]))
						return false;
			}
		}
		return true;
	}
	public static HashSet<Beacon>  function(ArrayList<Scanner> scanners) {
		int[][] perms=new int[48][3];
		int[][] orients=new int[48][3];
		getAllPermsAndOrientasions(perms,orients);
		HashSet<Beacon> beacons=new HashSet<Beacon>();
		ArrayList<Integer> knownScanners=new ArrayList<Integer>();
		HashSet<Integer> unknownScanners=new HashSet<Integer>();
		int pos=0;
		scanners.get(0).finalizeOrientation(new int[] {1,1,1});
		scanners.get(0).finalizePermutation(new int[] {0,1,2});
		scanners.get(0).finalizeScannerPosition(new int[]{0,0,0});
		knownScanners.add(0); 
		boolean[] identified=new boolean[scanners.size()];
		identified[0]=true;
		for (Beacon bc : scanners.get(0).beacons)
			beacons.add(bc);
		for (int i=1;i<scanners.size();++i)
			unknownScanners.add(i);
		while (!unknownScanners.isEmpty()) {
			ArrayList<Integer> toRemove=new ArrayList<Integer>();
			Scanner nextKnown=scanners.get(knownScanners.get(pos++));
			HashSet<Beacon> hashOfKnown=new HashSet<Beacon>();
			for (int i=0;i<nextKnown.beacons.size();++i) {
				hashOfKnown.add(nextKnown.getActualBeacon(i));
			}
			for (Integer sci : unknownScanners) {
				if (identified[sci])
					continue;
				for (int permId=0;permId<48;++permId) {
					Beacon sbeacon=new Beacon();
					int overlaps=countOlaps(nextKnown,scanners.get(sci),hashOfKnown,sbeacon,perms[permId],orients[permId]);
					if (overlaps>=12) {
						scanners.get(sci).finalizeOrientation(orients[permId]);
						scanners.get(sci).finalizePermutation(perms[permId]);
						scanners.get(sci).finalizeScannerPosition(sbeacon.xyz);
						if (ensureValidityOfScanner(scanners.get(sci))) {
						//	System.out.println("Scanner "+sci+": "+sbeacon.xyz[0]+" "+sbeacon.xyz[1]+" "+sbeacon.xyz[2]);
							for (int bci=0;bci<scanners.get(sci).beacons.size();++bci){
								Beacon actualBeacon=scanners.get(sci).getActualBeacon(bci);
								
								beacons.add(actualBeacon);
							}
							toRemove.add(sci);
							knownScanners.add(sci); 
							identified[sci]=true;
							break;
						}

					}
				}
			}
			for (Integer sci : toRemove)
				unknownScanners.remove(sci);
		}
		return beacons;
	}
	public static int manhattan(Scanner s1,Scanner s2) {
		int ans=0;
		for (int i=0;i<3;++i)
			ans+=Math.abs(s1.rxyz[i]-s2.rxyz[i]);
		return ans;
	}
	public static int function2(ArrayList<Scanner> scanners) {
		int manh=0;
		for (int i=0;i<scanners.size();++i) {
			for (int j=i+1;j<scanners.size();++j) {
				manh=Math.max(manhattan(scanners.get(i),scanners.get(j)),manh);
			}
		}
		return manh;
	}
	public static void main(String[] Args) throws IOException{
		String SAMPLE="input/day19_sample.txt";
		String REAL="input/day19.txt";
		
		ArrayList<Scanner> scanners=readScannerData(SAMPLE);
		HashSet<Beacon> beacons=function(scanners);
		System.out.println(beacons.size());
		System.out.println(function2(scanners));
		//
		scanners=readScannerData(REAL);
		 beacons=function(scanners);
		System.out.println(beacons.size());
		System.out.println(function2(scanners));
	}
}
