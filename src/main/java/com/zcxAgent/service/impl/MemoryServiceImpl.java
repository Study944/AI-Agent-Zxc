package com.zcxAgent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcxAgent.model.Memory;
import com.zcxAgent.service.MemoryService;
import com.zcxAgent.mapper.MemoryMapper;
import org.springframework.stereotype.Service;

/**
 * 对话记忆持久化
 */
@Service
public class MemoryServiceImpl extends ServiceImpl<MemoryMapper, Memory>
    implements MemoryService{

}




