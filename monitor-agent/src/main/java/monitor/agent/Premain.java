package monitor.agent;

import monitor.agent.log.AgentLoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/4.
 * java agent 用于增加方法监控，该类实现 premain 方法
 * 添加监控需要 java -javaagent:agent_path -Dmonitor_core_lib_path=libPath
 */
public class Premain {
    private static Logger LOG = AgentLoggerFactory.getLogger(Premain.class);

    /**
     * 该方法在 main 方法执行前调用
     *
     * @param agentArgs
     * @param instrumentation
     * @throws Exception
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        try {
            MonitorAgent.init(agentArgs, instrumentation);
        } catch (Throwable e) {
            LOG.log(Level.SEVERE, "Premain init monitor agent error, cause: ", e);
        }
    }
}
