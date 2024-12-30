package pl.com.mikrzg.workplanner.backend.constraint;

import pl.com.mikrzg.workplanner.backend.Shift;

class TestShift {

	private final Shift shift;

	public TestShift(Shift shift) {
		this.shift = shift;
	}

	public Shift getShift() {
		return shift;
	}

	@Override
	public String toString() {
		return String.format("[%s %s]", shift.getStart(), shift.getEnd());
	}

}