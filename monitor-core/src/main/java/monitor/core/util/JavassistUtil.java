package monitor.core.util;

import javassist.*;
import javassist.bytecode.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lizhitao on 2018/1/9.
 * javassist 工具类
 */
public class JavassistUtil {
    private JavassistUtil() {
    }

    /**
     * 将类添加到 classpath
     *
     * @param className
     * @param loader
     * @return
     * @throws ClassNotFoundException
     * @throws NotFoundException
     */
    public static CtClass addToClassPathIfNotExist(String className, ClassLoader loader) throws ClassNotFoundException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();

        try {
            return pool.get(className);
        } catch (NotFoundException e) {
            Class clazz = loader.loadClass(className);
            pool.insertClassPath(new ClassClassPath(clazz));
            return pool.get(className);
        }
    }

    /**
     * 获取方法全名
     *
     * @param ctMethod
     * @return
     */
    public static String getMethodFullName(CtMethod ctMethod) {
        MethodInfo methodInfo = getMethodInfo(ctMethod.getSignature());
        String methodName = ctMethod.getName() + methodInfo.getMethodSignature();
        String className = ctMethod.getDeclaringClass().getName();
        return className + "." + methodName;
    }

    /**
     * 获取方法信息
     *
     * @param signature
     * @return
     */
    public static MethodInfo getMethodInfo(String signature) {
        int idx1 = signature.indexOf("(");
        int idx2 = signature.indexOf(")");
        String source = signature.substring(idx1 + 1, idx2);
        String returnTypeString = signature.substring(idx2 + 1);
        int index = 0;
        List<MethodParameterSignature> methodParameterSignatures = new ArrayList<MethodParameterSignature>();
        StringBuilder sb = new StringBuilder();

        while (true) {
            if (sb.length() != 0) {
                methodParameterSignatures.add(new MethodParameterSignature(sb.toString()));
                sb.delete(0, sb.length());
            }

            if (index == source.length()) {
                List<String> classTypeStrings = new ArrayList<String>();
                StringBuilder methodName = new StringBuilder();
                methodName.append("(");

                for (int returnSign = 0; returnSign < methodParameterSignatures.size(); ++returnSign) {
                    MethodParameterSignature methodParameterSignature = methodParameterSignatures.get(returnSign);
                    String classTypeString = methodParameterSignature.getClassTypeString();
                    if (classTypeString != null) {
                        classTypeStrings.add(classTypeString);
                    }

                    if (returnSign == 0) {
                        methodName.append(methodParameterSignature.toDisplayString());
                    } else {
                        methodName.append(",");
                        methodName.append(methodParameterSignature.toDisplayString());
                    }
                }

                methodName.append(")");
                MethodParameterSignature methodParameterSignature = new MethodParameterSignature(returnTypeString);
                MethodInfo methodInfo = new MethodInfo(classTypeStrings, methodName.toString(), methodParameterSignature.getClassTypeString());
                return methodInfo;
            }

            char paramClassList = source.charAt(index);
            if (paramClassList == 91) {
                sb.append(paramClassList);
                ++index;
                paramClassList = source.charAt(index);
                if (paramClassList == 91) {
                    sb.append(paramClassList);
                    ++index;
                    index = getTypeString(sb, source, index);
                } else {
                    index = getTypeString(sb, source, index);
                }
            } else {
                index = getTypeString(sb, source, index);
            }
        }
    }

    private static int getTypeString(StringBuilder sb, String source, int index) {
        char c = source.charAt(index++);
        sb.append(c);
        if (c == 76) {
            do {
                c = source.charAt(index++);
                sb.append(c);
            } while (c != 59);
        }

        return index;
    }

    public static void copyMethodAnnotationdAttributes(javassist.bytecode.MethodInfo oldMethod, javassist.bytecode.MethodInfo newMethod) {
        if (oldMethod != null && newMethod != null) {
            AnnotationsAttribute oldAnnotations = (AnnotationsAttribute) oldMethod.getAttribute("RuntimeVisibleAnnotations");
            ParameterAnnotationsAttribute oldParamAnnotations = (ParameterAnnotationsAttribute) oldMethod.getAttribute("RuntimeVisibleParameterAnnotations");
            AnnotationsAttribute newAnnotations = (AnnotationsAttribute) newMethod.getAttribute("RuntimeVisibleAnnotations");
            ParameterAnnotationsAttribute newParamAnnotations = (ParameterAnnotationsAttribute) newMethod.getAttribute("RuntimeVisibleParameterAnnotations");
            AttributeInfo ann;
            if (oldAnnotations != null && newAnnotations == null) {
                ann = oldAnnotations.copy(newMethod.getConstPool(), Collections.EMPTY_MAP);
                newMethod.addAttribute(ann);
            }

            if (oldParamAnnotations != null && newParamAnnotations == null) {
                ann = oldParamAnnotations.copy(newMethod.getConstPool(), Collections.EMPTY_MAP);
                newMethod.addAttribute(ann);
            }

        }
    }

    public static String getLocalVariableString(CtMethod method) {
        javassist.bytecode.MethodInfo mi = method.getMethodInfo();
        if (mi == null) {
            return null;
        } else {
            CodeAttribute cb = mi.getCodeAttribute();
            if (cb == null) {
                return null;
            } else {
                LocalVariableAttribute attr = (LocalVariableAttribute) cb.getAttribute("LocalVariableTable");
                if (attr == null) {
                    return null;
                } else {
                    String[] variableNames = new String[0];

                    try {
                        variableNames = new String[method.getParameterTypes().length];
                    } catch (NotFoundException var11) {
                        var11.printStackTrace();
                    }

                    int staticIndex = Modifier.isStatic(method.getModifiers()) ? 0 : 1;

                    for (int sb = 0; sb < variableNames.length; ++sb) {
                        variableNames[sb] = attr.variableName(sb + staticIndex);
                    }

                    StringBuilder var12 = new StringBuilder();
                    var12.append("(");
                    String[] arr$ = variableNames;
                    int len$ = variableNames.length;

                    for (int i$ = 0; i$ < len$; ++i$) {
                        String s = arr$[i$];
                        var12.append(s).append(",");
                    }

                    var12.append(")");
                    return var12.toString();
                }
            }
        }
    }

    public static void removeAnnotationAttribute(javassist.bytecode.MethodInfo minfo) {
        if (minfo != null) {
            List allAttributes = minfo.getAttributes();
            if (allAttributes != null) {
                Iterator it = allAttributes.iterator();

                while (true) {
                    Object o;
                    do {
                        if (!it.hasNext()) {
                            return;
                        }

                        o = it.next();
                    } while (!(o instanceof AnnotationsAttribute) && !(o instanceof ParameterAnnotationsAttribute));

                    it.remove();
                }
            }
        }
    }

    public static String getMethodAttributeString(javassist.bytecode.MethodInfo minfo) {
        StringBuilder sb = new StringBuilder();
        if (minfo != null) {
            List allAttributes = minfo.getAttributes();
            if (allAttributes != null) {
                Iterator it = allAttributes.iterator();

                while (it.hasNext()) {
                    Object o = it.next();
                    sb.append(o.getClass().getSimpleName()).append(",");
                }
            }
        }

        sb.append(")");
        return sb.toString();
    }

    public static void createProxyMethod(CtMethod method, CtClass clazz, String before, String catchThrowable, String dofinally) throws CannotCompileException, NotFoundException {
        CtMethod newMethod = CtNewMethod.copy(method, clazz, (ClassMap) null);
        String oldName = method.getName() + "$sentryProxy";
        method.setName(oldName);
        if (Modifier.isPublic(method.getModifiers())) {
            int body = method.getModifiers() - 1 + 2;
            method.setModifiers(body);
        }

        StringBuilder body1 = new StringBuilder();
        if (method.getReturnType() == CtClass.voidType) {
            body1.append("{ " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.intType) {
            body1.append("{ int result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.booleanType) {
            body1.append("{ boolean result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.byteType) {
            body1.append("{ byte result  = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.charType) {
            body1.append("{ char result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.doubleType) {
            body1.append("{ double result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.floatType) {
            body1.append("{ float result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.longType) {
            body1.append("{ long result = " + oldName + "($$);");
        } else if (method.getReturnType() == CtClass.shortType) {
            body1.append("{ short result = " + oldName + "($$);");
        } else {
            body1.append("{ Object result = " + oldName + "($$);");
        }

        if (method.getReturnType() == CtClass.voidType) {
            body1.append("}");
        } else {
            body1.append("return result;}");
        }

        newMethod.setBody(body1.toString());
        newMethod.insertBefore(before);
        ClassPool pool = ClassPool.getDefault();
        CtClass etype = pool.get("java.lang.Throwable");
        newMethod.addCatch(catchThrowable, etype);
        newMethod.insertAfter(dofinally, true);
        clazz.addMethod(newMethod);
    }

    public static class MethodInfo {
        private List<String> paramClassList;
        private String methodSignature;
        private String returnClassString;

        public MethodInfo(List<String> paramClassList, String trimedMethodName, String returnClassString) {
            this.paramClassList = paramClassList;
            this.methodSignature = trimedMethodName;
            this.returnClassString = returnClassString;
        }

        public String getMethodSignature() {
            return this.methodSignature;
        }

        public List<String> getParamClassList() {
            return this.paramClassList;
        }

        public void setParamClassList(List<String> paramClassList) {
            this.paramClassList = paramClassList;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("paramClassList:").append(this.paramClassList.toString()).append("\n");
            sb.append("trimedMethodName:").append(this.methodSignature).append("\n");
            sb.append("returnClassString:").append(this.returnClassString);
            return sb.toString();
        }

        public String getReturnClassString() {
            return this.returnClassString;
        }
    }
}
