package io.vdubovsky.statemachineexample.statemachine;

import java.util.Map;
import java.util.UUID;

public interface StateMachineService {

    UUID startProcess(String processDefinitionId, Map<String, Object> context);

    Map<String,Object> executeProcessAndGetResult(String processDefinitionId, Map<String, Object> args);
}

