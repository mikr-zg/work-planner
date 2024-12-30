package pl.com.mikrzg.workplanner.backend.constraint;

import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getEmployeeShifts;

import java.util.Arrays;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import ai.timefold.solver.test.api.score.stream.SingleConstraintVerification;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Solution;

@SpringBootTest
public class ShiftConflictConstraintTest {

	@Autowired
	private SolverConfig config;

	@Test
	public void whenEmployeesHaveOverlappingShifts_ThenPenalize() {
		TestShift shift1 = WorkPlannerTestDataFactory.getShift("2024-01-01 08:00", "2024-01-01 16:00");
		TestShift shift2 = WorkPlannerTestDataFactory.getShift("2024-01-01 15:00", "2024-01-01 23:00");

		AvailableEmployee employee = new AvailableEmployee();
		employee.setShifts(getEmployeeShifts(employee, Arrays.asList(shift1, shift2)));

		Solution solution = new Solution();
		solution.setShifts(Arrays.asList(shift1.getShift(), shift2.getShift()));
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizesByMoreThan(0);
	}

	@Test
	public void whenEmployeesHaventOverlappingShifts_ThenDontPenalize() {
		TestShift shift1 = WorkPlannerTestDataFactory.getShift("2024-01-01 08:00", "2024-01-01 16:00");
		TestShift shift2 = WorkPlannerTestDataFactory.getShift("2024-01-01 16:00", "2024-01-01 23:00");

		AvailableEmployee employee = new AvailableEmployee();
		employee.setShifts(getEmployeeShifts(employee, Arrays.asList(shift1, shift2)));

		Solution solution = new Solution();
		solution.setShifts(Arrays.asList(shift1.getShift(), shift2.getShift()));
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizesBy(0);
	}

	private @NonNull SingleConstraintVerification<Solution> verifyThat() {

		final ConstraintVerifier<WorkPlannerConstraintProvider, Solution> verifier = ConstraintVerifier.create(config);

		return verifier.verifyThat((provider, factory) -> new ShiftConflictConstraintFactory().create(factory));
	}

}
