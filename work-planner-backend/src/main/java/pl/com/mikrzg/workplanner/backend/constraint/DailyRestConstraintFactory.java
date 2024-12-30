package pl.com.mikrzg.workplanner.backend.constraint;

import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Employee2Shift;
import pl.com.mikrzg.workplanner.backend.ShiftUtil;

class DailyRestConstraintFactory extends RestConstraintFactory {

	@Override
	protected boolean hasRest(AvailableEmployee employee) {
		return ShiftUtil.hasDailyRest(employee.getShifts().stream().map(Employee2Shift::getShift));
	}

}
