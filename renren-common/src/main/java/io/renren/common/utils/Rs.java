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

package io.renren.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午9:59:27
 */
public class Rs extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Rs() {
		put("errno", 0);
		put("errmsg", "success");
	}
	
	public static Rs error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static Rs error(String errmsg) {
		return error(500, errmsg);
	}
	
	public static Rs error(int errno, String errmsg) {
		Rs r = new Rs();
		r.put("errno", errno);
		r.put("errmsg", errmsg);
		return r;
	}

	public static Rs ok(String errmsg) {
		Rs r = new Rs();
		r.put("errmsg", errmsg);
		return r;
	}
	
	public static Rs ok(Map<String, Object> map) {
		Rs r = new Rs();
		r.putAll(map);
		return r;
	}
	
	public static Rs ok() {
		return new Rs();
	}

	@Override
	public Rs put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
