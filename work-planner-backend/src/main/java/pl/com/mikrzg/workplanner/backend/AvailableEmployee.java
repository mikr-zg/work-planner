package pl.com.mikrzg.workplanner.backend;

import java.util.ArrayList;
import java.util.List;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public class AvailableEmployee {

	private Employee employee;
	private int standardWorkingTimeInMinutes;
	private List<Absence> absences;

	@InverseRelationShadowVariable(sourceVariableName = "employee")
	private List<Employee2Shift> shifts = new ArrayList<>();

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int getStandardWorkingTimeInMinutes() {
		return standardWorkingTimeInMinutes;
	}

	public void setStandardWorkingTimeInMinutes(int standardWorkingTimeInMinutes) {
		this.standardWorkingTimeInMinutes = standardWorkingTimeInMinutes;
	}

	public List<Absence> getAbsences() {
		return absences;
	}

	public void setAbsences(List<Absence> absences) {
		this.absences = absences;
	}

	public List<Role> getRoles() {
		return employee.getRoles();
	}

	public List<Employee2Shift> getShifts() {
		return shifts;
	}

	public void setShifts(List<Employee2Shift> shifts) {
		this.shifts = shifts;
	}

}
