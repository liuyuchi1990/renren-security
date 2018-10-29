package io.renren.modules.sys.entity;

/**
 * 
 * @ClassName: ReturnCodeEnum
 * @Description: web返回值枚举类
 * @author amosshan
 * @date 18 Dec 2017 09:11:06
 *
 */
public enum ReturnCodeEnum {
	/**
	 * 成功
	 */
	SUCCESS("0000", "success"),
	/**
	 * 参数错误
	 */
	PARAM_ERROR("0100", "param error"),
	/**
	 * 系统错误
	 */
	SYSTEM_ERROR("-1", "system error"),
	/**
	 * 用户无效或者session过期
	 */
	INVALID_USER("0200", "invalid login name or password/session expired"),
	/**
	 * 没有权限
	 */
	NO_PERMISSION("0201", "no permissions"),

	/**
	 * 没有登陆授权
	 */
	NO_AUTH("0202", "no authorization"),
	
	/**
	 * 没有cr项目权限
	 */
	NO_CR_AUTH("0203", "no cr authorization"),

	/**
	 * 缺少请求
	 */
	MISSING_REQUEST("0300", "missing servlet request"),

	/**
	 * 类型不匹配
	 */
	TYPE_MISMATCH("0301", "type mismatch error"),

	/**
	 * http请求方式不支持
	 */
	HTTP_REQUEST_METHOD_NOT_SUPPORT("0302", "http request method not supported"),

	/**
	 * 数据库连接错误
	 */
	JDBC_CONNECTION_ERROR("0303", "JDBC connection fail"),

	/**
	 * 上传的文件不存在
	 */
	FILE_NOT_EXIST("0304", "file not exist"),

	/**
	 * 导入的文件格式不对
	 */
	FILE_FORMAT_ERROR("0305", "file format error"),

	/**
	 * 并发操作冲突
	 */
	CONCURRENCY_IMPORT_ERROR("0306", "concurrency import error"),
	
	/**
	 * 编辑操作冲突
	 */
	CONCURRENCY_EDIT_ERROR("0308", "concurrency edit error"),
	
	/**
	 * 文件导入失败
	 */
	FILE_IMPORT_ERROR("0307","file import error"),
	
	/**
	 * 文件导出失败
	 */
	FILE_EXPORT_ERROR("0309","file export error"),
	
	/**
	 * 用户在不同tab页或者浏览器重复在某一个项目下导入文件
	 */
	USER_REPEAT_IMPORT_PROJECT_FILE_ERROR("0310","user repeat import project file error"),
	
	/**
	 * 调用vendor kpmg方报错
	 */
	INVOKE_VENDOR_KPMG_ERROR("0400","invoke vendor kpmg error"),
	
	/**
	 * 调用vendor df方报错
	 */
	INVOKE_VENDOR_DF_ERROR("0401","invoke vendor df error"),
	
	/**
	 * 删除外键约束错误
	 */
	DELETE_CASCADE_ERROR("0500","delete cascade error"),
	
	/**
	 * 客户管理。06XX开头
	 */
	
	/**
	 * 判断添加成员是否存在
	 */
	CLIENTUSER_HAS_ADD_ERROR("0601","您添加的成员，已经存在当前客户中！"),
	
	/**
	 * 判断集团是否存在
	 */
	COMPANY_EXIST("0602","集团客已存在！"),
	
	
	/**
	 * 摘要编辑。07XX开头
	 */
	
	/**
	 * 报告已经被删除
	 */
	REPORT_HAS_BEEN_DEL_ERROR("0701","您添加的成员，已经存在当前客户中！");
	
	/**
	 * 返回编码
	 */
	private String code;

	/**
	 * 返回信息
	 */
	private String message;

	ReturnCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}