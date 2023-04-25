package com.ruoyi.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.entity.Config;
import com.ruoyi.system.mapper.ConfigMapper;
import com.ruoyi.system.service.ConfigService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigMapper configMapper;

    @Override
    @Cacheable(value = "config",key = "#id")
    public Config selectByPrimaryKey(Long id) {
        return configMapper.selectById(id);
    }

    @Override
    public Page<Config> getConfigList(Integer page, Integer size) {
        return configMapper.selectPage(new Page<>(page,size),new QueryWrapper<Config>());
    }

    @Override
    @Transactional
    @CachePut(value = "config",key = "#config.id")
    public Boolean addConfig(Config config) {
        return  configMapper.insert(config)>0;
    }

    @Override
    @Transactional
    @CachePut(value = "config",key = "#config.id")
    public Boolean updateConfig(Config config) {
        return configMapper.updateById(config) > 0;
    }

    @Override
    @Cacheable(value = "config")
    public List<Config> selectConfigList(Config config) {
        return configMapper.selectList(new QueryWrapper<>(config));
    }

    public ConcurrentMap<String,String> getConfigs(){
        List<Config> configs = configMapper.getConfigs();
        return configs.stream().collect(Collectors.toConcurrentMap(Config::getConfigKey, Config::getConfigValue));
    }

    @CachePut(value = "config", key = "#key", unless = "#result == null")
    public String getConfig(String key) {
        return Optional.ofNullable(configMapper.selectOne(new QueryWrapper<Config>()
                        .eq("config_key", key)).getConfigValue())
                .orElse(" ");
    }

}
