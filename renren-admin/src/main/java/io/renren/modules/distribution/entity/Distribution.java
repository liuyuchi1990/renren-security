package io.renren.modules.distribution.entity;

import com.baomidou.mybatisplus.annotations.TableId;

import java.math.BigDecimal;
import java.util.Date;

public class Distribution {
    /**
     *
     */
    @TableId
    private String id;
    /**
     * 活动是否开启状态
     */
    private Integer activeState;
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
     * 活动开始时间
     */
    private Date activityStartTime;
    /**
     * 活动结束时间
     */
    private Date activityEndTime;
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
     * 设置：活动是否开启状态
     */
    public void setActiveState(Integer activeState) {
        this.activeState = activeState;
    }
    /**
     * 获取：活动是否开启状态
     */
    public Integer getActiveState() {
        return activeState;
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
     * 设置：活动开始时间
     */
    public void setActivityStartTime(Date activityStartTime) {
        this.activityStartTime = activityStartTime;
    }
    /**
     * 获取：活动开始时间
     */
    public Date getActivityStartTime() {
        return activityStartTime;
    }
    /**
     * 设置：活动结束时间
     */
    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }
    /**
     * 获取：活动结束时间
     */
    public Date getActivityEndTime() {
        return activityEndTime;
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
}
