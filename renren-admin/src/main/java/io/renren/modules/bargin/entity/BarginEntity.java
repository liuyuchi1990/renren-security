package io.renren.modules.bargin.entity;

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
 * @date 2019-06-25 15:35:24
 */
@TableName("tb_bargin")
public class BarginEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "id",type = IdType.INPUT)
	private String id;

	private String address;

	private Integer barginNum;
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
	private String thumbnail;
	/**
	 * 
	 */
	private BigDecimal originalPrice;
	/**
	 * 
	 */
	private BigDecimal floorPrice;
	/**
	 * 
	 */
	private BigDecimal minReduction;
	/**
	 * 
	 */
	private BigDecimal maxReduction;
	/**
	 * 
	 */
	private Integer restrictTime;
	/**
	 * 
	 */
	private String prizeDescription;
	/**
	 * 
	 */
	private String activityRule;
	/**
	 * 
	 */
	private String prizeInfo;
	/**
	 * 
	 */
	private String companyDescription;
	/**
	 * 
	 */
	private String discount;
	/**
	 * 
	 */
	private String qrImg;
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
	private String headImage;
	/**
	 * 
	 */
	private String longitude;
	/**
	 * 
	 */
	private String phone;
	/**
	 * 
	 */
	private String latitude;
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
	private String gift;
	/**
	 * 
	 */
	private Integer prizeLeft;
	/**
	 * 
	 */
	private Integer prizeNum;

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
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	/**
	 * 获取：
	 */
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	/**
	 * 设置：
	 */
	public void setFloorPrice(BigDecimal floorPrice) {
		this.floorPrice = floorPrice;
	}
	/**
	 * 获取：
	 */
	public BigDecimal getFloorPrice() {
		return floorPrice;
	}
	/**
	 * 设置：
	 */
	public void setMinReduction(BigDecimal minReduction) {
		this.minReduction = minReduction;
	}
	/**
	 * 获取：
	 */
	public BigDecimal getMinReduction() {
		return minReduction;
	}
	/**
	 * 设置：
	 */
	public void setMaxReduction(BigDecimal maxReduction) {
		this.maxReduction = maxReduction;
	}
	/**
	 * 获取：
	 */
	public BigDecimal getMaxReduction() {
		return maxReduction;
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
	public void setPrizeDescription(String prizeDescription) {
		this.prizeDescription = prizeDescription;
	}
	/**
	 * 获取：
	 */
	public String getPrizeDescription() {
		return prizeDescription;
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
	public void setPrizeInfo(String prizeInfo) {
		this.prizeInfo = prizeInfo;
	}
	/**
	 * 获取：
	 */
	public String getPrizeInfo() {
		return prizeInfo;
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
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * 获取：
	 */
	public String getLatitude() {
		return latitude;
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
	public void setGift(String gift) {
		this.gift = gift;
	}
	/**
	 * 获取：
	 */
	public String getGift() {
		return gift;
	}
	/**
	 * 设置：
	 */
	public void setPrizeLeft(Integer prizeLeft) {
		this.prizeLeft = prizeLeft;
	}
	/**
	 * 获取：
	 */
	public Integer getPrizeLeft() {
		return prizeLeft;
	}
	/**
	 * 设置：
	 */
	public void setPrizeNum(Integer prizeNum) {
		this.prizeNum = prizeNum;
	}
	/**
	 * 获取：
	 */
	public Integer getPrizeNum() {
		return prizeNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getBarginNum() {
		return barginNum;
	}

	public void setBarginNum(Integer barginNum) {
		this.barginNum = barginNum;
	}
}
