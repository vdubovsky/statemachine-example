package io.vdubovsky.statemachineexample.statemachine.action.pingpong;

import io.vdubovsky.statemachineexample.model.pingpong.PingPongActionBO;
import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.PING_PONG_TO_PLAY_EVENT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState.PING_PONG_SET_EXTERNAL_REQUEST_STATE;

@Component
public class PingPongStateMachineSetExternalRequestAction implements ActionAware<PingPongState, PingPongEvent> {
    @Override
    public PingPongState getStateEntryPoint() {
        return PING_PONG_SET_EXTERNAL_REQUEST_STATE;
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }

    @Override
    public void execute(StateContext<PingPongState, PingPongEvent> context) {
        //Action
        String action = context.getExtendedState().get("action", String.class);
        context.getExtendedState().getVariables().put("pingPongAction", new PingPongActionBO().setAction(action));

        //Event
        context.getStateMachine().sendEvent(
                Mono.just(MessageBuilder.withPayload(PING_PONG_TO_PLAY_EVENT).build()))
                .subscribe();
    }
}
