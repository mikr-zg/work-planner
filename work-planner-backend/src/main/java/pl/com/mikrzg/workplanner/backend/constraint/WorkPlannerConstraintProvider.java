package pl.com.mikrzg.workplanner.backend.constraint;

import org.jspecify.annotations.NonNull;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;

public class WorkPlannerConstraintProvider implements ConstraintProvider {

	@Override
	public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory constraintFactory) {

		return new Constraint[] {

				new DailyRestConstraintFactory().create(constraintFactory),

				new MaximumEmployeesPerShiftConstraintFactory().create(constraintFactory),

				new MinimumEmployeesPerShiftConstraintFactory().create(constraintFactory),

				new OverhoursConstraintFactory().create(constraintFactory),

				new RequiredRolesConstraintFactory().create(constraintFactory),

				new ShiftConflictConstraintFactory().create(constraintFactory),

				new WeeklyRestConstraintFactory().create(constraintFactory),

				new WorkingNormConstraintFactory().create(constraintFactory)

		};
	}

}
