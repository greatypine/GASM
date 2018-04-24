package com.cnpc.pms.base.file.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/16.
 */
public class Test {
    public static void main(String args[]) throws IOException{

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.execute("SELECT * from dataanalysis.t_community");
        //RARTools.unRar("D:\\usr.rar","D:\\aa");
        System.out.println(HelloEnum.red);
        for (HelloEnum h : HelloEnum.values()){
            System.out.println(h.getValue());
        }
    }
}
