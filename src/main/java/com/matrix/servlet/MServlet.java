package com.matrix.servlet;

import com.matrix.annotation.MAitowrited;
import com.matrix.annotation.MController;
import com.matrix.annotation.MRequestMapping;
import com.matrix.annotation.MService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
  * 这是一个类说明
  * @author (yu.jiao)
  * @date 2020/02/08
  */
public class MServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private List<String> classNames= new ArrayList<String>();

    private Map<String,Object> ioc = new HashMap<String, Object>();

    private Map<String,Method> handlerMapping = new HashMap<String, Method>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI() ;
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath,"").replaceAll("/+","/");
        if (!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404 not found");
            return;
        }
        Map<String,String[]> params = req.getParameterMap();
        Method method = this.handlerMapping.get(url);
        String beanName = method.getDeclaringClass().getSimpleName();
        try {
            method.invoke(ioc.get(beanName),new Object[]{req,resp});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //加载配置文件
//        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        doLoadConfig("application.properties");
        //扫描相关类
        doSacnner(contextConfig.getProperty("scanPackage"));
        //初始话相关得类，并且保存到IOC容器中
        doInstance();
        //完成依赖注入
        doAutowrited();
        //初始化HandlerMapping
        initHandlerMapping();

        System.out.println("M spring framework is init");
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()){
            return;
        }
        for (Map.Entry<String,Object> entry : ioc.entrySet()){
            Class<?> clazz = entry.getValue().getClass() ;
            if (!clazz.isAnnotationPresent(MController.class)){
                continue;
            }
            String baseUrl = "";
            if (clazz.isAnnotationPresent(MRequestMapping.class)){
                MRequestMapping mapping = clazz.getAnnotation(MRequestMapping.class);
                baseUrl = mapping.value().trim();
            }
            for (Method method : clazz.getMethods()){
                if (!method.isAnnotationPresent(MRequestMapping.class)){
                    continue;
                }
                MRequestMapping mapping = method.getAnnotation(MRequestMapping.class);
                String url = ("/" + baseUrl + "/" + mapping.value().trim()).replaceAll("/+","/");
                handlerMapping.put(url,method);
                System.out.println("mapped : " + url + "," + method);
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()){
            return;
        }
        for (String className : classNames){
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MController.class)){
                    Object object = clazz.newInstance();
                    String beanName = toLowerFirst(clazz.getSimpleName());
                    ioc.put(beanName,object);
                }else if (clazz.isAnnotationPresent(MService.class)){
                    //1.beanName默认得是类名首字母小写
                    String beanName = toLowerFirst(clazz.getSimpleName());
                    //2.自定义beanName
                    MService service = clazz.getAnnotation(MService.class);
                    if (!"".equals(service)){
                        beanName = service.value();
                    }
                    Object object = clazz.newInstance();
                    ioc.put(beanName,object);
                    //3.根据类型注入，beanName保存为接口得全称
                    for (Class<?> i : clazz.getInterfaces()){
                        if (ioc.containsKey(i.getName())){
                            throw new Exception("The beanName " + i.getName() + " is exists !");
                        }
                        ioc.put(beanName,object);
                    }
                }else {
                    continue;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String toLowerFirst(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32 ;
        return new String(chars);
    }

    private void doAutowrited() {
        if (ioc.isEmpty()){
            return;
        }
        for (Map.Entry<String,Object> entry : ioc.entrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields){
                if (!field.isAnnotationPresent(MAitowrited.class)){
                    continue;
                }
                MAitowrited mAitowrited = field.getAnnotation(MAitowrited.class);
                String beanName = mAitowrited.value().trim();
                if ("".equals(beanName)){
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    //利用反射进行注入
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doSacnner(String scanPackage) {
        //包路径
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()){
            if (!file.isDirectory()){
                if (!file.getName().endsWith(".class")){
                    continue;
                }
                //此时如果扫描到得文件为xml properties 等等
                String className = (scanPackage + file.getName()).replace("class","") ;
                classNames.add(className);
            }else {
                doSacnner(scanPackage + "." + file.getName());
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != ins){
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
