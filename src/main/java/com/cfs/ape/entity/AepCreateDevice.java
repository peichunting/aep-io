package com.cfs.ape.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cfs.ape.enums.CommandStatusEnum;

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.IdType.ID_WORKER;

@TableName("cfs_aep_create_device")
public class AepCreateDevice {

    @TableId(type= ID_WORKER)
    private Long id;

    private String deviceName;

    private String imei;

    private String operator;

    private String  productId;

    private int autoObserver;

    private String imsi;

    private String pskValue;

    private String msg;

    private String result;

    private String deviceId;

    private String deviceSn;

    private CommandStatusEnum commandStatus;

    private String masterKey;

    private int code;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAutoObserver() {
        return autoObserver;
    }

    public void setAutoObserver(int autoObserver) {
        this.autoObserver = autoObserver;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getPskValue() {
        return pskValue;
    }

    public void setPskValue(String pskValue) {
        this.pskValue = pskValue;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public CommandStatusEnum getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(CommandStatusEnum commandStatus) {
        this.commandStatus = commandStatus;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }
}
