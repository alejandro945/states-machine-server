package net.state.statemachineserver.model;

import org.springframework.stereotype.Service;

@Service
public class Machine {

    /**
     * Name: minimizeMealy
     * Method used to minimize a Mealy machine from a Java String matrix. <br>
     * <b>pre: </b> The table (String matrix) of the Mealy machine is already
     * initialized and isn't empty. <br>
     * <b>post: </b> The table (String matrix) of the Mealy machine, after being
     * eventually converted to a connected machine, is minimized. <br>
     * 
     * @param matrix - Java String matrix representing a Mealy machine - matrix =
     *               String[][], matrix != null
     * @param format - Jave Boolean that determinates if we are calculating the related (true)
     *               or the minimized (false)
     * @return A String matrix with the minimized Mealy machine table.
     */
    public String[][] minimizeMealy(String[][] matrix, boolean format) {
        Mealy mealy = new Mealy(matrix);
        return mealy.partitioning(format);
    }

    /**
     * Name: minimizeMoore
     * Method used to minimize a Moore machine from a Java String matrix. <br>
     * <b>pre: </b> The table (String matrix) of the Moore machine is already
     * initialized and isn't empty. <br>
     * <b>post: </b> The table (String matrix) of the Moore machine, after being
     * eventually converted to a connected machine, is minimized. <br>
     * 
     * @param matrix - Java String matrix representing a Moore machine - matrix =
     *               String[][], matrix != null
     * @return A String matrix with the minimized Moore machine table.
     */
    public String[][] minimizeMoore(String[][] matrix, boolean format) {
        Moore moore = new Moore(matrix);
        return moore.partitioning(format);
    }
}