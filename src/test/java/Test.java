import com.zsb.exception.ZkException;
import com.zsb.zk.ZkPoolFactory;


public class Test {
	public static void main(String args[]){
		try {
			ZkPoolFactory.getZkPool("node01:2181/test", "test", "test");
		} catch (ZkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
