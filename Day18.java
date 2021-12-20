import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class TreePairNode{
	 public boolean isLeftChild;
	 public TreePairNode LEFT;
	 public TreePairNode RIGHT;
	 public boolean literalLeft;
	 public boolean literalRight;
	 public TreePairNode parent;
	 public int leftValue;
	 public int rightValue;
	 public int depth;
	 public long getMagnitude() {
		 return 3*getLeftMagnitude() + 2*getRightMagnitude();
	 }
	 public long getLeftMagnitude() {
		return (literalLeft) ? leftValue : LEFT.getMagnitude();
	 }
	 public long getRightMagnitude() {
		return (literalRight) ? rightValue : RIGHT.getMagnitude();
	}
	 private TreePairNode splitValue(int value) {
		 TreePairNode  np=new TreePairNode();
		 np.depth=depth+1;
		 np.literalLeft=true;
		 np.literalRight=true;
		 np.parent=this;
		 np.leftValue=(int)Math.floor(value/2.0);
		 np.rightValue=(int)Math.ceil(value/2.0); 
		 return np;
	 }
	 public void splitLeft() {
		LEFT=splitValue(leftValue);
		LEFT.isLeftChild=true;
		literalLeft=false;
	 }
	 public void splitRight() {
		 RIGHT=splitValue(rightValue);
		 RIGHT.isLeftChild=false;
		literalRight=false;
	 }
	 public boolean addLeftMost(int add,TreePairNode node) { //need leftmost element
		 if (node==null)
			 return false;
		 if (node.literalLeft) 
			 node.leftValue+=add;
		 else {
			 TreePairNode onLeft=node.LEFT;
			 while (true) {
				 if (onLeft.literalRight) {
					 onLeft.rightValue+=add;
					 break;
				 }
				 onLeft=onLeft.RIGHT;
			 }
		 }

		return true;
	 }
	 public boolean addRightMost(int add,TreePairNode node) { //need rightmost 

		 if (node==null)
			 return false;
		 if (node.literalRight) 
		 		node.rightValue+=add;
		 	else
		 	{
		 		 TreePairNode onRight=node.RIGHT;
				 while (true) {
					 if (onRight.literalLeft) {
						 onRight.leftValue+=add;
						 break;
					 }
					 onRight=onRight.LEFT;
				 }
		 	}
		 		return true;
	 }
	 public void explodeLeftChild() {
		 int  addOnLeft=LEFT.leftValue;
		 int  addOnRight=LEFT.rightValue;
		 LEFT=null;
		 literalLeft=true;
		 leftValue=0;
		 if (literalRight) 
			 rightValue+=addOnRight;
		 else
			addLeftMost(addOnRight,RIGHT);
		 TreePairNode curr=parent;
		  boolean currLeftIsAcceptable=!isLeftChild;
		  while (curr!=null) {
			  if (currLeftIsAcceptable) {
				  addLeftMost(addOnLeft,curr);
			  	  break;
		  		}else {
		  			currLeftIsAcceptable=!curr.isLeftChild;
				  curr=curr.parent;
			  }
		  }
		 
	 }
	
	 public void explodeRightChild() {
		 int  addOnLeft=RIGHT.leftValue;
		 int  addOnRight=RIGHT.rightValue;
		 RIGHT=null;
		 literalRight=true;
		 rightValue=0;
		 if (literalLeft) 
			 leftValue+=addOnLeft;
		 else
			 addRightMost(addOnLeft,LEFT);
		 TreePairNode curr=parent;
		  boolean currRightIsAcceptable=isLeftChild;
		  while (curr!=null) {
			  if (currRightIsAcceptable) {
				  addRightMost(addOnRight,curr);
			  	  break;
		  		}else {
				  currRightIsAcceptable=curr.isLeftChild;
				  curr=curr.parent;
			  }
		  }
	 }
	
	 
 }

public class Day18 {


	public static void parseString(String str,TreePairNode curr,int charposition) {
			if (str.charAt(charposition)=='[') {
				if (str.charAt(charposition+1)!='['){
					curr.literalLeft=true;
					curr.leftValue=Integer.parseInt(Character.toString(str.charAt(charposition+1)));
					parseString(str,curr,charposition+2);
				}else {
					TreePairNode createLeft=new TreePairNode();
					createLeft.parent=curr;
					createLeft.isLeftChild=true;
					curr.literalLeft=false;
					createLeft.depth=curr.depth+1;
					parseString(str,createLeft,charposition+1);
					curr.LEFT=createLeft;
				}
			}else if (str.charAt(charposition)==',') {
				if (str.charAt(charposition+1)!='['){
					curr.literalRight=true;
					curr.rightValue=Integer.parseInt(Character.toString(str.charAt(charposition+1)));
					parseString(str,curr,charposition+2);
				}else {
					TreePairNode createRight=new TreePairNode();
					createRight.parent=curr;
					createRight.isLeftChild=false;
					createRight.depth=curr.depth+1;
					curr.literalRight=false;
					parseString(str,createRight,charposition+1);
					curr.RIGHT=createRight;

				}
			}else { 
				if (charposition+1!=str.length())
					parseString(str,curr.parent,charposition+1);
			}
	}
	public static void recIncreaseDepthProcedure(TreePairNode curr) {
		curr.depth++;
		if (!curr.literalLeft)
			recIncreaseDepthProcedure(curr.LEFT);
		if (!curr.literalRight)
			recIncreaseDepthProcedure(curr.RIGHT);
	}
	public static TreePairNode joinTwoTrees(TreePairNode pair1,TreePairNode pair2) {
		TreePairNode newRoot=new TreePairNode();
		newRoot.literalLeft=false;
		newRoot.literalRight=false;
		newRoot.LEFT=pair1;
		pair1.parent=newRoot;
		pair2.parent=newRoot;
		pair1.isLeftChild=true;
		pair2.isLeftChild=false;
		newRoot.RIGHT=pair2;
		newRoot.depth=1;
		recIncreaseDepthProcedure(pair1);
		recIncreaseDepthProcedure(pair2);
		return newRoot;
	}
	public static int recExplode(TreePairNode curr) {
		int changes=0;
		if (curr!=null) {
			if (!curr.literalLeft) 
				if (curr.LEFT.literalLeft && curr.LEFT.literalRight && curr.LEFT.depth>=5) {
					curr.explodeLeftChild();
					changes++;
				} 
			changes+=recExplode(curr.LEFT);
			if (!curr.literalRight)
				if (curr.RIGHT.literalLeft && curr.RIGHT.literalRight && curr.RIGHT.depth>=5) {
					curr.explodeRightChild();
					changes++;
				}
			changes+=recExplode(curr.RIGHT);
		}
		return changes;
	}
	public static int recSplit(TreePairNode curr) {
		int changes=0;
		if (curr!=null) {
			if (curr.literalLeft && curr.leftValue>9) {
				curr.splitLeft();
				return 1;
			}
			changes=recSplit(curr.LEFT);
			if (changes>0) 
				return 1;
			if (curr.literalRight && curr.rightValue>9) {
				curr.splitRight();
				return 1;
			}
		
			changes+=recSplit(curr.RIGHT);
		}
		return changes;
	}
	public static void reduce(TreePairNode head) {
		int changes=1;
		while (changes>0)
			changes=recExplode(head) + recSplit(head);
	}

	public static long function(String fname) throws IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln=brdr.readLine();
		TreePairNode root=new TreePairNode(); root.depth=1;
		parseString(ln,root,0);
		while ((ln=brdr.readLine())!=null) {
			TreePairNode root2=new TreePairNode(); root2.depth=1;
			parseString(ln,root2,0);
			root=joinTwoTrees(root,root2);
			
			reduce(root);
		}
		return root.getMagnitude();
	}
	public static long function2(String fname) throws IOException {
		BufferedReader brdr=new BufferedReader(new FileReader(fname));
		String ln="";
		ArrayList<String> roots=new ArrayList<String>();
		while ((ln=brdr.readLine())!=null) {
			TreePairNode root=new TreePairNode(); root.depth=1;
			parseString(ln,root,0);
			roots.add(ln);
		}
		long maxMagnitute=0;
		for (int i=0;i<roots.size();++i) {
			for (int j=0;j<roots.size();++j) {
				if (i!=j) {
					TreePairNode root1=new TreePairNode(); root1.depth=1;
					parseString(roots.get(i),root1,0);
					TreePairNode root2=new TreePairNode(); root2.depth=1;
					parseString(roots.get(j),root2,0);
					TreePairNode root12=joinTwoTrees(root1,root2);
					reduce(root12);
					maxMagnitute=Math.max(maxMagnitute,root12.getMagnitude());
				}
			}
		}
		return maxMagnitute;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String SAMPLE="input/day18_sample.txt";
		String REAL="input/day18.txt";
		System.out.println(function(SAMPLE));
		System.out.println(function(REAL));
		System.out.println(function2(SAMPLE));
		System.out.println(function2(REAL));
	}

}
