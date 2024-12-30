package pl.com.mikrzg.workplanner.backend.constraint;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Employee2Shift;
import pl.com.mikrzg.workplanner.backend.ShiftUtil;

class OverhoursConstraintFactory implements WorkPlannerConstraintFactory {

	@Override
	public Constraint create(ConstraintFactory factory) {
		return factory.forEach(AvailableEmployee.class)

				.map(this::getOverhoursInMinutes)

				.penalize(RulesWeight.HEIGHT.getScore(), minutes -> minutes)

				.asConstraint("overhours");
	}

	private Integer getOverhoursInMinutes(AvailableEmployee employee) {
		int standardWorkingTime = employee.getStandardWorkingTimeInMinutes();

		int overhours = getWorkingTime(employee) - standardWorkingTime;

		return Math.max(0, overhours);
	}

	private int getWorkingTime(AvailableEmployee employee) {
		return employee.getShifts().stream()

				.map(Employee2Shift::getShift)

				.map(ShiftUtil::getWorkingTimeInMinutes)

				.reduce(0, (a, b) -> Math.addExact(a, b));
	}

}
