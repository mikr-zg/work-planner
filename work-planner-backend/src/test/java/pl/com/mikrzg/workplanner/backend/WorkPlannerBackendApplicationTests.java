package pl.com.mikrzg.workplanner.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ai.timefold.solver.core.config.solver.SolverConfig;

@SpringBootTest
class WorkPlannerBackendApplicationTests {

	@Autowired
	SolverConfig manager;

	@Test
	void contextLoads() {
	}

}
