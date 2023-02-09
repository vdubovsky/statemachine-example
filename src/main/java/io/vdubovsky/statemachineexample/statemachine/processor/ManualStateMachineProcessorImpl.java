package io.vdubovsky.statemachineexample.statemachine.processor;

import io.vdubovsky.statemachineexample.model.ExecutionInfo;
import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import io.vdubovsky.statemachineexample.statemachine.StateMachineProcessor;
import io.vdubovsky.statemachineexample.statemachine.repository.StateMachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;

@Component
@RequiredArgsConstructor
public class ManualStateMachineProcessorImpl implements StateMachineProcessor {

    private final StateMachineRepository stateMachineRepository;
    private final StateMachineInterceptor<String, String> stateMachineInterceptor;

    @Override
    public ExecutionInfo startProcessAndGetResult(StateMachine stateMachine, Object input) {
        ((StateMachine<String, String>)stateMachine).getStateMachineAccessor()
                .doWithAllRegions(sma -> sma.addStateMachineInterceptor(stateMachineInterceptor));

        // Can be moved into listener
        ExecutionInfo executionInfo = buildExecutionInfo(stateMachine);
        GenericExecutionResult<Object, ExecutionInfo> executionResult = buildGenericExecutionResult(input, executionInfo);
        stateMachine.getExtendedState().getVariables().put(GENERIC_EXECUTION_RESULT, executionResult);
        //

        Mono<Void> stateMachineMono = stateMachine.startReactively();
        stateMachineMono.subscribe();

        return executionResult.getOutput();
    }

    private GenericExecutionResult<Object, ExecutionInfo> buildGenericExecutionResult(Object input, ExecutionInfo executionInfo) {
        GenericExecutionResult<Object, ExecutionInfo> executionResult = new GenericExecutionResult<>();
        executionResult.setExecutionInfo(executionInfo);
        executionResult.setInput(input);
        executionResult.setOutput(executionInfo);

        return executionResult;
    }

    private ExecutionInfo buildExecutionInfo(StateMachine stateMachine) {
        return new ExecutionInfo()
                .setCurrentState(stateMachine.getState() == null ? null : (String) stateMachine.getState().getId())
                .setProcessId(stateMachine.getUuid())
                .setProcessDefinitionId(stateMachine.getId())
                .setStartedAt(LocalDateTime.now());
    }

    @Override
    public String getId() {
        return "MANUAL";
    }

    @Override
    public Object sendEventAndGetResult(UUID processId, String event, Object input) {
        StateMachine stateMachine = Optional.ofNullable(stateMachineRepository.findStateMachine(processId))
                .orElseThrow(() -> new RuntimeException(
                        "Statemachine for process id %s not found".formatted(processId)));

        Flux fluxStateMachine = stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build()));
        fluxStateMachine.subscribe();

        GenericExecutionResult<Object, ExecutionInfo> executionResult = (GenericExecutionResult<Object, ExecutionInfo>)
                stateMachine.getExtendedState().getVariables().get(GENERIC_EXECUTION_RESULT);

        return executionResult.getOutput();
    }
}
