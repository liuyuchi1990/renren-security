package io.renren.modules.gather.entity;

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
 * @date 2019-06-11 10:39:02
 */
@TableName("tb_gather")
public class GatherEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "id",type = IdType.INPUT)
	private String id;

	private String address;
	/**
	 * 
	 */
	private String activityName;
	/**
	 * 
	 */
	private String startTime;
	/**
	 * 
	 */
	private String endTime;
	/**
	 * 
	 */
	private Integer priceNum;
	/**
	 * 
	 */
	private Integer targetNum;
	/**
	 * 
	 */
	private Integer restrictTime;
	/**
	 * 
	 */
	private String priceDescription;
	/**
	 * 
	 */
	private String activityRule;
	/**
	 * 
	 */
	private String priceInfo;
	/**
	 * 
	 */
	private String companyDescription;
	/**
	 * 
	 */
	private String thumbnail;
	/**
	 * 
	 */
	private String discount;
	/**
	 * 
	 */
	private String updateUser;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String qrImg;
	/**
	 * 
	 */
	private String headImage;
	/**
	 * 
	 */
	private String phone;

	private String gift;

	private String longitude;//经度

	private String latitude;//纬度

	private Integer prizeLeft;

	private String footImage;

	private String bgImage;


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
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * 设置：
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * 设置：
	 */
	public void setPriceNum(Integer priceNum) {
		this.priceNum = priceNum;
	}
	/**
	 * 获取：
	 */
	public Integer getPriceNum() {
		return priceNum;
	}
	/**
	 * 设置：
	 */
	public void setTargetNum(Integer targetNum) {
		this.targetNum = targetNum;
	}
	/**
	 * 获取：
	 */
	public Integer getTargetNum() {
		return targetNum;
	}
	/**
	 * 设置：
	 */
	public void setRestrictTime(Integer restrictTime) {
		this.restrictTime = restrictTime;
	}
	/**
	 * 获取：
	 */
	public Integer getRestrictTime() {
		return restrictTime;
	}
	/**
	 * 设置：
	 */
	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}
	/**
	 * 获取：
	 */
	public String getPriceDescription() {
		return priceDescription;
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
	 * 设置：
	 */
	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}
	/**
	 * 获取：
	 */
	public String getPriceInfo() {
		return priceInfo;
	}
	/**
	 * 设置：
	 */
	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}
	/**
	 * 获取：
	 */
	public String getCompanyDescription() {
		return companyDescription;
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
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	/**
	 * 获取：
	 */
	public String getDiscount() {
		return discount;
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
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：
	 */
	public String getPhone() {
		return phone;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Integer getPrizeLeft() {
		return prizeLeft;
	}

	public void setPrizeLeft(Integer prizeLeft) {
		this.prizeLeft = prizeLeft;
	}

	public String getFootImage() {
		return footImage;
	}

	public void setFootImage(String footImage) {
		this.footImage = footImage;
	}

	public String getBgImage() {
		return bgImage;
	}

	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
