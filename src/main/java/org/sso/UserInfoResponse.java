package org.sso;

public class UserInfoResponse {
    // 用户信息字段
    // ...
    private String openId;
    private String certifType;
    private boolean enabled;
    private String unifiedCreditCode;
    private Integer userType;

    private String personName;

    private String mobile;

    private String personNameFuzzy;

    private String certifId;

    private String mobileFuzzy;


    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPersonNameFuzzy() {
        return personNameFuzzy;
    }

    public void setPersonNameFuzzy(String personNameFuzzy) {
        this.personNameFuzzy = personNameFuzzy;
    }

    public String getCertifId() {
        return certifId;
    }

    public void setCertifId(String certifId) {
        this.certifId = certifId;
    }

    public String getMobileFuzzy() {
        return mobileFuzzy;
    }

    public void setMobileFuzzy(String mobileFuzzy) {
        this.mobileFuzzy = mobileFuzzy;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCertifType() {
        return certifType;
    }

    public void setCertifType(String certifType) {
        this.certifType = certifType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUnifiedCreditCode() {
        return unifiedCreditCode;
    }

    public void setUnifiedCreditCode(String unifiedCreditCode) {
        this.unifiedCreditCode = unifiedCreditCode;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}