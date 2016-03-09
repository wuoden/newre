package com.test0;

public class ForAndWhile {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {			
			System.out.println("result1 is:"+i);				
			if(i==3){
				System.out.println();
				//continue;			
			}
		}
		System.out.println();
		int j=0;
		while (j<10) {			
			System.out.println("result2 is:"+j);			
			if(j==3){
				System.out.println();
				//continue;			
			}
			j++;
		}
	}

}
