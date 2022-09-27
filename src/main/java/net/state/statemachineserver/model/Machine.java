package net.state.statemachineserver.model;

import org.springframework.stereotype.Service;

@Service
public class Machine {

    /**
     * This method is used to minimize a Mealy machine.
     * @param matrix - matrix represents a Mealy machine.
     * @param format - format determinates if we are calculating the related
     *                 or the minimized
     */

    public String[][] minimizeMealy(String[][] matrix, boolean format) {
        Mealy mealy = new Mealy(matrix);
        return mealy.partitioning(format);
    }

    /**
     * This method is used to minimize a Moore machine.
     * @param matrix - matrix represents a Moore machine.
     */
     
    public String[][] minimizeMoore(String[][] matrix, boolean format) {
        Moore moore = new Moore(matrix);
        return moore.partitioning(format);
    }
}