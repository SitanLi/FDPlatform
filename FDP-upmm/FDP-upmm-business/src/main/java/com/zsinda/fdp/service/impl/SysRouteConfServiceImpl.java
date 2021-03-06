package com.zsinda.fdp.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsinda.fdp.entity.SysRouteConf;
import com.zsinda.fdp.mapper.SysRouteConfMapper;
import com.zsinda.fdp.service.SysRouteConfService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRouteConfServiceImpl extends ServiceImpl<SysRouteConfMapper, SysRouteConf> implements SysRouteConfService{

    @Override
    public List<SysRouteConf> getRoutes() {
        return list(Wrappers.<SysRouteConf>lambdaQuery().eq(SysRouteConf::getDelFlag,1));
    }

}
