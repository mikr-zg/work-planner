package pl.com.mikrzg.workplanner.backend;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Employee2Shift {

	@PlanningId
	private String id;

	private Shift shift;

	@PlanningVariable(valueRangeProviderRefs = "employee4Shift", allowsUnassigned = true)
	private AvailableEmployee employee;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	public AvailableEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(AvailableEmployee employee) {
		this.employee = employee;
	}

}
