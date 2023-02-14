package io.vdubovsky.statemachineexample.statemachine;

import io.vdubovsky.statemachineexample.statemachine.repository.InMemoryStateMachineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StateMachineFacade implements StateMachineService {

    private final Map<String, StateMachineFactory> stateMachineFactories;
    private final Map<String, StateMachineProcessor> stateMachineProcessors;

    public StateMachineFacade(
            List<StateMachineFactory> stateMachinesFactories,
            List<StateMachineProcessor> stateMachineProcessors) {

        this.stateMachineFactories = stateMachinesFactories.stream().collect(
                Collectors.toMap(factory -> factory.getStateMachine().getId(), Function.identity()));

        this.stateMachineProcessors = stateMachineProcessors.stream().collect(
                Collectors.toMap(StateMachineProcessor::getId, Function.identity()));
    }

    @Override
    public Object executeProcessAndGetResult(String processDefinitionId, Object input) {
        UUID processId = UUID.randomUUID();

        log.debug("Initializing statemachine for process definition id: {}, process id: {}", processDefinitionId, processId);
        log.trace("Initializing statemachine for process definition id: {} with input: {}, process id: {}", processDefinitionId, input, processId);

        StateMachine stateMachine = Optional.ofNullable(stateMachineFactories.get(processDefinitionId))
                .map(stateMachineFactory -> stateMachineFactory.getStateMachine(processId))
                .orElseThrow(() -> new RuntimeException(
                        "Statemachine factory for process definition id %s not found".formatted(processDefinitionId)));

        StateMachineProcessor stateMachineProcessor = Optional.ofNullable(stateMachineProcessors.get(processDefinitionId))
                .orElseThrow(() -> new RuntimeException(
                        "Statemachine processor for process definition id %s not found".formatted(processDefinitionId)));

        return stateMachineProcessor.startProcessAndGetResult(stateMachine, input);
    }

    @Override
    public Object sendEvent(String processDefinitionId, UUID processId, String event, Object input) {

        log.debug("Sending event to statemachine for process definition id: {}, process id: {}, event: {}",
                processDefinitionId, processId, event);
        log.trace("Sending event to statemachine for process definition id: {} with input: {}, process id: {}, event: {}",
                processDefinitionId, input, processId, event);

        StateMachineProcessor stateMachineProcessor = Optional.ofNullable(stateMachineProcessors.get(processDefinitionId))
                .orElseThrow(() -> new RuntimeException(
                        "Statemachine processor for process definition id %s not found".formatted(processDefinitionId)));

        return stateMachineProcessor.sendEventAndGetResult(processId, event, input);
    }
}
