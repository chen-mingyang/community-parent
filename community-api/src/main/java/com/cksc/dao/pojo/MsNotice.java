package com.cksc.dao.pojo;

import java.util.Date;

public class MsNotice {
    private Long nid;

    private Integer nType;

    private Date createTime;

    private Long nsUid;

    private Long nrUid;

    private String nContent;

    public Long getNid() {
        return nid;
    }

    public void setNid(Long nid) {
        this.nid = nid;
    }

    public Integer getnType() {
        return nType;
    }

    public void setnType(Integer nType) {
        this.nType = nType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getNsUid() {
        return nsUid;
    }

    public void setNsUid(Long nsUid) {
        this.nsUid = nsUid;
    }

    public Long getNrUid() {
        return nrUid;
    }

    public void setNrUid(Long nrUid) {
        this.nrUid = nrUid;
    }

    public String getnContent() {
        return nContent;
    }

    public void setnContent(String nContent) {
        this.nContent = nContent == null ? null : nContent.trim();
    }
}