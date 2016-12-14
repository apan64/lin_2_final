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
   public float[][] landscape;
   public smaAgent[] agents;
   public int agentCount;
   public int idCount;
   public Random rnd = new Random();
    public smaLandscape(int min1, int max1, int min2, int max2)
    {
        param_1_min = min1;
        param_2_min = min2;
        param_1_max = max1;
        param_2_max = max2;
        landscape = new float[max1 - min1 + 1][max2 - min2 + 1];
        agents = new smaAgent[10];
        for(int i = 0; i < 10; i++){
            agents[i] = new smaAgent(rnd.nextInt(max1 - min1), rnd.nextInt(max2 - min2), i);
        }
        for(int i = 0; i <= (max1 - min1); i++){
            for(int j = 0; j <= (max2 - min2); j++){
                //int randInt = rnd.nextInt(10)
                landscape[i][j] = 0;
            }
        }
        
    
    }

    public float fitness(int i, int j)
    {
        float profit = 0;
        
        return profit;
    }
    public float[][] step()
    {
        for(smaAgent a: agents){
            float curFit = fitness(a.i, a.j);
            landscape[a.i][a.i] = curFit;
            float[] fitnesses = new float[4];
            if(a.i != param_1_min){
                if(landscape[a.i - 1][a.j] == 0){
                    fitnesses[0] = fitness(a.i - 1, a.j);
                }
                else{
                    fitnesses[0] = landscape[a.i - 1][a.j];
                }
            }
            if(a.i != param_1_max){
                if(landscape[a.i + 1][a.j] == 0){
                    fitnesses[1] = fitness(a.i + 1, a.j);
                }
                else{
                    fitnesses[1] = landscape[a.i + 1][a.j];
                }
            }
            if(a.j != param_2_min){
                if(landscape[a.i][a.j - 1] == 0){
                    fitnesses[2] = fitness(a.i, a.j - 1);
                }
                else{
                    fitnesses[2] = landscape[a.i][a.j - 1];
                }
            }
            if(a.j != param_2_max){
                if(landscape[a.i][a.j + 1] == 0){
                    fitnesses[3] = fitness(a.i, a.j + 1);
                }
                else{
                    fitnesses[3] = landscape[a.i][a.j + 1];
                }
            }
            int maxPos = 0;
            for(int i = 1; i < 4; i++){
                if(fitnesses[maxPos] == 0 && fitnesses[i] != 0){
                    maxPos = i;
                }
                if(fitnesses[i] != 0 && fitnesses[i] > fitnesses[maxPos]){
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
}
