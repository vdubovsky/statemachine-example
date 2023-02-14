package io.vdubovsky.statemachineexample.statemachine.action.pingpong;

import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.PING_PONG_TO_SET_ACTION_PING_EVENT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.PING_PONG_TO_SET_ACTION_PONG_EVENT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState.PING_PONG_SET_COUNTER_STATE;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;

@Component
@RequiredArgsConstructor
public class PingPongStateMachineSetRetryCountAction implements ActionAware<PingPongState, PingPongEvent> {

    @Override
    public PingPongState getStateEntryPoint() {
        return PING_PONG_SET_COUNTER_STATE;
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }

    @Override
    public void execute(StateContext<PingPongState, PingPongEvent> context) {
        Integer retryCount = context.getExtendedState().get("retryCount", Integer.class);
        if (retryCount == null) {
            retryCount = 0;
        }
        retryCount++;
        context.getExtendedState().getVariables().put("retryCount", retryCount);

        if (Math.random() > 0.5) {
            context.getStateMachine().sendEvent(
                            Mono.just(MessageBuilder.withPayload(PING_PONG_TO_SET_ACTION_PING_EVENT).build()))
                    .subscribe();
        } else {
            context.getStateMachine().sendEvent(
                            Mono.just(MessageBuilder.withPayload(PING_PONG_TO_SET_ACTION_PONG_EVENT).build()))
                    .subscribe();
        }

    }
}
