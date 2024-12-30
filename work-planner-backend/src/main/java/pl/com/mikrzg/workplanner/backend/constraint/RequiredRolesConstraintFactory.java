package pl.com.mikrzg.workplanner.backend.constraint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Employee2Shift;
import pl.com.mikrzg.workplanner.backend.Role;
import pl.com.mikrzg.workplanner.backend.Shift;

class RequiredRolesConstraintFactory implements WorkPlannerConstraintFactory {

	@Override
	public Constraint create(ConstraintFactory factory) {
		return factory.forEach(Employee2Shift.class)

				.groupBy(Employee2Shift::getShift, ConstraintCollectors.toList(Employee2Shift::getEmployee))

				.filter((s, e) -> !hasRequiredRoles(s, e))

				.penalize(RulesWeight.HEIGHT.getScore())

				.asConstraint("roles");
	}

	private boolean hasRequiredRoles(Shift shift, List<AvailableEmployee> employees) {

		if (CollectionUtils.isEmpty(employees)) {
			return false;
		}

		List<Role> requiredRoles = shift.getRequiredRoles();
		if (CollectionUtils.isEmpty(requiredRoles)) {
			return true;
		}

		List<Role> roles = getAvailableRoles(employees);

		return CollectionUtils.containsAll(roles, requiredRoles);
	}

	private List<Role> getAvailableRoles(List<AvailableEmployee> employees) {
		return employees.stream()

				.map(AvailableEmployee::getRoles)

				.reduce(new ArrayList<>(), ListUtils::sum);
	}

}
