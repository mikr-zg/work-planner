package pl.com.mikrzg.workplanner.backend;

public class Configuration {

	private int dailyRest = 60 * 11;
	private int weeklyRest = 60 * 35;

	public int getDailyRest() {
		return dailyRest;
	}

	public void setDailyRest(int dailyRest) {
		this.dailyRest = dailyRest;
	}

	public int getWeeklyRest() {
		return weeklyRest;
	}

	public void setWeeklyRest(int weeklyRest) {
		this.weeklyRest = weeklyRest;
	}

}
