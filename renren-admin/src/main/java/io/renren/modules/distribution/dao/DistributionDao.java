package io.renren.modules.distribution.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.distribution.entity.Distribution;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-10-26 14:49:43
 */
@Mapper
public interface DistributionDao extends BaseMapper<Distribution> {


    List<Distribution> queryList(Map<String, Object> params);

    List<Distribution> queryListByPage(Map<String, Object> params);

    Distribution queryById(@Param("id") String id);

    int insertDistribution (Distribution insertDistribution);

    int release(String id);

    int rollback(String id);


    int addWatcher (Distribution insertDistribution);
}
