package io.vdubovsky.statemachineexample.statemachine.configuration.common;

import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;

@RequiredArgsConstructor
public class ResultProviderListener extends StateMachineListenerAdapter {

    private final DeferredResult deferredResult;

    @Override
    public void stateMachineStopped(StateMachine stateMachine) {
        Optional.ofNullable(stateMachine.getExtendedState().getVariables().get(GENERIC_EXECUTION_RESULT))
                .ifPresent(result -> ((GenericExecutionResult) result).getExecutionInfo().setCompletedAt(LocalDateTime.now()));

        deferredResult.setResult(((GenericExecutionResult) stateMachine.getExtendedState().getVariables().get(GENERIC_EXECUTION_RESULT)).getOutput());
    }

    @Override
    public void stateMachineError(StateMachine stateMachine, Exception exception) {
        deferredResult.setResult(((GenericExecutionResult) stateMachine.getExtendedState().getVariables().get(GENERIC_EXECUTION_RESULT)).getOutput());
    }
}
