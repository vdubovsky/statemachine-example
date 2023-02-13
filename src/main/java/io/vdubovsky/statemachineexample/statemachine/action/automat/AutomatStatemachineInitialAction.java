package io.vdubovsky.statemachineexample.statemachine.action.automat;

import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.statemachine.configuration.automat.StateMachineAutomatConfiguration.MACHINE_ID;

@Slf4j
@Component
public class AutomatStatemachineInitialAction implements ActionAware<String, String> {

    // BUG in State machine, initial action has to be run in separate thread
    // I found such bug 2 years ago, and it is still takes place.
    @Override
    public void execute(StateContext<String, String> context) {
        new Thread(() -> {
            log.info("Auto State machine action while entering to Initial state.");
            Flux fluxStateMachine = context.getStateMachine().sendEvent(
                    Mono.just(MessageBuilder.withPayload(MACHINE_ID + "_EVENT_1").build()));
            fluxStateMachine.subscribe();
        }).start();
    }

    @Override
    public String getStateEntryPoint() {
        return MACHINE_ID + "_STATE_INITIAL";
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }
}
