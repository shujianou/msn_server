package com.mianshiniu.manager.subject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 宝典
 * </p>
 *
 * @author vim
 * @since 2019-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_subject")
public class SubjectEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 答案 答案
     */
    @TableField("question")
    private String question;

    /**
     * 状态 (1:开启,:关闭,3:审核中)
     */
    @TableField("status")
    private Integer status;

    /**
     * 类型(1:JAVA,2:实施/运维)
     */
    @TableField("type")
    private Integer type;

    /**
     * 上传用户ID
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createTime;

    public static class Type {
        public static final Integer JAVA = 1;
        public static final Integer 实施运维 = 2;
    }

    public static class Status {
        public static final Integer 开启 = 1;
        public static final Integer 关闭 = 2;
        public static final Integer 审核中 = 3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
