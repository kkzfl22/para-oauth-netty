package com.paraview.oauth.utils;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class StringUtil {

    public static boolean startWith(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();
        if (ch2.length > ch1.length) {
            return false;
        }
        for (int i = 0; i < ch2.length; i++) {
            if (ch2[i] != ch1[i]) {
                return false;
            }
        }
        return true;
    }

    public static Map<String,String> parseBody(String body){
        String[] lines = body.split("\r\n");
        String lastKey = null;
        Map<String,String> param = new HashMap<>();
        for(String temp : lines){
            if(StrUtil.isEmpty(temp.trim())) {
                continue;
            }
            if(temp.endsWith("--")){
                break;
            }
            if(StringUtil.startWith(temp,"------")){
                continue;
            }
            if(temp.contains(";")){
                lastKey = temp.split(";")[1].split("=")[1].replace("\"","");
                continue;
            }
            param.put(lastKey,temp);
        }
        return param;
    }

}
