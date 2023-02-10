package io.vdubovsky.statemachineexample.statemachine.configuration.common;

import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;

@Slf4j
@Component
public class StateUpdater extends StateMachineInterceptorAdapter {

    @Override
    public void postStateChange(State state, Message message, Transition transition, StateMachine stateMachine,
                                StateMachine rootStateMachine) {
        log.debug("Updating state in execution result object, process definition id: {}, process id: {}",
                stateMachine.getId(), stateMachine.getUuid());

        Optional.ofNullable(stateMachine)
                .filter(executionResult -> stateMachineHasState(stateMachine))
                .map(StateMachine::getExtendedState)
                .map(ExtendedState::getVariables)
                .map(variables -> variables.get(GENERIC_EXECUTION_RESULT))
                .map(executionResultObject -> (GenericExecutionResult) executionResultObject)
                .map(GenericExecutionResult::getExecutionInfo)
                .ifPresent(executionInfo -> executionInfo.setCurrentState(stateMachine.getState().getId().toString()));
    }

    private boolean stateMachineHasState(StateMachine stateMachine) {
        return Optional.ofNullable(stateMachine.getState())
                .map(State::getId).isPresent();
    }
}
