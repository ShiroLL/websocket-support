#更新日志

## 1.5.3 - 2022-09-01

### 修改

1. Validated校验支持groups分组校验
2. 从@Validated修改为@ActionMapping进入切面

## 1.5.2 - 2022-09-01

### 修改

1. HibernateValidatedAop修改为ValidatedAop
2. Validated校验返回信息优化

## 1.5.1 - 2022-08-28

### 修改

1. 阻止WebSocketSession序列化

## 1.5 - 2022-08-26

### 添加

1. 支持自定义默认分发器异常忽略列表(由分发器捕获后直接返回给客户端，不再向上抛出)
2. 支持自定义ChannelStatus枚举类

### 修改

1. 修复打包时默认编码错误问题(统一为utf-8)

## 1.4 - 2022-08-26

### 修改

1. 完善additional-spring-configuration-metadata.json

## 1.3 - 2022-08-25

### 新增

1. 实现请求拦截器自定义

## 1.2 - 2022-08-25

### 新增

1. 实现@Validated参数校验

## 1.1 - 2022-08-24

### 新增

1. 实现ActionDispatcher消息分发器


## 1.0 - 2022-08-23

### 新增

1. 实现websocket配置注入，通过@WebSocketHandler注解自动扫描注册websocket服务
