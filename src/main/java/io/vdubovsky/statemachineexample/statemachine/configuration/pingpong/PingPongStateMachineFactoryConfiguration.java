package io.vdubovsky.statemachineexample.statemachine.configuration.pingpong;

import io.vdubovsky.statemachineexample.statemachine.action.ActionFactory;
import io.vdubovsky.statemachineexample.statemachine.listener.ContextConfigurerListener;
import io.vdubovsky.statemachineexample.statemachine.listener.InterceptorInjectorListener;
import io.vdubovsky.statemachineexample.statemachine.listener.PersistableLoggingListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.*;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState.*;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;

@Configuration
@EnableStateMachineFactory(name = MACHINE_ID)
@Slf4j
@RequiredArgsConstructor
public class PingPongStateMachineFactoryConfiguration extends EnumStateMachineConfigurerAdapter<PingPongState, PingPongEvent> {
    public final static String MACHINE_ID = "PING_PONG";

    private final PersistableLoggingListener persistableLoggingListener;
    private final InterceptorInjectorListener interceptorInjectorListener;
    private final ContextConfigurerListener contextConfigurerListener;

    private final ActionFactory actionFactory;

    @Override
    public void configure(StateMachineStateConfigurer<PingPongState, PingPongEvent> states) throws Exception {
        states
                .withStates()
                .initial(PING_PONG_IN_STATE)
                .state(PING_PONG_IN_STATE, actionFactory.getAction(MACHINE_ID, PING_PONG_IN_STATE))
                .state(PING_PONG_SET_ACTION_PING_STATE, actionFactory.getAction(MACHINE_ID, PING_PONG_SET_ACTION_PING_STATE))
                .state(PING_PONG_SET_ACTION_PONG_STATE, actionFactory.getAction(MACHINE_ID, PING_PONG_SET_ACTION_PONG_STATE))
                .state(PING_PONG_SET_EXTERNAL_REQUEST_STATE, actionFactory.getAction(MACHINE_ID, PING_PONG_SET_EXTERNAL_REQUEST_STATE))
                .state(PING_PONG_PLAY_STATE, actionFactory.getAction(MACHINE_ID, PING_PONG_PLAY_STATE))
                .state(PING_PONG_SET_COUNTER_STATE, actionFactory.getAction(MACHINE_ID, PING_PONG_SET_COUNTER_STATE))
                .state(PING_PONG_SET_RESPONSE_STATE, actionFactory.getAction(MACHINE_ID, PING_PONG_SET_RESPONSE_STATE))
                .state(PING_PONG_OUT_STATE)
                .end(PingPongState.PING_PONG_OUT_STATE)
        ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PingPongState, PingPongEvent> transitions) throws Exception {
        transitions
                .withExternal()

                .source(PING_PONG_IN_STATE)
                .target(PING_PONG_SET_ACTION_PING_STATE)
                .event(PING_PONG_TO_SET_ACTION_PING_EVENT)

                .and().withExternal()

                .source(PING_PONG_IN_STATE)
                .target(PING_PONG_SET_ACTION_PONG_STATE)
                .event(PING_PONG_TO_SET_ACTION_PONG_EVENT)

                .and().withExternal()

                .source(PING_PONG_SET_ACTION_PING_STATE)
                .target(PING_PONG_SET_EXTERNAL_REQUEST_STATE)
                .event(PING_PONG_TO_SET_EXTERNAL_REQUEST_EVENT)

                .and().withExternal()

                .source(PING_PONG_SET_ACTION_PONG_STATE)
                .target(PING_PONG_SET_EXTERNAL_REQUEST_STATE)
                .event(PING_PONG_TO_SET_EXTERNAL_REQUEST_EVENT)

                .and().withExternal()

                .source(PING_PONG_SET_EXTERNAL_REQUEST_STATE)
                .target(PING_PONG_PLAY_STATE)
                .event(PING_PONG_TO_PLAY_EVENT)

                .and().withExternal()

                .source(PING_PONG_PLAY_STATE)
                .target(PING_PONG_SET_RESPONSE_STATE)
                .event(PING_PONG_TO_SET_RESPONSE_EVENT)

                .and().withExternal()

                .source(PING_PONG_PLAY_STATE)
                .target(PING_PONG_SET_COUNTER_STATE)
                .event(PING_PONG_TO_SET_COUNTER_EVENT)

                .and().withExternal()

                .source(PING_PONG_SET_RESPONSE_STATE)
                .target(PING_PONG_OUT_STATE)
                .event(PING_PONG_TO_OUT_EVENT)

                .and().withExternal()

                .source(PING_PONG_SET_COUNTER_STATE)
                .target(PING_PONG_SET_ACTION_PONG_STATE)
                .event(PING_PONG_TO_SET_ACTION_PONG_EVENT)

                .and().withExternal()

                .source(PING_PONG_SET_COUNTER_STATE)
                .target(PING_PONG_SET_ACTION_PING_STATE)
                .event(PING_PONG_TO_SET_ACTION_PING_EVENT)

        ;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PingPongState, PingPongEvent> config) throws Exception {
        config.withConfiguration()
                .machineId(MACHINE_ID)
                .listener(persistableLoggingListener)
                .listener(interceptorInjectorListener)
                .listener(contextConfigurerListener)
        ;
    }

}

