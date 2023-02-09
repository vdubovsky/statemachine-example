package io.vdubovsky.statemachineexample.statemachine.configuration.pingpong;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.EnumSet;

import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;

@Configuration
@EnableStateMachineFactory(name = MACHINE_ID)
@Slf4j
public class PingPongStateMachineFactoryConfiguration extends EnumStateMachineConfigurerAdapter<State, Event> {
    final static String MACHINE_ID = "PING_PONG";

    @Override
    public void configure(StateMachineStateConfigurer<State, Event> states) throws Exception {
        states
                .withStates()
                .initial(State.PING_PONG_IN_STATE)
                .states(EnumSet.allOf(State.class))
                .end(State.PING_PONG_OUT_STATE)
        ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<State, Event> transitions) throws Exception {
        transitions
                .withExternal()

                .source(State.PING_PONG_IN_STATE)
                .target(State.PING_PONG_SET_ACTION_PING_STATE)
                .event(Event.PING_PONG_TO_SET_ACTION_PING_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_IN_STATE)
                .target(State.PING_PONG_SET_ACTION_PONG_STATE)
                .event(Event.PING_PONG_TO_SET_ACTION_PONG_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_SET_ACTION_PING_STATE)
                .target(State.PING_PONG_SET_EXTERNAL_REQUEST_STATE)
                .event(Event.PING_PONG_TO_SET_EXTERNAL_REQUEST_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_SET_ACTION_PONG_STATE)
                .target(State.PING_PONG_SET_EXTERNAL_REQUEST_STATE)
                .event(Event.PING_PONG_TO_SET_EXTERNAL_REQUEST_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_SET_EXTERNAL_REQUEST_STATE)
                .target(State.PING_PONG_PLAY_STATE)
                .event(Event.PING_PONG_TO_PLAY_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_PLAY_STATE)
                .target(State.PING_PONG_SET_RESPONSE_STATE)
                .event(Event.PING_PONG_TO_SET_RESPONSE_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_PLAY_STATE)
                .target(State.PING_PONG_SET_COUNTER_STATE)
                .event(Event.PING_PONG_TO_SET_COUNTER_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_SET_RESPONSE_STATE)
                .target(State.PING_PONG_OUT_STATE)
                .event(Event.PING_PONG_TO_OUT_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_SET_RESPONSE_STATE)
                .target(State.PING_PONG_SET_ACTION_PONG_STATE)
                .event(Event.PING_PONG_TO_SET_ACTION_PONG_EVENT)

                .and().withExternal()

                .source(State.PING_PONG_SET_RESPONSE_STATE)
                .target(State.PING_PONG_SET_ACTION_PING_STATE)
                .event(Event.PING_PONG_TO_SET_ACTION_PING_EVENT)

        ;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<State, Event> config) throws Exception {
        config.withConfiguration().machineId(MACHINE_ID).listener(loggingListener());
        super.configure(config);
    }

    @Bean
    public StateMachineListener<State, Event> loggingListener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(org.springframework.statemachine.state.State<State, Event> from,
                                     org.springframework.statemachine.state.State<State, Event> to) {
                super.stateChanged(from, to);
                log.info("State changed from: {} to: {}", from, to);
            }
        };
    }
}

