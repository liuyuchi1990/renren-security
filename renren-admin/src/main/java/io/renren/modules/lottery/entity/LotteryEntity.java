package io.renren.modules.lottery.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-25 17:32:00
 */
@TableName("tb_lottery")
public class LotteryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "id",type = IdType.INPUT)
	private String id;
	/**
	 * 活动规则
	 */
	private String activityRule;
	/**
	 * 机构介绍
	 */
	private String companyDescription;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 经度
	 */
	private String latitude;
	/**
	 * 纬度
	 */
	private String longitude;
	/**
	 * 活动名称
	 */
	private String activityName;
	/**
	 * 
	 */
	private String footImage;
	/**
	 * 
	 */
	private String bgImage;
	/**
	 * 
	 */
	private String updateUser;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String headImage;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String qrImg;
	/**
	 * 
	 */
	private String thumbnail;
	/**
	 * 
	 */
	private String address;
	/**
	 * 
	 */
	private Integer showNum;
	/**
	 * 最多中奖次数
	 */
	private Integer maxWin;
	/**
	 * 
	 */
	private Integer intervals;
	/**
	 * 最多抽奖次数
	 */
	private Integer maxTime;
	/**
	 * 
	 */
	private String music;

	/**
	 * 奖项设置
	 */
	private String prizeRule;
	/**
	 * 兑奖信息
	 */
	private String prizeInfo;
	/**
	 * 最近抽奖时间信息
	 */
	private String maxTimes;

	private Integer rollNum;

	private Integer friend;

	private Integer prizeLeft;

	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setActivityRule(String activityRule) {
		this.activityRule = activityRule;
	}
	/**
	 * 获取：
	 */
	public String getActivityRule() {
		return activityRule;
	}
	/**
	 * 设置：几人团
	 */
	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}
	/**
	 * 获取：几人团
	 */
	public String getCompanyDescription() {
		return companyDescription;
	}
	/**
	 * 设置：团长奖励
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：团长奖励
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：显示/隐藏 欢迎页
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * 获取：显示/隐藏 欢迎页
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * 设置：
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * 获取：
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * 设置：
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	/**
	 * 获取：
	 */
	public String getActivityName() {
		return activityName;
	}
	/**
	 * 设置：
	 */
	public void setFootImage(String footImage) {
		this.footImage = footImage;
	}
	/**
	 * 获取：
	 */
	public String getFootImage() {
		return footImage;
	}
	/**
	 * 设置：
	 */
	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}
	/**
	 * 获取：
	 */
	public String getBgImage() {
		return bgImage;
	}
	/**
	 * 设置：
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * 获取：
	 */
	public String getUpdateUser() {
		return updateUser;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：
	 */
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	/**
	 * 获取：
	 */
	public String getHeadImage() {
		return headImage;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setQrImg(String qrImg) {
		this.qrImg = qrImg;
	}
	/**
	 * 获取：
	 */
	public String getQrImg() {
		return qrImg;
	}
	/**
	 * 设置：
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	/**
	 * 获取：
	 */
	public String getThumbnail() {
		return thumbnail;
	}
	/**
	 * 设置：
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：
	 */
	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}
	/**
	 * 获取：
	 */
	public Integer getShowNum() {
		return showNum;
	}
	/**
	 * 设置：
	 */
	public void setMaxWin(Integer maxWin) {
		this.maxWin = maxWin;
	}
	/**
	 * 获取：
	 */
	public Integer getMaxWin() {
		return maxWin;
	}
	/**
	 * 设置：
	 */
	public void setIntervals(Integer intervals) {
		this.intervals = intervals;
	}
	/**
	 * 获取：
	 */
	public Integer getIntervals() {
		return intervals;
	}
	/**
	 * 设置：
	 */
	public void setMaxTime(Integer maxTime) {
		this.maxTime = maxTime;
	}
	/**
	 * 获取：
	 */
	public Integer getMaxTime() {
		return maxTime;
	}
	/**
	 * 设置：
	 */
	public void setMusic(String music) {
		this.music = music;
	}
	/**
	 * 获取：
	 */
	public String getMusic() {
		return music;
	}
	/**
	 * 设置：
	 */
	public void setPrizeRule(String prizeRule) {
		this.prizeRule = prizeRule;
	}
	/**
	 * 获取：
	 */
	public String getPrizeRule() {
		return prizeRule;
	}
	/**
	 * 设置：
	 */
	public void setPrizeInfo(String prizeInfo) {
		this.prizeInfo = prizeInfo;
	}
	/**
	 * 获取：
	 */
	public String getPrizeInfo() {
		return prizeInfo;
	}

	public String getMaxTimes() {
		return maxTimes;
	}

	public void setMaxTimes(String maxTimes) {
		this.maxTimes = maxTimes;
	}

	public Integer getRollNum() {
		return rollNum;
	}

	public void setRollNum(Integer rollNum) {
		this.rollNum = rollNum;
	}

	public Integer getFriend() {
		return friend;
	}

	public void setFriend(Integer friend) {
		this.friend = friend;
	}

	public Integer getPrizeLeft() {
		return prizeLeft;
	}

	public void setPrizeLeft(Integer prizeLeft) {
		this.prizeLeft = prizeLeft;
	}
}
