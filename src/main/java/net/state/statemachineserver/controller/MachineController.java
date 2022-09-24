package net.state.statemachineserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.state.statemachineserver.dto.MachineDTO;
import net.state.statemachineserver.model.Machine;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@CrossOrigin(origins = "*")
public class MachineController {

    // Dependency Injector 🤿
    @Autowired
    private Machine machine;

    @PostMapping("/minimize")
    public String[][] minimizeMachine(@RequestBody MachineDTO data) {
        System.out.println(data.getMachineType());
        String[][] minimized = !data.getMachineType() ? machine.minimizeMealy(data.getMatrix(), false)
                : machine.minimizeMoore(data.getMatrix(), false);

        return minimized;
    }
}
