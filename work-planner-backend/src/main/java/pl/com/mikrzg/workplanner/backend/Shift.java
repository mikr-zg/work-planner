package pl.com.mikrzg.workplanner.backend;

import java.time.LocalDateTime;
import java.util.List;

public class Shift {
	private LocalDateTime start;
	private LocalDateTime end;

	private int minNumberOfEmployees;
	private Integer maxNumberOfEmployees;

	private List<Role> requiredRoles;

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public int getMinNumberOfEmployees() {
		return minNumberOfEmployees;
	}

	public void setMinNumberOfEmployees(int minNumberOfEmployees) {
		this.minNumberOfEmployees = minNumberOfEmployees;
	}

	public Integer getMaxNumberOfEmployees() {
		return maxNumberOfEmployees;
	}

	public void setMaxNumberOfEmployees(Integer maxNumberOfEmployees) {
		this.maxNumberOfEmployees = maxNumberOfEmployees;
	}

	public List<Role> getRequiredRoles() {
		return requiredRoles;
	}

	public void setRequiredRoles(List<Role> requiredRoles) {
		this.requiredRoles = requiredRoles;
	}

}
