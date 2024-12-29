package pl.com.mikrzg.workplanner.backend;

import java.util.List;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class Solution {

	@ValueRangeProvider(id = "employee4Shift")
	@ProblemFactCollectionProperty
	private List<AvailableEmployee> employees;

	@ValueRangeProvider
	@ProblemFactCollectionProperty
	private List<Shift> shifts;

	@PlanningEntityCollectionProperty
	private List<Employee2Shift> entities;

	@PlanningScore
	private HardSoftScore score;

	public List<AvailableEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<AvailableEmployee> employees) {
		this.employees = employees;
	}

	public List<Shift> getShifts() {
		return shifts;
	}

	public void setShifts(List<Shift> shifts) {
		this.shifts = shifts;
	}

	public List<Employee2Shift> getEntities() {
		return entities;
	}

	public void setEntities(List<Employee2Shift> entities) {
		this.entities = entities;
	}

	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

}
