package pl.com.mikrzg.workplanner.backend;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jspecify.annotations.NonNull;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;

public class Rules implements ConstraintProvider {

	enum RulesWeight {

		VERY_HEIGHT(HardSoftScore.ofHard(1000)),

		HEIGHT(HardSoftScore.ofHard(100)),

		NORMAL(HardSoftScore.ofHard(10)),

		LOW(HardSoftScore.ONE_HARD);

		private HardSoftScore score;

		private RulesWeight(HardSoftScore score) {
			this.score = score;
		}

		public HardSoftScore getScore() {
			return score;
		}

	}

	@Override
	public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory constraintFactory) {
		return new Constraint[] {

				employeesWorkingNorm(constraintFactory),

				employeesRoles(constraintFactory),

				oneShiftAtTime(constraintFactory),

				minEmployeesPerShift(constraintFactory),

				maxEmployeesPerShift(constraintFactory),

				overhours(constraintFactory)

		};
	}

	private Constraint oneShiftAtTime(@NonNull ConstraintFactory constraintFactory) {

		return constraintFactory.forEach(AvailableEmployee.class)

				.filter(employee -> {

					Stream<Shift> shifts = employee.getShifts().stream().map(Employee2Shift::getShift);

					return ShiftUtil.isOverlapping(shifts);

				})

				.penalize(RulesWeight.VERY_HEIGHT.getScore())

				.asConstraint("oneShiftAtTime");

	}

	private Constraint employeesRoles(@NonNull ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Employee2Shift.class)

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

		List<Role> roles = employees.stream().map(AvailableEmployee::getRoles).reduce(new ArrayList<>(),
				ListUtils::sum);

		return CollectionUtils.containsAll(roles, requiredRoles);

	}

	private Constraint minEmployeesPerShift(@NonNull ConstraintFactory constraintFactory) {
		return constraintFactory.forEachIncludingUnassigned(Employee2Shift.class)

				.groupBy(e2s -> e2s.getShift(), ConstraintCollectors.toList(Employee2Shift::getEmployee))

				.map((shift, employees) -> {

					int min = shift.getMinNumberOfEmployees();
					int count = (int) employees.stream().filter(Objects::nonNull).count();

					return min - count;
				})

				.penalize(RulesWeight.HEIGHT.getScore(), value -> Math.max(0, value))

				.asConstraint("minEmployeesPerShift");
	}

	private Constraint maxEmployeesPerShift(@NonNull ConstraintFactory constraintFactory) {
		return constraintFactory.forEachIncludingUnassigned(Employee2Shift.class)

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

				.asConstraint("maxEmployeesPerShift");
	}

	private Constraint employeesWorkingNorm(@NonNull ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(AvailableEmployee.class)

				.map(employee -> {

					int minutes = getWorkingTimeInMinutes(employee.getShifts());

					return employee.getStandardWorkingTimeInMinutes() - minutes;

				}).penalize(RulesWeight.HEIGHT.getScore(), minutes -> {
					return Math.max(0, minutes);
				}).asConstraint("employeesWorkingNorm");
	}

	private Constraint overhours(@NonNull ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(AvailableEmployee.class)

				.map(employee -> {

					int minutes = getWorkingTimeInMinutes(employee.getShifts());

					return minutes - employee.getStandardWorkingTimeInMinutes();

				}).penalize(RulesWeight.HEIGHT.getScore(), minutes -> {
					return Math.max(0, minutes);
				}).asConstraint("overhours");
	}

	private int getWorkingTimeInMinutes(List<Employee2Shift> shifts) {

		if (CollectionUtils.isEmpty(shifts)) {
			return 0;
		}

		return shifts.stream().map(this::getWorkingTimeInMinutes).reduce(0, Math::addExact);
	}

	private int getWorkingTimeInMinutes(Employee2Shift shift) {
		return getWorkingTimeInMinutes(shift.getShift());
	}

	private int getWorkingTimeInMinutes(Shift shift) {
		return (int) ChronoUnit.MINUTES.between(shift.getStart(), shift.getEnd());
	}

}
