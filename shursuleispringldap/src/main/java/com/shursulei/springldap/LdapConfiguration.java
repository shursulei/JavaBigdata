package com.shursulei.springldap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * LDAP 的自动配置类
 *
 * 完成连接 及LdapTemplate生成
 */
@Configuration
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
 
    @Bean
    public LdapTemplate ldapTemplate() {
        if (null == ldapTemplate)
            ldapTemplate = new LdapTemplate(contextSource());
        return ldapTemplate;
    }
}
