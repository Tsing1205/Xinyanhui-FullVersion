package com.example.repository;

import com.example.pojo.ChatMsg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatLogDao {

    @Insert("insert into SessionChatLog(session_id,content,sender_type,time) values(#{sessionId},#{Msg.msg},#{Msg.senderType},#{Msg.time})")
    int addSessionChatLog(Integer sessionId, ChatMsg Msg);

    @Insert("insert into RecordChatLog(record_id,content,sender_type,time) values(#{sessionId},#{Msg.msg},#{Msg.senderType},#{Msg.time})")
    int addRecordChatLog(Integer sessionId, ChatMsg Msg);

    List<ChatMsg> getSessionChatLog(Integer sessionId);

    List<ChatMsg> getRecordChatLog(Integer sessionId);
}
