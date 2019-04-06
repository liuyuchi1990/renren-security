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

package io.renren.modules.sys.controller;

import io.renren.common.utils.Rs;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.sys.entity.*;
import io.renren.modules.sys.service.SysLogService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * 系统日志
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-08 10:40:56
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private DistributionService distributionService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);
		return R.ok().put("page", page);
	}

	@RequestMapping(value = "/queryAll",method= RequestMethod.GET)
	@ResponseBody
	public Rs queryAll(){
		List<Channel> channelList = new ArrayList<>();
		List<Banner> bannerList = new ArrayList<>();
		List<ContactWay> contactList = new ArrayList<>();
		List<GoodActivity> goodActitiyList = new ArrayList<>();
		List<Business> businessList = new ArrayList<>();
		Map<String,List> data = new HashMap<>();
		Map<String,Object> param = new HashMap<>();
		List<Map<String,String>> channelLst = sysLogService.queryAllChannel();
		List<Map<String,String>> bannerLst = sysLogService.queryAllBanner();
		List<Map<String,String>> businessLst = sysLogService.queryAllBusiness();
		List<Distribution> activityLst = distributionService.queryList(param);
		List<Map<String,String>> contactLst = sysLogService.queryAllContact();
		for(Map<String,String> map: channelLst){
			Channel channel = new Channel();
			channel.setId(map.get("id").toString());
			channel.setIcon_text(map.get("name").toString());
			channel.setUrl(map.get("url").toString());
			channel.setIcon_url(map.get("img_path").toString());
			channel.setCategory_id(map.get("id").toString());
			channel.setName(map.get("id").toString());
			channelList.add(channel);
		}
		for(Map<String,String> map: bannerLst){
			Banner banner = new Banner();
			banner.setId(map.get("id").toString());
			banner.setContent(map.get("name").toString());
			banner.setLink(map.get("url").toString());
			banner.setImage_url(map.get("img_path").toString());
			banner.setMedia_type(map.get("id").toString());
			banner.setName(map.get("id").toString());
			banner.setAd_position_id("1");
			banner.setEnd_time("1");
			banner.setEnabled("1");
			bannerList.add(banner);
		}
		for(Distribution map: activityLst){
			GoodActivity  ga = new GoodActivity();
			ga.setId(map.getId());
			ga.setName(map.getActivityName());
			ga.setList_pic_url(map.getThumbnail());
			ga.setRetail_price(map.getProductPrice().toString());
			ga.setHave_pay_num("1");
			goodActitiyList.add(ga);
		}
		for(Map<String,String> map: contactLst){
			ContactWay  contact = new ContactWay();
			contact.setId(map.get("id").toString());
			contact.setName(map.get("name").toString());
			contact.setBanner_url(map.get("img_path").toString());
			contactList.add(contact);
		}
		for(Map<String,String> map: businessLst){
			Business  bus = new Business();
			bus.setId(map.get("id").toString());
			bus.setName(map.get("name").toString());
			bus.setBanner_url(map.get("img_path").toString());
			businessList.add(bus);
		}
		data.put("channel",channelList);
		data.put("banner",bannerList);
		data.put("newGoodsList",goodActitiyList);
		data.put("categoryList",businessList);
		data.put("contactList",contactList);
		return Rs.ok().put("data",data);
	}
	
}
