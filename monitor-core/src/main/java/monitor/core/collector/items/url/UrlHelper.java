//package monitor.core.collector.items.url;
//
//import java.util.regex.MatchResult;
//
///**
// * Created by bjlizhitao on 2018/1/12.
// * tomcat url transformer
// */
//public class UrlHelper {
//    private UrlHelper() {
//    }
//
//    public static String generateBeforeCode(String param) {
//        String matchResultClassName = MatchResult.class.getName();
//        String collectorClassName = UrlStatsCollector.class.getName();
//        StringBuilder sb = new StringBuilder();
//        sb.append("{ ");
//        sb.append("javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest)" + param + ";");
//        sb.append(matchResultClassName + " result=" + collectorClassName + ".onMatch(req.getRequestURI());");
//        sb.append("if(result!=null) {");
//        sb.append("if(result.hasAction()){");
//        sb.append("req.setCharacterEncoding(result.cs);");
//        sb.append("String targeturl=result.transform(req.getParameterMap());");
//        sb.append(collectorClassName + ".onStart(targeturl,req.getMethod());");
//        sb.append("} else {");
//        sb.append(collectorClassName + ".onStart(result.targetUrl,req.getMethod());");
//        sb.append("}");
//        sb.append(collectorClassName + ".onStartCollectNativeUrlInfo(req.getRequestURI());");
//        sb.append("}");
//        sb.append(" }");
//        return sb.toString();
//    }
//}