import com.zsb.exception.ZkException;
import com.zsb.zk.ZkPool;
import com.zsb.zk.ZkPoolFactory;


public class Test {
	public static void main(String args[]){
		try {
			ZkPool zkPool = ZkPoolFactory.getZkPool("node01:2181/test", "test", "test");
			
			System.out.println("end!!!");
		} catch (ZkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
