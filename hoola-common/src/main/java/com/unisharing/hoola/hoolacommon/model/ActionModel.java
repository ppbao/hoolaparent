package com.unisharing.hoola.hoolacommon.model;

public class ActionModel {
	
	private long uid;
	private int action;
	private long cid;
	
	public static enum Action {
		CREATE(0),//创建
		FORWARD(1),	//转发
		LIKE(2), //喜欢
		RECOMMEND(3), //推荐
		COMMENT(4),//评论
		DISLIKE(5),//不喜欢
		SHARE(7),//分享
		AT(6);//@
		int mark;
		private Action(int mark) {
			this.mark = mark;
		}
		public int getMark() {
			return this.mark;
		}
		
	}

	public long getUid() {
		return uid;
	}

	public int getAction() {
		return action;
	}

	public long getCid() {
		return cid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public ActionModel(long uid, int action, long cid) {
		this.uid = uid;
		this.action = action;
		this.cid = cid;
	}
	
	@Override
	public String toString(){
		return uid+":"+action+":"+cid;
	}
	
	public ActionModel() {
		super();
	}

	public static ActionModel parse(String str){
		String[] params = str.split(":");
		if (params.length<3){
			return null;
		}else{
			ActionModel member = new ActionModel();
			member.setUid(Long.valueOf(params[0]));
			member.setAction(Integer.parseInt(params[1]));
			member.setCid(Long.valueOf(params[2]));
			return member;
		}
	}
	
}