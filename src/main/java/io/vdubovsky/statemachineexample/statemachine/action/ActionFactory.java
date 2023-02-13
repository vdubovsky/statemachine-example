package io.vdubovsky.statemachineexample.statemachine.action;

import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActionFactory {

    private final List<ActionAware<String, String>> actions;

    public Action<String, String> getAction(String stateMachineId, String stateEntryPoint){
        return actions.stream()
                .filter(a -> a.getStateEntryPoint().equals(stateEntryPoint)
                && a.getStateMachineId().equals(stateMachineId))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Action not found, stateMachineId: %s, stateEntryPoint: %s"
                        .formatted(stateMachineId, stateEntryPoint)));
    };
}
