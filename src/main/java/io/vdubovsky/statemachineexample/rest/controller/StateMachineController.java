package io.vdubovsky.statemachineexample.rest.controller;

import io.vdubovsky.statemachineexample.statemachine.StateMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/state-machines")
@RequiredArgsConstructor
public class StateMachineController {

    private final StateMachineService stateMachineService;

    @PostMapping("/{processDefinitionId}/start")
    public Mono<UUID> startProcess(
            @PathVariable(name = "processDefinitionId") String processDefinitionId,
            @RequestBody Map<String, Object> parameters) {

        return Mono.defer(() -> Mono.justOrEmpty(stateMachineService.startProcess(processDefinitionId, parameters)));
    }
}
