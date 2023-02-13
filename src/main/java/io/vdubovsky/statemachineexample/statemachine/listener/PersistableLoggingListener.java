package io.vdubovsky.statemachineexample.statemachine.listener;

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
public class PersistableLoggingListener extends StateMachineListenerAdapter {

    private final StateMachineRepository stateMachineRepository;

    @Override
    public void stateChanged(State from, State to) {
        log.debug("State machine: State changed from: {} to: {}", from, to);
    }

    @Override
    public void stateMachineStarted(StateMachine stateMachine) {
        log.debug("Statemachine started, process definition id: {}, process id: {}",
                stateMachine.getId(), stateMachine.getUuid());

        stateMachineRepository.addStateMachine(stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine stateMachine) {
        log.debug("Statemachine stopped process definition id: {}, process id: {}",
                stateMachine.getId(), stateMachine.getUuid());

        Optional.ofNullable(stateMachine.getExtendedState().getVariables().get(GENERIC_EXECUTION_RESULT))
                .ifPresent(result -> ((GenericExecutionResult) result).getExecutionInfo().setCompletedAt(LocalDateTime.now()));

        stateMachineRepository.remove(stateMachine.getUuid());
    }

    @Override
    public void stateMachineError(StateMachine stateMachine, Exception exception) {
        log.error("Statemachine stopped process definition id: {}, process id: {}, exception: {}",
                stateMachine.getId(), stateMachine.getUuid(), exception);

        stateMachineRepository.remove(stateMachine.getUuid());
    }
}
