package mafia.view;

import java.util.Scanner;

import mafia.controller.MafiaController;

public class MafiaView {
	private Scanner sc;
	private MafiaController mfCtrl;
	
	public MafiaView() {
		sc=new Scanner(System.in);
		mfCtrl=new MafiaController();
	}
	
	public void mainMenu() {
		while(true) {
			System.out.println("====== 마피아 게임 ======");
			System.out.println("1. 게임 방법");
			System.out.println("2. 게임 시작");
			System.out.println("0. 게임 종료");
			System.out.print("=> ");
			int choice=sc.nextInt();
			
			switch(choice) {
			case 1:gameRules(); break;
			case 2:startGame(); break;
			case 0:
				System.out.println("게임을 종료합니다.");
				return;
			default:
				System.out.println("올바른 번호를 입력해주세요.");
			}
		}
	}
	
	// 게임 방법 설명
	public void gameRules() {
		while(true) {
			System.out.println("====== 게임 방법 ======");
			System.out.println("1. 기본 규칙");
			System.out.println("2. 마피아 역할");
			System.out.println("3. 경찰 역할");
			System.out.println("4. 의사 역할");
			System.out.println("5. 시민 역할");
			System.out.println("0. 뒤로 가기");
			System.out.print("=> ");
			int choice=sc.nextInt();
			
			switch(choice) {
			case 1: basicRules(); break;
			case 2: roleOfMafia(); break;
			case 3: roleOfPolice(); break;
			case 4: roleOfDoctor(); break;
			case 5: roleOfCitizen(); break;
			case 0: return;
			default:
				System.out.println("올바른 번호를 입력해주세요.");
			}
		}
		
	}
	
	// 게임 시작
	public void startGame() {
		while(!mfCtrl.endGame()) {
			mfCtrl.increaseDay();
			System.out.println();
			
			mfCtrl.nightTime();
			
			System.out.println();
			if(mfCtrl.endGame())
				break;
			
			mfCtrl.dayTime();
		}
		
		mfCtrl.printResult();
		mfCtrl=new MafiaController();
	}
	
	public void basicRules() {
		System.out.println("마피아 게임은 총 5인으로 시작합니다.");
		System.out.println("모든 사람은 1부터 5까지의 번호를 부여받습니다.");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("플레이어는 항상 1번입니다.");
		System.out.println("마피아 1명, 경찰 1명, 의사 1명, 시민 2명으로 구성됩니다.");
		System.out.println("플레이어는 마피아, 경찰, 의사, 시민 중 한 가지의 역할을 무작위로 배정받습니다.");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("마피아를 제외한 나머지는 모두 시민 팀입니다.");
		System.out.println("밤 => 낮 => 밤 => ... 순으로 게임이 진행됩니다.");
		System.out.println("밤에는 마피아가 시민 한명을 죽이며, 낮에는 모두가 마피아일 것 같은 사람을 투표합니다.(본인은 투표할 수 없음)");
		System.out.println("낮에 가장 많은 표를 받은 사람이 최후의 심판에 오르며, 투표가 동률일 경우 아무도 처형당하지 않고 밤으로 넘어갑니다.");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("최후의 심판에 오른 사람을 대상으로 찬반투표를 진행하며, 투표가 동률이거나 반대표가 더 많을 경우 처형하지 않습니다.");
		System.out.println("찬성표가 더 많을 경우 해당 사람을 처형합니다.");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("모든 투표는 익명으로 진행합니다.");
		System.out.println("플레이어를 제외한 컴퓨터가 하는 투표는 특별한 경우를 제외하고는 랜덤입니다.(ex.컴퓨터 경찰이 마피아를 찾아냈을 경우)");
		System.out.println("플레이어가 죽더라도 게임은 계속 진행됩니다.");
	}
	
	public void roleOfMafia() {
		System.out.println("마피아는 본인을 제외하고 남겨진 사람이 1명이하일 때 승리합니다.");
		System.out.println("마피아는 매일 밤마다 시민을 한명 살해할 수 있으며, 자살은 불가하고 죽은 사람도 선택할 수 없습니다.");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("플레이어가 마피아가 아닐 경우, 살해되는 사람은 무작위로 지정됩니다.(마피아 제외)");
		System.out.println("마피아가 선택한 사람과 의사가 선택한 사람이 같을 경우, 해당 사람은 살해당하지 않습니다.");
	}
	
	public void roleOfPolice() {
		System.out.println("경찰은 시민 팀 중 유일하게 마피아를 정확하게 아는 직업입니다.");
		System.out.println("경찰은 매일 밤마다 사람 한명을 골라 조사할 수 있습니다.");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("조사한 사람이 마피아일 경우, 'O번은 마피아가 맞습니다.'라고 뜹니다.");
		System.out.println("조사한 사람이 마피아가 아닐 경우, 'O번은 마피아가 아닙니다.'라고 뜹니다.");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("플레이어가 경찰이 아닐 경우(=컴퓨터 경찰), 조사되는 대상은 무작위로 지정됩니다.(본인 제외)");
		System.out.println("컴퓨터 경찰이 무작위로 지정한 대상이 마피아일경우, 다음날 낮에 컴퓨터 경찰이 'O번 조사 결과 마피아'라고 발언합니다.");
		System.out.println("그러나 이 경우, 마피아 또한 무작위 사람을 대상으로 'O번 조사 결과 마피아'라고 발언합니다.(컴퓨터 마피아일 경우)");
		System.out.println();
		mfCtrl.delayTime(2500);
		System.out.println("마피아를 찾았을 경우, 컴퓨터 경찰은 무조건 마피아를 투표합니다.");
		System.out.println("마피아를 찾았어도, 해당 밤에 경찰이 죽었을 경우 조사 결과는 뜨지 않습니다.");
	}
	
	public void roleOfDoctor() {
		System.out.println("의사는 밤마다 한 사람을 지정하여 치료합니다.(본인 치료는 불가)");
		System.out.println("의사가 치료한 사람과 마피아가 살해한 사람이 같을 경우, 당사자는 죽지 않으며 다음날 낮에 'O번이 의사의 치료를 받고 살아났습니다!'라고 뜹니다.");
		System.out.println("플레이어가 의사가 아닐 경우(=컴퓨터 의사), 치료되는 사람은 무작위로 지정됩니다.");	
		System.out.println();
		mfCtrl.delayTime(2500);
	}
	
	public void roleOfCitizen() {
		System.out.println("시민은 특별한 능력이 없습니다.");
		System.out.println("추리력을 발휘하여 마피아를 투표하고 시민 팀을 승리로 이끄세요!");
		System.out.println();
		mfCtrl.delayTime(2500);
	}
	

}
