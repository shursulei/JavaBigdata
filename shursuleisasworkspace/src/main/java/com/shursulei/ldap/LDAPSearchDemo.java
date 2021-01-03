package com.shursulei.ldap;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
import com.novell.ldap.util.Base64;

public class LDAPSearchDemo {
	public static void main(String[] args) {

		String ldapHost = "localhost";
		String loginDN = "cn=Manager,dc=shursulei,dc=com";
		String password = "secret";
		String searchBase = "dc=shursulei,dc=com";
		String searchFilter = "objectClass=*";

		int ldapPort = LDAPConnection.DEFAULT_PORT;
		// 查询范围
		// SCOPE_BASE、SCOPE_ONE、SCOPE_SUB、SCOPE_SUBORDINATESUBTREE
		int searchScope = LDAPConnection.SCOPE_SUB;
		LDAPConnection lc = new LDAPConnection();
		try {
			lc.connect(ldapHost, ldapPort);
			lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
			LDAPSearchResults searchResults = lc.search(searchBase, searchScope, searchFilter, null, false);
			while (searchResults.hasMore()) {
				LDAPEntry nextEntry = null;
				try {
					nextEntry = searchResults.next();
				} catch (LDAPException e) {
					System.out.println("Error: " + e.toString());
					if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
							|| e.getResultCode() == LDAPException.CONNECT_ERROR) {
						break;
					} else {
						continue;
					}
				}
				System.out.println("DN =: " + nextEntry.getDN());
				System.out.println("|---- Attributes list: ");
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
				Iterator<LDAPAttribute> allAttributes = attributeSet.iterator();
				while (allAttributes.hasNext()) {
					LDAPAttribute attribute = allAttributes.next();
					String attributeName = attribute.getName();
					Enumeration<String> allValues = attribute.getStringValues();
					if (null == allValues) {
						continue;
					}
					while (allValues.hasMoreElements()) {
						String value = allValues.nextElement();
						if (!Base64.isLDIFSafe(value)) {
							// base64 encode and then print out
							value = Base64.encode(value.getBytes());
						}
						System.out.println("|---- ---- " + attributeName + " = " + value);
					}
				}
			}
		} catch (LDAPException e) {
			System.out.println("Error: " + e.toString());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error: " + e.toString());
		} finally {
			try {
				if (lc.isConnected()) {
					lc.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
