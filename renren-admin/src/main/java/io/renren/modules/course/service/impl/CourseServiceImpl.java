package io.renren.modules.course.service.impl;

import io.renren.modules.course.dao.CourseDao;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.service.CourseService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CourseEntity> page = this.selectPage(
                new Query<CourseEntity>(params).getPage(),
                new EntityWrapper<CourseEntity>()
        );

        return new PageUtils(page);
    }

}
