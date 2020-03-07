package mafia.model.dao;

import java.util.ArrayList;
import java.util.Random;

import mafia.model.vo.Citizen;

public class MafiaDao {
	private Citizen[] ctzList;
	private Random rand;
	
	public MafiaDao(){
		ctzList=new Citizen[5];
		rand=new Random();
		generateCitizen();
	}
	
	public void generateCitizen() {
		for(int ctzNum=0;ctzNum<5;ctzNum++) {
			ctzList[ctzNum]=new Citizen();
			int roleNum=rand.nextInt(5);
			
			for(int j=0;j<ctzNum;j++) {
				if(roleNum==ctzList[j].getRoleNum()) {
					roleNum=rand.nextInt(5);
					j=-1;
				}
			}
			ctzList[ctzNum].setRoleNum(roleNum);
		}
		
	}
	
	public void killCitizen(int ctzNum) {
		ctzList[ctzNum].setAlive(false);
	}
	
	public int investCitizen(int ctzNum) {
		return ctzList[ctzNum].getRoleNum();
	}
	
	public void cureCitizen(int ctzNum) {
		ctzList[ctzNum].setCured(true);
	}
	
	public Citizen getCitizen(int ctzNum) {
		return ctzList[ctzNum];
	}
}
