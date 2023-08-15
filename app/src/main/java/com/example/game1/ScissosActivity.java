package com.example.game1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

public class ScissosActivity extends AppCompatActivity implements View.OnClickListener {


    ImageButton btn_scissors, btn_rock, btn_paper;
    ImageView img_user_select,img_robot_select, image_vs;
    TextView score_user, score_robot;
    Integer temp;
    TextView remainCnt;
    TextView message;
    Animation anim;
    //가위 0 , 바위1, 보2
    int win[][] = {{1, 0}, {2, 1}, {0, 2}};
    //user - robot의 쌍중에서 user가 이기는 경우만 배열에 저장
    //1,0 : 바위-가위
    //2,1 : 보-바위
    //0,2 : 가위 - 보

    //[수정] 손그림을 아래의 리소스 배열로 처리하기로 수정함.
    int img_hands[] = {R.drawable.gif_scissors, R.drawable.gif_rock, R.drawable.gif_paper};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        btn_scissors = findViewById(R.id.btn_scissors);
        btn_rock = findViewById(R.id.btn_rock);
        btn_paper = findViewById(R.id.btn_paper);

        img_user_select = findViewById(R.id.img_user_select);
        img_robot_select = findViewById(R.id.img_robot_select);
        image_vs = findViewById(R.id.image_vs);

        score_user = findViewById(R.id.score_user);
        score_robot = findViewById(R.id.score_robot);

        remainCnt = findViewById(R.id.remainCnt);
        message = findViewById(R.id.message);

        //[수정]xml에 onCLick으로 정의되었던것을 수정하고 가위바위보 버튼에 이벤트를 아래처럼 수정함.
        findViewById(R.id.btn_scissors).setOnClickListener(this);
        findViewById(R.id.btn_rock).setOnClickListener(this);
        findViewById(R.id.btn_paper).setOnClickListener(this);
    }

    //컴퓨터가 랜덤하게 숫자를 뽑는다. 0:가위 , 1:바위, 2:보
    int doRobot()  {
        Random r = new Random();
        int num = r.nextInt(3);
        img_robot_select.setImageResource(img_hands[num]); //gif 이미지라도 정지화면으로 나옴
        return num;
    }

    @Override
    public void onClick(View view) {
        //로봇이 랜덤 값 뽑는다.
        int valRobot = doRobot();

        int valUser = 0;
        switch (view.getId()) {
            case R.id.btn_scissors:
                valUser = 0;    //가위
                break;
            case R.id.btn_rock:
                valUser = 1;    //바위
                break;
            case R.id.btn_paper:
                valUser = 2;    //보
                break;
        }
        img_user_select.setImageResource(img_hands[valUser]); //정지화면으로 손 그림 표시


        if (valRobot == valUser) {
            try {
                same(valRobot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if ((win[0][0] == valUser && win[0][1] == valRobot) ||
                (win[1][0] == valUser && win[1][1] == valRobot) ||
                (win[2][0] == valUser && win[2][1] == valRobot)) {
            userWin(valUser);    //[수정] :이긴 쪽의 손 그림을 gif로 움직이는 효과를 내기 위해 user가 선택한 가위바위보값을 전달 함.
        } else {
            userFail(valRobot);  //[수정] : 상동
        }

        isGameOver(); //10번 다했는지 체크
    }


    void isGameOver() {
        Integer cnt = Integer.parseInt(remainCnt.getText().toString()) - 1;
        remainCnt.setText(cnt.toString());

        if (cnt == 0) {
            int userScore = Integer.parseInt(score_user.getText().toString());
            int robotScore = Integer.parseInt(score_robot.getText().toString());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Game Over");

            if (userScore < robotScore) {
                builder.setMessage("컴퓨터가 이겼습니다.");
            } else if (userScore > robotScore) {
                builder.setMessage("축하합니다. 당신이 이겼습니다.");
            } else {
                builder.setMessage("비겼습니다.");
            }
            builder.setPositiveButton("새게임", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    remainCnt.setText("10");
                    score_robot.setText("0");
                    score_user.setText("0");
                    img_user_select.setImageResource(R.drawable.img_empty);
                    img_robot_select.setImageResource(R.drawable.img_empty);
                    ///image_vs.setImageResource(R.drawable.img_empty);
                }
            });
            builder.setNegativeButton("이제 그만", new DialogInterface.OnClickListener() {  //[수정] 버튼 추가
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setCancelable(false); //버튼 클릭해서 다이얼로그 닫기전에는 다른 버튼 실행안되도록 추가
            builder.create();
            builder.show();

        }
    }


    void userWin(int num) {  //내가 이겼을때 처리 : val : 0,1,2(가위,바위,보)
        try { //아래 메소드를 호출하려면 IOException 처리 필요하다고 오류 메세지 나옴, 가이드 대로  클릭하면 끝
            moveGifImage(img_user_select , img_hands[num]);   //user 이긴쪽 손 그림 gif 효과 연출
        } catch (IOException e) {
            e.printStackTrace();
        }
        temp = Integer.parseInt(score_user.getText().toString()) + 1;
        score_user.setText(temp.toString());
    }

    void userFail(int num) { //컴퓨터가 이겼을때 : val : 0,1,2(가위,바위,보)
        try {
            moveGifImage(img_robot_select , img_hands[num]); //로봇 선택 이미지에 이긴쪽 손 그림 gif 효과 연출
        } catch (IOException e) {
            e.printStackTrace();
        }

        temp = Integer.parseInt(score_robot.getText().toString()) + 1;
        score_robot.setText(temp.toString());
    }

    void same(int num) throws IOException { //같은걸 냈을때 처리 : 둘다 흔들기
        moveGifImage(img_robot_select , img_hands[num]);
        moveGifImage(img_user_select , img_hands[num]);
        //image_vs.setImageResource(R.drawable.same);
    } //별로 할게 없어서 그냥 둠.,


    //gif 그림의 움직이는 효과를 내기 위해 추가함.
    // 이 메소드를 쓰면 IOException 처리하라는 오류 메세지 나옴. 안내 메세지 클릭하여 처리
    private void moveGifImage(ImageView imgView, int gif_res) throws IOException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            // create AnimatedDrawable
            Drawable decodedAnimation = ImageDecoder.decodeDrawable(
                    // create ImageDecoder.Source object
                    ImageDecoder.createSource(getResources(), gif_res));
            // set the drawble as image source of ImageView
            imgView.setImageDrawable(decodedAnimation);

            // play the animation
            AnimatedImageDrawable ani = ((AnimatedImageDrawable) decodedAnimation);
            ani.setRepeatCount(1);
            ani.start();

        } else {
            imgView.setImageResource(gif_res);
        }
    }
}