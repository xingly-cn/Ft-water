package com.ruoyi.system.mapper;


import com.ruoyi.system.entity.FtSchool;
import com.ruoyi.system.request.SchoolRequest;
import com.ruoyi.system.response.IndexCountResponse;
import com.ruoyi.system.response.SchoolResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/30 15:59
 */
@Repository
public interface FtSchoolMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtSchool record);

    FtSchool selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtSchool record);

    int updateByPrimaryKey(FtSchool record);

    List<IndexCountResponse> countIndex(@Param("time") String time,
                                        @Param("schoolIds") List<Long> schoolIds);

    List<SchoolResponse> selectList(@Param("school") SchoolRequest request);

    List<FtSchool> selectBatchIds(@Param("ids") List<Long> ids);

    FtSchool getSchoolByRemark(@Param("remark") String remark);
}