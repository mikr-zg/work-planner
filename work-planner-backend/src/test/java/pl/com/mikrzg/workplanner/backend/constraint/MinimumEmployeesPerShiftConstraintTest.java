package pl.com.mikrzg.workplanner.backend.constraint;

import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getEmployeeShifts;
import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getShift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import ai.timefold.solver.test.api.score.stream.SingleConstraintVerification;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Employee2Shift;
import pl.com.mikrzg.workplanner.backend.Solution;

@SpringBootTest
public class MinimumEmployeesPerShiftConstraintTest {

	@Autowired
	private SolverConfig config;

	@Test
	public void whenMinimumNumberOfEmployeesHasNotBeenReached_thenPenalize() {

		TestShift shift = getShift("2024-01-01 08:00", "2024-01-01 16:00");
		shift.getShift().setMinNumberOfEmployees(2);

		AvailableEmployee employee = new AvailableEmployee();
		employee.setShifts(getEmployeeShifts(employee, Arrays.asList(shift)));

		Solution solution = new Solution();
		solution.setShifts(Arrays.asList(shift.getShift()));
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizesMoreThan(0);
	}

	@Test
	public void whenMinimumNumberOfEmployeesHasBeenReached_thenDontPenalize() {

		TestShift shift = getShift("2024-01-01 08:00", "2024-01-01 16:00");
		shift.getShift().setMinNumberOfEmployees(2);

		AvailableEmployee employee = new AvailableEmployee();
		employee.setShifts(getEmployeeShifts(employee, Arrays.asList(shift)));

		AvailableEmployee secondEmployee = new AvailableEmployee();
		secondEmployee.setShifts(getEmployeeShifts(secondEmployee, Arrays.asList(shift)));

		Solution solution = new Solution();
		solution.setShifts(Arrays.asList(shift.getShift()));
		solution.setEmployees(Arrays.asList(employee, secondEmployee));
		solution.setEntities(sum(employee.getShifts(), secondEmployee.getShifts()));

		verifyThat().givenSolution(solution).penalizesBy(0);
	}

	@SafeVarargs
	private List<Employee2Shift> sum(List<Employee2Shift>... lists) {
		List<Employee2Shift> sum = new ArrayList<>();
		for (List<Employee2Shift> list : lists) {
			sum = ListUtils.sum(sum, list);
		}

		return sum;
	}

	private @NonNull SingleConstraintVerification<Solution> verifyThat() {

		final ConstraintVerifier<WorkPlannerConstraintProvider, Solution> verifier = ConstraintVerifier.create(config);

		return verifier
				.verifyThat((provider, factory) -> new MinimumEmployeesPerShiftConstraintFactory().create(factory));
	}

}
