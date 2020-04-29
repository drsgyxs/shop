package com.shop.bean;

import com.shop.validate.Insert;
import com.shop.validate.Update;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * address
 * @author 
 */
public class Address implements Serializable {
    /**
     * 收货地址id
     */
    @NotNull(message = "id不能为空", groups = Update.class)
    private Integer id;

    /**
     * 省
     */
    @NotBlank(message = "请选择收货地址", groups = Insert.class)
    private String province;

    /**
     * 市
     */
    @NotBlank(message = "请选择收货地址", groups = Insert.class)
    private String city;

    /**
     * 区
     */
    @NotBlank(message = "请选择收货地址", groups = Insert.class)
    private String district;

    /**
     * 详细地址
     */
    @Size(message = "详细地址长度需在5-100个字符之间", groups = Insert.class, min = 5, max = 100)
    private String detailedAddress;

    /**
     * 收货人姓名
     */
    @Size(min = 2, max = 25, groups = Insert.class, message = "收货人姓名长度需在2-25个字符之间")
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank(message = "手机号码不能为空", groups = Insert.class)
    @Pattern(regexp = "^(13[0-9]|14[5-9]|15[0-3,5-9]|16[2,5,6,7]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$", message = "请正确输入11位手机号码", groups = Insert.class)
    private String tel;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = Update.class)
    private Integer userId;

    /**
     * 是否为默认地址
     */
    @NotNull(message = "是否为默认地址不能为空", groups = Insert.class)
    private Integer isDefault;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
        Address other = (Address) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getDistrict() == null ? other.getDistrict() == null : this.getDistrict().equals(other.getDistrict()))
            && (this.getDetailedAddress() == null ? other.getDetailedAddress() == null : this.getDetailedAddress().equals(other.getDetailedAddress()))
            && (this.getReceiverName() == null ? other.getReceiverName() == null : this.getReceiverName().equals(other.getReceiverName()))
            && (this.getTel() == null ? other.getTel() == null : this.getTel().equals(other.getTel()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getIsDefault() == null ? other.getIsDefault() == null : this.getIsDefault().equals(other.getIsDefault()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getDistrict() == null) ? 0 : getDistrict().hashCode());
        result = prime * result + ((getDetailedAddress() == null) ? 0 : getDetailedAddress().hashCode());
        result = prime * result + ((getReceiverName() == null) ? 0 : getReceiverName().hashCode());
        result = prime * result + ((getTel() == null) ? 0 : getTel().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getIsDefault() == null) ? 0 : getIsDefault().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", district=").append(district);
        sb.append(", detailedAddress=").append(detailedAddress);
        sb.append(", receiverName=").append(receiverName);
        sb.append(", tel=").append(tel);
        sb.append(", userId=").append(userId);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}