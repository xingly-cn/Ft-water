package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.FtHome;
import com.ruoyi.system.request.HomeRequest;
import com.ruoyi.system.response.IndexCountResponse;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/4/18 21:16
 */
@Repository
public interface FtHomeMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(FtHome record);

    FtHome selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FtHome record);

    int updateByPrimaryKey(FtHome record);

    List<FtHome> selectList(HomeRequest request);

    String getSchoolByRemark(String name);

    List<IndexCountResponse> countIndex(@Param("time") String time,
                                        @Param("schoolIds") List<Long> schoolIds);
}
