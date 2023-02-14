package io.vdubovsky.statemachineexample.statemachine.action.pingpong;

import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.PING_PONG_TO_SET_EXTERNAL_REQUEST_EVENT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState.PING_PONG_SET_ACTION_PONG_STATE;


@Component
public class PingPongStateMachineSetActionPongAction implements ActionAware<PingPongState, PingPongEvent> {
    @Override
    public PingPongState getStateEntryPoint() {
        return PING_PONG_SET_ACTION_PONG_STATE;
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }

    @Override
    public void execute(StateContext<PingPongState, PingPongEvent> context) {
        context.getExtendedState().getVariables().put("action", "pong");

        context.getStateMachine().sendEvent(
                Mono.just(MessageBuilder.withPayload(PING_PONG_TO_SET_EXTERNAL_REQUEST_EVENT).build()))
                .subscribe();
    }
}
