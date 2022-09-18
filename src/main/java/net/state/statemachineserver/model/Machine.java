package net.state.statemachineserver.model;

public class Machine {

    public String[][] minimizeMealy(String[][] matrix, boolean format) {
        Mealy mealy = new Mealy(matrix);
        return mealy.partitioning(format);
    }

   
    public String[][] minimizeMoore(String[][] matrix, boolean format) {
        Moore moore = new Moore(matrix);
        return moore.partitioning(format);
    }
}