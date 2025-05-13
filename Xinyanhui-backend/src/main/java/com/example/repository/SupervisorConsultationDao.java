package com.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.SupervisorConsultation;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface SupervisorConsultationDao extends BaseMapper<SupervisorConsultation> {

    List<Map<String,Object>> getAbnormalSupervise();

    List<Map<String,Object>> findWithNameBySupervisorId(Integer supervisorId, LocalDateTime startTime, LocalDateTime endTime);

    List<Map<String,Object>> findWithNameByConsultantId(Integer consultantId, LocalDateTime startTime, LocalDateTime endTime);
}
