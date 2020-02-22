package in.straightbuy.www.straightbuy;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProductAdapter extends ArrayAdapter<ProductDetails> {

    private Activity context;
    private ArrayList<ProductDetails> productDetails;

    public ProductAdapter(Activity context, ArrayList<ProductDetails> productDetails){
        super(context, R.layout.productitems, productDetails);
        this.context = context;
        this.productDetails = productDetails;
    }

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listItemView = inflater.inflate(R.layout.productitems , null , true);

        ImageView productImage = (ImageView) listItemView.findViewById(R.id.image_listview);
        TextView productTextView = (TextView) listItemView.findViewById(R.id.producttext_listview);
        TextView priceTextview = (TextView) listItemView.findViewById(R.id.price_textview);
        TextView opriceTextView = (TextView) listItemView.findViewById(R.id.oprice_textview);
        TextView discountTextview = (TextView) listItemView.findViewById(R.id.discount_textview);

        ProductDetails product = productDetails.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);

        Glide
                .with(context)
                .setDefaultRequestOptions(requestOptions.centerCrop())
                .load(product.getImageUrl())
                .transition(withCrossFade())
                .into(productImage);

        productTextView.setText(product.getProductName());
        priceTextview.setText(("₹" + product.getPrice()));
        opriceTextView.setText(product.getOprice());
        opriceTextView.setPaintFlags(opriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
        discountTextview.setText((product.getDiscount() + "% off"));

        return listItemView;
    }
}
