### spring mini版本实现

1. 配置阶段：
    - 配置web.xml 
    - 设定init-param
    - 设定Url-pattern
    - 配置Annotation 
2. 初始化阶段：
    - 加载配置文件
    - Map<Stirng,Object>
    - scan-package="com.matrix"
    - 通过反射机制将类实例化放入IOC容器中
    - 扫描IOC容器中得实例，给没有赋值得属性自动赋值
    - 将一个url和一个Method进行一对一得关联影射Map<String,Method>

3. 运行阶段
    - 调用doPost/doGet
    - 匹配HandlerMapping
    - 反射调用method,invoker()
    - response.getWrite().write()


