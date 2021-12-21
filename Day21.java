
public class Day21 {

	public static long  function(int player1,int player2) {
		long score1=0;
		long score2=0;
		long diceThrows=0;
		int b=0;
		long diceValue=1;
		while (Math.max(score1, score2) <1000) {
			long diceSumForRound=diceValue++;
			if (diceValue==101) diceValue=1;
			diceSumForRound+=diceValue++;
			if (diceValue==101) diceValue=1;
			diceSumForRound+=diceValue++;
			if (diceValue==101) diceValue=1;
			diceThrows+=3;
			if (b==0) {
				player1+=(diceSumForRound % 10);
				if (player1 > 10) player1-=10;
				score1+=player1;
			}else {
				player2+=(diceSumForRound % 10);
				if (player2 > 10) player2-=10;

				score2+=player2;
			}
			b=(b+1)%2;

		}
		return Math.min(score1,score2)*diceThrows;
	}
	private static int getPlayer(int numThrow) {
		if (numThrow % 3 ==0) 
			return ((numThrow-1)/3 % 2);
		else 
			return (numThrow /3) % 2; 
			
	}
	
	public static long function2(int playerPos1,int playerPos2,int WINNUM) {
		int NTHROWS=3*2*WINNUM; //super-upper bound
		long[][][][][][] dp=new long[NTHROWS][11][11][WINNUM+1][WINNUM+1][7];
		
		dp[0][playerPos1][playerPos2][0][0][0]=1;
		long wins1=0;
		long wins2=0;
		for (int diceThrows=1;diceThrows<=NTHROWS-1;++diceThrows) {
				int player = getPlayer(diceThrows);
				for (int dice=1;dice<=3;++dice) {
					for (int p1=1;p1<=10;++p1) {
						for (int p2=1;p2<=10;++p2) {
							for (int s1=0;s1<WINNUM;++s1) {
								//can ignore s1 if too low (or too high) based on diceThrows
								for (int s2=0;s2<WINNUM;++s2) {
									//likewise
									for (int carry=0;carry<=6;carry++) {
										long curr=dp[diceThrows-1][p1][p2][s1][s2][carry];;
										if (player==0) {
											if (diceThrows % 3 ==0) { //
												int sum=dice+carry;
												int nextp1=p1 + sum; 
												if (nextp1 >10) nextp1-=10;
												int nexts1=Math.min(WINNUM,s1+nextp1);
												if (nexts1==WINNUM) 
													wins1+=dp[diceThrows-1][p1][p2][s1][s2][carry];
												else
													dp[diceThrows][nextp1][p2][nexts1][s2][0]+=curr;

											}else if (carry<=3)
												dp[diceThrows][p1][p2][s1][s2][dice+carry]+=curr;
											
										}else {
											if (diceThrows % 3 ==0) { //
												int sum=dice+carry;
												int nextp2=p2 + sum; 
												if (nextp2 >10) nextp2-=10;
												int nexts2=Math.min(WINNUM,s2+nextp2);
												if (nexts2==WINNUM) {
													wins2+=dp[diceThrows-1][p1][p2][s1][s2][carry];
												}else
													dp[diceThrows][p1][nextp2][s1][nexts2][0]+=curr;

											}else if (carry<=3)
												dp[diceThrows][p1][p2][s1][s2][dice+carry]+=curr;
											}
									}
									
									
									}
							}
						}
					}
				}
			}
		
		
		return Math.max(wins1,wins2);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(function(4,8));
		System.out.println(function(10,9));
		System.out.println(function2(4,8,21));
		System.out.println(function2(10,9,21));
	}

}
