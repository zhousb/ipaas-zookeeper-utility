package com.zsb.exception;

/**
 * Date: 2016年5月28日 <br>
 * @author zhoushanbin
 */
public class ZkException extends Exception{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5346002928606325814L;
	
	private String errorCode;
	
	private String errorMsg;
	
	
	public ZkException(String errorMsg){
		super(errorMsg);
		this.errorMsg = errorMsg;
		
	}
	
	public ZkException(String errorCode,String errorMsg){
		super("errorCode:"+errorCode+" errorMsg:"+errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		
	}
	
	public ZkException(String errorMsg,Throwable thro){
		super(errorMsg,thro);
		this.errorMsg = errorMsg;
		
	}
	
	public ZkException(String errorCode,String errorMsg,Throwable thro){
		super("errorCode:"+errorCode+" errorMsg:"+errorMsg,thro);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
