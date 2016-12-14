public class dayData {
	public String date;
	public double close;
	public double open;
	public double high;
	public double low;
	
	// used for Heikin Ashi
	public double HAclose;
	public double HAopen;
	public double HAhigh;
	public double HAlow;
	
	dayData(String d, double c, double o, double h, double l) {
		date = d;
		close = c;
		open = o;
		high = h;
		low = l;
	}
	
	void displayDayData() {
		System.out.println(date + " | " + close + " | " + open + " | " + high + " | " + low);
	}
	
	void heikinAshi(dayData prev) {
		if (prev == null) {
			HAclose = (open + high + low + close) / 4;
			HAopen = (open + close) / 2;
			HAhigh = high;
			HAlow = low;
		}
		else {
			HAclose = (open + high + low + close) / 4;
			HAopen = (prev.open + prev.close) / 2;
			HAhigh = Math.max(Math.max(high, open), close);
			HAlow = Math.min(Math.min(low, open), close);
		}
	}
}
