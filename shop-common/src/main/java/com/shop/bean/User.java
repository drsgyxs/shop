package com.shop.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shop.validate.Insert;
import com.shop.validate.Select;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * user
 * @author 
 */
public class User implements Serializable {
    /**
     * 用户id
     */
    private Integer id;

    /**
     * 邮箱
     */
    @NotBlank(groups = { Insert.class, Select.class}, message = "邮箱不能为空")
    @Email(groups = Insert.class, regexp = "^[a-zA-Z0-9_-]{1,50}@[a-zA-Z0-9_-]{1,20}(\\.[a-zA-Z0-9_-]+)+$", message = "请输入合法邮箱")
    private String mail;

    /**
     * 密码
     */
//    "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d_]{8,16}$"
    @NotBlank(groups = { Insert.class, Select.class}, message = "密码不能为空")
    @Pattern(groups = Insert.class, regexp = "^[A-Za-z\\d_]{8,16}$", message = "密码不合法")
    private String password;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否被停用
     */
    private Integer isClose;

    private static final long serialVersionUID = 1L;

    public User(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMail() == null ? other.getMail() == null : this.getMail().equals(other.getMail()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getIsClose() == null ? other.getIsClose() == null : this.getIsClose().equals(other.getIsClose()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMail() == null) ? 0 : getMail().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getIsClose() == null) ? 0 : getIsClose().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mail=").append(mail);
        sb.append(", password=").append(password);
        sb.append(", createTime=").append(createTime);
        sb.append(", isClose=").append(isClose);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}