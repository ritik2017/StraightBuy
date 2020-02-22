package in.straightbuy.www.straightbuy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEdittext,emailEdittext, usernameEdittext, passwordEdittext, confirmPasswordEdittext , contactEdittext;
    private String fullname,email,username,password,confirmPassword, contactNumber;

    private Button signupButton;

    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;

    private String fileName = "USERDATA.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userDatabase.getReference("cartusers");

        nameEdittext = (EditText) findViewById(R.id.name_edittext);
        emailEdittext = (EditText) findViewById(R.id.email_edittext);
        usernameEdittext = (EditText) findViewById(R.id.username_edittext);
        passwordEdittext = (EditText) findViewById(R.id.password_edittext);
        confirmPasswordEdittext = (EditText) findViewById(R.id.confirmpassword_edittext);
        contactEdittext = (EditText) findViewById(R.id.contactno_edittext);

        signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullname = nameEdittext.getText().toString();
                email = emailEdittext.getText().toString();
                username = usernameEdittext.getText().toString();
                password = passwordEdittext.getText().toString();
                confirmPassword = confirmPasswordEdittext.getText().toString();
                contactNumber = contactEdittext.getText().toString();

                int luser = fullname.length();
                int lpass = password.length();
                int lcon = contactNumber.length();

                if(fullname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                        || contactNumber.isEmpty()){
                    Toast.makeText(SignUpActivity.this , "PLEASE FILL ALL THE CEREDENTIALS" , Toast.LENGTH_SHORT ).show();
                }
                else if(luser<8){
                    Toast.makeText(SignUpActivity.this , "USERNAME SHOULD BE ATLEAST 8 CHARACTERS LONG" , Toast.LENGTH_SHORT ).show();
                }
                else if(lpass<8){
                    Toast.makeText(SignUpActivity.this , "PASSWORD SHOULD BE ATLEAST 8 CHARACTERS LONG" , Toast.LENGTH_SHORT ).show();
                }
                else if(lcon!=10){
                    Toast.makeText(SignUpActivity.this , "PLEASE ENTER A VALID CONTACT NUMBER" , Toast.LENGTH_SHORT ).show();
                }
                else if (!(password.equals(confirmPassword))){

                    passwordEdittext.setText("");
                    confirmPasswordEdittext.setText("");
                    Toast.makeText(SignUpActivity.this , "PASSWORDS DO NOT MATCH\n PLEASE ENTER PASSWORD AGAIN" , Toast.LENGTH_SHORT ).show();
                }
                else if(username.contains(" ")){
                    usernameEdittext.setText("");
                    Toast.makeText(SignUpActivity.this , "USERNAME CANNOT CONTAIN SPACE" , Toast.LENGTH_SHORT ).show();
                }
                else if(password.contains(" ")){
                    passwordEdittext.setText("");
                    confirmPasswordEdittext.setText("");
                    Toast.makeText(SignUpActivity.this , "PASSWORD CANNOT CONTAIN SPACE" , Toast.LENGTH_SHORT ).show();
                }
                else if(email.contains(" ")){
                    emailEdittext.setText("");
                    Toast.makeText(SignUpActivity.this , "EMAIL ADDRESS CANNOT CONTAIN SPACE" , Toast.LENGTH_SHORT ).show();
                }
                else {
                    NewUser newUser = new NewUser(fullname, contactNumber, email ,username,password);
                    userDatabaseReference.push().setValue(newUser);

                    writeFile(fileName , (username + " " + password + " " + email + " " + contactNumber + " "
                            + fullname));

                    Toast.makeText(SignUpActivity.this , "SIGNED UP SUCCESSFULLY" , Toast.LENGTH_SHORT ).show();

                    Intent intent = new Intent(SignUpActivity.this, ProductPageActivity.class);
                    startActivity(intent);

                    finish();
                }

            }
        });
    }
    public void writeFile(String file , String text){

        try{
            FileOutputStream fos = openFileOutput(file , Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

