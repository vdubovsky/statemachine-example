package io.vdubovsky.statemachineexample.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExecutionResult<A, R> {

    private UUID processId;

    private A arguments;
    private R result;

    private Throwable exception;
}
