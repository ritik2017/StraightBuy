package in.straightbuy.www.straightbuy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

        private EditText productEditText , priceEdittext , mrpEditText , discountEdittext , descriptionEdittext;
        private TextView selectimagetextview;
        private Button addProductButton , chooseButton;
        private ImageView imageViewChooseImage;

        private FirebaseDatabase categoryDatabase;
        private DatabaseReference categoryReference;
        private ChildEventListener categoryListener;

        public static final int PICK_IMAGE_REQUEST = 71;

        private FirebaseDatabase productDatabase;
        private FirebaseDatabase categoryProductDatabase;
        private DatabaseReference productReference;
        private DatabaseReference categoryDatabaseReference;
        private FirebaseStorage categoryProductStorage;
        private FirebaseStorage allProductStorage;
        private StorageReference allProductReference;
        private StorageReference categoryProductReference;

        private String category,subcategory,price,oprice,description,discount;
        private String product;

        private Uri filepath;

        private StorageTask mUploadTask;
        private String imageUrlCategory,imageUrlAll;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_product);

            productDatabase = FirebaseDatabase.getInstance();
            categoryProductDatabase = FirebaseDatabase.getInstance();
            categoryProductStorage = FirebaseStorage.getInstance();
            allProductStorage = FirebaseStorage.getInstance();

            productReference = productDatabase.getReference().child("PRODUCTS");

            productEditText = (EditText) findViewById(R.id.productname_edittext);
            mrpEditText = (EditText) findViewById(R.id.mrp_edittext);
            discountEdittext = (EditText) findViewById(R.id.discount_edittext);
            priceEdittext = (EditText) findViewById(R.id.price_edittext);
            descriptionEdittext = (EditText) findViewById(R.id.description_edittext);
            selectimagetextview = (TextView) findViewById(R.id.selectimage_textview);
            chooseButton = (Button) findViewById(R.id.choose_button);
            addProductButton = (Button) findViewById(R.id.addproduct_button);
            imageViewChooseImage = (ImageView) findViewById(R.id.image_chooseimage);

            //CATEGORY SPINNER DROPDOWN

            categoryDatabase = FirebaseDatabase.getInstance();
            categoryReference = categoryDatabase.getReference().child("categories");

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

            categoryReference.addChildEventListener(categoryListener);

            final Spinner categorySpinner = (Spinner) findViewById(R.id.categoryDropdown_spinner);

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , categories);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryAdapter);

            //CHOOSE IMAGE RESOURCE

            chooseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });

            addProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product = productEditText.getText().toString();
                    price = priceEdittext.getText().toString();
                    description = descriptionEdittext.getText().toString();
                    oprice = mrpEditText.getText().toString();
                    category = categorySpinner.getSelectedItem().toString();
                    discount = discountEdittext.getText().toString();

                    if (product.isEmpty() || price.isEmpty() || description.isEmpty() || oprice.isEmpty() || category.equals("SELECT CATEGORY") || discount.isEmpty() || filepath == null) {

                        Toast.makeText(AddProductActivity.this , "ENTER VALID CEREDENTIALS" , Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if(mUploadTask != null && mUploadTask.isInProgress()){
                            Toast.makeText(AddProductActivity.this, "AN UPLOAD IS IN PROGRESS WAIT FOR IT TO GET OVER",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            uploadImageCategory();
                            uploadImageAll();
                        }
                    }
                }
            });

        }

        private void uploadImageCategory(){

            if(filepath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("ADDING PRODUCT...");
                progressDialog.show();

                String path = "ProductsImages/"+ category + "/" + product;

                categoryProductReference = categoryProductStorage.getReference().child(path);
                mUploadTask = categoryProductReference.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                imageUrlCategory = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                ProductDetails newProductCategory = new ProductDetails(product, discount, oprice, description, category,price, subcategory,imageUrlCategory);
                                categoryDatabaseReference = categoryProductDatabase.getReference().child(category);
                                categoryDatabaseReference.push().setValue(newProductCategory);
                                Toast.makeText(AddProductActivity.this , "PRODUCT ADDDED SUCCESSFULLY" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProductActivity.this , "FAILED" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = 100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage("ADDED  " + (int)progress + "%");
                            }
                        });
            }
            else{
                Toast.makeText(AddProductActivity.this,"NO IMAGE SELECTED",Toast.LENGTH_SHORT).show();
            }
        }

        private void uploadImageAll(){

            if(filepath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("UPLOADING TO DATABASE...");
                progressDialog.show();

                String path = "AllProducts/" + product;

                allProductReference = allProductStorage.getReference().child(path);
                allProductReference.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                imageUrlAll = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                ProductDetails newProductAll = new ProductDetails(product, discount, oprice, description, category,price, subcategory,imageUrlAll);
                                productReference.push().setValue(newProductAll);
                                Toast.makeText(AddProductActivity.this , "PRODUCT UPLOADED SUCCESSFULLY" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProductActivity.this , "FAILED" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = 100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage("ADDED  " + (int)progress + "%");
                            }
                        });
            }

        }

        private void chooseImage(){

            Intent intent = new Intent();
            intent.setType("images/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent , "Select Image" ), PICK_IMAGE_REQUEST);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!= null){

                filepath = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , filepath);
                    imageViewChooseImage.setImageBitmap(bitmap);
                    selectimagetextview.setText("SELECTED");
                    selectimagetextview.setTextColor(getResources().getColor(R.color.green));
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        private String getFileextension(Uri uri){
            ContentResolver cr = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cr.getType(uri));
        }
    }

