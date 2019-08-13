package io.renren.modules.groupon.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-27 17:17:35
 */
@TableName("tb_groupon")
public class GrouponEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "id",type = IdType.INPUT)
	private String id;

	private String address;
	/**
	 * 活动开始时间
	 */
	private String startTime;
	/**
	 * 活动结束时间
	 */
	private String endTime;
	/**
	 * 商品描述
	 */
	private String commodityDescription;
	/**
	 * 原价
	 */
	private BigDecimal originalPrice;
	/**
	 * 几人团
	 */
	private String companyDescription;
	/**
	 * 团长奖励
	 */
	private String phone;
	/**
	 * 主办方名片
	 */
	private String discount;
	/**
	 *
	 */
	private String latitude;
	/**
	 * 
	 */
	private String longitude;
	/**
	 * 
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

	private String prizeInfo;

	private String activityRule;

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
	 * 设置：活动开始时间
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：活动开始时间
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * 设置：活动结束时间
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：活动结束时间
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * 设置：商品描述
	 */
	public void setCommodityDescription(String commodityDescription) {
		this.commodityDescription = commodityDescription;
	}
	/**
	 * 获取：商品描述
	 */
	public String getCommodityDescription() {
		return commodityDescription;
	}
	/**
	 * 设置：原价
	 */
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	/**
	 * 获取：原价
	 */
	public BigDecimal getOriginalPrice() {
		return originalPrice;
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
	 * 设置：主办方名片
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	/**
	 * 获取：主办方名片
	 */
	public String getDiscount() {
		return discount;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrizeInfo() {
		return prizeInfo;
	}

	public void setPrizeInfo(String prizeInfo) {
		this.prizeInfo = prizeInfo;
	}

	public String getActivityRule() {
		return activityRule;
	}

	public void setActivityRule(String activityRule) {
		this.activityRule = activityRule;
	}
}
