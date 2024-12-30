package pl.com.mikrzg.workplanner.backend.constraint;

import java.util.Objects;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import pl.com.mikrzg.workplanner.backend.Employee2Shift;

class MaximumEmployeesPerShiftConstraintFactory implements WorkPlannerConstraintFactory {

	@Override
	public Constraint create(ConstraintFactory factory) {
		return factory.forEachIncludingUnassigned(Employee2Shift.class)

				.groupBy(e2s -> e2s.getShift(), ConstraintCollectors.toList(Employee2Shift::getEmployee))

				.map((shift, employees) -> {

					Integer max = shift.getMaxNumberOfEmployees();
					if (max == null) {
						return 0;
					}

					int count = (int) employees.stream().filter(Objects::nonNull).count();

					return count - max;
				})

				.penalize(RulesWeight.HEIGHT.getScore(), value -> Math.max(0, value))

				.asConstraint(getClass().getName());
	}

}
