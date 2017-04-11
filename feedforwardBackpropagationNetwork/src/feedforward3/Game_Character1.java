package feedforward3;


import java.util.Random;
import java.text.DecimalFormat;

public class Game_Character1
{
	private static final int INPUT_NEURONS = 4;
	private static final int HIDDEN_NEURONS = 3;
	private static final int OUTPUT_NEURONS = 4;

	private static final double LEARN_RATE = 0.6;    // Rho.
	private static final int TRAINING_REPS = 7500;

	// Input to Hidden Weights (with Biases).
	private static double wih[][] = null;

	// Hidden to Output Weights (with Biases).
	private static double who[][] = null;

	// Activations.
	private static double inputs[] = null;
	private static double hidden[] = null;
	private static double target[] = null;
	private static double actual[] = null;

	// Unit errors.
	private static double erro[] = null;
	private static double errh[] = null;

	private static final int MAX_SAMPLES = 18; // 1st dimension of arrays below...

	private static final double SAMPLE_HEALTH[] = new double[] {2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 
		1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 
		0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	private static final double SAMPLE_KNIFE[] = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 
		0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 
		0.0, 0.0, 0.0, 0.0, 1.0, 1.0};
	private static final double SAMPLE_GUN[] = new double[] {0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 
		0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 
		0.0, 0.0, 1.0, 1.0, 0.0, 0.0};
	private static final double SAMPLE_ENEMY[] = new double[] {0.0, 1.0, 1.0, 2.0, 2.0, 1.0, 
		0.0, 1.0, 1.0, 2.0, 2.0, 1.0, 
		0.0, 1.0, 1.0, 2.0, 2.0, 1.0};
	private static final double SAMPLE_OUT[][] = new double[][] {{0.0, 0.0, 1.0, 0.0}, 
		{0.0, 0.0, 1.0, 0.0}, 
		{1.0, 0.0, 0.0, 0.0}, 
		{1.0, 0.0, 0.0, 0.0}, 
		{0.0, 0.0, 0.0, 1.0}, 
		{1.0, 0.0, 0.0, 0.0}, 
		{0.0, 0.0, 1.0, 0.0}, 
		{0.0, 0.0, 0.0, 1.0}, 
		{1.0, 0.0, 0.0, 0.0}, 
		{0.0, 0.0, 0.0, 1.0}, 
		{0.0, 0.0, 0.0, 1.0}, 
		{0.0, 0.0, 0.0, 1.0}, 
		{0.0, 0.0, 1.0, 0.0}, 
		{0.0, 0.0, 0.0, 1.0}, 
		{0.0, 0.0, 0.0, 1.0}, 
		{0.0, 1.0, 0.0, 0.0}, 
		{0.0, 1.0, 0.0, 0.0}, 
		{0.0, 0.0, 0.0, 1.0}};

	private static final String[] STRINGS = new String[] {"Attack", "Run", "Wander", "Hide"};

	private static void initialize()
	{
		wih = new double[INPUT_NEURONS + 1][HIDDEN_NEURONS];
		who = new double[HIDDEN_NEURONS + 1][OUTPUT_NEURONS];
		
		//store O value
		inputs = new double[INPUT_NEURONS];
		hidden = new double[HIDDEN_NEURONS];
		
		
		target = new double[OUTPUT_NEURONS];
		actual = new double[OUTPUT_NEURONS];
		
		//store err
		erro = new double[OUTPUT_NEURONS];
		errh = new double[HIDDEN_NEURONS];
		return;
	}
   
	private static void neuralNetwork()
	{
		double err = 0.0;
		int sample = 0;
		int iterations = 0;
		int sum = 0;
		boolean stopLoop = false;
		DecimalFormat dfm = new java.text.DecimalFormat("###0.000");

		assignRandomWeights();

		// Train the network.
		while(!stopLoop)
		{
			sample += 1;
			if(sample == MAX_SAMPLES){
				sample = 0;
			}

			inputs[0] = SAMPLE_HEALTH[sample];
			inputs[1] = SAMPLE_KNIFE[sample];
			inputs[2] = SAMPLE_GUN[sample];
			inputs[3] = SAMPLE_ENEMY[sample];

			target[0] = SAMPLE_OUT[sample][0];
			target[1] = SAMPLE_OUT[sample][1];
			target[2] = SAMPLE_OUT[sample][2];
			target[3] = SAMPLE_OUT[sample][3];

			feedForward();

			err = 0.0;
			for(int i = 0; i < OUTPUT_NEURONS; i++)
			{
				err += Math.sqrt(SAMPLE_OUT[sample][i] - actual[i]);
			}
			err = 0.5 * err;

			if(iterations > TRAINING_REPS){
				stopLoop = true;
			}
			iterations += 1;

			backPropagate();
		}

		// Test the network.//—È÷§ ’¡≤
		for(int i = 0; i < MAX_SAMPLES; i++)
		{
			inputs[0] = SAMPLE_HEALTH[i];
			inputs[1] = SAMPLE_KNIFE[i];
			inputs[2] = SAMPLE_GUN[i];
			inputs[3] = SAMPLE_ENEMY[i];

			target[0] = SAMPLE_OUT[i][0];
			target[1] = SAMPLE_OUT[i][1];
			target[2] = SAMPLE_OUT[i][2];
			target[3] = SAMPLE_OUT[i][3];

			feedForward();

			if(action(actual) != action(target)){
				System.out.println(inputs[0] + "\t" + inputs[1] + "\t" + inputs[2] + "\t" + inputs[3] + 
						"\tActual: " + STRINGS[action(actual)] + 
						"\tExpected: " + STRINGS[action(target)]+"  i ="+i);
				System.out.println();
			}else{
				sum += 1;
			}
		}

		System.out.println("Network is " + dfm.format((double)sum / ((double)MAX_SAMPLES) * 100.0) + "% correct.");

		// Run some tests.
		System.out.println();
		//   Health          Knife               Gun             Enemy
		inputs[0] = 2.0 ; inputs[1] = 1.0 ; inputs[2] = 1.0 ; inputs[3] = 1.0;
		feedForward();
		System.out.println("2-1-1-1 Action: " + STRINGS[action(actual)]);

		inputs[0] = 1.0 ; inputs[1] = 1.0 ; inputs[2] = 1.0 ; inputs[3] = 2.0;
		feedForward();
		System.out.println("1-1-1-2 Action: " + STRINGS[action(actual)]);

		inputs[0] = 0.0 ; inputs[1] = 0.0 ; inputs[2] = 0.0 ; inputs[3] = 0.0;
		feedForward();
		System.out.println("0-0-0-0 Action: " + STRINGS[action(actual)]);

		inputs[0] = 0.0 ; inputs[1] = 1.0 ; inputs[2] = 1.0 ; inputs[3] = 1.0;
		feedForward();
		System.out.println("0-1-1-1 Action: " + STRINGS[action(actual)]);

		inputs[0] = 2.0 ; inputs[1] = 0.0 ; inputs[2] = 1.0 ; inputs[3] = 3.0;
		feedForward();
		System.out.println("2-0-1-3 Action: " + STRINGS[action(actual)]);

		inputs[0] = 2.0 ; inputs[1] = 1.0 ; inputs[2] = 0.0 ; inputs[3] = 3.0;
		feedForward();
		System.out.println("2-1-0-3 Action: " + STRINGS[action(actual)]);

		inputs[0] = 0.0 ; inputs[1] = 1.0 ; inputs[2] = 0.0 ; inputs[3] = 3.0;
		feedForward();
		System.out.println("0-1-0-3 Action: " + STRINGS[action(actual)]);

		return;
	}

	private static int action(double[] vector)
	{
		int sel = 0;
		double max = vector[sel];

		for(int index = 0; index < OUTPUT_NEURONS; index++)
		{
			if(vector[index] > max){
				max = vector[index];
				sel = index;
			}
		}
		return sel;
	}

	private static void feedForward()
	{
		double sum = 0.0;

		// Calculate input to hidden layer.
		for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
		{
			sum = 0.0;
			for(int inp = 0; inp < INPUT_NEURONS; inp++)
			{
				sum += inputs[inp] * wih[inp][hid];
			} // inp

			// Add in bias.
			sum += wih[INPUT_NEURONS][hid];
			hidden[hid] = sigmoid(sum);
		} // hid

		// Calculate the hidden to output layer.
		for(int out = 0; out < OUTPUT_NEURONS; out++)
		{
			sum = 0.0;
			for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
			{
				sum += hidden[hid] * who[hid][out];
			} // hid

			// Add in bias.
			sum += who[HIDDEN_NEURONS][out];
			actual[out] = sigmoid(sum);
		} // out
		return;
	}

	private static void backPropagate()
	{
		// Calculate the output layer error (step 3 for output cell).
		for(int out = 0; out < OUTPUT_NEURONS; out++)
		{
			erro[out] = (target[out] - actual[out]) * sigmoidDerivative(actual[out]);
		}

		// Calculate the hidden layer error (step 3 for hidden cell).
		for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
		{
			errh[hid] = 0.0;
			for(int out = 0; out < OUTPUT_NEURONS; out++)
			{
				errh[hid] += erro[out] * who[hid][out];
			} // out
			errh[hid] *= sigmoidDerivative(hidden[hid]);
		} // hid

		// Update the weights for the output layer (step 4).
		for(int out = 0; out < OUTPUT_NEURONS; out++)
		{
			for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
			{
				who[hid][out] += (LEARN_RATE * erro[out] * hidden[hid]);
			} // hid
			// Update the bias.
			who[HIDDEN_NEURONS][out] += (LEARN_RATE * erro[out]);
		} // out

		// Update the weights for the hidden layer (step 4).
		for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
		{
			for(int inp = 0; inp < INPUT_NEURONS; inp++)
			{
				wih[inp][hid] += (LEARN_RATE * errh[hid] * inputs[inp]);
			} // inp
			// Update the bias.
			wih[INPUT_NEURONS][hid] += (LEARN_RATE * errh[hid]);
		} // hid
		return;
	}

	private static void assignRandomWeights()
	{
		for(int inp = 0; inp < INPUT_NEURONS; inp++) // Do not subtract 1 here.
		{
			for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
			{
				// Assign a random weight value between -0.5 and 0.5
				wih[inp][hid] = new Random().nextDouble() - 0.5;
			} // hid
		} // inp

		for(int hid = 0; hid < HIDDEN_NEURONS; hid++) // Do not subtract 1 here.
		{
			for(int out = 0; out < OUTPUT_NEURONS; out++)
			{
				// Assign a random weight value between -0.5 and 0.5
				who[hid][out] = new Random().nextDouble() - 0.5;//nextDouble() return between 0.0 and 1.0
			} // out
		} // hid
		return;
	}

	private static double sigmoid(double val)
	{
		return 1.0 / (1.0 + Math.exp(-val));
	}

	private static double sigmoidDerivative(double val)
	{
		return val * (1.0 - val);
	}

	public static void main(String[] args)
	{
		initialize();
		neuralNetwork();
		return;
	}

}