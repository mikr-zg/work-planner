package pl.com.mikrzg.workplanner.backend.constraint;

import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getEmployeeShifts;
import static pl.com.mikrzg.workplanner.backend.constraint.WorkPlannerTestDataFactory.getShift;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import ai.timefold.solver.test.api.score.stream.SingleConstraintVerification;
import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Solution;

@SpringBootTest
public class WeeklyRestConstraintTest {

	@Autowired
	private SolverConfig config;

	private static Stream<Arguments> parameters4WhenEmployeeHasNotEnoughWeeklyRest() {

		List<List<TestShift>> arguments = Arrays.asList(

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 08:00", "2024-01-02 16:00"),
						getShift("2024-01-03 08:00", "2024-01-03 16:00"),
						getShift("2024-01-04 08:00", "2024-01-04 16:00"),
						getShift("2024-01-05 08:00", "2024-01-05 16:00"),
						getShift("2024-01-06 08:00", "2024-01-06 16:00"),
						getShift("2024-01-07 08:00", "2024-01-07 16:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 08:00", "2024-01-02 16:00"),
						getShift("2024-01-03 08:00", "2024-01-03 16:00"),
						getShift("2024-01-04 08:00", "2024-01-04 16:00"),
						getShift("2024-01-05 08:00", "2024-01-05 16:00"),
						getShift("2024-01-06 08:00", "2024-01-06 16:00"),
						getShift("2024-01-07 08:00", "2024-01-07 16:00"),
						getShift("2024-01-09 08:00", "2024-01-11 16:00"))

		);

		return arguments.stream().map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource("parameters4WhenEmployeeHasNotEnoughWeeklyRest")
	public void whenEmployeeHasNotEnoughWeeklyRest_thenPenalize(List<TestShift> shifts) {

		AvailableEmployee employee = new AvailableEmployee();
		employee.setShifts(getEmployeeShifts(employee, shifts));

		Solution solution = new Solution();
		solution.setShifts(shifts.stream().map(TestShift::getShift).toList());
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizes();

	}

	private static Stream<Arguments> parameters4WhenEmployeeHasEnoughWeeklyRest() {

		List<List<TestShift>> arguments = Arrays.asList(

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 08:00", "2024-01-02 16:00"),
						getShift("2024-01-03 08:00", "2024-01-03 16:00"),
						getShift("2024-01-04 08:00", "2024-01-04 16:00"),
						getShift("2024-01-05 08:00", "2024-01-05 16:00"),
						getShift("2024-01-06 08:00", "2024-01-06 16:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 08:00", "2024-01-02 16:00"),
						getShift("2024-01-03 08:00", "2024-01-03 16:00"),
						getShift("2024-01-04 08:00", "2024-01-04 16:00"),
						getShift("2024-01-05 08:00", "2024-01-05 16:00"),
						getShift("2024-01-06 08:00", "2024-01-06 16:00"),
						getShift("2024-01-08 03:00", "2024-01-08 11:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 08:00", "2024-01-02 16:00"),
						getShift("2024-01-03 08:00", "2024-01-03 16:00"),
						getShift("2024-01-04 08:00", "2024-01-04 16:00"),
						getShift("2024-01-06 03:00", "2024-01-06 11:00"),
						getShift("2024-01-07 03:00", "2024-01-07 11:00"),
						getShift("2024-01-08 03:00", "2024-01-08 11:00"),
						getShift("2024-01-09 03:00", "2024-01-09 11:00"))

		);

		return arguments.stream().map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource("parameters4WhenEmployeeHasEnoughWeeklyRest")
	public void whenEmployeeHasEnoughWeeklyRest_thenDontPenalize(List<TestShift> shifts) {

		AvailableEmployee employee = new AvailableEmployee();
		employee.setShifts(getEmployeeShifts(employee, shifts));

		Solution solution = new Solution();
		solution.setShifts(shifts.stream().map(TestShift::getShift).toList());
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizes(0);

	}

	private @NonNull SingleConstraintVerification<Solution> verifyThat() {

		final ConstraintVerifier<WorkPlannerConstraintProvider, Solution> verifier = ConstraintVerifier.create(config);

		return verifier.verifyThat((provider, factory) -> new WeeklyRestConstraintFactory().create(factory));
	}

}
