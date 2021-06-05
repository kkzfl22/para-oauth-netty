package com.paraview;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.paraview.oauth.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MyTest {

    public static void main(String[] args) throws IOException {
        long s1 = System.nanoTime();
        for(int i=0;i<100 * 10000;i++){
            String body = "------WebKitFormBoundarylEpYK0n9Etitj2ZE\n" +
                    "Content-Disposition: form-data; name=\"grant_type\"\n" +
                    "\n" +
                    "password\n" +
                    "------WebKitFormBoundarylEpYK0n9Etitj2ZE\n" +
                    "Content-Disposition: form-data; name=\"username\"\n" +
                    "\n" +
                    "user1\n" +
                    "------WebKitFormBoundarylEpYK0n9Etitj2ZE\n" +
                    "Content-Disposition: form-data; name=\"password\"\n" +
                    "\n" +
                    "000000\n" +
                    "------WebKitFormBoundarylEpYK0n9Etitj2ZE--";
            String[] lines = body.split("\n");
            String lastKey = null;
            Map<String,String> param = new HashMap<>();
            for(String temp : lines){
                if(StrUtil.isEmpty(temp.trim()))
                    continue;
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
        }
        long s2 = System.nanoTime();
        System.out.println(s2 -s1);
    }

}
