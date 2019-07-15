package io.renren.modules.groupon.dao;

import io.renren.modules.groupon.entity.GrouponEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-27 17:17:35
 */
public interface GrouponDao extends BaseMapper<GrouponEntity> {
    List<Map<String, Object>> queryList (String id);
	
}
