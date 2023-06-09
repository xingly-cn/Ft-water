package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.FtNotices;
import com.ruoyi.system.request.NoticesRequest;
import com.ruoyi.system.response.NoticesResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/19 21:48
 */
public interface FtNoticesMapper {

    int deleteByPrimaryKey(Long id);

    int insert(FtNotices record);

    int insertSelective(FtNotices record);

    NoticesResponse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtNotices record);

    int updateByPrimaryKey(FtNotices record);

    List<NoticesResponse> selectList(@Param("notices") NoticesRequest request);

    FtNotices selectLastByHomeIdAndOrderType(@Param("homeId") Long homeId,
                                             @Param("orderType") Integer orderType);
}