package com.shursulei.ldap.enty;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import com.shursulei.springldap.entry.LdapUser;

public class LdapUserAttributeMapper {

	 
	/**
	 * 将单个Attributes转成单个对象
	 * @param attrs
	 * @return
	 * @throws NamingException
	 */
	public Object mapFromAttributes(Attributes attrs) throws NamingException {
		LdapUser user  = new LdapUser();
 
		if(attrs.get("uid") != null){
			user.setUsername( attrs.get("uid").get().toString());
		}
		if(attrs.get("cn") != null){
			user.setUserCn( attrs.get("cn").get().toString());
		}
		if(attrs.get("mobile") != null){
			user.setMobile( attrs.get("mobile").get().toString());
		}
		if(attrs.get("mail") != null){
			user.setMail( attrs.get("mail").get().toString());
		}
		if(attrs.get("employeeNumber") != null){
			user.setUserNumber( attrs.get("employeeNumber").get().toString());
		}
 
		if(attrs.get("smart-type") != null){
			user.setUserType( attrs.get("smart-type").get().toString());
		}
		if(attrs.get("smart-py") != null){
			user.setPinyin(attrs.get("smart-py").get().toString());
		}
		if(attrs.get("smart-alias") != null){
			user.setAlias(attrs.get("smart-alias").get().toString());
		}
		if(attrs.get("departmentNumber") != null){
			user.setDeptId(attrs.get("departmentNumber").get().toString());
		}
		if(attrs.get("departmentName") != null){
			user.setDeptName(attrs.get("departmentName").get().toString());
		}
		if(attrs.get("customized-jobname") != null){
			user.setPositionName(attrs.get("customized-jobname").get().toString());
		}
		if(attrs.get("modifyTimestamp") != null){
			user.setModifyTimestamp(attrs.get("modifyTimestamp").get().toString());
		}
		return user;
	}
}
