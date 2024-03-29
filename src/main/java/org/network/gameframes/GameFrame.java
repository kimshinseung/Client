package org.network.gameframes;

import org.network.UserData;
import org.network.UserSocket;
import org.network.WindowConfig;
import org.network.data.PocketMonData;
import org.network.gamecore.*;
import org.network.packet.ChoosePocketPacket;
import org.network.packet.UserBattlePacket;
import org.network.packet.UserChatPacket;
import org.network.packet.UserListPacket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import java.awt.event.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GameFrame extends JFrame implements ListSelectionListener {
    private static GameFrame current = null;
    private static JPanel choosePocketForBattlePanel;// 처음시작 포켓몬 패널
    private static DefaultListModel model;
    private static JTextPane textArea;
    //전투 ui 정보
    //private List<Integer> args= new ArrayList<>();
    private static JPanel firstPocketMonSelectPanel;
    private JLayeredPane gameLayer = new JLayeredPane();//게임 패널
    //private JSplitPane gameFrameMainPanel = new JSplitPane();
    private JSplitPane gameServerInfoPanel = new JSplitPane();
    private JList<String> userList;
    private JPanel userListPanel = new JPanel();//유저 리스트 패널
    private JPanel userChatPanel = new JPanel();//유저 채팅 패널
    //배틀 버튼 리스트
    private JButton[] jbt;
    private GameCanvas gameCanvas;//게임 캔버스
    private GameThread gameThread;
    private JTextField txtInput;

    private JButton btnSend;
    private JLabel lblUserName;
    private JButton firi;
    private JButton ggobugi;
    private JButton ggomadol;
    private JButton gorapaduk;
    private JButton isanghessi;
    private JButton pikachu;
    private JButton complete;
    private List<Integer> pocketMonList= new ArrayList<>();

    private JButton img1,img2,img3;
    //배틀 컨트롤 프레임 생성
    private BattleControlFrame battleControlFrame;
    private OkOrNoFrame okOrNoFrame;
    private JLabel explain;
    private String currentSelectedUser = "-ALL-";
    public GameFrame(){
        new Music("music/lobby.wav",true).start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setResizable(false);
        gameLayer.setBackground(Color.CYAN);
        gameLayer.setSize(WindowConfig.WIDTH,WindowConfig.HEIGHT);
        setContentPane(gameLayer);

        //상하단 나누기 패널
        gameServerInfoPanel = new JSplitPane();
        gameServerInfoPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
        gameServerInfoPanel.setDividerLocation((WindowConfig.HEIGHT/4));
        gameServerInfoPanel.setTopComponent(userListPanel);
        gameServerInfoPanel.setBottomComponent(userChatPanel);
        gameServerInfoPanel.setSize(WindowConfig.WIDTH/3,WindowConfig.HEIGHT);
        gameServerInfoPanel.setLocation(gameLayer.getWidth()-gameServerInfoPanel.getWidth(),0);
        gameServerInfoPanel.setEnabled(false);
        gameLayer.add(gameServerInfoPanel,JLayeredPane.POPUP_LAYER);
        //gameFrameMainPanel.setRightComponent(gameServerInfoPanel);

        userListPanel.setBackground(Color.white);
        userListPanel();
        userChatPanel.setBackground(Color.white);
        initUserChatPanel();
        initSelectFirstPocketMonPanel();
        initChoosePocketForBattlePanel();
        battleControlFrame = new BattleControlFrame();
        gameLayer.add(battleControlFrame,JLayeredPane.DEFAULT_LAYER);
        okOrNoFrame = new OkOrNoFrame();
        gameLayer.add(okOrNoFrame,JLayeredPane.DRAG_LAYER);

        //gameFrameMainPanel.setLeftComponent(gameLayer);
        //gameLayer.add(new BattleControlFrame(),JLayeredPane.DEFAULT_LAYER);
        gameCanvas = new GameCanvas(this);
        gameCanvas.setBounds(0,0,gameLayer.getWidth(),gameLayer.getHeight());
        gameLayer.add(gameCanvas,JLayeredPane.FRAME_CONTENT_LAYER);

        gameCanvas.repaint();
        setVisible(true);

        gameThread = new GameThread(this,gameCanvas);
        gameThread.start();
        //게임 실행 부
        addKeyListener(new GameInputKeyListener());
    }
    //싱글턴
    public static synchronized GameFrame getInstance(){
        if (current==null){
            current = new GameFrame();
        }
        return current;
    }
    private void initSelectFirstPocketMonPanel(){
        firstPocketMonSelectPanel = new JPanel();
        firstPocketMonSelectPanel.setBackground(Color.WHITE);
        firstPocketMonSelectPanel.setSize(WindowConfig.WIDTH/2,450);
        firstPocketMonSelectPanel.setLocation(100,100);
        firstPocketMonSelectPanel.setLayout(null);

        ImageIcon f= new ImageIcon(getClass().getResource("/pocketmon/firi-front.png"));
        Image f1=f.getImage();
        Image f2=f1.getScaledInstance(250,250,Image.SCALE_SMOOTH);
        ImageIcon f3=new ImageIcon(f2);
        firi=new JButton(f3);
        firi.setBackground(Color.WHITE);
        firi.setBorder(BorderFactory.createEmptyBorder());
        firi.setBounds(firstPocketMonSelectPanel.getX()-90, firstPocketMonSelectPanel.getY()-100, 200,200);
        firstPocketMonSelectPanel.add(firi);

        ImageIcon g= new ImageIcon(getClass().getResource("/pocketmon/ggobugi-front.png"));
        Image g1=g.getImage();
        Image g2=g1.getScaledInstance(250,250,Image.SCALE_SMOOTH);
        ImageIcon g3=new ImageIcon(g2);
        ggobugi=new JButton(g3);
        ggobugi.setBackground(Color.WHITE);
        ggobugi.setBorder(BorderFactory.createEmptyBorder());
        ggobugi.setBounds(firstPocketMonSelectPanel.getX()+120, firstPocketMonSelectPanel.getY()-100, 200,200);
        firstPocketMonSelectPanel.add(ggobugi);

        ImageIcon gg= new ImageIcon(getClass().getResource("/pocketmon/ggomadol-front.png"));
        Image gg1=gg.getImage();
        Image gg2=gg1.getScaledInstance(250,250,Image.SCALE_SMOOTH);
        ImageIcon gg3=new ImageIcon(gg2);
        ggomadol=new JButton(gg3);
        ggomadol.setBackground(Color.WHITE);
        ggomadol.setBorder(BorderFactory.createEmptyBorder());
        ggomadol.setBounds(firstPocketMonSelectPanel.getX()+330, firstPocketMonSelectPanel.getY()-100, 200,200);
        firstPocketMonSelectPanel.add(ggomadol);

        ImageIcon go= new ImageIcon(getClass().getResource("/pocketmon/gorapaduk-front.png"));
        Image go1=go.getImage();
        Image go2=go1.getScaledInstance(250,250,Image.SCALE_SMOOTH);
        ImageIcon go3=new ImageIcon(go2);
        gorapaduk=new JButton(go3);
        gorapaduk.setBackground(Color.WHITE);
        gorapaduk.setBorder(BorderFactory.createEmptyBorder());
        gorapaduk.setBounds(firstPocketMonSelectPanel.getX()-90, firstPocketMonSelectPanel.getY()+100, 200,200);
        firstPocketMonSelectPanel.add(gorapaduk);

        ImageIcon is= new ImageIcon(getClass().getResource("/pocketmon/isanghessi-front.png"));
        Image is1=is.getImage();
        Image is2=is1.getScaledInstance(250,250,Image.SCALE_SMOOTH);
        ImageIcon is3=new ImageIcon(is2);
        isanghessi=new JButton(is3);
        isanghessi.setBackground(Color.WHITE);
        isanghessi.setBorder(BorderFactory.createEmptyBorder());
        isanghessi.setBounds(firstPocketMonSelectPanel.getX()+120, firstPocketMonSelectPanel.getY()+100, 200,200);
        firstPocketMonSelectPanel.add(isanghessi);

        ImageIcon p= new ImageIcon(getClass().getResource("/pocketmon/pikachu-front.png"));
        Image p1=p.getImage();
        Image p2=p1.getScaledInstance(250,250,Image.SCALE_SMOOTH);
        ImageIcon p3=new ImageIcon(p2);
        pikachu=new JButton(p3);
        pikachu.setBackground(Color.WHITE);
        pikachu.setBorder(BorderFactory.createEmptyBorder());
        pikachu.setBounds(firstPocketMonSelectPanel.getX()+330, firstPocketMonSelectPanel.getY()+100, 200,200);
        firstPocketMonSelectPanel.add(pikachu);

        complete = new JButton("선택");
        complete.setBackground(Color.white);
        complete.setFont(new Font("굴림",Font.BOLD,20));
        complete.setBounds(firstPocketMonSelectPanel.getX()+170, isanghessi.getY()+200, 120,40);
        firstPocketMonSelectPanel.add(complete,JLayeredPane.POPUP_LAYER);

        explain= new JLabel("포켓몬을 세 마리 선택해주세요.");
        explain.setFont(new Font("굴림",Font.BOLD,16));
        explain.setBounds(firstPocketMonSelectPanel.getX()-80, isanghessi.getY()+200, 240,40);
        firstPocketMonSelectPanel.add(explain,JLayeredPane.POPUP_LAYER);

        firi.addActionListener(e -> {
            if(firi.getBackground()==Color.WHITE) {
                firi.setBackground(Color.gray);
                pocketMonList.add(0);
            }
            else if(firi.getBackground()==Color.GRAY){
                firi.setBackground(Color.WHITE);
                pocketMonList.remove((Object)0);
            }
        });
        ggobugi.addActionListener(e -> {
            if(ggobugi.getBackground()==Color.WHITE) {
                ggobugi.setBackground(Color.gray);
                pocketMonList.add(1);
            }
            else if(ggobugi.getBackground()==Color.GRAY){
                ggobugi.setBackground(Color.WHITE);
                pocketMonList.remove((Object)1);
            }
        });
        ggomadol.addActionListener(e -> {
            if(ggomadol.getBackground()==Color.WHITE) {
                ggomadol.setBackground(Color.gray);
                pocketMonList.add(2);
            }
            else if(ggomadol.getBackground()==Color.GRAY){
                ggomadol.setBackground(Color.WHITE);
                pocketMonList.remove((Object)2);
            }
        });
        gorapaduk.addActionListener(e -> {
            if(gorapaduk.getBackground()==Color.WHITE) {
                gorapaduk.setBackground(Color.gray);
                pocketMonList.add(3);
            }
            else if(gorapaduk.getBackground()==Color.GRAY){
                gorapaduk.setBackground(Color.WHITE);
                pocketMonList.remove((Object)3);
            }
        });
        isanghessi.addActionListener(e -> {
            if(isanghessi.getBackground()==Color.WHITE) {
                isanghessi.setBackground(Color.gray);
                pocketMonList.add(4);
            }
            else if(isanghessi.getBackground()==Color.GRAY){
                isanghessi.setBackground(Color.WHITE);
                pocketMonList.remove((Object)4);
            }
        });
        pikachu.addActionListener(e -> {
            if(pikachu.getBackground()==Color.WHITE) {
                pikachu.setBackground(Color.gray);
                pocketMonList.add(5);
            }
            else if(pikachu.getBackground()==Color.GRAY){
                pikachu.setBackground(Color.WHITE);
                pocketMonList.remove((Object)5);
            }
        });
        complete.addActionListener(e->{
            if(pocketMonList.size()==3){
                UserData.pocketMonList=pocketMonList;
                ChoosePocketPacket choosePocketPacket = new ChoosePocketPacket(UserData.id, UserData.username, pocketMonList);
                UserSocket.getInstance().sendObject(choosePocketPacket);
                firstPocketMonSelectPanel.setVisible(false);
            }else{
                AppendTextR("3마리를 선택해주세요.");
            }
        });
        firstPocketMonSelectPanel.setVisible(false);
        gameLayer.add(firstPocketMonSelectPanel,JLayeredPane.DEFAULT_LAYER);

    }
    public static void enableFirstPocketSelectPanel(boolean isEnable){
        firstPocketMonSelectPanel.setVisible(isEnable);
    }
    //포켓몬 처음에 골라야되면 이 패널 오류남.
    private void initChoosePocketForBattlePanel(){
        choosePocketForBattlePanel =new JPanel();
        choosePocketForBattlePanel.setBounds(100,100,600,300);
        choosePocketForBattlePanel.setBackground(Color.white);
        choosePocketForBattlePanel.setLayout(null);

        img1=new JButton();
        img1.setBounds(choosePocketForBattlePanel.getX()-95, choosePocketForBattlePanel.getY()-100, 190,220);
        img1.setBackground(Color.WHITE);
        img1.setBorder(BorderFactory.createEmptyBorder());
        choosePocketForBattlePanel.add(img1);

        img2=new JButton();
        img2.setBounds(choosePocketForBattlePanel.getX()+110, choosePocketForBattlePanel.getY()-100, 190,220);
        img2.setBackground(Color.WHITE);
        img2.setBorder(BorderFactory.createEmptyBorder());
        choosePocketForBattlePanel.add(img2);

        img3=new JButton();
        img3.setBounds(choosePocketForBattlePanel.getX()+300, choosePocketForBattlePanel.getY()-100, 190,220);
        img3.setBackground(Color.WHITE);
        img3.setBorder(BorderFactory.createEmptyBorder());
        choosePocketForBattlePanel.add(img3);

        JLabel text1 = new JLabel("처음 나갈 포켓몬을 선택해주세요.");
        text1.setFont(new Font("굴림",Font.BOLD,20));
        text1.setBounds(choosePocketForBattlePanel.getX()+60, 150, 320,220);
        choosePocketForBattlePanel.add(text1,JLayeredPane.POPUP_LAYER);

        img1.addActionListener(e -> {
            choosePocketForBattlePanel.setVisible(false);
            //myPocketMonImage.setImage(PocketMonData.monsterInfo.get(UserData.pocketMonList.get(0)).getBackPath());
            List<Integer> args = new ArrayList<>();
            args.add(0);
            UserBattlePacket userBattlePacket = new UserBattlePacket(
                    UserData.id,
                    UserData.username,
                    "CHANGE",
                    UserData.username,
                    args
            );
            UserSocket.getInstance().sendObject(userBattlePacket);
            battleControlFrame.setPlayerImage(
                    false,
                    PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getBackPath()
            );
            battleControlFrame.setPlayerName(
                    false,
                    PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getName()
            );
            battleControlFrame.setCurrentPocketMon(0);
        });

        img2.addActionListener(e -> {
            choosePocketForBattlePanel.setVisible(false);
            //myPocketMonImage.setImage(PocketMonData.monsterInfo.get(UserData.pocketMonList.get(1)).getBackPath());
            List<Integer> args = new ArrayList<>();
            args.add(1);
            UserBattlePacket userBattlePacket = new UserBattlePacket(
                    UserData.id,
                    UserData.username,
                    "CHANGE",
                    UserData.username,
                    args
            );
            UserSocket.getInstance().sendObject(userBattlePacket);
            battleControlFrame.setPlayerImage(
                    false,
                    PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getBackPath()
            );
            battleControlFrame.setPlayerName(
                    false,
                    PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getName()
            );
            battleControlFrame.setCurrentPocketMon(1);
        });

        img3.addActionListener(e -> {
            choosePocketForBattlePanel.setVisible(false);
            List<Integer> args = new ArrayList<>();
            args.add(2);
            UserBattlePacket userBattlePacket = new UserBattlePacket(
                    UserData.id,
                    UserData.username,
                    "CHANGE",
                    UserData.username,
                    args
            );
            UserSocket.getInstance().sendObject(userBattlePacket);
            //myPocketMonImage.setImage(PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getBackPath());
            battleControlFrame.setPlayerImage(
                    false,
                    PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getBackPath()
            );
            battleControlFrame.setPlayerName(
                    false,
                    PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getName()
            );
            battleControlFrame.setCurrentPocketMon(2);
        });

        choosePocketForBattlePanel.setVisible(false);
        gameLayer.add(choosePocketForBattlePanel,JLayeredPane.DRAG_LAYER);
    }
    public void enableChoosePocketForBattlePanel(boolean isEnable){
        System.out.println("Enable battle first choose");
        ImageIcon i=new ImageIcon(getClass().getResource("/"+PocketMonData.monsterInfo.get(UserData.pocketMonList.get(0)).getFrontPath()));
        Image i1=i.getImage();
        Image i2=i1.getScaledInstance(180,200,Image.SCALE_SMOOTH);
        ImageIcon i3= new ImageIcon(i2);
        img1.setIcon(i3);

        ImageIcon j=new ImageIcon(getClass().getResource("/"+PocketMonData.monsterInfo.get(UserData.pocketMonList.get(1)).getFrontPath()));
        Image j1=j.getImage();
        Image j2=j1.getScaledInstance(180,200,Image.SCALE_SMOOTH);
        ImageIcon j3= new ImageIcon(j2);
        img2.setIcon(j3);

        System.out.println("Open choose pocket");
        ImageIcon k=new ImageIcon(getClass().getResource("/"+PocketMonData.monsterInfo.get(UserData.pocketMonList.get(2)).getFrontPath()));
        Image k1=k.getImage();
        Image k2=k1.getScaledInstance(180,200,Image.SCALE_SMOOTH);
        ImageIcon k3= new ImageIcon(k2);
        img3.setIcon(k3);
        choosePocketForBattlePanel.setVisible(isEnable);
    }
    private void userListPanel() {
        model=new DefaultListModel();
        userList = new JList<String>((ListModel<String>) model);
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBounds(1, 1, 401, 180);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//하나만 선택되게

        //선택되면 귓속말 창
        userList.addListSelectionListener(this);

        userListPanel.add(scrollPane);
        userListPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        userListPanel.setLayout(null);
    }
    public static void updateUserList(UserListPacket userListPacket){
        model.clear();
        for (String username : userListPacket.userlist){
            model.addElement(username);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {//리스트 선택시 실행되는 메소드
        if(!e.getValueIsAdjusting()) {
            currentSelectedUser = userList.getSelectedValue();
        }
    }


    // 화면에 출력
    public void AppendText(String msg) {
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLACK);
        doc.setParagraphAttributes(doc.getLength(), 1, left, false);
        try {
            doc.insertString(doc.getLength(), msg+"\n", left );
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);

    }
    public void AppendTextR(String msg) {
        msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.BLUE);
        doc.setParagraphAttributes(doc.getLength(), 1, right, false);
        try {
            doc.insertString(doc.getLength(),msg+"\n", right );
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
    }
    //유저 채팅 패널 초기화
    private void initUserChatPanel() {
        //userChatPanel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(11, 10, 380, 370);
        userChatPanel.add(scrollPane);
        textArea = new JTextPane();
        textArea.setEditable(true);
        textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
        scrollPane.setViewportView(textArea);

        userChatPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        userChatPanel.setLayout(null);

        txtInput = new JTextField();
        txtInput.setBounds(100,420,200,40);
        userChatPanel.add(txtInput);
        txtInput.setColumns(10);

        txtInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                GameThread.setIsFocusable(false);//게임 포커스 비활성화
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                GameThread.setIsFocusable(true);//게임 포커스 활성화
            }
        });

        btnSend = new JButton("전송");
        btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
        btnSend.setBounds(320, 420, 70, 40);
        userChatPanel.add(btnSend);

        //사용자 이름 보여주기
        lblUserName = new JLabel(UserData.username);
        lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
        lblUserName.setBackground(Color.WHITE);
        lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
        lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserName.setBounds(10, 420, 70, 40);
        userChatPanel.add(lblUserName);
        
        TextSendAction action = new TextSendAction();
        btnSend.addActionListener(action);
        txtInput.addActionListener(action);
        txtInput.requestFocus();
    }
    class TextSendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Send button을 누르거나 메시지 입력하고 Enter key 치면
            if (e.getSource() == btnSend || e.getSource() == txtInput) {
                String msg = null;
                msg = String.format(" %s\n", txtInput.getText());
                AppendTextR(msg);
                UserChatPacket chatPacket = null;
                if(currentSelectedUser.equals(UserData.username) || currentSelectedUser.equals("-ALL-")){
                     chatPacket = new UserChatPacket(UserData.id, UserData.username, msg, "-ALL-");
                }else{
                     chatPacket = new UserChatPacket(UserData.id, UserData.username, msg, currentSelectedUser);
                }
                UserSocket.getInstance().sendObject(chatPacket);
                txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
            }
        }
    }
    public void enableBattleWindow(boolean isEnable){
        battleControlFrame.resetBattleFrame();
        battleControlFrame.setVisible(isEnable);
    }
    public BattleControlFrame getBattleControlFrame() {
        return battleControlFrame;
    }

    public OkOrNoFrame getOkOrNoFrame() {
        return okOrNoFrame;
    }
}
