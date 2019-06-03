/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.distribution.dao.DistributionDao;
import io.renren.modules.sys.dao.SysLogDao;
import io.renren.modules.sys.entity.SysLogEntity;
import io.renren.modules.sys.service.SysLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");

        Page<SysLogEntity> page = this.selectPage(
            new Query<SysLogEntity>(params).getPage(),
            new EntityWrapper<SysLogEntity>().like(StringUtils.isNotBlank(key),"username", key)
        );

        return new PageUtils(page);
    }

    public List<Map<String,String>> queryAllChannel(){
        return sysLogDao.queryAllChannel();
    };

    public List<Map<String,String>> queryAllBanner(){
        return sysLogDao.queryAllBanner();
    };

    public List<Map<String,String>> queryAllActivity(Map<String,Object> map){
        return sysLogDao.queryAllActivity();
    };

    public List<Map<String,String>> queryAllBusiness(Map<String,Object> map){
        return sysLogDao.queryAllBusiness(map);
    };

    public List<Map<String,String>> queryAllContact(){
        return sysLogDao.queryAllContact();
    };

    public List<Map<String,String>> queryAllVedio(){
        return sysLogDao.queryAllVedio();
    };

    public int insertApp(Map<String,Object> map){
        return sysLogDao.insertApp(map);
    };

    public int  queryMaxSort (String type){
        return sysLogDao.queryMaxSort(type);
    };

    public int deleteApp(String id){
        return sysLogDao.deleteApp(id);
    };

    public int updateApp(Map<String,Object> map){
        return sysLogDao.updateApp(map);
    };;
}
