package in.straightbuy.www.straightbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategoryActivity extends AppCompatActivity {

        private EditText categoryEdittext;

        private DatabaseReference categoryReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_category);

            FirebaseDatabase categorydatabase = FirebaseDatabase.getInstance();
            categoryReference = categorydatabase.getReference().child("categories");

            categoryEdittext = (EditText) findViewById(R.id.addcategory_edittext);
            Button addCategoryButton = (Button) findViewById(R.id.addcategory_button);

            addCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryname = categoryEdittext.getText().toString();

                    categoryname = categoryname.toUpperCase();

                    if(!(categoryname.isEmpty())){

                        CategoryClass newCategory = new CategoryClass(categoryname);
                        categoryReference.push().setValue(newCategory);

                        Toast.makeText(AddCategoryActivity.this,"CATEGORY ADDED SUCCESSFULLY" , Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    else {
                        Toast.makeText(AddCategoryActivity.this,"ENTER VALID CATEGORY NAME" , Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
