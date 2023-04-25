package com.ruoyi.web.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.web.entity.Config;
import java.util.List;

@Repository
public interface ConfigMapper extends BaseMapper<Config> {
    int deleteByPrimaryKey(Integer id);

    int insert(Config record);

    int insertSelective(Config record);

    Config selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);

    List<Config> getConfigsByKeys(@Param("keys")List<String> keys);

    List<Config> getConfigs();
}