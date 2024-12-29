package pl.com.mikrzg.workplanner.backend;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

public abstract class ShiftUtil {

	private ShiftUtil() {
	}

	public static boolean isOverlapping(Stream<Shift> streamOfShifts) {

		Mutable<Shift> previous = new MutableObject<>();

		Predicate<Shift> filter = shift -> {

			boolean isOverlapping = isOverlapping(previous.getValue(), shift);

			previous.setValue(shift);

			return isOverlapping;

		};

		Comparator<Shift> comparator = (s1, s2) -> s1.getStart().compareTo(s2.getStart());

		return streamOfShifts.sorted(comparator).filter(filter).findFirst().isPresent();
	}

	private static boolean isOverlapping(Shift shift1, Shift shift2) {

		if (shift1 == null || shift2 == null) {
			return false;
		}

		LocalDateTime start1 = shift1.getStart();
		LocalDateTime end1 = shift1.getEnd();

		LocalDateTime start2 = shift2.getStart();
		LocalDateTime end2 = shift2.getEnd();

		return isOverlapping(start1, end1, start2, end2);

	}

	private static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2,
			LocalDateTime end2) {
		return !(start1.isAfter(end2) || start2.isAfter(end1));
	}

	public static long durationInMinutes(Shift shift) {
		return ChronoUnit.MINUTES.between(shift.getStart(), shift.getEnd());
	}

}
