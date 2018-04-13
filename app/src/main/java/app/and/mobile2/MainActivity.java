package app.and.mobile2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private void initializeView(){
        mLogin = findViewById(R.id.main_input_login);
        mPass = findViewById(R.id.main_input_pass);
        mSignIn = findViewById(R.id.main_button_log);
        mRegister = findViewById(R.id.main_button_reg);
    }
    private void initializeData(){

    }
    private void initializeListeners(){
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // login in database
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // register dialog
            }
        });
    }
}
