package io.vdubovsky.statemachineexample.statemachine.impl;

import lombok.Data;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachineRepository {

    private Map<UUID, StateMachine<String, String>> stateMachines = new ConcurrentHashMap<>();

    public StateMachine<String, String> findStateMachine(UUID processId){
        return stateMachines.get(processId);
    }

    public StateMachine<String, String> removeAndGet(UUID processId){
        return stateMachines.remove(processId);
    }

    public void addStateMachine(UUID processId, StateMachine<String, String> stateMachine){
        stateMachines.put(processId, stateMachine);
    }

}
