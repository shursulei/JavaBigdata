package com.shursulei.ldap.enty;

public class LdapUser {
	private String username;
	private String userCn;
	private String mobile;
	private String mail;
	private String userNumber;
	private String UserType;
	private String pinyin;
	private String alias;
	private String deptId;
	private String deptName;
	private String positionName;
	private String modifyTimestamp;
	public String getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	public String getUserType() {
		return UserType;
	}
	public void setUserType(String userType) {
		UserType = userType;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getModifyTimestamp() {
		return modifyTimestamp;
	}
	public void setModifyTimestamp(String modifyTimestamp) {
		this.modifyTimestamp = modifyTimestamp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserCn() {
		return userCn;
	}
	public void setUserCn(String userCn) {
		this.userCn = userCn;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

}
