package io.vdubovsky.statemachineexample.model.pingpong;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PingPongResultDto {

    private Integer retryCount;
}
