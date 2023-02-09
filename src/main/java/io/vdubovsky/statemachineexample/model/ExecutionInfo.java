package io.vdubovsky.statemachineexample.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ExecutionInfo {
    private UUID processId;
    private String processDefinitionId;
    private String currentState;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
