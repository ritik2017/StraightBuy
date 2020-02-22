package in.straightbuy.www.straightbuy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddSubCategoryActivity extends AppCompatActivity {

    private FirebaseDatabase categoryProductDatabase;
    private DatabaseReference categoryDatabaseReference;
    private ChildEventListener categoryListener;

    private Spinner categorySpinner;

    private EditText subcategoryEditText;
    private Button addSubcategoryButton;

    private FirebaseDatabase subcategoryDatabase;
    private DatabaseReference subcategoryDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_category);

        categoryProductDatabase = FirebaseDatabase.getInstance();
        subcategoryDatabase = FirebaseDatabase.getInstance();
        categoryDatabaseReference = categoryProductDatabase.getReference().child("categories");
        subcategoryDatabaseReference = subcategoryDatabase.getReference().child("subcategories");

        final ArrayList<String> categories = new ArrayList<String>();
        categories.add("SELECT CATEGORY");

        categoryListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CategoryClass newCategory = dataSnapshot.getValue(CategoryClass.class);
                categories.add(newCategory.getCategory());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        categoryDatabaseReference.addChildEventListener(categoryListener);

        categorySpinner = (Spinner) findViewById(R.id.categorySpinnerDropdown);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        addSubcategoryButton = (Button) findViewById(R.id.addsubcategory_button);
        subcategoryEditText = (EditText) findViewById(R.id.addsubcategory_edittext);

        addSubcategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = categorySpinner.getSelectedItem().toString();
                String subcategory = subcategoryEditText.getText().toString();

                if(subcategory.isEmpty() || category.equalsIgnoreCase("SELECT CATEGORY")){
                    Toast.makeText(AddSubCategoryActivity.this,"ENTER VALID CEREDENTIALS" , Toast.LENGTH_SHORT).show();
                }

                else{
                   SubcategoryClass subcategoryObject = new SubcategoryClass(category,subcategory);
                   subcategoryDatabaseReference.push().setValue(subcategory);
                   Toast.makeText(AddSubCategoryActivity.this, "SUBCATEGORY ADDED SUCCESSFULLY" , Toast.LENGTH_SHORT).show();
                   finish();
                }

            }
        });
    }
}
