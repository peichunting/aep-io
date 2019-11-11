package com.cfs.ape.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyObjectHandler implements MetaObjectHandler {

    private static final String CREATED_TIME = "createTime";
    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */

    @Override
    public void insertFill(MetaObject metaObject) {
        if (getFieldValByName(CREATED_TIME, metaObject) == null) {
            setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
