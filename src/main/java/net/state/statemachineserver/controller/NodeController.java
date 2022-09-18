package net.state.statemachineserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class NodeController {

    // Dependency Injector 🤿
    // @Autowired
    // private NodeRepository nodeRepository;

    @GetMapping("nodes")
    public String getNodes() {
        return "Hi from server";
    }
}
