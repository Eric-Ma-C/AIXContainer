# AIXContainer
The management module of containers running machine learning algorithms from zju-vipa AI+X platform

AIXContainer分为三部分:
- **Client**           容器客户端
- **ManagementCenter**   平台管理服务器
- **Common**             公共模块

## Client   
容器客户端,常驻容器中(ContainerManager)

- 入口为`ContainerManager`类

- `TcpClient`负责主动建立网络连接

- `ShellTask`封装了在大妈机上执行shell命令的功能

- `ContainerController`为总的调度器,负责容器所有功能的调度

## ManagementCenter   
平台管理中心,是一个TCP server,监视管理所有容器

- `ContainerManagementCenter`为入口

- `DbManager`负责从AIX mysql数据库存取数据

- `CenterTcpServer`负责启动服务器的socket监听,为所有容器提供服务

- `SocketHandler`负责单个socket请求的处理,目前使用短连接模式,socket请求处理完成后即断开连接


## Common   
公共模块,如数据格式,统一接口等

- `Message`规范了大妈机和平台通信的数据格式

- `Intent`定义了Message的类型

- `NetworkConfig`定义了网络参数,如平台服务器ip和端口号

- `Utils`包定义了Client和ManagementCenter工程通用的工具类

# 流程图

