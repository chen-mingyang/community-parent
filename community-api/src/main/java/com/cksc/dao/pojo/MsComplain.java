package com.cksc.dao.pojo;

import java.util.Date;

public class MsComplain {
    private Long coId;

    private Integer coType;

    private Integer coState;

    private Date createTime;

    private Date disposeTime;

    private Long apId;

    private Long cid;

    private Long cosUid;

    private Long corUid;

    private String coExplain;

    public Long getCoId() {
        return coId;
    }

    public void setCoId(Long coId) {
        this.coId = coId;
    }

    public Integer getCoType() {
        return coType;
    }

    public void setCoType(Integer coType) {
        this.coType = coType;
    }

    public Integer getCoState() {
        return coState;
    }

    public void setCoState(Integer coState) {
        this.coState = coState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime) {
        this.disposeTime = disposeTime;
    }

    public Long getApId() {
        return apId;
    }

    public void setApId(Long apId) {
        this.apId = apId;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getCosUid() {
        return cosUid;
    }

    public void setCosUid(Long cosUid) {
        this.cosUid = cosUid;
    }

    public Long getCorUid() {
        return corUid;
    }

    public void setCorUid(Long corUid) {
        this.corUid = corUid;
    }

    public String getCoExplain() {
        return coExplain;
    }

    public void setCoExplain(String coExplain) {
        this.coExplain = coExplain == null ? null : coExplain.trim();
    }
}