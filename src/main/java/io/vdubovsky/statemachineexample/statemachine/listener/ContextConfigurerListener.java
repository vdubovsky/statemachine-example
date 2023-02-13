package io.vdubovsky.statemachineexample.statemachine.listener;

import io.vdubovsky.statemachineexample.model.ExecutionInfo;
import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import io.vdubovsky.statemachineexample.statemachine.repository.StateMachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;

@Slf4j
@RequiredArgsConstructor
@Component
public class ContextConfigurerListener extends StateMachineListenerAdapter {

    @Override
    public void stateMachineStarted(StateMachine stateMachine) {
        ExecutionInfo executionInfo = buildExecutionInfo(stateMachine);
        GenericExecutionResult<Object, ExecutionInfo> executionResult = buildGenericExecutionResult(executionInfo);
        stateMachine.getExtendedState().getVariables().put(GENERIC_EXECUTION_RESULT, executionResult);
    }

    private GenericExecutionResult<Object, ExecutionInfo> buildGenericExecutionResult(ExecutionInfo executionInfo) {
        GenericExecutionResult<Object, ExecutionInfo> executionResult = new GenericExecutionResult<>();
        executionResult.setExecutionInfo(executionInfo);
        return executionResult;
    }

    private ExecutionInfo buildExecutionInfo(StateMachine stateMachine) {
        return new ExecutionInfo()
                .setCurrentState(stateMachine.getState() == null ? null : (String) stateMachine.getState().getId())
                .setProcessId(stateMachine.getUuid())
                .setProcessDefinitionId(stateMachine.getId())
                .setStartedAt(LocalDateTime.now());
    }
}
