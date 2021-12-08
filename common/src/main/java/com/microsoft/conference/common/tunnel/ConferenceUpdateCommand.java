package com.microsoft.conference.common.tunnel;

import com.alibaba.c2m.smartlinker.command.AbstractAdvanceLocalCommand;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.microsoft.conference.common.dataobject.ConferenceDO;
import com.microsoft.conference.common.mapper.ConferenceMapper;

public class ConferenceUpdateCommand extends AbstractAdvanceLocalCommand<Integer, Integer> {

    private ConferenceMapper conferenceMapper;

    private ConferenceDO conferenceDO;

    private LambdaUpdateWrapper<ConferenceDO> updateWrapper;

    public ConferenceUpdateCommand(ConferenceMapper conferenceMapper, ConferenceDO conferenceDO, LambdaUpdateWrapper<ConferenceDO> updateWrapper) {
        this.conferenceMapper = conferenceMapper;
        this.conferenceDO = conferenceDO;
        this.updateWrapper = updateWrapper;
    }

    @Override
    protected Integer callMethod() {
        return conferenceMapper.update(conferenceDO, updateWrapper);
    }

    @Override
    protected Integer convertResult(Integer rpcResult) {
        return rpcResult;
    }

    @Override
    protected String getResourceKey() {
        return ConferenceUpdateCommand.class.getName();
    }

    @Override
    protected Integer getFallbackResult() {
        return 0;
    }
}
