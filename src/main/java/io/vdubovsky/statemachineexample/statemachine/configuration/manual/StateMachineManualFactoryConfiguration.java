package io.vdubovsky.statemachineexample.statemachine.configuration.manual;

import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import io.vdubovsky.statemachineexample.statemachine.repository.StateMachineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.manual.StateMachineManualFactoryConfiguration.MACHINE_ID;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = MACHINE_ID)
@Slf4j
public class StateMachineManualFactoryConfiguration extends StateMachineConfigurerAdapter<String, String> {
    final static String MACHINE_ID = "MANUAL";

    private final StateMachineRepository stateMachineRepository;

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial(MACHINE_ID + "_STATE_INITIAL")
                .state(MACHINE_ID + "_STATE_1")
                .state(MACHINE_ID + "_STATE_2")
                .state(MACHINE_ID + "_STATE_COMPLETED")
                .end(MACHINE_ID + "_STATE_COMPLETED")
        ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(MACHINE_ID + "_STATE_INITIAL").target(MACHINE_ID + "_STATE_1")
                .event(MACHINE_ID + "_EVENT_1")

                .and().withExternal()
                .source(MACHINE_ID + "_STATE_1").target(MACHINE_ID + "_STATE_2")
                .event(MACHINE_ID + "_EVENT_2")

                .and().withExternal()
                .source(MACHINE_ID + "_STATE_2").target(MACHINE_ID + "_STATE_COMPLETED")
                .event(MACHINE_ID + "_EVENT_COMPLETED");
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration()
                .machineId(MACHINE_ID)
                .listener(getListener());
    }

    @Bean
    public StateMachineListenerAdapter<String, String> getListener() {
        return new StateMachineListenerAdapter<>() {

            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                log.info("Manual machine: State changed from: {} to: {}", from, to);
            }

            @Override
            public void stateMachineStarted(StateMachine<String, String> stateMachine) {
                log.debug("Statemachine started, process definition id: {}, process id: {}",
                        stateMachine.getId(), stateMachine.getUuid());

                stateMachineRepository.addStateMachine(stateMachine);
            }

            @Override
            public void stateMachineStopped(StateMachine<String, String> stateMachine) {
                log.debug("Statemachine stopped process definition id: {}, process id: {}",
                        stateMachine.getId(), stateMachine.getUuid());

                Optional.ofNullable(stateMachine.getExtendedState().getVariables().get(GENERIC_EXECUTION_RESULT))
                        .ifPresent(result -> ((GenericExecutionResult) result).getExecutionInfo().setCompletedAt(LocalDateTime.now()));

                stateMachineRepository.remove(stateMachine.getUuid());
            }

            @Override
            public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
                log.error("Statemachine stopped process definition id: {}, process id: {}, exception: {}",
                        stateMachine.getId(), stateMachine.getUuid(), exception);

                stateMachineRepository.remove(stateMachine.getUuid());
            }
        };
    }

    @Bean
    public StateMachineInterceptor<String, String> stateMachineInterceptor() {
        return new StateMachineInterceptorAdapter<>() {
            @Override
            public void postStateChange(State<String, String> state, Message<String> message,
                                       Transition<String, String> transition, StateMachine<String, String> stateMachine,
                                       StateMachine<String, String> rootStateMachine) {

                log.debug("Updating state in execution result object, process definition id: {}, process id: {}",
                        stateMachine.getId(), stateMachine.getUuid());
                Optional.ofNullable(stateMachine.getExtendedState().getVariables().get(GENERIC_EXECUTION_RESULT))
                        .ifPresent(result -> ((GenericExecutionResult) result).getExecutionInfo().setCurrentState(stateMachine.getState().getId()));
            }
        };
    }
}

