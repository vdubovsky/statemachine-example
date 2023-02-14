package io.vdubovsky.statemachineexample.statemachine.action;

import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActionFactory {

    private final List<ActionAware<String, String>> actions;
    private final List<ActionAware<PingPongState, PingPongEvent>> pingPongActions;

    public Action<String, String> getAction(String stateMachineId, String stateEntryPoint){
        return actions.stream()
                .filter(a -> a.getStateEntryPoint().equals(stateEntryPoint)
                && a.getStateMachineId().equals(stateMachineId))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Action not found, stateMachineId: %s, stateEntryPoint: %s"
                        .formatted(stateMachineId, stateEntryPoint)));
    };

    public Action<PingPongState, PingPongEvent> getAction(String stateMachineId, PingPongState stateEntryPoint){
        return pingPongActions.stream()
                .filter(a -> a.getStateEntryPoint().equals(stateEntryPoint)
                        && a.getStateMachineId().equals(stateMachineId))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Action not found, stateMachineId: %s, stateEntryPoint: %s"
                        .formatted(stateMachineId, stateEntryPoint)));
    };
}
