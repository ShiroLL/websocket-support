# WebSocket Support

帮助SpringBoot应用更简单、快速地实现对websocket的支持。

### 特性

1. 支持自定义握手拦截器
2. 支持@Validated参数校验
3. 提供消息分发器默认实现，写法类似controller层的GetMapping （解析消息，分发，包装返回值，提供了基于Base64的字节数组传输，仅支持JSON数据且数据格式要符合RequestMessage.class定义）
4. 可通过切面扩展接口鉴权

### 使用说明

1. 引入`com.senjone:websocket-support`模块
2. 继承`BaseWebSocketHandler.class`，并添加`@WebSocketHandler`注解
3. 如果使用请求分发器，则需要注入`ActionDispatcher`，然后在`handleMessage`直接执行`ActionDispatcher.doAction`方法
4. 新建执行器类，需要使用`@Action`注解修饰。执行方法必须为public，且使用`@ActionMapping`注解修饰
5. 若不需要自动将数据返回客户端，请调用`WebSocketUtil.finish`方法

### 参数校验说明

Controller层以外@Validated注解不生效，因此通过切面实现了@Validated在其他接口的校验功能

1. 实体类属性校验规则与Controller层VO校验规则写法相同，但不支持group分组校验(务必不要写groups，否则会导致校验失效)
2. 嵌套校验需要在属性名上添加@Valid注解
3. 在需要校验的方法上添加`@Validated`注解，当前类必须被spring容器通过cgLib代理
4. 使用`@Valid`或其他`@NotNull`之类的限定注解修饰被校验方法参数

### 校验注解说明

| 分类         | 限定注解                   | 说明                                                                                |
| ------------ | -------------------------- | ----------------------------------------------------------------------------------- |
| *嵌套校验    | @Valid                     | *被此注解修饰的属性或参数，才会对其内部的属性进行校验。一般需要配合其他注解一起使用 |
|              |                            |                                                                                     |
| 空和非空检查 | @Null                      | 限制必须为null                                                                      |
|              | @NotNull                   | 限制不能为null                                                                      |
|              | @NotEmpty                  | 限制不能为null且不能为空(字符串长度部位0、集合大小不为0)                            |
|              | @NotBlank                  | 限制不能为null且不能为空(去除首尾空格后长度不为0)，只能用于String类型               |
|              |                            |                                                                                     |
| Boolean检查  | @AssertFalse               | 限制必须为false                                                                     |
|              | @AssertTrue                | 限制必须为true                                                                      |
|              |                            |                                                                                     |
| 长度检查     | @Size(max,min)             | 限制字符长度必须在min和max之间                                                      |
|              |                            |                                                                                     |
| 日期检查     | @Past                      | 限制必须为过去的时间                                                                |
|              | @PastOrPresent             | 限制必须为过去的时间或现在                                                          |
|              | @Future                    | 限制必须为未来的时间                                                                |
|              | @FutureOrPresent           | 限制必须为未来的时间或现在                                                          |
|              |                            |                                                                                     |
| 数值检查     | @Max(value)                | 限制必须为一个不大于指定值的数字                                                    |
|              | @Min(value)                | 限制必须为一个不小于指定值的数字                                                    |
|              | @DecimalMax(value)         | 限制必须为一个不大于指定值的数字                                                    |
|              | @DecimalMin(value)         | 限制必须为一个不小于指定值的数字                                                    |
|              | @Digits(integer, fraction) | 限制必须为一个小数，且整数部分的位数不超过integer，小数部分的位数不超过fraction     |
|              | @Negative                  | 限制必须为负整数                                                                    |
|              | @NegativeOrZero            | 限制必须为负整数或0                                                                 |
|              | @Positive                  | 限制必须为正整数                                                                    |
|              | @PositiveOrZero            | 限制必须为正整数或0                                                                 |
|              |                            |                                                                                     |
| 其他校验     | @Pattern                   | 限制必须符合指定的正则表达式                                                        |
|              | @Email                     | 限制必须为email                                                                     |

### 接口说明

1. BaseWebSocketHandler.class

| 方法名                     | 功能说明             | 备注                                                                   |
| -------------------------- | -------------------- | ---------------------------------------------------------------------- |
| afterConnectionEstablished | 连接建立后事件       |                                                                        |
| handleMessage              | 收到消息后事件       | 默认实现会根据消息类型转发到对应事件处理，若重写此方法则不会被默认转发 |
| handleTextMessage          | 收到Text消息后事件   |                                                                        |
| handleBinaryMessage        | 收到Binary消息后事件 |                                                                        |
| handlePongMessage          | 收到Pong消息后事件   |                                                                        |
| handleTransportError       | 连接中错误事件       |                                                                        |
| afterConnectionClosed      | 连接断开后事件       |                                                                        |
| supportsPartialMessages    | 是否支持部分消息     |

注意，父类已经统一引入log对象，子类可直接使用，无需重新引入。

2. @WebSocketHandler注解

| 方法名      | 类型                                  | 默认值                            | 功能说明                                              |
| ----------- | ------------------------------------- | --------------------------------- | ----------------------------------------------------- |
| value       | String                                | 必填                              | 注册监听的endpoint                                    |
| withSockJS  | boolean                               | false                             | 是否启用SockJS                                        |
| interceptor | Class<? extends HandshakeInterceptor> | DefaultHandshakeInterceptor.class | 请求握手拦截器，在接收到upgrade请求，且连接升级前触发 |

系统会根据此注解自动注册Handler，注意在单一模块内，确保value唯一

3. @Action注解

| 方法名 | 类型          | 默认值 | 功能说明                                                       |
| ------ | ------------- | ------ | -------------------------------------------------------------- |
| value  | String        | 必填   | 注册分发器uri前缀，会拼接在当前类方法@ActionMapping注解value前 |
| filter | Array{String} | {}     | 只允许制定endpoint的连接调用该分发器                           |

4. @ActionMapping注解

| 方法名 | 类型    | 默认值 | 功能说明                                                  |
| ------ | ------- | ------ | --------------------------------------------------------- |
| value  | String  | 必填   | 注册分发器uri前缀                                         |
| check  | boolean | true   | 是否检查连接有效性(token存活等)，此功能需要自行在切面实现 |

## application.yml配置说明

```yaml
spring:
  websocket:
    # 缓冲区大小，需要带单位（KB/MB/GB)，默认为8KB
    buffer-size: 8KB
    # 字节数据编码方式，可选值为Base32/Base62/Base64，默认为Base64
    codec: Base64
    # 默认分发器忽略的异常列表
    ignore-exceptions:
      - "AuthException"
      - "LibreOfficeException"
      - "PermissionException"
      - "RequestMethodException"
      - "ServiceException"
      - "ValidationException"
```

### 完整示例

`WebSocketHandshakeInterceptor.class`握手拦截器

```java
package com.senjone.platform.servers.resource.handler;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.web.UaUtil;
import WebSocketConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 拒绝没有UA标识的连接
        UserAgent userAgent = UaUtil.getUserAgent(request.getHeaders());
        if (userAgent == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        // 将一些参数放入session属性域
        // header
        attributes.put(request.getHeaders().getClass().getSimpleName(), request.getHeaders());
        // uri
        attributes.put(WebSocketConstant.ENDPOINT, request.getURI().getPath());
        // query参数
        HttpUtil.decodeParamMap(request.getURI().getQuery(), StandardCharsets.UTF_8).forEach(attributes::put);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
```

`MessageWebSocketHandler.class`连接处理Handler

```java
// handler
package com.senjone.platform.servers.resource.handler;

import WebSocketHandler;
import ActionDispatcher;
import BaseWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

@WebSocketHandler(value = "/message", interceptor = WebSocketHandshakeInterceptor.class)
public class MessageWebSocketHandler extends BaseWebSocketHandler {

    @Resource
    private ActionDispatcher actionDispatcher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 直接调用请求分发器
        actionDispatcher.doAction(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
```

`MessageAction.class`事务执行器

```java
package com.senjone.platform.servers.resource.action;

import com.senjone.platform.servers.resource.entity.Message;
import Action;
import ActionMapping;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Action
public class MessageAction {

    @Validated
    @ActionMapping(value = "message", check = false)
    public Message message(@Valid @NotNull(message = "消息不能为空") Message message) {
        return message;
    }
}
```

`Message.class`消息类

```java
package com.senjone.platform.servers.resource.entity;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Message implements Serializable {

    @Valid
    @NotNull(message = "发送者不能为空")
    private User from;

    @Valid
    @NotNull(message = "接收者不能为空")
    private User to;

    @NotBlank(message = "消息内容不能为空")
    private String content;
}
```

`User.class`用户类

```java
package com.senjone.platform.servers.resource.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
public class User implements Serializable {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Positive(message = "用户id不合法")
    @NotNull(message = "用户id不能为空")
    private Long id;

}
```

### 请求示例

连接

```
ws://127.0.0.1:10080/message
```

发送（需要转为Text或Binary）

```json
{
  "uri": "/message",
  "data": {
    "from": {
      "id": 1234567890123,
      "userName": "发送人"
    },
    "to": {
      "id": 1234567890124
    },
    "content": "测试消息"
  }
}

```

接收（需要转为Text或Binary）

```json
{
  "uri": "/message",
  "data": {
    "success": false,
    "code": 500,
    "message": "用户名不能为空"
  }
}
```
