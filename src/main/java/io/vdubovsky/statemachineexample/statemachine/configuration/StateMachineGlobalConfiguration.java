package io.vdubovsky.statemachineexample.statemachine.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@Slf4j
public class StateMachineGlobalConfiguration {

    @Bean
    public StateMachineListener<String, String> loggingListener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                super.stateChanged(from, to);
                log.info("State changed from: {} to: {}", from, to);
            }
        };
    }
}
