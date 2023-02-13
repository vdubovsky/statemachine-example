package io.vdubovsky.statemachineexample.statemachine.processor;

import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import io.vdubovsky.statemachineexample.statemachine.StateMachineProcessor;
import io.vdubovsky.statemachineexample.statemachine.listener.ResultProviderListener;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;

@Component
@RequiredArgsConstructor
public class AutomatStateMachineProcessorImpl implements StateMachineProcessor {

    @Override
    public Object startProcessAndGetResult(StateMachine stateMachine, Object input) {
        DeferredResult result = new DeferredResult();

        Mono<Void> stateMachineMono = stateMachine.startReactively();
        stateMachineMono.subscribe();

        GenericExecutionResult executionResult = (GenericExecutionResult) stateMachine.getExtendedState()
                .getVariables().get(GENERIC_EXECUTION_RESULT);
        executionResult.setInput(input);
        executionResult.setOutput(executionResult.getExecutionInfo());

        stateMachine.addStateListener(new ResultProviderListener(result));
        return result;
    }

    @Override
    public String getId() {
        return "AUTOMAT";
    }

    @Override
    public Object sendEventAndGetResult(UUID processId, String event, Object input) {
        throw new RuntimeException("AUTOMAT state machine doesn't support incoming events");
    }
}
