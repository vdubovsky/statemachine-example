package io.vdubovsky.statemachineexample.statemachine.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;

import static io.vdubovsky.statemachineexample.statemachine.configuration.StateMachine2FactoryConfiguration.MACHINE_ID;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = MACHINE_ID)
public class StateMachine2FactoryConfiguration extends StateMachineConfigurerAdapter<String, String> {
    final static String MACHINE_ID = "PROCESS_2_";

    private final StateMachineListener<String, String> loggingListener;

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial(MACHINE_ID + "STATE_INITIAL")
                .state(MACHINE_ID + "STATE_1")
                .state(MACHINE_ID + "STATE_2");
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(MACHINE_ID + "STATE_INITIAL").target(MACHINE_ID + "STATE_1")
                .event(MACHINE_ID + "EVENT_1")
                .and()
                .withExternal()
                .source(MACHINE_ID + "STATE_1").target(MACHINE_ID + "STATE_2")
                .event(MACHINE_ID + "EVENT_2");
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration().machineId(MACHINE_ID).listener(loggingListener);
        super.configure(config);
    }
}

