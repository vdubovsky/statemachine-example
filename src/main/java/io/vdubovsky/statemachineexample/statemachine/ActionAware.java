package io.vdubovsky.statemachineexample.statemachine;

import org.springframework.statemachine.action.Action;

public interface ActionAware<S,E> extends Action<S,E> {

    E getStateEntryPoint();

    String getStateMachineId();
}
