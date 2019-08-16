package io.renren.common.utils;

import com.alibaba.fastjson.JSON;
import io.renren.modules.lottery.entity.Gift;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 不同概率抽奖工具包
 * @author:richard
 * @date:2019年7月20日
 * @version:1.0
 */
public class LotteryUtil {
    /**
     * 抽奖
     *
     * @param orignalRates 原始的概率列表，保证顺序和实际物品对应
     * @return 物品的索引
     */
    public static Gift lottery(List<Gift> orignalRates) {
        if (orignalRates == null || orignalRates.isEmpty()) {
            return null;
        }

        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (Gift rate : orignalRates) {
            sumRate += rate.getProbability();
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        for (Gift rate : orignalRates) {
            tempSumRate += rate.getProbability();
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return orignalRates.get(sortOrignalRates.indexOf(nextDouble));
    }

    public static void main(String[] args) {

        List<Gift> gifts = new ArrayList<Gift>();
        // 序号==物品Id==物品名称==概率
//        gifts.add(new Gift(1, "P1", "物品1", 0.2d));
//        gifts.add(new Gift(2, "P2", "物品2", 0.2d));
//        gifts.add(new Gift(3, "P3", "物品3", 0.4d));
//        gifts.add(new Gift(4, "P4", "物品4", 0.3d));
//        gifts.add(new Gift(5, "P5", "物品5", 0d));
//        gifts.add(new Gift(6, "P6", "物品6", -0.1d));
//        gifts.add(new Gift(7, "P7", "物品7", 0.008d));


        List<Double> orignalRates = new ArrayList<Double>(gifts.size());
        System.out.println(LotteryUtil.lottery(gifts));

    }

    /**
     * 将一个map组成的list转成实体类bean组成的list
     * @param mapList 存了map对象的list
     * @param clazz 需要将这些map转成哪个实体类对象
     * @return
     */
    public static <T> List<T> convertMapListToBeanList(List<Map> mapList,Class<T> clazz) throws InvocationTargetException {
        List<T> list = new ArrayList<T>();
        for (Map map : mapList) {
            try {
                T obj = clazz.newInstance();//创建bean的实例对象
                for (Object o : map.keySet()) {//遍历map的key
                    for (Method m : clazz.getMethods()) {//遍历bean的类中的方法，找到set方法进行赋值
                        if (m.getName().toLowerCase().equals("set" + o.toString().toLowerCase())) {
                            m.invoke(obj, map.get(o));
                        }
                    }
                }
                list.add(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
		return list;
    }

    public static  List<Gift> convertGiftList(List<Map> mapList) {
        List<Gift> list = new ArrayList<Gift>();

        for(Map map:mapList){
            Gift gift = new Gift(Integer.parseInt(map.get("index").toString()),map.get("gitfId").toString(),map.get("giftName").toString(),
                    Double.valueOf(map.get("probability").toString()),map.get("url").toString(),Integer.parseInt(map.get("num").toString()),
                    map.get("giftDescription").toString());
            list.add(gift);
        }
        return list;
    }
}
