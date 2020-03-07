package mafia.model.vo;

import mafia.model.dao.MafiaDao;

public class Citizen {
	protected int roleNum; // 0 마피아, 1 경찰, 2 의사, 3/4 시민
	protected boolean isAlive; // 0 사망, 1 생존
	protected boolean isCured; // 0 치료x, 1 치료o

	public Citizen() {
		isAlive = true;
		isCured = false;
		roleNum=-1;
	}

	public int getRoleNum() { return roleNum;}
	public void setRoleNum(int roleNum) { 
		
		this.roleNum = roleNum;}

	public boolean isAlive() { return isAlive;}
	public void setAlive(boolean isAlive) { this.isAlive = isAlive;}
	
	public boolean isCured() { return isCured;}
	public void setCured(boolean isCured) { this.isCured = isCured;}

}
