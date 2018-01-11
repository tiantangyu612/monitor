package monitor.core.collector.base.transformer;

import monitor.core.log.MonitorLogFactory;
import monitor.core.util.CollectionUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bjlizhitao on 2018/1/11.
 * 监控 Transformer
 */
public class MonitorTransformer implements ClassFileTransformer {
    private final Logger logger = MonitorLogFactory.getLogger(getClass());

    private List<MatchedClassTransformer> matchedClassTransformers = new ArrayList<MatchedClassTransformer>();
    private Map<String, SpecifiedClassTransformer> specifiedClassTransformers = new HashMap<String, SpecifiedClassTransformer>();

    /**
     * 添加 BytecodeTransformer
     *
     * @param bytecodeTransformer
     */
    public void addTransformer(BytecodeTransformer bytecodeTransformer) {
        if (bytecodeTransformer instanceof MatchedClassTransformer) {
            MatchedClassTransformer matchedClassTransformer = (MatchedClassTransformer) bytecodeTransformer;
            matchedClassTransformers.add(matchedClassTransformer);
        } else if (bytecodeTransformer instanceof SpecifiedClassTransformer) {
            SpecifiedClassTransformer specifiedClassTransformer = (SpecifiedClassTransformer) bytecodeTransformer;
            Set<String> specifiedClassNames = specifiedClassTransformer.getClassNames();
            if (CollectionUtils.isNotEmpty(specifiedClassNames)) {
                for (String specifiedClassName : specifiedClassNames) {
                    specifiedClassTransformers.put(specifiedClassName, specifiedClassTransformer);
                }
            }
        } else {
            logger.info("can not addTransformer because bytecodeTransformer is not exists!");
        }
    }

    /**
     * 转换类字节码
     *
     * @param loader
     * @param className
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            SpecifiedClassTransformer specifiedClassTransformer = specifiedClassTransformers.get(className);
            if (specifiedClassTransformer != null) {
                return specifiedClassTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            }

            for (MatchedClassTransformer matchedClassTransformer : matchedClassTransformers) {
                MatchedClass matchedClass = matchedClassTransformer.getMatchedClass(loader, className, classfileBuffer, classBeingRedefined);
                if (null != matchedClass) {
                    return matchedClassTransformer.transform(matchedClass);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed to transform byte code for class:" + className, e);
        }

        return classfileBuffer;
    }
}
