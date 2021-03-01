package com.shursulei.metaOper;

/**
 * sas元数据操作
 * TODO * @version 1.0 * @author shursulei * @date 2021/3/1 14:55
 */
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.PropertyResourceBundle;
//import java.util.ResourceBundle;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//
//import com.sas.metadata.remote.AccessControlEntry;
//import com.sas.metadata.remote.AccessControlTemplate;
//import com.sas.metadata.remote.AssociationList;
//import com.sas.metadata.remote.AuthenticationDomain;
//import com.sas.metadata.remote.DeployedComponent;
//import com.sas.metadata.remote.ExternalTable;
//import com.sas.metadata.remote.File;
//import com.sas.metadata.remote.Identity;
//import com.sas.metadata.remote.IdentityGroup;
//import com.sas.metadata.remote.InternalLogin;
//import com.sas.metadata.remote.Job;
//import com.sas.metadata.remote.LogicalServer;
//import com.sas.metadata.remote.Login;
//import com.sas.metadata.remote.Machine;
//import com.sas.metadata.remote.MdAuthorizationIdentityInfoImpl;
//import com.sas.metadata.remote.Person;
//import com.sas.metadata.remote.PhysicalTable;
//import com.sas.metadata.remote.RelationalTable;
//import com.sas.metadata.remote.SASLibrary;
//import com.sas.metadata.remote.ServerComponent;
//import com.sas.metadata.remote.Tree;
//import com.sas.metadata.remote.impl.SoftwareComponentImpl;
//import com.sas.metadata.remote.MdFactoryImpl;
//import com.sas.metadata.remote.MdOMIUtil;
//import com.sas.metadata.remote.MdOMRConnection;
//import com.sas.metadata.remote.MdObjectStore;
//import com.sas.metadata.remote.MdPermissionInfo;
//import com.sas.metadata.remote.MetadataObjects;
//import com.sas.metadata.remote.Permission;
//import com.sas.meta.SASOMI.ISecurity_1_1;
//import com.sas.iom.SASIOMDefs.VariableArray2dOfStringHolder;
//import com.sas.metadata.remote.impl.AccessControlEntryImpl;
//import com.sas.metadata.remote.impl.IdentityGroupImpl;
//import com.sas.metadata.remote.impl.LoginImpl;
//import com.sas.metadata.remote.impl.LoginImpl_Stub;
//import com.sas.metadata.remote.impl.MdObjectBaseImpl;
//import com.sas.metadata.remote.impl.PrimaryTypeImpl;
//import com.sas.metadata.remote.impl.SecondaryTypeImpl;

public class MetaOper {

//	String serverName = "";
//	String serverPort = "";
//	String serverUser = "";
//	String serverPass = "";
//	MdOMRConnection connection = null;
//	MdFactoryImpl _factory = null;
//	ISecurity_1_1 iSecurity = null;
//	static Logger log = LogManager.getLogger(MetaOper.class);
//
//	Map<Tree,String> treemapall = new HashMap<Tree,String>();
//
//	class PermissionList {
//		public static final String READ_METADATA="ReadMetadata";
//		public static final String WRITE_METADATA="WriteMetadata";
//		public static final String CHECKIN_METADATA="CheckInMetadata";
//		public static final String READ="Read";
//		public static final String WRITE="Write";
//		public static final String ADMINISTER="Administer";
//		public static final String CREATE="Create";
//		public static final String DELETE="Delete";
//		public static final String EXECUTE="Execute";
//		public static final String WRITE_MEMBER_METADATA="WriteMemberMetadata";
//		public static final String CREATE_TABLE="Create Table";
//		public static final String DROP_TABLE="Drop Table";
//		public static final String ALTER_TABLE="Alter Table";
//		public static final String SELECT="Select";
//		public static final String INSERT="Insert";
//		public static final String UPDATE="Update";
//		public static final String REFERENCES="References";
//		public static final String MANAGE_CREDENTIALS_METADATA="ManageCredentialsMetadata";
//		public static final String MANAGE_MEMBER_METADATA="ManageMemberMetadata";
//    }
//
//	class GrantOrDeny {
//		public static final String GRANT="GRANT";
//		public static final String DENY="DENY";
//    }
//
//	public int connectToMetadata(String name, String port, String user, String pass) throws Exception {
//		serverName = name;
//		serverPort = port;
//		serverUser = user;
//		serverPass = pass;
//		_factory = new MdFactoryImpl(false);
//		connection = _factory.getConnection();
//		connection.makeOMRConnection(serverName, serverPort, serverUser, serverPass);
//		iSecurity = connection.MakeISecurityConnection();
//		return 0;
//
//	}
//
//	public MetaOper() {
//	};
//
//	public Person getPerson(String username) throws Exception {
//		MdObjectStore objectStore = null;
//		Person person = null;
//
//		final String[][] options = { { "ReturnUnrestrictedSource", "" } };
//		VariableArray2dOfStringHolder info = new VariableArray2dOfStringHolder();
//		iSecurity.GetInfo("GetIdentityInfo", "Person:" + username, options, info);
//
//		String[][] returnArray = info.value;
//		String personMetaID = new String();
//		for (int i = 0; i < returnArray.length; i++) {
//			if (returnArray[i][0].compareTo("IdentityObjectID") == 0) {
//				personMetaID = returnArray[i][1];
//			}
//		}
//		objectStore = _factory.createObjectStore();
//
//		person = (Person) _factory.createComplexMetadataObject(objectStore, username, MetadataObjects.PERSON,
//				personMetaID);
//		//System.out.println(person.getName());
//		return person;
//
//	}
//
//	public List getInterAccts() throws Exception {
//			Person person = null;
//			MdObjectStore objectStore = null;
//			objectStore = _factory.createObjectStore();
//			int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//			List persons = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//					_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.PERSON, flags, "");
//			List interAccts = new ArrayList();
//			for (Object i : persons) {
//				person = (Person) i;
//				InternalLogin il = person.getInternalLoginInfo();
//				if (il != null ) {
//					interAccts.add(il);
//					//System.out.println(person.getName()+"@saspw");
//				}
//
//			}
//			return interAccts;
//		}
//
//	public List getInterAcct(String username) throws Exception {
//		Person person = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List persons = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.PERSON, flags, "");
//		List interAccts = new ArrayList();
//		for (Object i : persons) {
//			person = (Person) i;
//			InternalLogin il = person.getInternalLoginInfo();
//			if (il != null && person.getName().equals(username)) {
//				interAccts.add(il);
//				//System.out.println(person.getName()+"@saspw");
//			}
//
//		}
//		return interAccts;
//	}
//
//	public List getLogins() throws Exception {
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List iLogin = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.LOGIN, flags, "");
//		for (Object o : iLogin) {
//			Login l = (Login) o;
//			//System.out.println(l.getUserID()+"  "+l.getAttributeNames());
//
//		}
//		return iLogin;
//	}
//
//	public Login getLogin(String username) throws Exception {
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List iLogin = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.LOGIN, flags, "");
//		for (Object o : iLogin) {
//			Login l = (Login) o;
//			if (l.getUserID().equals(username)) {
//				//System.out.println(l.getUserID());
//				return l;
//			}
//
//		}
//		return null;
//	}
//
//	public Login changeExtAcctPasswd(String loginname,String password) throws Exception {
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List iLogin = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.LOGIN, flags, "");
//		for (Object o : iLogin) {
//			Login l = (Login) o;
//			if (l.getUserID().equals(loginname)) {
//				l.setPassword(password);
//				//System.out.println(l.getUserID());
//				return l;
//			}
//
//		}
//		return null;
//	}
//
//
//	public void addLogin(String username,String user_id, AuthenticationDomain domain, Person person) throws Exception {
//
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//		LoginImpl login=null;
//		objectStore = _factory.createObjectStore();
//		String reposFQID = _factory.getOMIUtil().getFoundationReposID();
//		//new MdObjectBaseImpl().setName(strName);;
//		String shortReposID = reposFQID.substring(reposFQID.indexOf('.') + 1, reposFQID.length());
//		Integer login_seq=1;
//		if (domain.getLogins().size()!=0) {
//			for (Object Obj : domain.getLogins()) {
//				if (((Login)Obj).getName().contains("Login"+username+"."+String.format("%02d", login_seq))){
//					login_seq+=1;
//				}
//			}
//		}
//
//		login = (LoginImpl) _factory.createComplexMetadataObject(objectStore, "Login"+username+"."+String.format("%02d", login_seq), MetadataObjects.LOGIN,
//				shortReposID);
//		login.setUserID(user_id);
//		login.setDomain(domain);
//		login.setAssociatedIdentity((Identity)person);
//		login.updateMetadataAll();
//
//		//System.out.println("�����û���" + IdentityName);
//
//	}
//
//
//	public void changeExtPasswd(String username,String password) throws Exception {
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List iLogin = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.LOGIN, flags, "");
//		for (Object o : iLogin) {
//			Login l = (Login) o;
//			if (l.getUserID().equals(username)) {
//				l.setPassword(password);
//				l.updateMetadataAll();
//			}
//
//		}
//
//	}
//
//	public void changeExtPasswd(Login login,String password) throws Exception {
//		login.setPassword(password);
//		login.updateMetadataAll();
//	}
//
//	public int changeInterPasswd(String IdentityName, String IdentityPassword) throws Exception {
//		MdObjectStore objectStore = null;
//		Person person = null;
//		final String[][] options = { { "ReturnUnrestrictedSource", "" } };
//		VariableArray2dOfStringHolder info = new VariableArray2dOfStringHolder();
//		iSecurity.GetInfo("GetIdentityInfo", "Person:" + IdentityName, options, info);
//		String[][] returnArray = info.value;
//		String personMetaID = new String();
//		for (int i = 0; i < returnArray.length; i++) {
//			if (returnArray[i][0].compareTo("IdentityObjectID") == 0) {
//				personMetaID = returnArray[i][1];
//			}
//		}
//		objectStore = _factory.createObjectStore();
//		person = (Person) _factory.createComplexMetadataObject(objectStore, IdentityName, MetadataObjects.PERSON,
//				personMetaID);
//		iSecurity.SetInternalPassword(IdentityName, IdentityPassword);
//		person.updateMetadataAll();
//		//System.out.println(IdentityName + ":�������޸�.");
//		return 0; // success
//
//	}
//
//	public int changeInterPasswd(Person IdentityName, String IdentityPassword) throws Exception {
//		final String[][] options = { { "ReturnUnrestrictedSource", "" } };
//		VariableArray2dOfStringHolder info = new VariableArray2dOfStringHolder();
//
//		iSecurity.SetInternalPassword(IdentityName.getName(), IdentityPassword);
//		IdentityName.updateMetadataAll();
//		//System.out.println(IdentityName.getName() + ":�������޸�.");
//		return 0; // success
//	}
//
//	public int addUser(String IdentityName) throws Exception {
//		MdObjectStore objectStore = null;
//		Person person = null;
//		objectStore = _factory.createObjectStore();
//		String reposFQID = _factory.getOMIUtil().getFoundationReposID();
//
//		String shortReposID = reposFQID.substring(reposFQID.indexOf('.') + 1, reposFQID.length());
//		person = (Person) _factory.createComplexMetadataObject(objectStore, IdentityName, MetadataObjects.PERSON,
//				shortReposID);
//
//		person.updateMetadataAll();
//
//		//System.out.println("�����û���" + IdentityName);
//		return 0; // success
//	}
//
//	public int addGroup2Group(IdentityGroup g1, IdentityGroup g2) throws Exception {
//		g1.getIdentityGroups().add(g2);
//		g1.updateMetadataAll();
//
//		//System.out.println("�����û���" + IdentityName);
//		return 0; // success
//	}
//
//	public List getGroups() throws Exception {
//		IdentityGroup group = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List igroup = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.IDENTITYGROUP, flags, "");
//		List group2 = new ArrayList();
//		for (Object i : igroup) {
//			group = (IdentityGroup) i;
//			if (group.getGroupType().length()==0){
//				group2.add(group);
//				//System.out.println(group.getName());
//			}
//
//
//		}
//		return group2;
//	}
//
//
//	public IdentityGroup getGroup(String groupname) throws Exception {
//		IdentityGroup group = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List igroup = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.IDENTITYGROUP, flags, "");
//		for (Object i : igroup) {
//			group = (IdentityGroup) i;
//			if (group.getName().equals(groupname) && group.getGroupType().length()==0) {
//				//System.out.println(group.getName());
//				//System.out.println(group.getAssociatedObjects(MetadataObjects.ACCESSCONTROLTEMPLATE));
//				return group;
//			}
//
//		}
//		return group;
//	}
//	public void createGroup(String groupname) throws Exception {
//		IdentityGroupImpl group = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//		String reposFQID = _factory.getOMIUtil().getFoundationReposID();
//
//		String shortReposID = reposFQID.substring(reposFQID.indexOf('.') + 1, reposFQID.length());
//		group = (IdentityGroupImpl) _factory.createComplexMetadataObject(objectStore, groupname, MetadataObjects.IDENTITYGROUP,
//				shortReposID);
//
//		group.updateMetadataAll();
//
//
//	}
//	public List getRoles() throws Exception {
//		IdentityGroup role = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List roles = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.IDENTITYGROUP, flags, "");
//		List roles2 = new ArrayList();
//		for (Object i : roles) {
//			role = (IdentityGroup) i;
//			if (role.getGroupType().length()!=0){
//				roles2.add(role);
//				//System.out.println(role.getName());
//			}
//
//
//		}
//		return roles2;
//	}
//
//	public IdentityGroup getRole(String rolename) throws Exception {
//		IdentityGroup role = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List igroup = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.IDENTITYGROUP, flags, "");
//		for (Object i : igroup) {
//			role = (IdentityGroup) i;
//			if (role.getName().matches(".*" + rolename +".*" ) && role.getGroupType().equals("role")) {
//				//System.out.println(role.getName());
//				return role;
//			}
//
//		}
//		return null;
//	}
//
//	public void createRole(String rolename) throws Exception {
//		IdentityGroupImpl role = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//		String reposFQID = _factory.getOMIUtil().getFoundationReposID();
//
//		String shortReposID = reposFQID.substring(reposFQID.indexOf('.') + 1, reposFQID.length());
//		role = (IdentityGroupImpl) _factory.createComplexMetadataObject(objectStore, rolename, MetadataObjects.IDENTITYGROUP,
//				shortReposID);
//		role.setGroupType("role");
//		role.updateMetadataAll();
//	}
//
//	public List getActs() throws Exception {
//		AccessControlTemplate act = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List acts = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.ACCESSCONTROLTEMPLATE, flags,
//				"");
//		for (Object i : acts) {
//			act = (AccessControlTemplate) i;
//			//System.out.println(act.getName());
//
//		}
//		return acts;
//	}
//
//	public AccessControlTemplate getAct(String actname) throws Exception {
//		AccessControlTemplate act = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List acts = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.ACCESSCONTROLTEMPLATE, flags,
//				"");
//		for (Object i : acts) {
//			act = (AccessControlTemplate) i;
//
//			if (act.getName().equals(actname)) {
//				//System.out.println(act.getName());
//				return act;
//			}
//
//
//		}
//		return null;
//	}
//
//	public List getActItems(String actname) throws Exception {
//		AccessControlTemplate act = null;
//		MdObjectStore objectStore = null;
//		List result = new ArrayList();
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List acts = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.ACCESSCONTROLTEMPLATE, flags,
//				"");
//		for (Object i : acts) {
//			act = (AccessControlTemplate) i;
//
//			if (act.getName().equals(actname)) {
//
//				AssociationList al = act.getAccessControlItems();
//				for (Object object : al) {
//					AssociationList al2 = ((AccessControlEntryImpl)object).getIdentities();
//					//System.out.println(((AccessControlEntryImpl)object).getPermissions());
//					for (Object object2 : al2) {
//						Identity ide = (Identity)object2;
//						result.add(ide);
//						//System.out.println(ide.getName());
//
//					}
//				}
//			}
//		}
//		return result;
//	}
//
//	public void createAct(String actname) throws Exception {
//		AccessControlTemplate act = null;
//		AccessControlEntry ace = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//		String reposFQID = _factory.getOMIUtil().getFoundationReposID();
//
//		String shortReposID = reposFQID.substring(reposFQID.indexOf('.') + 1, reposFQID.length());
//		act = (AccessControlTemplate) _factory.createComplexMetadataObject(objectStore, actname, MetadataObjects.ACCESSCONTROLTEMPLATE,
//				shortReposID);
//		ace = (AccessControlEntry) _factory.createComplexMetadataObject(objectStore, actname, MetadataObjects.ACCESSCONTROLENTRY,
//				shortReposID);
//		ace.updateMetadataAll();
//		act.getAccessControlItems().add(ace);
//		act.updateMetadataAll();
//
//	}
//
//	public void addIdentity2Act(String actname, Identity personOrGroup, List<Permission> permission) throws Exception {
//		AccessControlTemplate act = null;
//		MdObjectStore objectStore = null;
//		Identity p = personOrGroup;
//		objectStore = _factory.createObjectStore();
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List acts = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.ACCESSCONTROLTEMPLATE, flags,
//				"");
//		for (Object i : acts) {
//			act = (AccessControlTemplate) i;
//
//			if (act.getName().equals(actname)) {
//
//				AssociationList al = act.getAccessControlItems();
//				for (Object object : al) {
//					AccessControlEntryImpl ace=(AccessControlEntryImpl)object;
//					for (Object object2 : permission) {
//						ace.getPermissions().add(object2);
//					}
//
//					AssociationList al2 = ace.getIdentities();
//					ace.updateMetadataAll();
//					al2.add(p);
//
//				}
//			}
//			act.updateMetadataAll();
//		}
//
//	}
//
//	public List<Permission> setPermissions(Map<String, String> permission) throws Exception{
//		List<Permission> resultList = new ArrayList<Permission>();
//		List<Permission> permissionList = this.getPermissions();
//		for (Map.Entry<String, String> me : permission.entrySet()) {
//			for (Permission perm : permissionList) {
//				if (me.getKey().equals(perm.getName()) && me.getValue().equals(perm.getType())){
//					resultList.add(perm);
//				}
//			}
//
//		}
//		return resultList;
//
//
//	}
//
//	public List getDomains() throws Exception {
//		AuthenticationDomain ad = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ads = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.AUTHENTICATIONDOMAIN, flags,
//				"");
//		for (Object i : ads) {
//			ad = (AuthenticationDomain) i;
//			//System.out.println(ad.getName()+"  "+ad.getFQID().hashCode());
//
//		}
//		return ads;
//	}
//
//	public AuthenticationDomain getDomain(String domain) throws Exception {
//		AuthenticationDomain ad = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ads = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.AUTHENTICATIONDOMAIN, flags,
//				"");
//		for (Object i : ads) {
//			ad = (AuthenticationDomain) i;
//
//			if (ad.getName().equals(domain)) {
//				//System.out.println(ad.getName());
//				return ad;
//			}
//
//		}
//		return null;
//	}
//
//	public void createDomain(String domain) throws Exception {
//		AuthenticationDomain ad = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//		String reposFQID = _factory.getOMIUtil().getFoundationReposID();
//
//		String shortReposID = reposFQID.substring(reposFQID.indexOf('.') + 1, reposFQID.length());
//		ad = (AuthenticationDomain) _factory.createComplexMetadataObject(objectStore, domain, MetadataObjects.AUTHENTICATIONDOMAIN,
//				shortReposID);
//		ad.updateMetadataAll();
//	}
//
//	public List getSoftwareComponents() throws Exception {
//		SoftwareComponentImpl sc = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List scs = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.SOFTWARECOMPONENT, flags,
//				"");
//		for (Object i : scs) {
//			sc = (SoftwareComponentImpl) i;
//			//System.out.println(sc.getName());
//
//		}
//		return scs;
//	}
//
//	public List getMachines() throws Exception {
//		Machine machine = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List machines = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.MACHINE, flags, "");
//		for (Object i : machines) {
//			machine = (Machine) i;
//			//System.out.println(machine.getName());
//
//		}
//		return machines;
//	}
//
//	public List getServerComponents() throws Exception {
//		ServerComponent sc = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List scs = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.SERVERCOMPONENT, flags, "");
//		for (Object i : scs) {
//			sc = (ServerComponent) i;
//			//System.out.println(sc.getName());
//
//		}
//		return scs;
//	}
//
//	public List getLibraries() throws Exception {
//		SASLibrary lib = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List libs = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.SASLIBRARY, flags, "");
//		for (Object i : libs) {
//			lib = (SASLibrary) i;
//			//System.out.println(lib.getName());
//
//		}
//		return libs;
//	}
//
//	public List getPersons() throws Exception {
//		Person person = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List persons = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.PERSON, flags, "");
//		for (Object i : persons) {
//			person = (Person) i;
//			//System.out.println(person.getName());
//
//		}
//		return persons;
//	}
//
//	public List getPhysicalTables() throws Exception {
//		PhysicalTable pt = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List pts = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.PHYSICALTABLE, flags, "");
//		for (Object i : pts) {
//			pt = (PhysicalTable) i;
//			//System.out.println(pt.getName());
//
//		}
//		return pts;
//	}
//
//	public List getExternalTables() throws Exception {
//		ExternalTable at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.EXTERNALTABLE, flags, "");
//		for (Object i : ats) {
//			at = (ExternalTable) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//	public List getRelationalTables() throws Exception {
//		RelationalTable at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.RELATIONALTABLE, flags, "");
//		for (Object i : ats) {
//			at = (RelationalTable) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//	public List getFiles() throws Exception {
//		File at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.FILE, flags, "");
//		for (Object i : ats) {
//			at = (File) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//	public List getJobs() throws Exception {
//		Job at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.JOB, flags, "");
//		for (Object i : ats) {
//			at = (Job) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//	public List getLogicalServers() throws Exception {
//		LogicalServer at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.LOGICALSERVER, flags, "");
//		for (Object i : ats) {
//			at = (LogicalServer) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//	public List getDeployedComponents() throws Exception {
//		DeployedComponent at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.DEPLOYEDCOMPONENT, flags, "");
//		for (Object i : ats) {
//			at = (DeployedComponent) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//	public List<Permission> getPermissions() throws Exception {
//		Permission at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.PERMISSION, flags, "");
//		for (Object i : ats) {
//			at = (Permission) i;
//			//System.out.println(at.getName()+" "+at.getType());
//		}
//		return ats;
//	}
//
//	public List getTree() throws Exception {
//		Tree at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.TREE, flags, "");
//		int level=1;
//		List<String> treeList= new ArrayList();
//		Map<Tree,String> mm =  new HashMap();
//		for (Object i : ats) {
//			at = (Tree) i;
//
//			if (at.getPublicType() !=null && at.getPublicType().equals("Folder") ) {
//				if (at.getParentTree() ==null) {
//					mm.put(at, at.getName());
//				}else {
//					this.getSubTrees(mm, level);
//				}
//
//			}
//
//
//		}
//		for (Map.Entry<Tree,String> entry : treemapall.entrySet()) {
//			treeList.add(entry.getValue());
//			//System.out.println(entry.getValue());
//		}
//		return treeList;
//	}
//
//	public List<String> getFolder() throws Exception {
//		List<String> folderList = new ArrayList<String>();
//		this.getTree();
//		for (Map.Entry<Tree,String> entry : treemapall.entrySet()) {
//			if (entry.getValue().matches("^User Folders.*")) {
//				folderList.add(entry.getValue());
//				//System.out.println(entry.getValue());
//			}
//
//		}
//		return folderList;
//	}
//
//	public void getSubTrees(Map<Tree,String> treemap,int level) throws Exception {
//		Map<Tree,String> tempMap = new HashMap<Tree, String>();
//
//		for (Map.Entry<Tree,String> entry : treemap.entrySet()) {
//			tempMap.put(entry.getKey(), entry.getValue());
//		}
//
//		level+=1;
//		for (Map.Entry<Tree,String> entry2 : tempMap.entrySet()) {
//			AssociationList al = entry2.getKey().getSubTrees();
//			for(Object oo : al) {
//				Tree subT = (Tree)oo;
//				if (subT.getParentTree() == entry2.getKey()) {
//					treemapall.put(subT, entry2.getValue()+"/"+subT.getName());
//					treemapall.remove(entry2.getKey());
//					this.getSubTrees(treemapall, level);
//				}
//			}
//		}
//
//	}
//
//
//	public List getPrimaryTypes(String str) throws Exception {
//		PrimaryTypeImpl at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), str, flags, "");
//		for (Object i : ats) {
//			at = (PrimaryTypeImpl) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//	public List getSecondaryTypes(String str) throws Exception {
//		SecondaryTypeImpl at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), str, flags, "");
//		for (Object i : ats) {
//			at = (SecondaryTypeImpl) i;
//			//System.out.println(at.getName());
//
//		}
//		return ats;
//	}
//
//
//
//	public void setUserGroup(Person user, IdentityGroup group) throws Exception {
//		user.getIdentityGroups().addElement(group);
//		user.updateMetadataAll();
//		//System.out.println("����û�" + user.getName() + " ����" + group.getName());
//
//	}
//
//	public Tree getTreeNode(String fullPath) throws Exception {
//		String[] treeList = fullPath.split("/");
//		Tree at = null;
//		MdObjectStore objectStore = null;
//		objectStore = _factory.createObjectStore();
//
//		int flags = MdOMIUtil.OMI_GET_METADATA | MdOMIUtil.OMI_ALL;
//		List ats = (List) _factory.getOMIUtil().getMetadataObjectsSubset(objectStore,
//				_factory.getOMIUtil().getFoundationRepository().getFQID(), MetadataObjects.TREE, flags, "");
//		for (int i = 0; i < treeList.length; i++) {
//			for (Object o : ats) {
//				at = (Tree) o;
//				if (at.getPublicType() !=null && at.getPublicType().equals("Folder") && at.getName().equals(treeList[i]) ) {
//
//					if (i>0 && i==treeList.length-1 && at.getParentTree().getName().equals(treeList[i-1]) && at.getSubTrees().size() == 0 && at.getName().equals(treeList[i])){
//						return at;
//
//					}else if(treeList.length==1) {
//						return at;
//					}else {
//						continue;
//					}
//				}
//
//			}
//
//		}
//		return null;
//	}
//
//	public void createFolder(String fullPath) throws Exception {
//		String[] treeList = fullPath.split("/");
//		String[] fullTree = new String[treeList.length];
//		for (int i = 0; i < treeList.length; i++) {
//			if (i==0) {
//				fullTree[i]=treeList[i];
//			}else {
//				fullTree[i]=fullTree[i-1]+"/"+treeList[i];
//			}
//		}
//		for (int j = 0; j < fullTree.length; j++) {
//			if (this.getTreeNode(fullTree[j]) != null ) {
//				continue;
//			}else {
//				MdObjectStore objectStore = null;
//				objectStore = _factory.createObjectStore();
//				Tree t=null;
//				String reposFQID = _factory.getOMIUtil().getFoundationReposID();
//
//				String shortReposID = reposFQID.substring(reposFQID.indexOf('.') + 1, reposFQID.length());
//				t = (Tree) _factory.createComplexMetadataObject(objectStore, treeList[j], MetadataObjects.TREE,
//						shortReposID);
//
//				t.setParentTree(this.getTreeNode(fullTree[j-1]));
//				t.setPublicType("Folder");
//				t.setTreeType("BIP Folder");
//				t.updateMetadataAll();
//			}
//
//		}
//
//
//	}
//
//	public void addIdentity2Folder(Tree t, Identity identity, List<MdPermissionInfo> permissionInfo) throws Exception {
//
//		//t.setAssociatedIdentity(identity);
//		t.getAuthorizationUtil().getIdentityInfo().add(new MdAuthorizationIdentityInfoImpl(identity));
//		t.getAuthorizationUtil().setAuthorizations(identity, permissionInfo);
//		t.getAuthorizationUtil().commit();
//		t.updateMetadataAll();
//
//	}
//	public static void main(String[] args) throws Exception {
//
//		String proFilePath = System.getProperty("user.dir");
//		InputStream in = new BufferedInputStream(new FileInputStream(proFilePath+"/metainfo.properties"));
//		ResourceBundle bundle = new PropertyResourceBundle(in);
//		String host = bundle.getString("host");
//		String port = bundle.getString("port");
//		String adminuser= bundle.getString("adminuser");
//		String password = bundle.getString("password");
//		MetaOper a = new MetaOper();
//		//a.connectToMetadata("sasfraud", "8561", "sasadm@saspw", "sfm1234");
//		a.connectToMetadata(host, port, adminuser, password);
//
//	        try {
//	            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("File")));//换成你的文件名
//	            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
//	            String line = null;
//	            List<String[]> list = new ArrayList();
//	            while((line=reader.readLine())!=null){
//	                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
//	                list.add(item);
//	            }
//	            if (System.getProperty("Type").equals("addLogin")){
//	            	for (String[] strings : list) {
//	            		a.addLogin(strings[1], strings[1], a.getDomain(strings[2]), a.getPerson(strings[0]));
//	            		log.info("add login for user:"+strings[0] + ",login_id:" + strings[1] + ",domain:"+strings[2]);
//	            	}
//		        }else if (System.getProperty("Type").equals("addPerson2Group")){
//	            	for (String[] strings : list) {
//	            		a.setUserGroup(a.getPerson(strings[0]), a.getGroup(strings[1]));
//	            		log.info("add Person:"+strings[0] + " to Group:" + strings[1] );
//	            	}
//		        }else if (System.getProperty("Type").equals("addGroup2Group")){
//	            	for (String[] strings : list) {
//	            		a.addGroup2Group(a.getGroup(strings[0]), a.getGroup(strings[1]));
//	            		log.info("add Group:"+strings[0] + " to Group:" + strings[1] );
//	            	}
//		        }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//		}
//		// 查询所有的组
//		// a.getGroups();
//		//创建组
//		//a.createGroup("testgroup");
//		//查询指定组
//		//a.getGroup("tgr");
//
//		//修改指定Login账号的密码
//		//a.changeExtPasswd("sasdemo", "sfm1234");
//		//a.changeExtPasswd(a.getExtUser("sasdemo"), "sfm1234");
//		//为指定用户创建Login
//		//a.addLogin("test1234", "test1234", a.getDomain("SFM2"),a.getPerson("test1234"));
//
//		// 查询所有的角色
//		// a.getRoles();
//		// 创建角色
//		//a.createRole("testrole");
//		//查询指定角色
//		//a.getRole("testrole");
//
//
//		// 查询所有的认证域
//		//a.getDomains();
//		// 查询指定的认证域
//		//a.getDomain("SFM");
//		//创建认证域
//		//a.createDomain("SFM2");
//
//		// 查询所有的ACT
//		 //a.getActs();
//		 //a.getAct("ACT2");
//		//a.getActItems("ACT1");
//		//a.createAct("ACT2");
//
//		//给用户或者组添加ACT
//		//Map permissionMap = new HashMap();
//		//permissionMap.put(PermissionList.READ_METADATA, GrantOrDeny.GRANT);
//		//permissionMap.put(PermissionList.WRITE_METADATA, GrantOrDeny.GRANT);
//		//a.addIdentity2Act("ACT2", a.getPerson("sasdemo"),a.setPermissions(permissionMap));
//		//a.addIdentity2Act("ACT2", a.getGroup("testgroup"),a.setPermissions(permissionMap));
//		// 查询所有的组件
//		 //a.getSoftwareComponents();
//		// 查询所有的服务器
//		//a.getMachines();
//		// 查询所有的服务组件
//		// a.getServerComponents();
//		// 查询所有的Library
//		// a.getLibraries();
//
//
//		// 查询所有的用户
//		//a.getPersons();
//		// 查询指定的用户
//		//a.getPerson("sasdemo");
//		//给指定用户添加组
//		//a.setUserGroup(a.getPerson("test"), a.getGroup("BI"));
//
//		//查询用户的Login账号
//		//a.getLogins();
//		// 查询指定用户的Login账号
//		 //a.getLogin("sasdemo");
//		// 修改指定login账号密码
//		// a.changeExtAcctPasswd("sasdemo","sfm1234");
//
//		//查询用户的内部账号
//		//a.getInterAccts();
//		//查询指定用户的内部账号
//		// a.getInterAcct("sasdemo");
//		// 修改指定用户的内部账号密码
//		//a.changeInterPasswd("sasdemo", "sfm1234");
//		//a.changeInterPasswd(a.getPerson("sasdemo"), "sfm1234");
//
//		// 查询所有的物理表
//		// a.getPhysicalTables();
//		// 查询所有的外部表
//		// a.getExternalTables();
//		// 查询所有的关联表
//		// a.getRelationalTables();
//		// 查询所有的文件
//		// a.getFiles();
//		// 查询所有的作业
//		// a.getJobs();
//		// 查询所有的逻辑服务器
//		// a.getLogicalServers();
//
//		// 通用查询:主类型对象
//		//a.getPrimaryTypes(MetadataObjects.PERMISSION);
//		// 通用查询:第二类型对象
//		// a.getSecondaryTypes(MetadataObjects.XPATH);
//
//		//获取所有的树
//		 //a.getTree();
//
//		//获取所有的用户文件夹
//		// a.getFolder();
//
//		// a.getTreeNode("User Folders/sasadm/My Folder");
//
//		 //创建文件夹
//		//a.createFolder("User Folders/foldertest/My Folder");
//
//		 //为文件夹添加用户或组
////		 List<MdPermissionInfo> permissionInfo = new ArrayList<MdPermissionInfo>();
////		permissionInfo.add(new MdPermissionInfoImpl(MdPermissionInfo.PermissionType.GRANT,MdPermissionInfo.PERMISSION_READMETADATA));
////		 permissionInfo.add(new MdPermissionInfoImpl(MdPermissionInfo.PermissionType.GRANT,MdPermissionInfo.PERMISSION_WRITEMETADATA));
////		 permissionInfo.add(new MdPermissionInfoImpl(MdPermissionInfo.PermissionType.GRANT,MdPermissionInfo.PERMISSION_CHECKINMETADATA));
////		a.addIdentity2Folder(a.getTreeNode("User Folders/foldertest/My Folder"),a.getGroup("testgroup"),permissionInfo);
//
//		//查询可添加的权限;
//		//a.getPermissions();
//		//获取部署的组件
//		//a.getDeployedComponents();
//
//		a.connection.closeOMRConnection();
//
//	}

}
