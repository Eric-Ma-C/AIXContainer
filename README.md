# AIXContainer
The management project of docker containers running machine learning algorithms from zju-vipa AI+X platform

AIXContainer分为四部分:
- **Client**           容器客户端
- **ManagementCenter**   平台管理服务器
- **Common**             公共模块
- **dubbo-api**          Dubbo接口定义

## Client   
容器客户端,常驻容器中(ContainerManager)

- 入口为`Client`类

- `NettyTcpClient`负责主动建立网络连接

- `ShellProcess`封装了在容器中执行shell命令的功能

- `ClientTaskController`为总的调度器,负责容器所有功能的调度

## ManagementCenter   
平台管理中心,是一个TCP server,监视管理所有容器

- `ManagementCenter`为程序入口

- `AixDbManager`负责从AIX mysql数据库存取数据

- `NettyTcpServer`负责启动服务器的socket监听,为所有容器提供服务

- `SocketHandler`负责单个socket请求的处理,目前使用短连接模式,socket请求处理完成后即断开连接


## Common   
公共模块,如数据格式,统一接口等

- `Message`规范了容器和平台通信的数据格式

- `Intent`定义了Message的类型

- `NetworkConfig`定义了网络参数,如平台服务器ip和端口号

- `Utils`包定义了Client和ManagementCenter工程通用的工具类


