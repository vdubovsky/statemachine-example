package io.vdubovsky.statemachineexample.statemachine.action.pingpong;

import io.vdubovsky.statemachineexample.model.pingpong.PingPongActionBO;
import io.vdubovsky.statemachineexample.statemachine.ActionAware;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent;
import io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongEvent.*;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongState.PING_PONG_PLAY_STATE;

@Component
@RequiredArgsConstructor
public class PingPongStateMachinePlayAction implements ActionAware<PingPongState, PingPongEvent> {

    private final RestTemplate restTemplate;

    @Override
    public PingPongState getStateEntryPoint() {
        return PING_PONG_PLAY_STATE;
    }

    @Override
    public String getStateMachineId() {
        return MACHINE_ID;
    }

    @Override
    public void execute(StateContext<PingPongState, PingPongEvent> context) {
        //Action
        PingPongActionBO actionBO = context.getExtendedState().get("pingPongAction", PingPongActionBO.class);
        HttpEntity<PingPongActionBO> request = new HttpEntity<>(actionBO);
        PingPongActionBO response = restTemplate.postForObject("http://localhost:9999/api/ping-pong-server", request, PingPongActionBO.class);

        //Event
        if (response.getAction().equals("ping")) {
            context.getStateMachine().sendEvent(
                            Mono.just(MessageBuilder.withPayload(PING_PONG_TO_SET_RESPONSE_EVENT).build()))
                    .subscribe();
        } else {
            context.getStateMachine().sendEvent(
                            Mono.just(MessageBuilder.withPayload(PING_PONG_TO_SET_COUNTER_EVENT).build()))
                    .subscribe();
        }
    }
}
