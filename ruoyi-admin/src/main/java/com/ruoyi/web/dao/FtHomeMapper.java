package com.ruoyi.web.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.entity.FtHome;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 21:16
 */
@Repository
public interface FtHomeMapper extends BaseMapper<FtHome> {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtHome record);

    FtHome selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtHome record);

    int updateByPrimaryKey(FtHome record);
}
