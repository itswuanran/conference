package com.microsoft.conference.common.tunnel;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.microsoft.conference.common.dataobject.ConferenceDO;
import com.microsoft.conference.common.mapper.ConferenceMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 数据通道，屏蔽底层mapper差异
 */
@Component
public class ConferenceTunnel {

    @Resource
    private ConferenceMapper conferenceMapper;

    public CompletableFuture<Integer> updateAsync(ConferenceDO conferenceDO, LambdaUpdateWrapper<ConferenceDO> updateWrapper) {
        return new ConferenceUpdateCommand(conferenceMapper, conferenceDO, updateWrapper).queue();
    }

    public Integer update(ConferenceDO conferenceDO, LambdaUpdateWrapper<ConferenceDO> updateWrapper) {
        return new ConferenceUpdateCommand(conferenceMapper, conferenceDO, updateWrapper).execute();
    }
}
