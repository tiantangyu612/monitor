package me.flyness.monitor.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import me.flyness.monitor.log.CollectorLogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/5.
 * JavaMethodTransformer，使用 Javassist 增加被监控的方法，记录方法的执行 rt、错误数、最大并发、异常等信息
 */
public class JavaMethodTransformer implements ClassFileTransformer {
    private static Logger LOG = CollectorLogFactory.getLogger(JavaMethodTransformer.class);

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        ByteArrayInputStream classFileByteArrayInputStream = null;
        try {
            classFileByteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(classFileByteArrayInputStream);

            CtMethod[] methods = ctClass.getDeclaredMethods();
            if (methods != null && methods.length != 0) {
                for (CtMethod method : methods) {
                    System.out.println(method);
                }
            }
        } catch (IOException e) {
            String errorMsg = "makeClass error, className is: " + className;
            LOG.log(Level.SEVERE, errorMsg, e);
        } finally {
            if (null != classFileByteArrayInputStream) {
                try {
                    classFileByteArrayInputStream.close();
                } catch (IOException e) {
                    String errorMsg = "classFileByteArrayInputStream error";
                    LOG.log(Level.SEVERE, errorMsg, e);
                }
            }
        }

        return classfileBuffer;
    }
}
