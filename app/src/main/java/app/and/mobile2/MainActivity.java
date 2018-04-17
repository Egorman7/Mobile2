package app.and.mobile2;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.and.mobile2.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    private EditText mLogin, mPass;
    private Button mSignIn, mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();
        initializeData();
        initializeListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        System.exit(0);
    }

    private void initializeView(){
        mLogin = findViewById(R.id.main_input_login);
        mPass = findViewById(R.id.main_input_pass);
        mSignIn = findViewById(R.id.main_button_log);
        mRegister = findViewById(R.id.main_button_reg);
    }
    private void initializeData(){

    }
    private void initializeListeners(){
        // Вход в приложение
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                String login = mLogin.getText().toString(), pass = mPass.getText().toString();
                if(login.isEmpty() || pass.isEmpty()) return;
                int id = DBHelper.login(getApplicationContext(),login,pass);
                if(id==-1) Toast.makeText(getApplicationContext(),"Пользователь не существует!", Toast.LENGTH_SHORT).show();
                else if(id==-2) Toast.makeText(getApplicationContext(), "Неверный пароль!", Toast.LENGTH_SHORT).show();
                else {
                    // Запуск активити списка картинок с реддита
                    Intent intent = new Intent(MainActivity.this, RedditActivity.class);
                    intent.putExtra("user_id", id);
                    intent.putExtra("username", login);
                    startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, mLogin, "login").toBundle());
                }
            }
        });
        // Регистрация в приложении
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getLayoutInflater().inflate(R.layout.dialog_register, null);
                final EditText dLogin = v.findViewById(R.id.dialog_reg_input_login);
                final EditText dPass = v.findViewById(R.id.dialog_reg_input_pass);
                final EditText dPass2 = v.findViewById(R.id.dialog_reg_input_pass2);
                // Диалог для регистрации
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Регистрация")
                        .setView(v)
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(dLogin.getText().toString().isEmpty() || dPass.getText().toString().isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Поля пусты!", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                                if(!dPass.getText().toString().equals(dPass2.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), DBHelper.register(getApplicationContext(),dLogin.getText().toString(), dPass.getText().toString()), Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            }
                        }).show();
            }
        });
    }
}
