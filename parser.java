import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class NKModel {

	public static void main(String[] args) throws IOException {
		String url = "C:\\Users\\dpapp\\Desktop\\exchange.csv";
		
		ArrayList<dayData> data = new ArrayList<dayData>();
		
		int lineNum = 0; // skip first few lines
		for (String line : Files.readAllLines(Paths.get(url))) {
			if (lineNum < 2) {
				lineNum++;
				continue;
			}
			int firstComma = line.indexOf(',');
			String date = line.substring(0, firstComma);
			int secondComma = line.indexOf(',', firstComma + 1);
			double close = Double.parseDouble(line.substring(firstComma + 1, secondComma));
			int thirdComma = line.indexOf(',', secondComma + 1);
			double high = Double.parseDouble(line.substring(secondComma + 1, thirdComma));
			double low = Double.parseDouble(line.substring(thirdComma + 1));
			double open = 0;
			data.add(new dayData(date, close, open, high, low));
		}
		
		for (int i = 1; i < data.size(); i++) {
			data.get(i).open = data.get(i - 1).close;
		}
		
		int nmin = 1;
		int nmax = 50;
		int mmin = 3;
		int mmax = 100;
		int numagents = 300;
		smaLandscape Land = new smaLandscape(nmin, nmax, mmin, mmax, numagents);
		for (int i = 0; i < 10; i++) {
			Land.step(data);
			//display(Land.landscape);
		}
		System.out.println("Global max is : " + Land.global_max);
	}
	
	public static void display(double[][] landscape) {
		for (int i = 0; i < landscape.length; i++) {
			for (int j = 0; j < landscape[0].length; j++) {
				System.out.print(landscape[i][j] + ", ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static double calcSMA(ArrayList<dayData> data, int n1, int n2) {
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
	
	public static double calcEMA(ArrayList<dayData> data, int n1, int n2, double alpha) {
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
