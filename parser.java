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
		int nmax = 10;
		int mmin = 10;
		int mmax = 20;
		smaLandscape Land = new smaLandscape(nmin, nmax, mmin, mmax);
		double[][] landscape = new double[nmax - nmin][mmax - mmin];
		for (int i = 0; i < 1; i++) {
			landscape = Land.step(data);
			display(landscape);
		}
	}
	
	public static void display(double[][] landscape) {
		for (int i = 0; i < landscape.length; i++) {
			for (int j = 0; j < landscape[0].length; j++) {
				System.out.print(landscape[i][j] + ", ");
			}
			System.out.println();
		}
	}
	
}
