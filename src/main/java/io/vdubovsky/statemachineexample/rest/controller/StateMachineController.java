package io.vdubovsky.statemachineexample.rest.controller;

import io.vdubovsky.statemachineexample.statemachine.StateMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/state-machines")
@RequiredArgsConstructor
public class StateMachineController {

    private final StateMachineService stateMachineService;

    @PostMapping("/{processDefinitionId}/start")
    public Mono<Object> startProcess(
            @PathVariable(name = "processDefinitionId") String processDefinitionId,
            @RequestBody Object input) {

        return Mono.defer(() -> Mono.justOrEmpty(stateMachineService.executeProcessAndGetResult(processDefinitionId, input)));
    }

    @PostMapping("{processDefinitionId}/processes/{processId}/events/{event}")
    public Mono<Object> sendEvent(
            @PathVariable(name = "processDefinitionId") String processDefinitionId,
            @PathVariable(name = "processId") UUID processId,
            @PathVariable(name = "event") String event,
            @RequestBody Object input
    ) {

        return Mono.defer(() -> Mono.justOrEmpty(stateMachineService.sendEvent(processDefinitionId, processId, event, input)));
    }
}
