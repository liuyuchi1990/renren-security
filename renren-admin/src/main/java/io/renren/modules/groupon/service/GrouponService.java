package io.renren.modules.groupon.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.groupon.entity.GrouponEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-27 17:17:35
 */
public interface GrouponService extends IService<GrouponEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Map<String, Object>> queryList (String id);
}

