package pl.com.mikrzg.workplanner.backend;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;

public class SolverTest {

	@Test
	public void solve() {

		SolverConfig config = new SolverConfig()

				.withSolutionClass(Solution.class)

				.withEntityClasses(AvailableEmployee.class, Employee2Shift.class)

				.withConstraintProviderClass(Rules.class)

				.withTerminationSpentLimit(Duration.ofSeconds(10));

		Solution solution = SolverFactory.<Solution>create(config).buildSolver().solve(loadSolution());

		print(solution);
	}

	private void print(Solution solution) {

		Map<Shift, List<Employee2Shift>> shift2Employees = solution.getEntities().stream()
				.collect(Collectors.groupingBy(Employee2Shift::getShift));

		List<Shift> shifts = shift2Employees.keySet().stream().sorted((a, b) -> a.getStart().compareTo(b.getStart()))
				.collect(Collectors.toList());

		System.err.println("HS: " + solution.getScore().hardScore());

		for (Shift shift : shifts) {

			List<Employee2Shift> employees = shift2Employees.get(shift);

			System.err.println("\nSTART: " + shift.getStart());
			System.err.println("END: " + shift.getEnd());

			for (Employee2Shift employee2Shift : employees) {
				AvailableEmployee employee = employee2Shift.getEmployee();
				if (employee != null) {
					System.err.print(employee.getEmployee().getName() + " ");
				}
			}

			System.err.println("");
		}

		for (AvailableEmployee employee : solution.getEmployees()) {

			long sum = employee.getShifts().stream().map(Employee2Shift::getShift).map(ShiftUtil::durationInMinutes)
					.reduce(0l, (a, b) -> a + b);

			System.err.println(employee.getEmployee().getName() + ": " + (sum / 60));

		}

	}

	private @NonNull Solution loadSolution() {
		List<Shift> shifts = getShifts(2025, Month.JANUARY);
		List<Employee> employees = getEmployees();

		Solution solution = new Solution();
		solution.setEmployees(getAvailableEmployees());
		solution.setShifts(shifts);

		List<Employee2Shift> e2s = new ArrayList<>();

		int i = 1;
		for (Shift shift : shifts) {
			for (Employee employee : employees) {
				Employee2Shift employees2Shift = new Employee2Shift();
				employees2Shift.setId(String.valueOf(i++));
				employees2Shift.setEmployee(null);
				employees2Shift.setShift(shift);
				e2s.add(employees2Shift);
			}
		}

		solution.setEntities(e2s);
		return solution;
	}

	private List<AvailableEmployee> getAvailableEmployees() {

		return getEmployees().stream().map(e -> {
			AvailableEmployee ae = new AvailableEmployee();
			ae.setEmployee(e);
			ae.setStandardWorkingTimeInMinutes(21 * 8 * 60);
			return ae;
		}).toList();
	}

	private List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<>();
		employees.add(createEmployee("1", "Pracownik funkcyjny"));
		employees.add(createEmployee("2", "Pracownik funkcyjny"));
		employees.add(createEmployee("3", "Pracownik"));
		employees.add(createEmployee("4", "Pracownik"));
		employees.add(createEmployee("5", "Pracownik"));
		employees.add(createEmployee("6", "Pracownik"));
		employees.add(createEmployee("7", "Pracownik"));
		employees.add(createEmployee("8", "Pracownik"));
		return employees;
	}

	private Employee createEmployee(String name, String... roles) {
		Employee employee = new Employee();
		employee.setName(name);
		employee.setRoles(getRoles(roles));
		return employee;
	}

	private List<Shift> getShifts(int year, Month month) {

		YearMonth yearMonth = YearMonth.of(year, month);

		List<Shift> shifts = new ArrayList<>();
		for (int day = 1; day <= 26; day++) {
			shifts.add(createShift(yearMonth.atDay(day)));
		}

		return shifts;
	}

	private Shift createShift(LocalDate day) {
		Shift shift = new Shift();
		shift.setStart(LocalDateTime.of(day, LocalTime.of(8, 30)));
		shift.setEnd(LocalDateTime.of(day, LocalTime.of(20, 15)));
		shift.setMinNumberOfEmployees(2);
		shift.setMaxNumberOfEmployees(5);
		shift.setRequiredRoles(getRoles("Pracownik funkcyjny", "Pracownik"));
		return shift;
	}

	private List<Role> getRoles(String... roles) {

		Function<String, Role> converter = name -> {
			Role role = new Role();
			role.setName(name);
			return role;
		};

		return Stream.of(roles).map(converter).toList();
	}

}
