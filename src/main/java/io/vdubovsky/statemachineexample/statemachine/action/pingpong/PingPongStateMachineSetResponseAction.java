package io.vdubovsky.statemachineexample.statemachine.action.pingpong;

import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import io.vdubovsky.statemachineexample.model.pingpong.PingPongOutputBO;
import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.PING_PONG_TO_OUT_EVENT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState.*;

@Component
@RequiredArgsConstructor
public class PingPongStateMachineSetResponseAction implements ActionAware<PingPongState, PingPongEvent> {

    @Override
    public PingPongState getStateEntryPoint() {
        return PING_PONG_SET_RESPONSE_STATE;
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }

    @Override
    public void execute(StateContext<PingPongState, PingPongEvent> context) {
        //Action
        Integer retryCount = context.getExtendedState().get("retryCount", Integer.class);
        GenericExecutionResult executionResult = context.getExtendedState().get(GENERIC_EXECUTION_RESULT, GenericExecutionResult.class);
        PingPongOutputBO output = (PingPongOutputBO) executionResult.getOutput();
        output.setRetryCount(retryCount == null ? 0 : retryCount);

        //Event
        context.getStateMachine().sendEvent(
                        Mono.just(MessageBuilder.withPayload(PING_PONG_TO_OUT_EVENT).build()))
                .subscribe();
    }
}
