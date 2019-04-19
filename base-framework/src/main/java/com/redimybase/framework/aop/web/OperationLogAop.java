package com.redimybase.framework.aop.web;

import com.redimybase.framework.annotation.SysLog;
import com.redimybase.manager.logger.entity.SysLogEntity;
import com.alibaba.fastjson.JSON;
import com.redimybase.manager.logger.service.SysLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 日志记录
 * Created by Irany 2018/7/30 10:30
 */
@Aspect
@Component
@Slf4j
public class OperationLogAop {


    @Pointcut("execution(public * com.*.*.controller..*.*(..))")
    public void logRequest() {

    }

    @Pointcut("execution(public * com.*.*.*.service..*.*(..))")
    public void logService() {

    }

    @Pointcut("execution(public * com.*.monitor.consumer..*.*(..))")
    public void logAutoCheckService() {

    }

    /**
     * Controller调用日志
     */
    @Before("logRequest()")
    public void beforeRequest(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLog syslog = method.getAnnotation(SysLog.class);

        if (syslog != null && !syslog.enable()) return;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        log.info("Request: " + request.getRequestURL().toString());
        log.info("Mapping: " + request.getAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping"));
        log.info("Method: " + request.getMethod());
        log.info("IP: " + this.getIpAddr(request) + "\n");

        StringBuffer buffer = new StringBuffer();

        Enumeration<String> enu = request.getParameterNames();
        if (enu.hasMoreElements()) {
            buffer.append("incoming params: [");
            while (enu.hasMoreElements()) {
                String key = enu.nextElement();
                buffer.append("(key: ").append(key).append(",");
                buffer.append("value: ").append(request.getParameter(key)).append(")");
            }
            buffer.append("]");
            log.info(buffer.toString());
        }

        saveLog(buffer.toString(), point);

        Object[] obj = point.getArgs();
        buffer = new StringBuffer();
        if (obj != null) {
            buffer.append("params:");
            for (int i = 0, j = 1; i < obj.length; i++) {
                Object o = obj[i];
                if (o instanceof Model) {
                    continue;
                }
                String parameter;
                try {
                    parameter = JSON.toJSONString(o);
                } catch (Exception e) {
                    continue;
                }
                buffer.append(" [参数").append(j).append(":");
                buffer.append(parameter);
                buffer.append("]");
                j++;
            }
        }

        String str = buffer.toString();
        if (StringUtils.isNotBlank(str)) {
            log.info(str);
        }

    }


    /**
     * Service调用日志
     *
     * @param point
     */
    @Before("logService()")
    public void beforeService(JoinPoint point) {
        log.info("Call Service: [" + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "]\n");
    }

    /**
     * 保存日志
     *
     * @param point
     */
    private void saveLog(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLog = new SysLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.desc());

            sysLog.setType(syslog.type());
        }

        //请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = point.getArgs();
        if (args.length > 0) {
            String params = JSON.toJSONString(args[0]);
            sysLog.setParams(params);
        }

        //获取request
        /*HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //设置IP地址
        sysLog.setIp(request.getRemoteAddr());

        sysLog.setUserName("irany");

        sysLog.setCreateTime(new Date());
        //保存系统日志
        service.save(sysLog);*/
    }

    /**
     * Job运行日志
     *
     * @param point
     */
    @Before("logAutoCheckService()")
    public void beforeAutoCheckService(JoinPoint point) {
        saveLog(null, point);
    }


    /**
     * 保存日志
     *
     * @param point
     */
    private void saveLog(String params, JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLog = new SysLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);

        if (syslog != null && !syslog.enable()) {
            return;
        }
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.desc());

            sysLog.setType(syslog.type());
        }

        //请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        if (StringUtils.isEmpty(params)) {
            Object[] args = point.getArgs();
            if (args.length > 0) {
                if (!(args[0] instanceof ShiroHttpServletRequest)) {
                    String ps = JSON.toJSONString(args[0]);
                    sysLog.setParams(ps);
                }
            }
        } else {
            sysLog.setParams(params);
        }

        /*//获取request
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //设置IP地址
        sysLog.setIp(request.getRemoteAddr());

        sysLog.setUserName("irany");

        sysLog.setCreateTime(new Date());
        //保存系统日志
        service.save(sysLog);*/
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public class Type {
        public static final String DEFAULT = "default";
        public static final String JOB = "default";
        public static final String SERVICE = "service";
        public static final String CONTROLLER = "controller";
        public static final String DAO = "dao";
        public static final String MANAGER = "manager";
    }


    @Autowired
    @Lazy
    private SysLogServiceImpl service;
}
