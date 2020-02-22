package in.straightbuy.www.straightbuy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AdminLoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.addcategory_menu) {

            addCategory();

        } else if (id == R.id.addproduct_menu) {

            addProduct();

        } else if (id == R.id.scanqr_menu) {

        } else if(id == R.id.addsliderimages_menu){

            addSliderImage();

        }else if(id == R.id.send_notification){

            sendNotification();

        }else if(id == R.id.addsubcategory_menu){
            addSubcategory();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addCategory(){

        Intent intent = new Intent(AdminLoginActivity.this,AddCategoryActivity.class);
        startActivity(intent);
    }
    public void addProduct(){

        Intent intent = new Intent(AdminLoginActivity.this,AddProductActivity.class);
        startActivity(intent);
    }

    public void addSubcategory(){
        Intent intent = new Intent(AdminLoginActivity.this , AddSubCategoryActivity.class);
        startActivity(intent);
    }

    public void sendNotification(){
        Intent intent = new Intent(AdminLoginActivity.this , AddNotificationActivity.class);
        startActivity(intent);
    }

    public void addSliderImage(){
        Intent intent = new Intent(AdminLoginActivity.this , AddSliderImageActivity.class);
        startActivity(intent);
    }

}
