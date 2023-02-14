package io.vdubovsky.statemachineexample.statemachine.processor;

import com.google.gson.Gson;
import io.vdubovsky.statemachineexample.model.GenericExecutionResult;
import io.vdubovsky.statemachineexample.model.pingpong.PingPongInputBO;
import io.vdubovsky.statemachineexample.model.pingpong.PingPongOutputBO;
import io.vdubovsky.statemachineexample.statemachine.StateMachineProcessor;
import io.vdubovsky.statemachineexample.statemachine.listener.ResultProviderListener;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

import static io.vdubovsky.statemachineexample.model.ApplicationConstants.GENERIC_EXECUTION_RESULT;
import static io.vdubovsky.statemachineexample.statemachine.configuration.pingpong.PingPongStateMachineFactoryConfiguration.MACHINE_ID;

@Component
@RequiredArgsConstructor
public class PingPongStateMachineProcessor implements StateMachineProcessor {

    private final Gson gson;

    @Override
    public Object startProcessAndGetResult(StateMachine stateMachine, Object input) {
        DeferredResult result = new DeferredResult();

        Mono<Void> stateMachineMono = stateMachine.startReactively();
        stateMachineMono.subscribe();

        GenericExecutionResult executionResult = (GenericExecutionResult) stateMachine.getExtendedState()
                .getVariables().get(GENERIC_EXECUTION_RESULT);
        executionResult.setInput(input);
        PingPongInputBO inputBO;
        try {
            inputBO = gson.fromJson(gson.toJsonTree(input), PingPongInputBO.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize input");
        }

        executionResult.setOutput(new PingPongOutputBO().setPlayer(inputBO.getPlayer()));
        executionResult.setInput(inputBO);
        stateMachine.addStateListener(new ResultProviderListener(result));
        return result;
    }

    @Override
    public String getId() {
        return MACHINE_ID;
    }
}
