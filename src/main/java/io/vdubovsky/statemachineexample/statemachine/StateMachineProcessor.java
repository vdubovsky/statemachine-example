package io.vdubovsky.statemachineexample.statemachine;

import org.springframework.statemachine.StateMachine;

import java.util.UUID;

public interface StateMachineProcessor {

    Object startProcessAndGetResult(StateMachine stateMachine, Object input);

    String getId();

    default Object sendEventAndGetResult(UUID processId, String event, Object input) {
        throw new RuntimeException(getId() + " state machine doesn't support incoming events");
    }
}
