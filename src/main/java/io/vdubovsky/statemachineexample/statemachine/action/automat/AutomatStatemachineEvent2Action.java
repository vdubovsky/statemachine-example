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
public class AutomatStatemachineEvent2Action implements ActionAware<String, String> {

    @Override
    public void execute(StateContext<String, String> context) {
        log.debug("Auto State machine action while entering to State 2");
        Flux fluxStateMachine = context.getStateMachine().sendEvent(
                Mono.just(MessageBuilder.withPayload(MACHINE_ID + "_EVENT_COMPLETED").build()));
        fluxStateMachine.subscribe();
    }

    @Override
    public String getStateEntryPoint() {
        return MACHINE_ID + "_STATE_2";
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }
}
