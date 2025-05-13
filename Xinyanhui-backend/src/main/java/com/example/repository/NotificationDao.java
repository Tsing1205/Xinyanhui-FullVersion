package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationDao extends BaseMapper<Notification> {
}
