import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.IntStream;
class Move{
	public int toY;
	public int toX;
	public int cost;
	public boolean  isLeftMove;
	public  boolean onlyVertical;
	public Move(int toY,int toX,int cost,boolean isLeftMove,boolean onlyVertical) {
		this.toX=toX;
		this.toY=toY;
		this.cost=cost;
		this.isLeftMove=isLeftMove;
		this.onlyVertical=onlyVertical;
	}
}
class Visitor{
	public int typeID;
	public int y;
	public int x;
	public int type;
	public int costToMove;
	public boolean doneLeft;
	public boolean doneRight;
	public Visitor(int typeID,int type,int startingy,int startingx,boolean doneLeft,boolean doneRight) {
		this.type=type;
		this.typeID=typeID;
		costToMove=(int)Math.pow(10, type);
		y=startingy;
		x=startingx;
		this.doneLeft=doneLeft;
		this.doneRight=doneRight;
	}

	public int canRiseToZero(boolean[][] verticalValid) {
		boolean canRiseToSurface=true;
		int costToRiseToZero=0;
		int tempy=y-1;
		while (tempy!=-1 && canRiseToSurface) {
			if (tempy==0) 
				if (verticalValid[x][0])
					costToRiseToZero+=costToMove;
				else
					canRiseToSurface=false;
			else 
				if (verticalValid[x][tempy])
					costToRiseToZero+=costToMove;
				else
					canRiseToSurface=false;
			tempy--;
		}
		return (canRiseToSurface) ? costToRiseToZero : -1;
			
	}
	public ArrayList<Move> getVisitorMoves(int costToRiseToZero,int[] correctly,int[] roomCounts,boolean[][] verticalValid){
		ArrayList<Move>  moves=new ArrayList<Move>();
		int[] ROOMS= {2,4,6,8};
		boolean[] ROOM= new boolean[11];
		for (int i=0;i<4;++i) ROOM[ROOMS[i]]=true;
	
		for (int direction=0;direction<2;++direction) {
			if (doneLeft && direction==0) continue;
			if (doneRight && direction==1) continue;
			int i = (direction==0) ? x-1 : x+1;
			int target=(direction==0) ? -1 : 11;
			while (i!=target) {
				if (verticalValid[i][0]) {
					int cost=costToRiseToZero + Math.abs(x-i)*costToMove;
					if (!ROOM[i])
						moves.add(new Move(0,i,cost,direction==0,false));
				}else 
					break;	
				i+= (direction==0) ? -1 : 1;
			}
			
		}
		return moves;

	}
	public boolean isValid() {
		return x==2*(type+1) &&  y>0;
	}
	@Override
	public boolean equals(Object o) {
		 if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        Visitor vi = (Visitor) o;
	      return  vi.x==x && vi.y==y && vi.typeID==typeID && vi.type==type;
	}
	@Override
	 public int hashCode() {
	      return 31 * y + 13*x + 19*typeID + 17*type;
	  }
	  public boolean cannotReach() {
			int[] ROOMS= {2,4,6,8};
		  if (doneLeft && x > ROOMS[type]) return true;
		  if (doneRight && x < ROOMS[type]) return true;
		  return false;
	  }
	  public boolean makesSenseToMove(int[] correctly,int[] ROOMS,int n,int m) {
			return correctly[type]<n  && !(y==m && x==ROOMS[type]);
	  }
}
class State implements Comparable<State>{
	public int cost=0;
	public Visitor[] visitors;
	public int pre;
	public int n;
	public int m;
	public boolean stateIsStillValid() {
		for (int i=0;i<n;++i) 
			if (visitors[i].cannotReach())
				return false;
		return true;
	}
	@Override
	public boolean equals(Object o) {
		 if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        State st = (State) o;
	        for (int i=0;i<n;++i) {
	        	if (!(st.visitors[i].equals(visitors[i])))
	        		return false;
	        }
	        return  true;
	    }
		@Override
	    public int hashCode() {
	    	int hc=0;
	    	for (Visitor vi : visitors)
	    		hc+=vi.hashCode();
	    	return hc;
	    }
	    public State(int n) {
	    	this.n=n;
	    	this.m=n/4;
	    	visitors=new Visitor[n];
	    	cost=0;
	    	pre=-1;
	    }
	    public State(Visitor[] vv,int preCost,int p,Move move) {
	    	this.cost=preCost+move.cost;
	    	this.n=vv.length;
	    	this.m=vv.length/4;
	    	visitors=new Visitor[n];
	    
	    	for (int i=0;i<n;++i) 
	    		if (i!=p)
	    			visitors[i]=new Visitor(vv[i].typeID,vv[i].type,vv[i].y,vv[i].x,vv[i].doneLeft,vv[i].doneRight);
	    		else {
	    			visitors[i]=new Visitor(vv[i].typeID,vv[i].type,move.toY,move.toX,vv[i].doneLeft,vv[i].doneRight);
	    			if (!move.onlyVertical) {
	    				if (move.isLeftMove)
	    					visitors[i].doneLeft=true;
	    				else
	    					visitors[i].doneRight=true;
	    			}
	    		}
	    		
	    	pre=p;
	    }
	    public boolean isValidState() {
	    	for (int i=0;i<n;++i) 
	    		if (!visitors[i].isValid())
	    			return false;
	    	return true;
	    }
	    public void AddVisitor(Visitor vi,int i) {
	    	visitors[i]=vi;
	    }
	    public void initHelpArrays(boolean[][] verticalValid,int[] roomCounts,int[] correctly,int[][] incorrectlyBelowOrEqual) {
	    	int[] ROOMS= {2,4,6,8};
			boolean[] ROOM=new boolean[11];
	    	for (Integer  r: ROOMS)
				ROOM[r]=true;
		

			for (int i=0;i<11;++i)
				for (int j=0;j<=m;++j)
					verticalValid[i][j]=true;
			for (int i=0;i<n;++i) {
				int typei=visitors[i].type;
				int xi=visitors[i].x;
				int ri=ROOMS[typei];
				int yi=visitors[i].y;
				roomCounts[xi]++;
				if (xi==ri && yi>=1) 
					correctly[typei]++;
				if (ROOM[xi] && xi!=ri) 
					incorrectlyBelowOrEqual[xi/2-1][yi]=1;
					verticalValid[xi][yi]=false;
			}
			for (int i=0;i<4;++i) {
				for (int j=m-1;j>=1;--j) {
					incorrectlyBelowOrEqual[i][j]+=incorrectlyBelowOrEqual[i][j-1];
				}
			}

	    }
	    public ArrayList<State> returnAllValidNextStates(){
	    	ArrayList<State> moves=new ArrayList<State>();
	    	int[] roomCounts=new int[11];
	    	int[] correctly=new int[11];
	    	int[][] incorrectlyBelowOrEqual=new int[4][1+m];
	    	int[] ROOMS= {2,4,6,8};
			int[] canRise=new int[n];
			boolean[][] verticalValid=new boolean[11][1+m];
			boolean[][] horizontalMove=new boolean[11][11];
			
			initHelpArrays(verticalValid,roomCounts,correctly,incorrectlyBelowOrEqual);

			for (int i=0;i<11;++i) {
				horizontalMove[i][i]=true;
				for (int j=i+1;j<11;++j) 
					horizontalMove[i][j] = (horizontalMove[i][j-1] && verticalValid[j][0]);	
					
				for (int j=i-1;j>=0;--j)
					horizontalMove[i][j]= (horizontalMove[i][j+1]) && verticalValid[j][0];
				
			}
			for (int i=0;i<n;++i)
				canRise[i]=visitors[i].canRiseToZero(verticalValid);
			boolean singleMove=false;
		
			for (int i=0;(i<n && !singleMove);++i) {
				int typei=visitors[i].type;
				int xi=visitors[i].x;
				int yi=visitors[i].y;
				int ri=ROOMS[typei];
				if (xi==ri) {				
						if (yi>0 && yi<m && incorrectlyBelowOrEqual[typei][yi]==0 && verticalValid[xi][yi]) {
							singleMove=true;
							//can jump directly to last empty but whatever...
							moves.add(new State(visitors,cost,i,new Move(yi+1,xi,visitors[i].costToMove,false,true)));
						}
				}else if (horizontalMove[xi][ri] && canRise[i]!=-1) {
					if (roomCounts[ri]==correctly[typei]) {
							int ocost=canRise[i];
						if (roomCounts[ri]==0) {
							 ocost+= (Math.abs(xi-ri)+m)*visitors[i].costToMove;
							moves.add(new State(visitors,cost,i,new Move(m,ri,ocost,ri < xi,false)));
							singleMove=true;
						}else if (roomCounts[ri]==1 & !verticalValid[ri][m]) {
							 ocost+= (Math.abs(xi-ri)+m-1)*visitors[i].costToMove;
							moves.add(new State(visitors,cost,i,new Move(m-1,ri,ocost,ri < xi,false)));
							singleMove=true;
						}else if (roomCounts[ri]==2 & !verticalValid[ri][m] && !verticalValid[ri][m-1]) {
							 ocost+=(Math.abs(xi-ri)+m-2)*visitors[i].costToMove;
							moves.add(new State(visitors,cost,i,new Move(m-2,ri,ocost,ri < xi,false)));
							singleMove=true;
						}else if (roomCounts[ri]==3 & !verticalValid[ri][m] && !verticalValid[ri][m-1] && !verticalValid[ri][m-2]) {
							 ocost+= (Math.abs(xi-ri)+m-3)*visitors[i].costToMove;
							moves.add(new State(visitors,cost,i,new Move(m-3,ri,ocost,ri < xi,false)));
							singleMove=true;
						}
					}
				}
			}
			
			if (!singleMove) {
				for (int i=0;i<n;++i) {
					if (pre==i) continue;
						if (canRise[i]!=-1 && visitors[i].makesSenseToMove(correctly, ROOMS,n,m)) {
							ArrayList<Move> vimoves=visitors[i].getVisitorMoves(canRise[i],correctly,roomCounts,verticalValid);
							for (Move move : vimoves) {
								State temp=new State(visitors,cost,i,move);
								if (temp.stateIsStillValid())
									moves.add(temp);
							}
						}
				}
			}
	    	return moves;
	    }
		@Override
		public int compareTo(State st) {
			if (cost < st.cost) return -1;
			if (cost > st.cost) return 1;
			return 0;
		}
}
public class Day23 {
	
	public static int function(String plan) {
		HashSet<State> states=new HashSet<State>();
		PriorityQueue<State> pq=new PriorityQueue<State>();
		
		int[] sx=new int[plan.length()];
		int curr=0;
		for (int i=0;i<4;++i)
			for (int j=0;j<plan.length()/4;++j)
				sx[curr++]=2+ 2*i;
		int[] nt=new int[4];
		State origState=new State(plan.length());
		for (int i=0;i<plan.length();++i) 
			origState.AddVisitor(new Visitor(i,plan.charAt(i)-'A', 1 + (i % (plan.length()/4)), sx[i],false,false),i);
		pq.add(origState);
		int counter=0;

		while (!pq.isEmpty()) {
			State currState=pq.poll();
			while (states.contains(currState) && pq.size()>0) 	
				currState=pq.poll();
			if (states.contains(currState))
					break;
			counter++;

			if (currState.isValidState())
			{	System.out.println(counter+" "+pq.size());
				return currState.cost;
			}
			states.add(currState);
			ArrayList<State> nextStates=currState.returnAllValidNextStates();
			for (State st : nextStates) 
				if (!states.contains(st)) 
					pq.add(st); 
					
		}
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(function("BACDBCDA"));
		System.out.println(function("BDCDCABA"));

		System.out.println(function("BDDACCBDBBACDACA"));
		System.out.println(function("BDDDCCBDCBAABACA"));


	}

}
