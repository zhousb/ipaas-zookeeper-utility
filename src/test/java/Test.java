
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import com.google.gson.Gson;
import com.zsb.consts.Consts;
import com.zsb.exception.ZkException;
import com.zsb.lock.DistributedLock;
import com.zsb.model.ZkClient;
import com.zsb.utils.ZookeeperUtils;
import com.zsb.zk.ZkPool;
import com.zsb.zk.ZkPoolFactory;


public class Test {
	private static final Logger LOG = Logger.getLogger(Test.class);
	public static void main(String args[]){
		try {
			//testZkClient();
			//testZkPoolFc();
			//testLock();
			//testWatcher();
			testAcl();
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
			
			client.createNode("/test", "ddd");
			
			if(true){
				return;
			}
			
			
			InterProcessLock lock = client.getInterProcessLock("/bmcConfCenter");
			lock.acquire();
			
			boolean f = client.exists("/bmcConfCenter");
			List<String> cds = client.getChildren("/bmcConfCenter");
			Gson gson = new Gson();
			System.out.println(gson.toJson(cds));
			System.out.println(client.getNodeData("/bmcConfCenter"));
			//client.createNode("/bmcConfCenter/test","datass");
			System.out.println("end!!!!!");
			System.out.println(f);
			lock.release();
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
	
	public static void testLock() throws ZkException{
		DistributedLock lock = new DistributedLock("node01:2181", "test1", "test123", "serviceid", "bis");
		lock.lock();
		System.out.println("3333333");
		DistributedLock lock2 = new DistributedLock("node01:2181", "test1", "test123", "serviceid", "bis");
		lock2.lock();
		System.out.println("4444444");
		DistributedLock lock3 = new DistributedLock("node01:2181", "test1", "test123", "serviceid", "bis");
		lock3.lock();
		
		DistributedLock lock11 = new DistributedLock("node01:2181", "test1", "test123", "serviceid", "bis111");
		lock11.lock();
		
		DistributedLock lock22 = new DistributedLock("node01:2181", "test1", "test123", "serviceid", "bis111");
		lock22.lock();
		
		DistributedLock lock33 = new DistributedLock("node01:2181", "test1", "test123", "serviceid", "bis111");
		lock33.lock();
		
		
		
		
		try {
			Thread.sleep(30000);
			
			try {
				lock.releaseLock();
				
				lock2.releaseLock();
				
				lock3.releaseLock();
				
				lock11.releaseLock();
				lock22.releaseLock();
				lock33.releaseLock();
				
				
			} catch (ZkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testWatcher() throws Exception{
		//ZkPool pool1 = ZkPoolFactory.getZkPool("node01:2181","test1","pwd");
		//System.out.println("111111111");
		//ZkClient client1 = pool1.getZkClient("node01:2181","test1");
		LOG.debug("test.....");
		LOG.info("test.....");
		ZkPool pool = ZkPoolFactory.getZkPool("node01:2181","test2","pwd");
		System.out.println("111111111");
		ZkClient client = pool.getZkClient("node01:2181","test2");
		if(!client.exists("/test2/t")){
			client.createNode("/test2/t", "ddd2");
		}
		
		//System.out.println(client1.getNodeData("/test2"));
		
		client.exists("/test2", new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				System.out.println("wwwwwwwwwwwwwwwwwww");
			}
			
		});
		
		//client.getNodeData("/test2",true);
		//client.setNodeData("/test2/t", "set2222");
		client.deleteNode("/test2/t");
		
	}
	
	public static void testAcl() throws Exception{
		List<ACL> acls = ZookeeperUtils.getAcls(Consts.ACL_DIGEST, "test", "test123", ZooDefs.Perms.ALL);
		
		ZkPool pool = ZkPoolFactory.getZkPool("node01:2181","test2","pwd","serviceId");
		System.out.println("111111111");
		ZkClient client = pool.getZkClient("node01:2181","test2","serviceId");
		//client.createNode("/test5", acls, "", CreateMode.PERSISTENT);
		
		client.deleteNode("/test5");
	}
	
}
