package com.ktv.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePwdDTO {
    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "原密码不能为空")
    @Size(min = 6, max = 50, message = "原密码长度6-50位")
    private String oldPwd;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "新密码长度6-50位")
    private String newPwd;

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getOldPwd() { return oldPwd; }
    public void setOldPwd(String oldPwd) { this.oldPwd = oldPwd; }

    public String getNewPwd() { return newPwd; }
    public void setNewPwd(String newPwd) { this.newPwd = newPwd; }
}