package in.straightbuy.www.straightbuy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView signupButton, skipButton;

    private String username, password;

    private String fileName = "USERDATA.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);

        loginButton = (Button) findViewById(R.id.login_button);
        signupButton = (TextView) findViewById(R.id.signup_loginpage);
        skipButton = (TextView) findViewById(R.id.skip_loginpage);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (username.equals("admin") && password.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "INVALID CEREDENTIALS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ProductPageActivity.class);
                startActivity(intent);
                writeFile(fileName, "NOTHING");
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void writeFile(String file, String text) {

        try {
            FileOutputStream fos = openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

