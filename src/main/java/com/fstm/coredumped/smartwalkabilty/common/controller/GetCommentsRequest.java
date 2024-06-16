package com.fstm.coredumped.smartwalkabilty.common.controller;

public class GetCommentsRequest extends BaseRequest {
    private int siteId;

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }
}
