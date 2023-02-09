package io.vdubovsky.statemachineexample.statemachine.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "app.state-machine.repository.type", havingValue = "IN_MEMORY", matchIfMissing = true)
public class InMemoryStateMachineRepository implements StateMachineRepository {

    private Map<UUID, StateMachine> stateMachines = new ConcurrentHashMap<>();

    @Override
    public StateMachine findStateMachine(UUID processId) {
        return stateMachines.get(processId);
    }

    @Override
    public void remove(UUID processId) {
        stateMachines.remove(processId);
    }

    @Override
    public void addStateMachine(StateMachine stateMachine) {
        stateMachines.put(stateMachine.getUuid(), stateMachine);
    }
}
