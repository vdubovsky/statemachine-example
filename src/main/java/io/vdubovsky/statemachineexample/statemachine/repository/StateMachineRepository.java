package io.vdubovsky.statemachineexample.statemachine.repository;

import org.springframework.statemachine.StateMachine;

import java.util.UUID;

public interface StateMachineRepository {

    StateMachine findStateMachine(UUID processId);

    void remove(UUID processId);

    void addStateMachine(StateMachine stateMachine);
}

