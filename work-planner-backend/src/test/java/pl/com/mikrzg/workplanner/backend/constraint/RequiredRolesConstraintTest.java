package pl.com.mikrzg.workplanner.backend.constraint;

import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getEmployee;
import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getEmployeeShifts;
import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getRole;
import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getShift;

import java.util.Arrays;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import ai.timefold.solver.test.api.score.stream.SingleConstraintVerification;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Role;
import pl.com.mikrzg.workplanner.backend.Solution;

@SpringBootTest
public class RequiredRolesConstraintTest {

	@Autowired
	private SolverConfig config;

	@Test
	public void whenEmployeesHaveNotRequiredRoles_thenPenalize() {
		Role roleX = getRole("roleX");
		Role roleY = getRole("roleY");
		Role roleZ = getRole("roleZ");

		TestShift shift = getShift("2024-01-01 08:00", "2024-01-01 16:00");
		shift.getShift().setRequiredRoles(Arrays.asList(roleX, roleY, roleZ));

		AvailableEmployee employee = new AvailableEmployee();
		employee.setEmployee(getEmployee("A B", roleX, roleZ));
		employee.setShifts(getEmployeeShifts(employee, Arrays.asList(shift)));

		Solution solution = new Solution();
		solution.setShifts(Arrays.asList(shift.getShift()));
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizesByMoreThan(0);
	}

	@Test
	public void whenEmployeesHaveRequiredRoles_thenDontPenalize() {
		Role roleX = getRole("roleX");
		Role roleY = getRole("roleY");
		Role roleZ = getRole("roleZ");

		TestShift shift = getShift("2024-01-01 08:00", "2024-01-01 16:00");
		shift.getShift().setRequiredRoles(Arrays.asList(roleX, roleY, roleZ));

		AvailableEmployee employee = new AvailableEmployee();
		employee.setEmployee(getEmployee("A B", roleX, roleZ, roleY));
		employee.setShifts(getEmployeeShifts(employee, Arrays.asList(shift)));

		Solution solution = new Solution();
		solution.setShifts(Arrays.asList(shift.getShift()));
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizesBy(0);
	}

	private @NonNull SingleConstraintVerification<Solution> verifyThat() {

		final ConstraintVerifier<WorkPlannerConstraintProvider, Solution> verifier = ConstraintVerifier.create(config);

		return verifier.verifyThat((provider, factory) -> new RequiredRolesConstraintFactory().create(factory));
	}

}
