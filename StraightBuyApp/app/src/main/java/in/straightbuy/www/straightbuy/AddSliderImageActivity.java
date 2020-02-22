package in.straightbuy.www.straightbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddSliderImageActivity extends AppCompatActivity {

    private Button chooseImageButton;
    public static final int PICK_IMAGE_REQUEST = 71;

    private ImageView imageViewChooseImage;
    private TextView selectImageTextView;

    private FirebaseStorage imageFirebaseStorage;
    private StorageReference imageStorageReference;

    private Uri filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slider_image);

        imageFirebaseStorage = FirebaseStorage.getInstance();

        chooseImageButton = (Button) findViewById(R.id.choose_button);
        imageViewChooseImage = (ImageView) findViewById(R.id.image_choosesliderimage);
        selectImageTextView = (TextView) findViewById(R.id.selectimage_textview);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
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
                selectImageTextView.setText("SELECTED");
                selectImageTextView.setTextColor(getResources().getColor(R.color.green));
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){

        if(filepath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("ADDING PRODUCT...");
            progressDialog.show();

            imageStorageReference = imageFirebaseStorage.getReference().child("images/Slider");
            imageStorageReference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSliderImageActivity.this , "PRODUCT ADDDED SUCCESSFULLY" , Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddSliderImageActivity.this , "FAILED" , Toast.LENGTH_SHORT).show();
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
}
