package org.optaplanner.examples.nurserostering;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.jdom.JDOMException;
import org.junit.Assert;
import org.junit.Test;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import org.optaplanner.examples.nurserostering.app.NurseRosteringApp;
import org.optaplanner.examples.nurserostering.domain.Employee;
import org.optaplanner.examples.nurserostering.domain.NurseRoster;
import org.optaplanner.examples.nurserostering.domain.Shift;
import org.optaplanner.examples.nurserostering.domain.ShiftAssignment;
import org.optaplanner.examples.nurserostering.domain.contract.ContractLineType;
import org.optaplanner.examples.nurserostering.domain.contract.MaxConsecutiveShiftsContractLine;
import org.optaplanner.examples.nurserostering.persistence.NurseRosteringImporter;

public class RosteringRulesTest {
    private SolverFactory<NurseRoster> solverFactory = SolverFactory.createFromXmlResource(NurseRosteringApp.SOLVER_CONFIG);
    private NurseRosteringImporter solutionImporter = new NurseRosteringImporter();

    @Test
    public void maxConsecutiveNightShifts() throws IOException, JDOMException {
        NurseRoster solution = solutionImporter.readSolution(
            new File("./src/test/resources/org/optaplanner/examples/nurserostering/data/rosteringMaxConsecutiveShiftsData.xml")
        );

        Employee employee = solution.getEmployeeList().iterator().next();
        MaxConsecutiveShiftsContractLine contractLine = solution.getContractLineList().stream()
            .filter(cl -> cl.getContractLineType() == ContractLineType.IDENTICAL_CONSECUTIVE_SHIFTS)
            .map(cl -> (MaxConsecutiveShiftsContractLine) cl)
            .filter(cl -> cl.getShiftTypeId().equals("N"))
            .findAny().get();

        List<ShiftAssignment> shiftAssignments = solution.getShiftList().stream()
            .filter(shift -> shift.getShiftType().getCode().equals("N"))
            .sorted(Comparator.comparing(Shift::getShiftDate))
            .limit(contractLine.getMaxValue() + 1)
            .map(shift -> createShiftAssignment(shift, employee))
            .collect(Collectors.toList());
        solution.setShiftAssignmentList(shiftAssignments);

        // when
        ScoreDirectorFactory<NurseRoster> scoreDirectorFactory = solverFactory.buildSolver().getScoreDirectorFactory();
        ScoreDirector<NurseRoster> scoreDirector = scoreDirectorFactory.buildScoreDirector();
        scoreDirector.setWorkingSolution(solution);
        scoreDirector.calculateScore();

        // then
        Assert.assertEquals(-contractLine.getWeight(), solution.getScore().getSoftScore());
    }

    private ShiftAssignment createShiftAssignment(Shift shift, Employee employee) {
        ShiftAssignment assignment = new ShiftAssignment();
        assignment.setId(shift.getId());
        assignment.setShift(shift);
        assignment.setEmployee(employee);
        return assignment;
    }
}
