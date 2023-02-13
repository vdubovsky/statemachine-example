package io.vdubovsky.statemachineexample.statemachine.configuration.manual;

import io.vdubovsky.statemachineexample.statemachine.listener.ContextConfigurerListener;
import io.vdubovsky.statemachineexample.statemachine.listener.InterceptorInjectorListener;
import io.vdubovsky.statemachineexample.statemachine.listener.PersistableLoggingListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static io.vdubovsky.statemachineexample.statemachine.configuration.manual.StateMachineManualFactoryConfiguration.MACHINE_ID;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = MACHINE_ID)
@Slf4j
public class StateMachineManualFactoryConfiguration extends StateMachineConfigurerAdapter<String, String> {

    final static String MACHINE_ID = "MANUAL";
    private final PersistableLoggingListener persistableLoggingListener;
    private final InterceptorInjectorListener interceptorInjectorListener;
    private final ContextConfigurerListener contextConfigurerListener;

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
                .listener(persistableLoggingListener)
                .listener(interceptorInjectorListener)
                .listener(contextConfigurerListener)
        ;
    }
}

