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
public class DailyRestConstraintTest {

	@Autowired
	private SolverConfig config;

	private static Stream<Arguments> parameters4WhenEmployeeHasNotEnoughDailyRest() {

		List<List<TestShift>> arguments = Arrays.asList(

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 02:30", "2024-01-02 08:30")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 00:00", "2024-01-02 08:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-01 20:00", "2024-01-02 04:00"),
						getShift("2024-01-02 08:00", "2024-01-02 16:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 03:00", "2024-01-02 11:00"),
						getShift("2024-01-02 21:59", "2024-01-03 06:00"))

		);

		return arguments.stream().map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource("parameters4WhenEmployeeHasNotEnoughDailyRest")
	public void whenEmployeeHasNotEnoughDailyRest_thenPenalize(List<TestShift> shifts) {

		AvailableEmployee employee = new AvailableEmployee();
		employee.setShifts(getEmployeeShifts(employee, shifts));

		Solution solution = new Solution();
		solution.setShifts(shifts.stream().map(TestShift::getShift).toList());
		solution.setEmployees(Arrays.asList(employee));
		solution.setEntities(employee.getShifts());

		verifyThat().givenSolution(solution).penalizes();

	}

	private static Stream<Arguments> parameters4WhenEmployeeHasEnoughDailyRest() {

		List<List<TestShift>> arguments = Arrays.asList(

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 03:00", "2024-01-02 11:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-01 16:30", "2024-01-01 21:00")),

				Arrays.asList(getShift("2024-01-01 08:00", "2024-01-01 16:00"),
						getShift("2024-01-02 03:00", "2024-01-02 11:00"),
						getShift("2024-01-02 22:00", "2024-01-03 06:00"))

		);

		return arguments.stream().map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource("parameters4WhenEmployeeHasEnoughDailyRest")
	public void whenEmployeeHasEnoughDailyRest_thenDontPenalize(List<TestShift> shifts) {

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

		return verifier.verifyThat((provider, factory) -> new DailyRestConstraintFactory().create(factory));
	}

}
