package com.shop.bean;

import com.shop.validate.Insert;
import com.shop.validate.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * commodity_type
 * @author 
 */
public class CommodityType implements Serializable {
    /**
     * 商品分类id
     */
    @NotNull(message = "分类id不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 分类名
     */
    @NotNull(message = "分类名不能为空", groups = {Insert.class, Update.class})
    private String name;

    /**
     * 父分类id
     */
    private Integer pid;

    private Set<CommodityType> childrenSet;

    public Set<CommodityType> getChildrenSet() {
        return childrenSet;
    }

    public void setChildrenSet(Set<CommodityType> childrenSet) {
        this.childrenSet = childrenSet;
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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
        CommodityType other = (CommodityType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", pid=").append(pid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}