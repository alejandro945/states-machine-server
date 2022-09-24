package net.state.statemachineserver.dto;

import lombok.Data;

@Data
public class MachineDTO {
    private Boolean machineType;
    private String[][] matrix;
}
