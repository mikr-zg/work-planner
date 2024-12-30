package pl.com.mikrzg.workplanner.backend.constraint;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;

abstract class RestConstraintFactory implements WorkPlannerConstraintFactory {

	protected abstract boolean hasRest(AvailableEmployee employee);

	@Override
	public Constraint create(ConstraintFactory factory) {
		return factory.forEach(AvailableEmployee.class)

				.filter(employee -> !hasRest(employee))

				.penalize(RulesWeight.VERY_HEIGHT.getScore())

				.asConstraint(getClass().getSimpleName());
	}

}
