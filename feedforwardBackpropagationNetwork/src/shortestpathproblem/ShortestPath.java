package shortestpathproblem;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ShortestPath {
	private static final int SIZE = 7;
	private static double[][] Graph=null;
	private static Map<Integer, List<Character>> map=new TreeMap<>();
	private static double[] minDist=new double[SIZE];
	private static boolean[] S=null;



	private static void init(){
		S=new boolean[]{false,false,false,false,false,false,false};

		Graph=new double[][]{
				{ 0, 7, 9,-1,-1,14,22},
				{ 7, 0,10,15,-1,-1,5},
				{ 9,10, 0,11,-1, 2,7},
				{-1,15,11, 0, 6,-1,-1},
				{-1,-1,-1, 6, 0, 9,12},
				{14,-1, 2,-1, 9, 0,-1},
				{22,5, 7,-1, 12, -1,0},
	
		};
		//printmatrix();
		for (int i = 0; i < SIZE; i++) {
			List< Character> list =new LinkedList<>();
			map.put(i, list);
		}

	}
	private static void findShortPath(int start){
		boolean[] U=new boolean[SIZE];
		double currentMinDist=Integer.MAX_VALUE;
		int pos=start-1;
		int cur=-1;

		boolean isEnd=false;
		S[start-1]=true;
		for (int i = 0; i <SIZE; i++) {
			updatePath(i,pos);
		}
		for (int i = 0; i < SIZE; i++) {
			minDist[i]=Graph[i][pos];			
		}
		double minv=Integer.MAX_VALUE;
		for (int i = 0; i <SIZE; i++) {
			if(pos!=i&&minDist[i]!=-1&&minDist[i]<minv){
				pos=i;
				minv=minDist[i];
			}


		}
		for (int i = 0; i < S.length; i++) {
			if(S[i]!=true) updatePath(i, i);

		}
		while (!isEnd) {
			if(nextpoint(S)==-1){
				isEnd=true;
				printPath();

			}
			for (int i = 0; i < U.length; i++) {
				if(S[i]!=true) U[i]=false;
			}

			currentMinDist=Integer.MAX_VALUE;
			for(int i=0;i<SIZE;i++){
				if(minDist[i]==-1) minDist[i]=Integer.MAX_VALUE;
				if(i!=pos&&Graph[i][pos]!=-1&&U[i]==false&&S[i]!=true){
					if(minDist[i]>=minDist[pos]+Graph[i][pos]){
						minDist[i]=minDist[pos]+Graph[i][pos];
						map.put(i, copy(map.get(pos), i));
					}														
				}
				U[i]=true;				
			}
			S[pos]=true;
			for (int i = 0; i < SIZE; i++) {
				if(minDist[i]<currentMinDist&&S[i]!=true){
					currentMinDist=minDist[i];
					cur=i;
				}
			}
			pos=cur;
			//printPath();			
		}

	}

	private static void printmatrix(){
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.print(Graph[i][j]+"   ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static char getpoint(int i){
		char c= (char) (i+'A'-0);
		return c;
	}

	private static List<Character> copy(List<Character> list1,int pos){
		List<Character> l1=new LinkedList<>();  
		for (int i = 0; i < list1.size(); i++) {
			l1.add(list1.get(i));
		}
		l1.add(getpoint(pos));
		return l1;

	}

	private static int  nextpoint(boolean[] bool){
		for (int i = 0; i < bool.length; i++) {
			if(!bool[i]){
				return i;
			}
		}
		//全为真，返回-1
		return -1;
	}
	private static void updatePath(int pointV,int val){
		char v;	
		v=getpoint(val);
		List<Character> list=map.get(pointV);
		list.add(v);
		map.put(pointV, list);			
	}

	private static void printPath(){
		for (int i = 0; i < SIZE; i++) {
			List<Character> list = map.get(i);
			if(list.size()==1){
				System.out.print(list.get(0)+"->"+list.get(0)+"   ");
				System.out.println("the shortest Path between points:  "+minDist[i]);
			}else {
				System.out.print(list.get(0)+"->");
				for (int j = 1; j < list.size()-1; j++) {
					System.out.print(list.get(j)+"->");
				}
				System.out.print(list.get(list.size()-1)+"   ");
				System.out.print("the shortest Path between points:  "+minDist[i]);
				System.out.println();
			}

		}
	}
	public static void main(String[] args) {
		init();
		findShortPath(1);
	}


}

















