package pl.com.mikrzg.workplanner.backend.constraint;

import java.util.stream.Stream;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Employee2Shift;
import pl.com.mikrzg.workplanner.backend.Shift;
import pl.com.mikrzg.workplanner.backend.ShiftUtil;

class ShiftConflictConstraintFactory implements WorkPlannerConstraintFactory {

	@Override
	public Constraint create(ConstraintFactory factory) {
		return factory.forEach(AvailableEmployee.class)

				.filter(employee -> {

					Stream<Shift> shifts = employee.getShifts().stream().map(Employee2Shift::getShift);

					return ShiftUtil.isOverlapping(shifts);
				})

				.penalize(RulesWeight.VERY_HEIGHT.getScore())

				.asConstraint(getClass().getSimpleName());
	}

}
