package pl.com.mikrzg.workplanner.backend.constraint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.com.mikrzg.workplanner.backend.AvailableEmployee;
import pl.com.mikrzg.workplanner.backend.Employee;
import pl.com.mikrzg.workplanner.backend.Employee2Shift;
import pl.com.mikrzg.workplanner.backend.Role;
import pl.com.mikrzg.workplanner.backend.Shift;

public class WorkPlannerTestDataFactory {

	public static Employee getEmployee(String name, Role... roles) {
		Employee employee = new Employee();
		employee.setName(name);
		employee.setRoles(Arrays.asList(roles));
		return employee;
	}

	public static List<Employee2Shift> getEmployeeShifts(AvailableEmployee employee, List<TestShift> shifts) {
		List<Employee2Shift> employeeShifts = new ArrayList<>();
		for (int i = 0; i < shifts.size(); i++) {
			employeeShifts.add(getEmployee2Shift(employee, employee + String.valueOf(i), shifts.get(i)));
		}

		return employeeShifts;
	}

	public static Employee2Shift getEmployee2Shift(AvailableEmployee employee, String id, TestShift shift) {
		Employee2Shift employee2Shift = new Employee2Shift();
		employee2Shift.setId(id);
		employee2Shift.setEmployee(employee);
		employee2Shift.setShift(shift.getShift());
		return employee2Shift;
	}

	public static TestShift getShift(String start, String end) {
		return getShift(start, end, 0);
	}

	public static TestShift getShift(String start, String end, int minNumberOfEmployees) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return getShift(LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter),
				minNumberOfEmployees);
	}

	private static TestShift getShift(LocalDateTime start, LocalDateTime end, int minNumberOfEmployees) {
		Shift shift = new Shift();
		shift.setStart(start);
		shift.setEnd(end);
		shift.setMinNumberOfEmployees(minNumberOfEmployees);
		return new TestShift(shift);
	}

	public static Role getRole(String name) {
		Role role = new Role();
		role.setName(name);
		return role;
	}

}
