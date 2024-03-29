package org.network.gamecore;

import org.network.gamecompnent.GameManager;

import javax.swing.*;

public class GameThread extends Thread{
    private final GameCanvas target;
    private final JFrame parent;

    private static int cnt = 0;//루프 제어용 컨트롤 변수
    private final int delay = 17;//루프 딜레이. 1/1000초 단위.
    private long preTime;//루프 간격을 조절하기 위한 시간 체크값
    private static boolean isFocusable = true;
    public GameThread(JFrame parent, GameCanvas target){
        this.parent = parent;
        this.target = target;
        new GameManager();
    }

    @Override
    public void run() {
        while (true){
            preTime=System.currentTimeMillis();
            if (isFocusable){
                parent.requestFocus();
            }
            Update();//키 처리
            target.repaint();//화면 리페인트
            //프레임 처리
            if(System.currentTimeMillis()-preTime<delay) {
                try {
                    Thread.sleep(delay-System.currentTimeMillis()+preTime);
                } catch (InterruptedException e) {
                    //ignored
                }
            }
            cnt++;
        }
    }
    private void Update() {
        GameObject.updateGameObjects();
        Input.clearInputDown();//한 프레임 인식용 키입력 버퍼 비우기
    }
    public static int getCnt() {
        return cnt;
    }

    public static void setIsFocusable(boolean enable) {
        isFocusable = enable;
        Input.clearInput();
    }
}
