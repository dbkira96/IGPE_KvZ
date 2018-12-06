package gameManager;

import java.awt.event.KeyEvent;

public enum Action {
	
	PLAYER_JUMP(KeyEvent.VK_W),
	PLAYER_ATTACK(KeyEvent.VK_F),
	PLAYER_MOVE_RIGHT(KeyEvent.VK_D),
	PLAYER_MOVE_LEFT(KeyEvent.VK_A),
	PLAYER_CROUCH(KeyEvent.VK_S),
	PLAYER_MOVE_REST,
	PLAYER_STAND,
	
	PLAYER2_JUMP(KeyEvent.VK_UP),
	PLAYER2_ATTACK(KeyEvent.VK_M),
	PLAYER2_MOVE_RIGHT(KeyEvent.VK_RIGHT),
	PLAYER2_MOVE_LEFT(KeyEvent.VK_LEFT),
	PLAYER2_CROUCH(KeyEvent.VK_DOWN),
	PLAYER2_MOVE_REST,
	PLAYER2_STAND,
	
	SELECT_MENU(KeyEvent.VK_ENTER),
	START_MULTIPLAYER_GAME, 
	OPEN_SETTING, 
	START_TRAINING,
	CLOSE_GAME,
	START_GAME,
	PAUSE(KeyEvent.VK_ESCAPE),
	RESUME,
	CHOOSE_PLAYER_MULTIPLAYER,
	START_LOCAL_GAME,
	CREAPARTITA,
	PARTECIPA,
	BACKTOMENU,
	MUTE(KeyEvent.VK_0),
	PLAYER_CHOOSED_MULTIPLAYER, MENU_START_LOCAL_GAME, MENU_START_MULTIPLAYER_GAME, MENU_CLOSE_GAME, GAMEOVER, OPEN_MENU; 
	
	public int key;
	
	Action(int k){
		key=k;
	}
	Action(){
		key=-1;
	}
	
}
