接口文档：https://xinyanhuiteam.postman.co/workspace/xinyanhui~c77005d1-b448-408c-93e2-555e484fa8d7/collection/43016002-73a9fe89-25d7-4690-96ff-590b0330658c?action=share&creator=43016002



## WebSocket设计

### 在线聊天

#### 路径

ws://localhost:8080/chat

#### 参数

| Key       | value | description                                                  |
| --------- | ----- | ------------------------------------------------------------ |
| token     |       | Jwt令牌                                                      |
| sessionId | 2     | 会话Id，对应ConsultationSession表中的session id和SupervisorConsultations表中的record id |
| type      | 0     | 0表示咨询师与来访的对话，1表示咨询师与督导的对话             |

#### 发送消息格式

```json
{
    "msg": "hello"
}
```

#### 接收消息格式

```json
{
    "isSystem": false,     //表示是否是来自系统的消息
    "msg":"hi",
    "time":"2025-03-20T08:26:45.3917069"
}
```



### 弹窗通知

#### 路径

ws://localhost:8080/notify

#### 参数

| Key   | value | description |
| ----- | ----- | ----------- |
| token |       | Jwt令牌     |

#### 接收消息格式

```json
{
    "type": 0,     //0不携带其他数据信息，仅通知
    "msg":"您有新的请假申请待处理",
    "time":"2025-03-20T08:26:45.3917069"
    "data":null
}
```



```json
{
    "type": 1,     //1表示咨询师请假后，来访收到的取消通知；或来访取消后咨询师收到的通知
    "msg":"您好，您有一条预约状态变更",
    "time":"2025-03-20T08:26:45.3917069"
    "data":{
    	"apointmentId":3,
    	"consultantId":5   //便于其重新预约，若是来访者取消没有该数据
	}
}
```



```json
{
    "type": 2,     //2表示新的对话
    "msg":"您有新的对话",
    "time":"2025-03-20T08:26:45.3917069"
    "data":{
    	"sessionId":4,  //用于开启新会话的chat会话id
    	"observeSessionId":5 //该参数只会在咨询师求助督导时，向督导客户端推送咨询师和来访间的会话id
	}
}
```



### 监听聊天（督导）

#### 路径

ws://localhost:8080/observe

#### 参数

| Key       | value | description                      |
| --------- | ----- | -------------------------------- |
| token     |       | Jwt令牌                          |
| sessionId |       | 被监听的来访和咨询师聊天的会话id |

#### 接收消息格式

```json
{
	"msg":"hi",
	"senderType": false,  //false为来访者发送，true为咨询师发送
	"time":"2025-03-20T08:26:45.3917069"
}
```

