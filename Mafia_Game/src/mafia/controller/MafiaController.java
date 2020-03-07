package mafia.controller;

import java.util.Random;
import java.util.Scanner;

import mafia.model.dao.MafiaDao;
import mafia.model.vo.Citizen;
public class MafiaController {
	private MafiaDao mafiaDao;
	private Scanner sc;
	private Random rand;
	
	private int oneDayNum;  // 몇 번째 day
	
	private int playerRole; // 플레이어의 역할
	private int mafiaNum;	// 마피아인 사람 번호
	private int policeNum;	// 경찰인 사람 번호
	private int doctorNum;	// 의사인 사람 번호 
	
	private int killNum;	// 해당 밤의 살해 대상
	private int cureNum;	// 해당 밤의 치료 대상
	private int investNum;	// 해당 밤의 조사 대상
	private boolean investMafia;	// 조사 결과 마피아이면 true
	private boolean tellMafia;		// Mafia라고 말했으면 true 
	
	private int voteCtz;	// 최종 투표대상자
	
	public MafiaController() {
		sc=new Scanner(System.in);
		rand=new Random();
		mafiaDao=new MafiaDao();
		
		playerRole=mafiaDao.getCitizen(0).getRoleNum();		
		
		
		oneDayNum=0; 
		investMafia=false;	
		killNum=-1;			
		cureNum=-1;			
		investNum=-1;	
		voteCtz=-1;
		tellMafia=false;
		
		findRole();
	}
	
	public void findRole() {
		for(int ctzNum=0;ctzNum<5;ctzNum++) {
			int roleNum=mafiaDao.getCitizen(ctzNum).getRoleNum();
			if(roleNum==0)
				mafiaNum=ctzNum;
			else if(roleNum==1)
				policeNum=ctzNum;
			else if(roleNum==2)
				doctorNum=ctzNum;
		}
	}
	
	public void nightTime() {
		
		if(oneDayNum==1) {
			switch(playerRole) {
			case 0:
				System.out.println("당신은 마피아입니다."); break;
			case 1:
				System.out.println("당신은 경찰입니다."); break;
			case 2:
				System.out.println("당신은 의사입니다."); break;
			case 3:
			case 4:
				System.out.println("당신은 시민입니다."); break;
			}
			System.out.println("당신은 1번입니다.");
		}
		
		System.out.println(oneDayNum+"번째 밤입니다.");
		
		delayTime(2000);
		
		printAlive();
		turnOfMafia();
		turnOfPolice();
		turnOfDoctor();
		
		delayTime(2000);
	}
	
	public void dayTime() {
		System.out.println(oneDayNum+"번째 낮이 밝았습니다.");
		// 의사가 힐했을때 or 마피아가 죽였을 때
		cureOrKill();
		
		// 경찰이 찾아냈을 때
		investOrNot();
		
		printAlive();
		
		delayTime(2000);
		
		// 투표 시간
		if(voteTime()) {
			yesOrNo();	// 찬반 투표
		}
		
		resetData();
	}
	
	public void increaseDay() {
		oneDayNum++;
	}
	
	public void turnOfMafia() {
		int choice;
		
		// 플레이어가 마피아일 경우
		if(playerRole==0) {
			System.out.print("제거할 사람을 선택하세요 : ");
			choice=sc.nextInt();
			
			// 범위에서 벗어나거나, 죽은 사람이거나, 자기 자신인 경우 
			while(choice<1||choice>5||choice==1||!mafiaDao.getCitizen(choice-1).isAlive()) {
				System.out.print("잘못된 대상입니다. 다시 선택해주세요 : ");
				choice=sc.nextInt();
			}
			
			System.out.println(choice+"번을 선택하였습니다.");
			killNum=choice-1;
		}
		
		// 플레이어가 마피아가 아닐 경우
		else {
			choice=rand.nextInt(5);
			// 범위에서 벗어나거나, 죽은 사람이거나, 자기 자신인 경우 
			while(choice<0||choice>4||choice==mafiaNum||!mafiaDao.getCitizen(choice).isAlive()) {
				choice=rand.nextInt(5);
			}
			killNum=choice;
		}
		
	}
	
	public void turnOfPolice() {
		
		// 경찰이 살아있을 동안
		if(mafiaDao.getCitizen(policeNum).isAlive()==true) {
			int choice;
			int roleNum;
			
			//플레이어가 경찰일 경우
			if(playerRole==1) {
				System.out.print("조사할 사람을 선택하세요 : ");
				choice=sc.nextInt();
				
				// 범위에서 벗어나거나, 죽은 사람이거나, 자기 자신인 경우 
				while(choice<1||choice>5||choice==1||!mafiaDao.getCitizen(choice-1).isAlive()) {
					System.out.print("잘못된 대상입니다. 다시 선택해주세요 : ");
					choice=sc.nextInt();
				}
				
				roleNum=mafiaDao.investCitizen(choice-1);
				if(roleNum==0) {
					System.out.println(choice+"번은 마피아가 맞습니다.");
					investMafia=true;
				}
				else
					System.out.println(choice+"번은 마피아가 아닙니다.");
				investNum=choice-1;
			}
			
			//플레이어가 경찰이 아닐 경우
			else {
				choice=rand.nextInt(5);
				
				// 범위에서 벗어나거나, 죽은 사람이거나, 자기 자신인 경우 
				while(choice<0||choice>4||choice==policeNum||!mafiaDao.getCitizen(choice).isAlive()) {
					choice=rand.nextInt(5);
				}
				
				roleNum=mafiaDao.investCitizen(choice);
				investNum=choice;
				if(roleNum==0)
					investMafia=true;
			}

		}
	}
	
	public void turnOfDoctor() {
		
		// 의사가 살아있을 때 
		if(mafiaDao.getCitizen(doctorNum).isAlive()) {
			int choice;
			// 플레이어가 의사일 경우
			if(playerRole==2) {
				System.out.print("치료할 사람을 선택하세요 : ");
				choice=sc.nextInt();
				
				// 범위에서 벗어나거나, 죽은 사람이거나, 자기 자신인 경우 
				while(choice<1||choice>5||choice==1||!mafiaDao.getCitizen(choice-1).isAlive()) {
					System.out.print("잘못된 대상입니다. 다시 선택해주세요 : ");
					choice=sc.nextInt();
				}
							
							
				mafiaDao.cureCitizen(choice-1);
				System.out.println(choice+"번을 선택하였습니다.");
				cureNum=choice-1;
			}
			
			// 플레이어가 의사가 아닐 경우
			else {
				choice=rand.nextInt(5);
				
				// 범위에서 벗어나거나, 죽은 사람이거나, 자기 자신인 경우 
				while(choice<1||choice>4||choice==doctorNum||!mafiaDao.getCitizen(choice).isAlive()) {
					choice=rand.nextInt(5);
				}
							
				mafiaDao.cureCitizen(choice);
				cureNum=choice;
			}

		}
		
	}

	public void cureOrKill() {
		// 의사가 힐했을때 or 마피아가 죽였을 때
		if(cureNum==killNum)
			System.out.println(cureNum+1+"번이 의사의 치료를 받고 살아났습니다!");
		else {
			mafiaDao.killCitizen(killNum);
			System.out.println(killNum+1+"번이 살해당했습니다.");
		}
	}
	
	public void investOrNot() {
		// 경찰이 찾아냈을 때
		if(investMafia&&!tellMafia) {
			int sequence=rand.nextInt(2);
			
			if(sequence==0) {
				System.out.println((policeNum+1)+" : "+(investNum+1)+"번 조사 결과 마피아");
				System.out.println((mafiaNum+1)+" : "+(policeNum+1)+"번 조사 결과 마피아");
			}
			else{
				System.out.println((mafiaNum+1)+" : "+(policeNum+1)+"번 조사 결과 마피아");
				System.out.println((policeNum+1)+" : "+(investNum+1)+"번 조사 결과 마피아");
			}
			tellMafia=true;
		}
	}
	
	public boolean voteTime() {
		
		int[] voteArr= {0,0,0,0,0};
		
		System.out.println("투표 시간입니다.");
		int choice;
		
		// 플레이어가 살아있을 때
		if(mafiaDao.getCitizen(0).isAlive()) {
			System.out.print("투표할 사람을 선택해 주세요 : ");
			choice=sc.nextInt();
			
			while(choice<2||choice>5||!mafiaDao.getCitizen(choice-1).isAlive()) {
				System.out.print("잘못된 번호입니다. 다시 선택해주세요 : ");
				choice=sc.nextInt();
			}
			
			voteArr[choice-1]++;
		}
		
		for(int ctzNum=1;ctzNum<5;ctzNum++) {
			// 살아있는 사람들만 투표 
			if(mafiaDao.getCitizen(ctzNum).isAlive()) {
				// 만약 경찰이 마피아를 찾아냈더라면
				if(ctzNum==policeNum) {
					if(investMafia) {
						voteArr[mafiaNum]++;
						continue;
					}
				}
				
				choice=rand.nextInt(5);
				
				// 만약 투표한 사람이 살아있지 않거나, 자기자신을 투표했을 경우 
				while(!mafiaDao.getCitizen(choice).isAlive()||choice==ctzNum) {
					choice=rand.nextInt(5);
				}
				
				voteArr[choice]++;
			}
		}
				
		// 최대 값 찾기
		int maxNum=0;
		for(int index=1;index<voteArr.length;index++) {
			if(voteArr[maxNum]<voteArr[index])
				maxNum=index;
		}
		
		// 동률의 투표 있는지 확인 
		int count=0;
		for(int index=0;index<voteArr.length;index++) {
			if(voteArr[maxNum]==voteArr[index])
				count++;
		}
		
		delayTime(2000);
		
		// 투표 결과 출력
		int countPeople=1;
		for(int vote:voteArr) {
			System.out.println(countPeople+" : "+vote+" ");
			countPeople++;
		}
		
		if(count<2) {	// 1이면 자기 자신밖에 같은게 없음 
			voteCtz=maxNum;
			System.out.println((voteCtz+1)+"번이 "+voteArr[voteCtz]+"표로 가장 많은 표를 얻어 찬반투표를 진행합니다.");
			delayTime(2000);
			return true;
		}
		else {
			System.out.println("투표 결과 최대 득표수가 동률이므로, 찬반투표를 진행하지 않습니다.");
			delayTime(2000);
			return false;
		}
	
	}
	
	public void yesOrNo() {
		int[] voteArr= {-1,-1,-1,-1,-1};
		
		System.out.println((voteCtz+1)+"번의 찬반 투표 시간입니다.");
		int choice;
		
		// 플레이어가 살아있을 때
		if(mafiaDao.getCitizen(0).isAlive()) {
			System.out.print((voteCtz+1)+"번을 처형에 찬성하면 1, 아니면 0을 입력해주세요 : ");
			choice=sc.nextInt();
			
			while(choice!=1&&choice!=0) {
				System.out.print("잘못된 번호입니다. 다시 선택해주세요 : ");
				choice=sc.nextInt();
			}
			
			voteArr[0]=choice;
		}
		
		for(int ctzNum=1;ctzNum<5;ctzNum++) {
			// 살아있는 사람들만 투표 
			if(mafiaDao.getCitizen(ctzNum).isAlive()) {
				if(ctzNum==policeNum&&investMafia&&voteCtz==mafiaNum) {
						voteArr[ctzNum]=1;
						continue;
					}
				
				if(ctzNum==voteCtz) {
					voteArr[ctzNum]=0;
					continue;
				}
				
				choice=rand.nextInt(2);
				voteArr[ctzNum]=choice;
			}
		}
		
		// 찬성표와 반대 표 세기
		int yes=0;
		int no=0;
		for(int index=0;index<voteArr.length;index++) {
			if(voteArr[index]==0)
				no++;
			else if(voteArr[index]==1)
				yes++;
		}
		
		
		delayTime(2000);
		
		// 결과 발표
		if(yes>no) {
			System.out.println("찬성표가 "+yes+"표, 반대표가 "+no+"표이므로 처형합니다.");
			mafiaDao.killCitizen(voteCtz);
		}
		else {
			System.out.println("찬성표가 "+yes+"표, 반대표가 "+no+"표이므로 처형이 부결됩니다.");
		}
		
		delayTime(2000);
		
	}
	
	public boolean endGame() {
		// 마피아가 죽었을 때
		if(!mafiaDao.getCitizen(mafiaNum).isAlive()) {
			System.out.println("마피아가 죽었습니다! 시민이 승리하였습니다!");
			return true;
		}
		
		// 시민이 1명 이하로 살아남았을 때
		int aliveCount=0;
		for(int ctzNum=0;ctzNum<5;ctzNum++) {
			if(mafiaDao.getCitizen(ctzNum).isAlive())
				aliveCount++;
		}
		
		if(aliveCount<=2) {
			System.out.println("마피아가 살아남았습니다! 마피아가 승리하였습니다!");
			return true;
		}
		
		return false;
	}
	
	public void printAlive() {
		System.out.println("=== 현재 살아남은 사람 ===");
		for(int ctzNum=0;ctzNum<5;ctzNum++) {
			if(mafiaDao.getCitizen(ctzNum).isAlive())
				System.out.print("["+(ctzNum+1)+ "] ");
		}
		System.out.println();
	}
	
	public void printResult() {
		System.out.println();
		System.out.println("====== 결과 ======");
		System.out.println("게임 시간 : "+oneDayNum+"일");
		int role;
		for(int i=0;i<5;i++) {
			role=mafiaDao.getCitizen(i).getRoleNum();
			
			switch(role) {
			case 0:
				System.out.println((i+1)+"번 : 마피아");
				break;
			case 1:
				System.out.println((i+1)+"번 : 경찰");
				break;
			case 2:
				System.out.println((i+1)+"번 : 의사");
				break;
			case 3:
			case 4:
				System.out.println((i+1)+"번 : 시민");
				break;
			}
		}
		System.out.println();
	}
	
	public void delayTime(int time) {
		try {Thread.sleep(time);}
		catch(InterruptedException e) {e.printStackTrace();}
	}
	
	public void resetData() {
		killNum=-1;	// 해당 밤의 살해 대상
		cureNum=-1;	// 해당 밤의 치료 대상
		investNum=-1;	// 해당 밤의 조사 대상
		voteCtz=-1;	// 최종 투표대상자
	}
}
