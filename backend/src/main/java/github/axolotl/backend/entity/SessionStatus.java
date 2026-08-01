package github.axolotl.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于存储一个运行时的会话的数据
 * 该类仅存在于内存，不会被反序列化
 */
@Data
@Builder
@AllArgsConstructor
public class SessionStatus {
}
