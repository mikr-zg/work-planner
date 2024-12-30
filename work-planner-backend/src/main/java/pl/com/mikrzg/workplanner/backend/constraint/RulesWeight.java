package pl.com.mikrzg.workplanner.backend.constraint;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

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