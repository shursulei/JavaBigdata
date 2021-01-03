package com.shursulei.ldap;

import java.util.ArrayList;
import java.util.Map;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;

public class LdapTest1 {
	// 当前配置信息
	private static String ldapHost = "192.168.146.22";
	private static int ldapPort = 389;
	private static String ldapBindDN = "dc=my-domain,dc=com";;
	private static String ldapPassword = "123456";
	private static LDAPConnection connection = null;

	/** 连接LDAP */
	public static void openConnection() {
		if (connection == null) {
			try {
				connection = new LDAPConnection(ldapHost, ldapPort, ldapBindDN, ldapPassword);
				System.out.println("====");
			} catch (Exception e) {
				System.out.println("连接LDAP出现错误：\n" + e.getMessage());
			}
		}
	}

	/** 创建DC */
	public static void createDC(String baseDN, String dc) {
		String entryDN = "dc=" + dc + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "organization", "dcObject"));
				attributes.add(new Attribute("dc", dc));
				attributes.add(new Attribute("o", dc));
				connection.add(entryDN, attributes);
				System.out.println("创建DC" + entryDN + "成功！");
			} else {
				System.out.println("DC " + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建DC出现错误：\n" + e.getMessage());
		}
	}

	/** 创建组织 */
	public static void createO(String baseDN, String o) {
		String entryDN = "o=" + o + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "organization"));
				attributes.add(new Attribute("o", o));
				connection.add(entryDN, attributes);
				System.out.println("创建组织" + entryDN + "成功！");
			} else {
				System.out.println("组织" + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建组织出现错误：\n" + e.getMessage());
		}
	}

	/** 创建组织单元 */
	public static void createOU(String baseDN, String ou) {
		String entryDN = "ou=" + ou + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "organizationalUnit"));
				attributes.add(new Attribute("ou", ou));
				connection.add(entryDN, attributes);
				System.out.println("创建组织单元" + entryDN + "成功！");
			} else {
				System.out.println("组织单元" + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建组织单元出现错误：\n" + e.getMessage());
		}
	}

	/** 创建用户 */
	public static void createEntry(String baseDN, String uid) {
		String entryDN = "uid=" + uid + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "account"));
				attributes.add(new Attribute("uid", uid));
				connection.add(entryDN, attributes);
				System.out.println("创建用户" + entryDN + "成功！");
			} else {
				System.out.println("用户" + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建用户出现错误：\n" + e.getMessage());
		}
	}

	/** 修改用户信息 */
	public static void modifyEntry(String requestDN, Map<String, String> data) {
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(requestDN);
			if (entry == null) {
				System.out.println(requestDN + " user:" + requestDN + "不存在");
				return;
			}
			// 修改信息
			ArrayList<Modification> md = new ArrayList<Modification>();
			for (String key : data.keySet()) {
				md.add(new Modification(ModificationType.REPLACE, key, data.get(key)));
			}
			connection.modify(requestDN, md);

			System.out.println("修改用户信息成！");
		} catch (Exception e) {
			System.out.println("修改用户信息出现错误：\n" + e.getMessage());
		}
	}

	/** 删除用户信息 */
	public static void deleteEntry(String requestDN) {
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(requestDN);
			if (entry == null) {
				System.out.println(requestDN + " user:" + requestDN + "不存在");
				return;
			}
			// 删除
			connection.delete(requestDN);
			System.out.println("删除用户信息成！");
		} catch (Exception e) {
			System.out.println("删除用户信息出现错误：\n" + e.getMessage());
		}
	}

	/** 查询 */
	public static void queryLdap(String searchDN, String filter) {
		try {
			// 连接LDAP
			openConnection();

			// 查询企业所有用户
			SearchRequest searchRequest = new SearchRequest(searchDN, SearchScope.SUB, "(" + filter + ")");
			searchRequest.addControl(new SubentriesRequestControl());
			SearchResult searchResult = connection.search(searchRequest);
			System.out.println(">>>共查询到" + searchResult.getSearchEntries().size() + "条记录");
			int index = 1;
			for (SearchResultEntry entry : searchResult.getSearchEntries()) {
				System.out.println((index++) + "\t" + entry.getDN());
			}
		} catch (Exception e) {
			System.out.println("查询错误，错误信息如下：\n" + e.getMessage());
		}
	}
	public static void main(String[] args) {
		String root = "com";
		String dc = "truesens";
		String o = "kedacom";
		String ou = "people";
		String uid = "admin";
		String filter = "objectClass=account";
		createDC("dc=" + root, dc);
	}
}
