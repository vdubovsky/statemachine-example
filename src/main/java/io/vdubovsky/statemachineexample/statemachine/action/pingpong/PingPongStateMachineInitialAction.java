package io.vdubovsky.statemachineexample.statemachine.action.pingpong;

import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.PING_PONG_TO_SET_ACTION_PING_EVENT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.PING_PONG_TO_SET_ACTION_PONG_EVENT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState.PING_PONG_IN_STATE;

@Component
public class PingPongStateMachineInitialAction implements ActionAware<PingPongState, PingPongEvent> {
    @Override
    public PingPongState getStateEntryPoint() {
        return PING_PONG_IN_STATE;
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }

    @Override
    // BUG in State machine, initial action has to be run in separate thread
    // I found such bug 2 years ago, and it still takes place.
    public void execute(StateContext<PingPongState, PingPongEvent> context) {
        //Action

        //Event
        new Thread(() -> {
            if (Math.random() > 0.5) {
                context.getStateMachine().sendEvent(
                                Mono.just(MessageBuilder.withPayload(PING_PONG_TO_SET_ACTION_PING_EVENT).build()))
                        .subscribe();
            } else {
                context.getStateMachine().sendEvent(
                                Mono.just(MessageBuilder.withPayload(PING_PONG_TO_SET_ACTION_PONG_EVENT).build()))
                        .subscribe();
            }
        }).start();
    }
}
