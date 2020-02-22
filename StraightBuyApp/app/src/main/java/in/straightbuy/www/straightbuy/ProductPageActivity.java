package in.straightbuy.www.straightbuy;

import android.animation.IntArrayEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewFlipper imageViewFlipper;

    private ProductAdapter mproductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase categoryDatabase = FirebaseDatabase.getInstance();
        DatabaseReference categoryReference = categoryDatabase.getReference().child("categories");

        FirebaseDatabase productsDatabase = FirebaseDatabase.getInstance();
        DatabaseReference productReference = productsDatabase.getReference().child("PRODUCTS");

        final ArrayList<String> categories = new ArrayList<String>();
        categories.add("SELECT CATEGORY");

        ChildEventListener categoryListener = new ChildEventListener() {
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

        Spinner categorySpinner = (Spinner) findViewById(R.id.categories_dropdown);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String category = categories.get(i);

                if (!category.equalsIgnoreCase("SELECT CATEGORY")) {
                    Intent intent = new Intent(ProductPageActivity.this, ProductCategoryActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("categoryName", category);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        int images[] = {R.drawable.simage1,R.drawable.simage2,R.drawable.simage3};

        imageViewFlipper = (ViewFlipper) findViewById(R.id.image_slider_viewflipper);

        for(int image : images){

            flipperImages(image);
        }

        // PRODUCT LIST

        final ArrayList<ProductDetails> productDetails = new ArrayList<ProductDetails>();

        mproductAdapter = new ProductAdapter(this, productDetails);

        ListView mproductListview = (ListView) findViewById(R.id.product_listview);
        mproductListview.setAdapter(mproductAdapter);

        ChildEventListener productListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ProductDetails product = dataSnapshot.getValue(ProductDetails.class);
                mproductAdapter.add(product);
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

        productReference.addChildEventListener(productListener);

        // FLOATING BUTTON

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductPageActivity.this,MyCartActivity.class);
                startActivity(intent);
            }
        });

        // NAVIGATION DRAWER

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ONCLICK LISTENER FOR LISTVIEW PRODUCTS

        mproductListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductDetails currentProduct = productDetails.get(i);

                Intent intent = new Intent(ProductPageActivity.this,ProductDetailActivity.class);

                intent.putExtra("Product",currentProduct);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_cart) {

            Intent intent = new Intent(ProductPageActivity.this,MyCartActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_signin){
            Intent intent = new Intent(ProductPageActivity.this , LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_cart) {

            Intent intent = new Intent(ProductPageActivity.this , MyCartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(ProductPageActivity.this , AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_help) {

        }else if (id == R.id.nav_feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void flipperImages(int image){

        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        imageView.setAdjustViewBounds(true);

        imageViewFlipper.addView(imageView);
        imageViewFlipper.setFlipInterval(4000);
        imageViewFlipper.setAutoStart(true);

        imageViewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        imageViewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }
}
