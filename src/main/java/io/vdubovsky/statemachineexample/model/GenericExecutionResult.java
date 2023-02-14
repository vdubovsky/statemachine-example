package io.vdubovsky.statemachineexample.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class GenericExecutionResult<I, O> implements Serializable {

    private ExecutionInfo executionInfo;

    private I input;
    private O output;

    public void setOutput(O output) {
        this.output = output;
    }
}
