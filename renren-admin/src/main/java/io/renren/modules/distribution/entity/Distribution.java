package io.renren.modules.distribution.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-11-01 15:55:29
 */
@TableName("tb_distribution")
public class Distribution implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private String id;
    /**
     * 是否关注弹出
     */
    private Integer focusOnPop;
    /**
     * 是否关注发红包
     */
    private Integer focusOnRed;
    /**
     *  是否购买成功后分享得红包
     */
    private Integer shareRedEnvelopes;
    /**
     * 分销级数
     */
    private Integer distributionSeries;
    /**
     * 活动主题
     */
    private String activityTheme;
    /**
     * 触发关键词
     */
    private String triggerKeywords;
    /**
     * 微信描述
     */
    private String wechatDescription;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 虚拟人气值设置
     */
    private Integer virtualPopularity;
    /**
     * 音乐
     */
    private String music;
    /**
     * 音乐自动播放
     */
    private Integer autoPlayMusic;
    /**
     * 电话
     */
    private String phone;
    /**
     * 目标量
     */
    private Integer targetQuantity;
    /**
     * 产品价格
     */
    private BigDecimal productPrice;

    private String qrImg;
    /**
     * 不需要付款人数
     */
    private Integer noPaymentNum;
    /**
     * 推广随机红包最大值
     */
    private BigDecimal maxValue;
    /**
     * 推广随机红包最小值
     */
    private BigDecimal minValue;
    /**
     * 红包名称
     */
    private String redEnvelopesName;
    /**
     * 红包祝福语
     */
    private String redEnvelopesBlessings;
    /**
     * 微信一键关注
     */
    private String oneKeyConcern;
    /**
     * 微信关注图片
     */
    private String wechatPic;
    /**
     * 模板界面风格
     */
    private String templateStyle;
    /**
     * 活动规则填写
     */
    private String activityRules;
    /**
     * 红包发放规则填写：图片上传
     */
    private String redRules;

    /**
     * 创建活动说明填写：图片上传
     */
    private String activityDestription;

    /**
     *
     */
    private String activityName;
    /**
     *
     */
    private String activityType;
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
    private String updateUser;
    /**
     *
     */
    private Date updateTime;

    private Date createTime;
    /**
     *
     */
    private Integer activityState;

    private String watcher;
    //访问数
    private Integer visits;

    private String headImage;

    private String orderNum;

    private String totalPrice;
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
     * 设置：是否关注弹出
     */
    public void setFocusOnPop(Integer focusOnPop) {
        this.focusOnPop = focusOnPop;
    }
    /**
     * 获取：是否关注弹出
     */
    public Integer getFocusOnPop() {
        return focusOnPop;
    }
    /**
     * 设置：是否关注发红包
     */
    public void setFocusOnRed(Integer focusOnRed) {
        this.focusOnRed = focusOnRed;
    }
    /**
     * 获取：是否关注发红包
     */
    public Integer getFocusOnRed() {
        return focusOnRed;
    }
    /**
     * 设置： 是否购买成功后分享得红包
     */
    public void setShareRedEnvelopes(Integer shareRedEnvelopes) {
        this.shareRedEnvelopes = shareRedEnvelopes;
    }
    /**
     * 获取： 是否购买成功后分享得红包
     */
    public Integer getShareRedEnvelopes() {
        return shareRedEnvelopes;
    }
    /**
     * 设置：分销级数
     */
    public void setDistributionSeries(Integer distributionSeries) {
        this.distributionSeries = distributionSeries;
    }
    /**
     * 获取：分销级数
     */
    public Integer getDistributionSeries() {
        return distributionSeries;
    }
    /**
     * 设置：活动主题
     */
    public void setActivityTheme(String activityTheme) {
        this.activityTheme = activityTheme;
    }
    /**
     * 获取：活动主题
     */
    public String getActivityTheme() {
        return activityTheme;
    }
    /**
     * 设置：触发关键词
     */
    public void setTriggerKeywords(String triggerKeywords) {
        this.triggerKeywords = triggerKeywords;
    }
    /**
     * 获取：触发关键词
     */
    public String getTriggerKeywords() {
        return triggerKeywords;
    }
    /**
     * 设置：微信描述
     */
    public void setWechatDescription(String wechatDescription) {
        this.wechatDescription = wechatDescription;
    }
    /**
     * 获取：微信描述
     */
    public String getWechatDescription() {
        return wechatDescription;
    }
    /**
     * 设置：缩略图
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    /**
     * 获取：缩略图
     */
    public String getThumbnail() {
        return thumbnail;
    }
    /**
     * 设置：虚拟人气值设置
     */
    public void setVirtualPopularity(Integer virtualPopularity) {
        this.virtualPopularity = virtualPopularity;
    }
    /**
     * 获取：虚拟人气值设置
     */
    public Integer getVirtualPopularity() {
        return virtualPopularity;
    }
    /**
     * 设置：音乐
     */
    public void setMusic(String music) {
        this.music = music;
    }
    /**
     * 获取：音乐
     */
    public String getMusic() {
        return music;
    }
    /**
     * 设置：音乐自动播放
     */
    public void setAutoPlayMusic(Integer autoPlayMusic) {
        this.autoPlayMusic = autoPlayMusic;
    }
    /**
     * 获取：音乐自动播放
     */
    public Integer getAutoPlayMusic() {
        return autoPlayMusic;
    }
    /**
     * 设置：电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * 获取：电话
     */
    public String getPhone() {
        return phone;
    }
    /**
     * 设置：目标量
     */
    public void setTargetQuantity(Integer targetQuantity) {
        this.targetQuantity = targetQuantity;
    }
    /**
     * 获取：目标量
     */
    public Integer getTargetQuantity() {
        return targetQuantity;
    }
    /**
     * 设置：产品价格
     */
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
    /**
     * 获取：产品价格
     */
    public BigDecimal getProductPrice() {
        return productPrice;
    }
    /**
     * 设置：不需要付款人数
     */
    public void setNoPaymentNum(Integer noPaymentNum) {
        this.noPaymentNum = noPaymentNum;
    }
    /**
     * 获取：不需要付款人数
     */
    public Integer getNoPaymentNum() {
        return noPaymentNum;
    }
    /**
     * 设置：推广随机红包最大值
     */
    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }
    /**
     * 获取：推广随机红包最大值
     */
    public BigDecimal getMaxValue() {
        return maxValue;
    }
    /**
     * 设置：推广随机红包最小值
     */
    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }
    /**
     * 获取：推广随机红包最小值
     */
    public BigDecimal getMinValue() {
        return minValue;
    }
    /**
     * 设置：红包名称
     */
    public void setRedEnvelopesName(String redEnvelopesName) {
        this.redEnvelopesName = redEnvelopesName;
    }
    /**
     * 获取：红包名称
     */
    public String getRedEnvelopesName() {
        return redEnvelopesName;
    }
    /**
     * 设置：红包祝福语
     */
    public void setRedEnvelopesBlessings(String redEnvelopesBlessings) {
        this.redEnvelopesBlessings = redEnvelopesBlessings;
    }
    /**
     * 获取：红包祝福语
     */
    public String getRedEnvelopesBlessings() {
        return redEnvelopesBlessings;
    }
    /**
     * 设置：微信一键关注
     */
    public void setOneKeyConcern(String oneKeyConcern) {
        this.oneKeyConcern = oneKeyConcern;
    }
    /**
     * 获取：微信一键关注
     */
    public String getOneKeyConcern() {
        return oneKeyConcern;
    }
    /**
     * 设置：微信关注图片
     */
    public void setWechatPic(String wechatPic) {
        this.wechatPic = wechatPic;
    }
    /**
     * 获取：微信关注图片
     */
    public String getWechatPic() {
        return wechatPic;
    }
    /**
     * 设置：模板界面风格
     */
    public void setTemplateStyle(String templateStyle) {
        this.templateStyle = templateStyle;
    }
    /**
     * 获取：模板界面风格
     */
    public String getTemplateStyle() {
        return templateStyle;
    }
    /**
     * 设置：活动规则填写
     */
    public void setActivityRules(String activityRules) {
        this.activityRules = activityRules;
    }
    /**
     * 获取：活动规则填写
     */
    public String getActivityRules() {
        return activityRules;
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
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    /**
     * 获取：
     */
    public String getActivityType() {
        return activityType;
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
    public void setActivityState(Integer activityState) {
        this.activityState = activityState;
    }
    /**
     * 获取：
     */
    public Integer getActivityState() {
        return activityState;
    }

    public String getRedRules() {
        return redRules;
    }

    public void setRedRules(String redRules) {
        this.redRules = redRules;
    }

    public String getActivityDestription() {
        return activityDestription;
    }

    public void setActivityDestription(String activityDestription) {
        this.activityDestription = activityDestription;
    }

    public String getWatcher() {
        return watcher;
    }

    public void setWatcher(String watcher) {
        this.watcher = watcher;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public String getQrImg() {
        return qrImg;
    }

    public void setQrImg(String qrImg) {
        this.qrImg = qrImg;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
