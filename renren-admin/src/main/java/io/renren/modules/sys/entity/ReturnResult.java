package io.renren.modules.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: ReturnResult
 * @Description: 统一返回数据格式
 * @author Richard
 * @date 15 Dec 2017 09:29:53
 *
 */
public class ReturnResult implements Serializable {

	/** 
	 *  
	 */
	private static final long serialVersionUID = 7475919483473633240L;

	private String code = ReturnCodeEnum.SUCCESS.getCode();

	private String msg;

	private transient Map<String, Object> result = new HashMap<>();

	public ReturnResult(String msg) {
		this.msg = msg;
	}

	public ReturnResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ReturnResult [code=" + code + ", msg=" + msg + ", result=" + result + "]";
	}

}
