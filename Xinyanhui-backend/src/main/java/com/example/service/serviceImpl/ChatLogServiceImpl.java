package com.example.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.example.constants.ChatConstant;
import com.example.pojo.ChatExportVo;
import com.example.pojo.ChatMsg;
import com.example.repository.ChatLogDao;
import com.example.service.ChatLogService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatLogServiceImpl implements ChatLogService {
    private final ChatLogDao chatLogDao;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public ChatLogServiceImpl(ChatLogDao chatLogDao, StringRedisTemplate redisTemplate) {
        this.chatLogDao = chatLogDao;
        this.redisTemplate = redisTemplate;
    }
    @Override
    public Result<List<ChatMsg>> getSessionLog(Integer sessionId) {
        List<ChatMsg> msgList= null;
        String key = "chat:msg:list:"+ ChatConstant.CONSULTANTION_TYPE+":"+sessionId;
        if(redisTemplate.hasKey(key)){
            List<String> list = redisTemplate.opsForList().range(key, 0, -1);
            msgList = new ArrayList<>();
            while(!list.isEmpty()){
                ChatMsg chatMsg = JSON.parseObject(list.remove(0), ChatMsg.class);  //0为最近一条消息
                msgList.add(0, chatMsg);  //每一次加入的消息的发送时间都比上一次早，添加到最前面
            }
            return Result.success(msgList);
        }
        msgList = chatLogDao.getSessionChatLog(sessionId);
        if(msgList ==null || msgList.isEmpty()){
            return Result.error("无记录");
        }
        return Result.success(msgList);
    }

    @Override
    public Result<List<ChatMsg>> getRecordLog(Integer recordId) {
        List<ChatMsg> msgList= null;
        String key = "chat:msg:list:"+ ChatConstant.SUPERVISE_TYPE+":"+recordId;
        if(redisTemplate.hasKey(key)){
            List<String> list = redisTemplate.opsForList().range(key, 0, -1);
            msgList = new ArrayList<>();
            while(!list.isEmpty()){
                ChatMsg chatMsg = JSON.parseObject(list.remove(0), ChatMsg.class);  //0为最近一条消息
                msgList.add(0, chatMsg);  //每一次加入的消息的发送时间都比上一次早，添加到最前面
            }
            return Result.success(msgList);
        }
        msgList=chatLogDao.getRecordChatLog(recordId);
        if(msgList ==null || msgList.isEmpty()){
            return Result.error("无记录");
        }
        return Result.success(msgList);
    }

    @Override
    public Result<List<ChatExportVo>> getExportDataBySessionId(Integer sessionId) {
        List<ChatMsg> chatMsgs = chatLogDao.getSessionChatLog(sessionId); // 使用 chatLogDao

        if (chatMsgs == null || chatMsgs.isEmpty()) {
            return Result.error("无记录");
        }

        List<ChatExportVo> exportList = new ArrayList<>();
        for (ChatMsg msg : chatMsgs) {
            ChatExportVo vo = new ChatExportVo();
            vo.setSender(msg.getSenderType() ? "咨询师" : "用户");
            vo.setReceiver(msg.getSenderType() ? "用户" : "咨询师");
            vo.setMessage(msg.getMsg());
            vo.setTimestamp(msg.getTime());
            exportList.add(vo);
        }
        return Result.success(exportList);
    }
}