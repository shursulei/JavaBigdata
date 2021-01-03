package com.shursulei.ldap;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AuthenticationSource;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.util.Random;

/**
 * Created by xhh on 2018/4/1 14:47.
 */
public class LdapDemoTest {
    public static void main(String[] args) {

        LdapContextSource cs = new LdapContextSource();
        cs.setCacheEnvironmentProperties(false);
        cs.setUrl("ldap://192.168.146.22:389");
        cs.setBase("dc=honor,dc=zhe,dc=wang");
        //cn=Manager,dc=my-domain,dc=com
        // 用户名：cn=Manager,dc=honor,dc=zhe,dc=wang
        // 密码：123456
        cs.setAuthenticationSource(new AuthenticationSource() {
            public String getCredentials() {
                return "cn=Manager,dc=honor,dc=zhe,dc=wang";
            }
            public String getPrincipal() {
                return "123456";
            }
        });
        LdapTemplate template = new LdapTemplate(cs);


        //创建第二层：（第一层数据一般是初始化的）
        createSecondEntry(template,"daye");

        //创建第三层：（第一层数据一般是初始化的，第二层需要创建好）
        createThirdEntry(template,"daye","houzi");

    }

    /**
     ** 构造dn，Name
     * @param type
     * @param commonName
     * @return
     */
    public static DistinguishedName getDn(String type, String commonName) {
        DistinguishedName dn = new DistinguishedName();
        if (StringUtils.isNotBlank(type)) {
            dn.add("ou", type);
        }
        if (StringUtils.isNotBlank(commonName)) {
            dn.add("cn", commonName);
        }
        return dn;
    }

    /**
     * bind方法即是创建；BasicAttribute 是基本属性，有了类属性之后，才能添加具体的属性
     * @param template
     * @param secondName
     */
    public static void createSecondEntry(LdapTemplate template, String secondName){
        Name dn = getDn(secondName,null);
        BasicAttribute baAttr = new BasicAttribute("objectClass");
        baAttr.add("top");
        baAttr.add("organizationalUnit");
        Attributes attrs = new BasicAttributes();
        attrs.put(baAttr);
        attrs.put("ou", secondName);
        template.bind(dn, null, attrs);

    }

    /**
     * 属性top，person,posixAccount决定了下面的属性：cn,sn,uid,gidNumber等
     * @param template
     * @param secondName
     * @param thirdName
     */
    public static void createThirdEntry(LdapTemplate template, String secondName, String thirdName){
        Name dn = getDn(secondName,thirdName);
        BasicAttribute baAttr = new BasicAttribute("objectClass");
        baAttr.add("top");
        baAttr.add("person");
        baAttr.add("inetOrgPerson");
        baAttr.add("posixAccount");
        baAttr.add("shadowAccount");
        Attributes attrs = new BasicAttributes();
        attrs.put(baAttr);
        attrs.put("cn", thirdName);
        attrs.put("sn", thirdName);
        Random random = new Random();
        String uidNumber = random.nextInt(2000)+"";
        attrs.put("uid", thirdName);
        attrs.put("gidNumber", uidNumber);
        attrs.put("uidNumber", uidNumber);
        attrs.put("loginShell","/bin/bash");
        template.bind(dn, null, attrs);

    }
}
