package io.vdubovsky.statemachineexample.statemachine;

import org.springframework.statemachine.StateMachine;

import java.util.UUID;

public interface StateMachineRepository {

    StateMachine<String, String> findStateMachine(UUID processId);

    StateMachine<String, String> removeAndGet(UUID processId);

    void addStateMachine(UUID processId, StateMachine<String, String> stateMachine);
}

