package feedforward4;

import java.util.Arrays;
import java.util.Random;

public class Network1 {
	//define structure
	private static final int INPUT = 3;
	private static final int HIDE =16;
	private static final int OUTPUT =2; 
	// weights
	private static  double[][] inhide=null;
	private static  double[][] hideout=null;
	//activate/output
	private static  double[] input=null;
	private static  double[][] sample=null;
	private static double[][] out_sample=null;
	private static  double[] hidden=null;
	private static  double[] target=null;
	private static  double[] actual=null;
	//error
	private static  double[] herro=null;
	private static  double[] oerro=null;
	//define params
	private static final double learning_rate=0.8;  
	private static final double noise=0.25;
	//practise times
	private static final int Max_times=10000;
	private static final int Samp=8;
    
	//对应值
	//private static String[] str =null;


	private static void  initiate(){
		inhide=new double[INPUT+1][HIDE];//添加一个偏置节点值
		hideout=new double[HIDE+1][OUTPUT];
		
		input=new double[INPUT];
		hidden=new double[HIDE];
		actual=new double[OUTPUT];
		target =new double[OUTPUT];
		
		sample = new double[][]{{0,0,0},{0,0,1},{0,1,0},{1,0,0},{1,0,1},{1,1,0},{0,1,1},{1,1,1}};
		out_sample=new double[][]{{1,0},{0,1},{1,0},{1,0},{0,1},{1,0},{0,1},{0,1}};
		
		//str=new String[]{"even","odd"};

		herro=new double[HIDE];
		oerro=new double[OUTPUT];

	}

	private static void train(){
		boolean isEnd =false;
		int times=0;

		assignRandomWeights();
		while (!isEnd) {
			for(int i=0;i<Samp;i++){
				for(int j=0;j<INPUT;j++){
					input[j]=sample[i][j];					
				}
				for(int j=0;j<OUTPUT;j++){
					target[j]=out_sample[i][j];
				}

				forward();
				times++;
				if(times>Max_times){
					isEnd=true;
				}
				backward();
			}
		}
		
		for(int i=0;i<Samp;i++){
			for(int j=0;j<INPUT;j++){
				input[j]=sample[i][j]+new Random().nextDouble()*noise;;					
			}

			forward();
			
			
			if(active()==0){
				System.out.println(actual[0]+","+actual[1]);
				System.out.println(Arrays.toString(input)+"-->even");	
			}else {
				System.out.println(actual[0]+","+actual[1]);
				System.out.println(Arrays.toString(input)+"-->odd");
			}			
			
		}
//		System.out.println();
//		//test : hava a noisy
//		input[0]=0.9+new Random().nextDouble()*noise;input[1]=0.8;input[2]=0.03;
//		forward();
//		if(active()==0){
//			System.out.println(actual[0]+","+actual[1]);
//			System.out.println(Arrays.toString(input)+"-->even");	
//		}else {
//			System.out.println(actual[0]+","+actual[1]);
//			System.out.println(Arrays.toString(input)+"-->odd");
//		}	
		
	}
	
	private static int active(){
		double max=0.0;
		int index=0;
		for(int i=0;i<OUTPUT;i++){
			if(actual[i]>max){
				max=actual[i];
				index=i;
			}
		}
		return index;
	}
	private static void forward(){
		double sum;
		//hidden layer output
		for(int i=0;i<HIDE;i++){
			sum=0.0;
			for(int j=0;j<INPUT;j++){
				sum+=input[j]*inhide[j][i];			
			}
			sum+=inhide[INPUT][i];
			hidden[i]=sigmoid(sum);
		}

		for(int i=0;i<OUTPUT;i++){
			sum=0.0;
			for(int j=0;j<HIDE;j++){
				sum=+hidden[j]*hideout[j][i];
			}
			sum+=hideout[HIDE][i];
			actual[i]=sigmoid(sum);//network ouput value;
		}

	}
	private static void backward(){

		//output error
		for(int i=0;i<OUTPUT;i++){
			oerro[i]=actual[i]*(1-actual[i])*(target[i]-actual[i]);
		}
		//hide error
		for(int i=0 ;i<HIDE;i++){
			for(int j=0;j<OUTPUT;j++){
				herro[i]=+hidden[i]*(1-hidden[i])*oerro[j]*hideout[i][j];
			}
		}
		//renew hide->output weights
		for (int i = 0; i < OUTPUT ; i++) {
			for(int j=0;j < HIDE;j++){
				hideout[j][i]=hideout[j][i]+learning_rate*oerro[i]*hidden[j];
			}
			hideout[HIDE][i]=+learning_rate*oerro[i];
		}
		//renew input->hide weights
		for(int i=0;i<HIDE;i++){
			for(int j=0;j<INPUT;j++){
				inhide[j][i]=inhide[j][i]+learning_rate*herro[i]*input[j];
			}
			inhide[INPUT][i]=+learning_rate*herro[i];
		}


	}
	private static void assignRandomWeights(){

		for(int i=0;i<=INPUT;i++){
			for(int j=0;j<HIDE;j++){
				inhide[i][j]=new Random().nextDouble()-0.5;
			}
		}
		for(int i=0;i<=HIDE;i++){
			for(int j=0;j<OUTPUT;j++){
				hideout[i][j]=new Random().nextDouble()-0.5;
			}
		}

	}

	private static double sigmoid(double val){		
		return 1/(1+Math.exp(-val)) ;
	}
	
	public static void main(String[] args) {
		initiate();
		train();
	}

}
