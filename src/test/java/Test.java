
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import com.google.gson.Gson;
import com.zsb.exception.ZkException;
import com.zsb.model.ZkClient;
import com.zsb.zk.ZkPool;
import com.zsb.zk.ZkPoolFactory;


public class Test {
	private static final Logger LOG = Logger.getLogger(Test.class);
	public static void main(String args[]){
		try {
			//testZkClient();
			testZkPoolFc();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void testZkClient(){
		try {
			LOG.debug("test.....");
			LOG.info("test.....");
			ZkClient client = new ZkClient("node01:2181", 2000);
			System.out.println("111111111");
			boolean f = client.exists("/bmcConfCenter");
			List<String> cds = client.getChildren("/bmcConfCenter");
			Gson gson = new Gson();
			System.out.println(gson.toJson(cds));
			System.out.println(client.getNodeData("/bmcConfCenter"));
			client.createNode("/bmcConfCenter/test","datass");
			System.out.println("end!!!!!");
			System.out.println(f);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void testZkPoolFc() throws Exception{
		ZkPool po = ZkPoolFactory.getZkPool("node01:2181", "test", "test");
		ZkClient cl = po.getZkClient("node01:2181", "test");
		//System.out.println(cl.exists("/bmcConfCenter"));
	
		/*****/
		List<ACL> acls = new ArrayList<ACL>(2);     
		  
		Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));  
		ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);  
		  
		Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("guest:guest123"));  
		ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);  
		  
		acls.add(acl1);  
		acls.add(acl2);  
		//ZkPool po = ZkPoolFactory.getZkPool("node01:2181", "test", "test");
		//cl.createNode("/bmcConfCenter/test1", acls, "test1111", CreateMode.PERSISTENT);
		
		
		ZkPool po2 = ZkPoolFactory.getZkPool("node01:2181", "admin", "admin123");
		ZkClient cl2 = po2.getZkClient("node01:2181", "admin");
		InterProcessLock lock = cl2.getInterProcessLock("/bmcConfCenter/test1");
		lock.acquire();
		System.out.println(cl2.getNodeData("/bmcConfCenter/test1"));
		Thread.sleep(5000);
		lock.release();
		System.out.println("relase client lock");
		//ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 10000, new DefaultWatcher());  
		//zk.create("/test", new byte[0], acls, CreateMode.PERSISTENT);  
		ZkPool po1 = ZkPoolFactory.getZkPool("node01:2181", "admin", "admin123");
		ZkClient cl1 = po1.getZkClient("node01:2181", "admin");
		System.out.println(cl1.getNodeData("/bmcConfCenter/test1"));
	}
}
