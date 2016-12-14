import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
public class smaLandscape
{
   public int param_1_min;
   public int param_1_max;
   public int param_2_min;
   public int param_2_max;
   public double[][] landscape;
   public smaAgent[] agents;
   public int agentCount;
   public int idCount;
   public Random rnd = new Random();
   public double global_max;
   
    public smaLandscape(int min1, int max1, int min2, int max2, int numagents)
    {
        param_1_min = min1;
        param_2_min = min2;
        param_1_max = max1;
        param_2_max = max2;
        global_max = -1000;
        landscape = new double[max1 - min1 + 1][max2 - min2 + 1];
        agents = new smaAgent[numagents];
        for(int i = 0; i < numagents; i++){
            agents[i] = new smaAgent(rnd.nextInt(max1 - min1), rnd.nextInt(max2 - min2), i);
            System.out.println("agent at " + agents[i].i + "," + agents[i].j);
        }
        for(int i = 0; i <= (max1 - min1); i++){
            for(int j = 0; j <= (max2 - min2); j++){
                landscape[i][j] = -1000;
            }
        }
    }


    public double fitness(int i, int j, ArrayList<dayData> data)
    {
    	if (i == 0 || j == 0) {
    		System.out.println("BOUNDARY----------------------------");
    		return -1000.0;
    	}
    	double fit = calcSMA(data, i, j);
    	if (fit != 0) {
    		global_max = Math.max(global_max, fit);
    	}
    	return fit;
        
        // return calcEMA(data, i, j, 0.05);
    }
    public double[][] step(ArrayList<dayData> data)
    {
        for(smaAgent a: agents){
            double curFit = fitness(a.i, a.j, data);
            landscape[a.i][a.j] = curFit;
            double[] fitnesses = new double[4];
            if(a.i > 0){
                if(landscape[a.i - 1][a.j] == -1000){
                    fitnesses[0] = fitness(a.i - 1, a.j, data);
                }
                else{
                    fitnesses[0] = landscape[a.i - 1][a.j];
                }
            }
            else {
            	fitnesses[0] = -1000;
            }
            if(a.i < param_1_max - param_1_min){
                if(landscape[a.i + 1][a.j] == -1000){
                    fitnesses[1] = fitness(a.i + 1, a.j, data);
                }
                else{
                    fitnesses[1] = landscape[a.i + 1][a.j];
                }
            }
            else {
            	fitnesses[1] = -1000;
            }
            if(a.j > 0){
                if(landscape[a.i][a.j - 1] == -1000){
                    fitnesses[2] = fitness(a.i, a.j - 1, data);
                }
                else{
                    fitnesses[2] = landscape[a.i][a.j - 1];
                }
            }
            else {
            	fitnesses[2] = -1000;
            }
            if(a.j < param_2_max - param_2_min){
                if(landscape[a.i][a.j + 1] == -1000){
                    fitnesses[3] = fitness(a.i, a.j + 1, data);
                }
                else{
                    fitnesses[3] = landscape[a.i][a.j + 1];
                }
            }
            else {
            	fitnesses[3] = -1000;
            }
            int maxPos = 0;
            for(int i = 0; i < 4; i++){
                if(fitnesses[maxPos] == -1000 && fitnesses[i] != -1000){
                    maxPos = i;
                }
                if(fitnesses[i] != -1000 && fitnesses[i] > fitnesses[maxPos]){
                    maxPos = i;
                }
            }
            if(fitnesses[maxPos] > curFit){
                switch (maxPos){
                    case 0: a.i--;
                        break;
                    case 1: a.i++;
                        break;
                    case 2: a.j--;
                        break;
                    case 3: a.j++;
                        break;
                    }
                }
            }
        return landscape;
    }
    
    public double calcSMA(ArrayList<dayData> data, int n1, int n2) {
		// n1 is the number of days the short term SMA is calculated from 
		// n2 is the number of days the long term EMA is calculated from
		
		double short_term_SMA_sum = 0.0;
		double long_term_SMA_sum = 0.0;
		double short_term_SMA = 0.0;
		double long_term_SMA = 0.0;
		
		double profit = 0.0;
		double last_sell_value = 0.0;
		boolean long_term_greater = false;
		
		for (int i = 0; i < data.size(); i++) {
			short_term_SMA_sum += data.get(i).close;
			long_term_SMA_sum += data.get(i).close;
			
			if (i >= n1) {
				short_term_SMA_sum -= data.get(i - n1).close;
				short_term_SMA = short_term_SMA_sum / n1;
			}
			if (i >= n2) {
				long_term_SMA_sum -= data.get(i - n2).close;
				long_term_SMA = long_term_SMA_sum / n2;
				
				if (short_term_SMA > long_term_SMA && long_term_greater) {
					profit += (data.get(i).close);
					last_sell_value = profit;
					long_term_greater = false;
					//System.out.println("SELL on " + data.get(i).date);
				}
				if (long_term_SMA > short_term_SMA && !long_term_greater) {
					profit -= data.get(i).close;
					long_term_greater = true;
					//System.out.println("BUY on " + data.get(i).date);
				}
			}
		}
		return last_sell_value;
	}
    
    public double calcEMA(ArrayList<dayData> data, int n1, int n2, double alpha) {
		double long_term_SMA_sum = 0.0;
		double short_term_EMA = 0.0;
		double long_term_SMA = 0.0;
		
		double profit = 0.0;
		double last_sell_value = 0.0;
		boolean long_term_greater = false;
		
		for (int i = 0; i < data.size(); i++) {
			short_term_EMA = short_term_EMA + alpha * (data.get(i).close - short_term_EMA);
			long_term_SMA_sum += data.get(i).close;
			
			if (i >= n2) {
				long_term_SMA_sum -= data.get(i - n2).close;
				long_term_SMA = long_term_SMA_sum / n2;
				
				if (short_term_EMA > long_term_SMA && long_term_greater) {
					profit += (data.get(i).close);
					last_sell_value = profit;
					long_term_greater = false;
					//System.out.println("SELL on " + data.get(i).date);
				}
				if (long_term_SMA > short_term_EMA && !long_term_greater) {
					profit -= data.get(i).close;
					long_term_greater = true;
					//System.out.println("BUY on " + data.get(i).date);
				}
			}
		}
		return last_sell_value;
	}
}
