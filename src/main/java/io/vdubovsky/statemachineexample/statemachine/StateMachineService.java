package io.vdubovsky.statemachineexample.statemachine;


import java.util.UUID;

public interface StateMachineService {

    Object executeProcessAndGetResult(String processDefinitionId, Object input);

    Object sendEvent(String processDefinitionId, UUID processId, String event, Object input);
}

