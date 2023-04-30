package com.ruoyi.system.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.Config;

import java.util.List;

public interface ConfigService {

    Config selectByPrimaryKey(Long id);

    Page<Config> getConfigList(Integer page, Integer size);

    Boolean addConfig(Config config);

    Boolean updateConfig(Config config);

    List<Config> selectConfigList(Config config);
}
