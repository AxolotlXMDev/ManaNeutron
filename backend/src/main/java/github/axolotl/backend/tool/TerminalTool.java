package github.axolotl.backend.tool;

import dczx.axolotl.command.ExecUtil;
import dczx.axolotl.terminal.ProcessTerminal;
import dczx.axolotl.terminal.SimpleTerminal;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.List;

public class TerminalTool {
        @Tool("Run command in Powershell/Bash")
//TODO <env>提示词注入
        List<SimpleTerminal.HistoryEntry> run_command(String command, @P(required = false) String workDir) throws Exception {
                if (workDir == null || workDir.isEmpty()) {
                        workDir = ".";
                }
                String startCommand =
                        System.getProperty("os.name").toLowerCase().contains("windows") ? "powershell" : "bash";//TODO 从配置文件读取
                ProcessTerminal terminal = new ProcessTerminal(startCommand, workDir);//TODO 考虑是否维护这个对象
                terminal.execute(command);
                return terminal.getHistory();

        }
}
