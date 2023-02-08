package io.vdubovsky.statemachineexample.statemachine.impl;

import io.vdubovsky.statemachineexample.statemachine.StateMachineService;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StateMachineServiceImpl implements StateMachineService {

    private final Map<String, StateMachineFactory<String, String>> stateMachineFactories;

    private final StateMachineRepository stateMachineRepository;

    public StateMachineServiceImpl(List<StateMachineFactory<String, String>> stateMachinesFactories, StateMachineRepository stateMachineRepository) {
        this.stateMachineFactories = stateMachinesFactories.stream().collect(
                Collectors.toMap(factory -> factory.getStateMachine().getId(), Function.identity()));
        this.stateMachineRepository = stateMachineRepository;
    }

    @Override
    public UUID startProcess(String processDefinitionId, Map<String, Object> context) {
        StateMachine<String, String> stateMachine = Optional.ofNullable(stateMachineFactories.get(processDefinitionId))
                .map(StateMachineFactory::getStateMachine)
                .orElseThrow(() -> new RuntimeException("Statemachine with processId: %s not found".formatted(processDefinitionId)));

        UUID processId = UUID.randomUUID();
        stateMachineRepository.addStateMachine(processId, stateMachine);

        Mono<Void> mono = stateMachine.startReactively();
        mono.subscribe();

        Flux<StateMachineEventResult<String, String>> eventResult1 = stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload("PROCESS_1_EVENT_1").build()));
        eventResult1.subscribe();

        Flux<StateMachineEventResult<String, String>> eventResult2 = stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload("PROCESS_1_EVENT_2").build()));
        eventResult2.subscribe();

        return processId;
    }
}
