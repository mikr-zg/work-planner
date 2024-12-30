package pl.com.mikrzg.workplanner.backend.constraint;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;

interface WorkPlannerConstraintFactory {

	Constraint create(ConstraintFactory factory);

}
