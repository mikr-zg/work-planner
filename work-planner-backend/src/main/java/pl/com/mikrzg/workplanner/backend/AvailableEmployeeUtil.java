package pl.com.mikrzg.workplanner.backend;

public class AvailableEmployeeUtil {

	public static boolean hasDailyRest(AvailableEmployee employee) {
		return ShiftUtil.hasDailyRest(employee.getShifts().stream().map(Employee2Shift::getShift));
	}

	public static boolean hasWeeklyRest(AvailableEmployee employee) {
		return ShiftUtil.hasDailyRest(employee.getShifts().stream().map(Employee2Shift::getShift));
	}

}
