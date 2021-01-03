package com.shursulei.interview;

import java.text.SimpleDateFormat;
import java.util.*;

public class sortbydemo {
    public static void main(String args) {
    }

    private List<Map<String, Object>> parseData(List<Map<String, Object>> source, Map<String, Object> params) {
        List<Map<String, Object>> rt = new ArrayList<Map<String, Object>>();
        if (source == null || source.size() == 0) {
            return rt;
        }
        // 日期分组
        SortedMap<String, List<Map<String, Object>>> dataMap = new TreeMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> temp = null;
        for (Map<String, Object> data : source) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String createDate = sdf.format(data.get("beginTime"));
            if (dataMap.get(createDate) != null) {
                dataMap.get(createDate).add(data);
            } else {
                temp = new ArrayList<Map<String, Object>>();
                temp.add(data);
                dataMap.put(createDate, temp);
            }
        }
// 转换格式
        SortedMap<String, Object> rtMap = null;

        for (Map.Entry<String, List<Map<String, Object>>> entry : dataMap.entrySet()) {
            rtMap = new TreeMap<String, Object>();
            rtMap.put("createTime", entry.getKey());
            params.put("dateTime", entry.getKey());//"%Y-%m-%d"
//            String allTotal = bookingManageMapper.getDayTotal(params);
//            rtMap.put("allTotal", allTotal);
            rtMap.put("bookingManageList", entry.getValue());
            rt.add(rtMap);
        }
        return rt;
    }

}
