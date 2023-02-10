package io.vdubovsky.statemachineexample.statemachine.configuration.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class InterceptorInjectorListener extends StateMachineListenerAdapter {

    private final StateUpdater stateMachineInterceptor;

    @Override
    public void stateMachineStarted(StateMachine stateMachine) {
        stateMachine.getStateMachineAccessor().doWithAllRegions(addInterceptor());
    }

    private Consumer<StateMachineAccess> addInterceptor() {
        return sma -> sma.addStateMachineInterceptor(stateMachineInterceptor);
    }
}
