package github.axolotl.backend.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum SessionStatusType {
        WAITING_RESPONSE("等待AI回复"),
    WAITING_TOLL_CALL("等待工具调用"),
    STOP("对话结束")
    ;
        String name;
}
