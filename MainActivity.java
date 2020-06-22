package org.gachonmp2020.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "main";

    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference mRootRef, mReservedRef;

    String name;
    TextView textView;
    Boolean isLogin=false;



    Button loginBtn, seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8, seat9, seat10;
    Map<Button, Table> tableInfoMap = new HashMap();
    final String available = "available";
    final String reserved = "reserved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mReservedRef = mRootRef.child("tables");

        seat1 = (Button) findViewById(R.id.seat1);
        seat2 = (Button) findViewById(R.id.seat2);
        seat3 = (Button) findViewById(R.id.seat3);
        seat4 = (Button) findViewById(R.id.seat4);
        seat5 = (Button) findViewById(R.id.seat5);
        seat6 = (Button) findViewById(R.id.seat6);
        seat7 = (Button) findViewById(R.id.seat7);
        seat8 = (Button) findViewById(R.id.seat8);
        seat9 = (Button) findViewById(R.id.seat9);
        seat10 = (Button) findViewById(R.id.seat10);

        seat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat1);
            }
        });
        seat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat2);
            }
        });
        seat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat3);
            }
        });
        seat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat4);
            }
        });
        seat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat5);
            }
        });
        seat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat6);
            }
        });
        seat7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat7);
            }
        });
        seat8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat8);
            }
        });
        seat9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat9);

            }
        });
        seat10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TablePopup(seat10);

            }
        });

        loginBtn = (Button)findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogin){
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivityForResult(intent,1);
                    overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
                }else{
                    signOut();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTableInfo();
    }

    public void getTableInfo(){

        Query myTableQuery = mRootRef.child("tables");
        myTableQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "setTable / onDataChange");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Table table = postSnapshot.getValue(Table.class);
                    if(table.table_num==1){
                        tableInfoMap.put(seat1, table);
                    }else  if(table.table_num==2){
                        tableInfoMap.put(seat2, table);
                    }else if(table.table_num==3){
                        tableInfoMap.put(seat3, table);
                    }else if(table.table_num==4){
                        tableInfoMap.put(seat4, table);
                    }else if(table.table_num==5){
                        tableInfoMap.put(seat5, table);
                    }else if(table.table_num==6){
                        tableInfoMap.put(seat6, table);
                    }else if(table.table_num==7){
                        tableInfoMap.put(seat7, table);
                    }else if(table.table_num==8){
                        tableInfoMap.put(seat8, table);
                    }else if(table.table_num==9){
                        tableInfoMap.put(seat9, table);
                    }else if(table.table_num==10){
                        tableInfoMap.put(seat10, table);
                    }else{
                        Toast.makeText(getApplicationContext(), "table_num exception", Toast.LENGTH_SHORT).show();
                    }
                }
                setMainTablesView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, String.valueOf(databaseError));
            }
        });
    }//database 에서 정보를 받아서 tableInfoMap에 저장

    public void setMainTablesView(){
        Log.d(TAG, "set View");
        for(Button button:tableInfoMap.keySet()){
            Log.d(TAG, String.valueOf(tableInfoMap.get(button).table_num)+tableInfoMap.get(button).reserved);
            if(tableInfoMap.get(button).available == true ){
                button.setBackgroundResource(R.drawable.button_background);
            }else{
                button.setBackgroundResource(R.drawable.button_reserve);
            }
        }
    }//메인에 있는 테이블 view update
    public void TablePopup(Button button){

        final Table table = tableInfoMap.get(button);
        if(isLogin){

            AlertDialog.Builder resCheck = new AlertDialog.Builder(MainActivity.this);
            resCheck.setTitle("예약 현황");

            resCheck.setMessage("Table Info" +
                    "\ntable number: "+String.valueOf(table.table_num)+
                    "\ntable available: "+ String.valueOf(table.available)+
                    "\ntable capacity: "+String.valueOf(table.capacity)+
                    "\ntable reserved: "+table.reserved+
                    "\ntable reserved time: "+String.valueOf(table.time));
            if(table.available == true) {
                resCheck.setPositiveButton("이용중", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mReservedRef.child("table" + String.valueOf(table.table_num)).child(available).setValue(false);
                        mReservedRef.child("table" + String.valueOf(table.table_num)).child(reserved).setValue("managerAuth");
                        Toast.makeText(getApplicationContext(), "Table 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                resCheck.setPositiveButton("이용가능", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mReservedRef.child("table" + String.valueOf(table.table_num)).child(available).setValue(true);
                        mReservedRef.child("table" + String.valueOf(table.table_num)).child(reserved).setValue("none");
                        Toast.makeText(getApplicationContext(), "Table 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            resCheck.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            resCheck.show();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("메세지");
            alert.setMessage("로그인이 필요합니다");
            alert.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivityForResult(intent,1);
                    overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
                }
            });
            alert.setNegativeButton("닫기",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {}
            });
            alert.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == 2) {   ///로그인 갔다가 돌아오는거
                isLogin = data.getBooleanExtra("isLogin", false);
                loginBtn.setText("Logout");
                Toast.makeText(getApplicationContext(), "환영합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        isLogin=false;
                        Toast.makeText(getApplicationContext(), "로그아웃 되엇습니다.", Toast.LENGTH_SHORT).show();
                        loginBtn.setText("로그인");
                    }
                });
    }


}
