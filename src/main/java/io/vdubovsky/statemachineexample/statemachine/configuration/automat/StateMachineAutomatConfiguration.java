package io.vdubovsky.statemachineexample.statemachine.configuration.automat;

import io.vdubovsky.statemachineexample.statemachine.configuration.common.ContextConfigurerListener;
import io.vdubovsky.statemachineexample.statemachine.configuration.common.InterceptorInjectorListener;
import io.vdubovsky.statemachineexample.statemachine.configuration.common.PersistableLoggingListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.statemachine.configuration.automat.StateMachineAutomatConfiguration.MACHINE_ID;


@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = MACHINE_ID)
@Slf4j
public class StateMachineAutomatConfiguration extends StateMachineConfigurerAdapter<String, String> {

    final static String MACHINE_ID = "AUTOMAT";

    private final PersistableLoggingListener persistableLoggingListener;
    private final InterceptorInjectorListener interceptorInjectorListener;
    private final ContextConfigurerListener contextConfigurerListener;

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial(MACHINE_ID + "_STATE_INITIAL", processInitial())
                .state(MACHINE_ID + "_STATE_1", processState1())
                .state(MACHINE_ID + "_STATE_2", processState2())
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
                .event(MACHINE_ID + "_EVENT_COMPLETED")
        ;
    }

    private Action<String, String> processInitial() {
        // BUG in State machine
        return context -> {
            new Thread(() -> {
                log.info("Auto State machine action while entering to Initial state.");
                Flux fluxStateMachine = context.getStateMachine().sendEvent(
                        Mono.just(MessageBuilder.withPayload(MACHINE_ID + "_EVENT_1").build()));
                fluxStateMachine.subscribe();
            }).start();
        };
    }

    private Action<String, String> processState1() {
        return context -> {
            log.debug("Auto State machine action while entering to State 1");
            Flux fluxStateMachine = context.getStateMachine().sendEvent(
                    Mono.just(MessageBuilder.withPayload(MACHINE_ID + "_EVENT_2").build()));
            fluxStateMachine.subscribe();
        };
    }

    private Action<String, String> processState2() {
        return context -> {
            log.debug("Auto State machine action while entering to State 2");
            Flux fluxStateMachine = context.getStateMachine().sendEvent(
                    Mono.just(MessageBuilder.withPayload(MACHINE_ID + "_EVENT_COMPLETED").build()));
            fluxStateMachine.subscribe();
        };
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration()
                .machineId(MACHINE_ID)
                .listener(persistableLoggingListener)
                .listener(interceptorInjectorListener)
                .listener(contextConfigurerListener)
        ;
    }
}

