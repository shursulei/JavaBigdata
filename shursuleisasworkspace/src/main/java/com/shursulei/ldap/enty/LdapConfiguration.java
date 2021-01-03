package com.shursulei.ldap.enty;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;


public class LdapConfiguration {
	private LdapTemplate ldapTemplate;
	@Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        Map<String, Object> config = new HashMap();
        contextSource.setUrl(LdapConstans.LDAP_URL);
        contextSource.setBase(LdapConstans.BASE_DC);
        contextSource.setUserDn(LdapConstans.USER_NAME);
        contextSource.setPassword(LdapConstans.PASS_WORD);
        //  解决 乱码 的关键一句
        config.put("java.naming.ldap.attributes.binary", "objectGUID");
        contextSource.setPooled(true);
        contextSource.setBaseEnvironmentProperties(config);
        return contextSource;
    }
}
