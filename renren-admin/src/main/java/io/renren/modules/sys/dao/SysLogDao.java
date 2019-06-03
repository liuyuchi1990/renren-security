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

package io.renren.modules.sys.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.sys.entity.SysLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-08 10:40:56
 */
public interface SysLogDao extends BaseMapper<SysLogEntity> {
    List<Map<String,String>> queryAllChannel();

    List<Map<String,String>> queryAllBanner();

    List<Map<String,String>> queryAllActivity();

    List<Map<String,String>> queryAllBusiness(Map<String,Object> map);

    List<Map<String,String>> queryAllContact();

    List<Map<String,String>> queryAllVedio();

    int  queryMaxSort (String type);

    int insertApp(Map<String,Object> map);

    int updateApp(Map<String,Object> map);

    int deleteApp(String id);
}
