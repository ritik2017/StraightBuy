package in.straightbuy.www.straightbuy;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetails implements Parcelable {

    private String imageUrl;
    private String productName , discount , oprice , description ,price ,category,subcategory;


    public ProductDetails(){

    }

    public ProductDetails(String productName, String discount, String oprice, String description, String category, String price, String subcategory) {
        this.productName = productName;
        this.discount = discount;
        this.oprice = oprice;
        this.description = description;
        this.category = category;
        this.price = price;
        this.subcategory = subcategory;
        this.imageUrl = "No image";
    }



    public ProductDetails(String productName, String discount, String oprice, String description, String category, String price,String subcategory,String imageUrl) {
        this.productName = productName;
        this.discount = discount;
        this.oprice = oprice;
        this.description = description;
        this.category = category;
        this.price = price;
        this.subcategory = subcategory;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getDiscount() {
        return discount;
    }

    public String getOprice() {
        return oprice;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getSubcategory(){
        return subcategory;
    }

    public ProductDetails(Parcel in ) {
        readFromParcel( in );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Object createFromParcel(Parcel in ) {
            return new ProductDetails( in );
        }

        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(productName);
        dest.writeString(discount);
        dest.writeString(oprice);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(price);
        dest.writeString(subcategory);
        dest.writeString(imageUrl);
    }

    private void readFromParcel(Parcel in ) {

        productName = in.readString();
        discount  = in.readString();
        oprice  = in.readString();
        description = in.readString();
        category = in.readString();
        price = in.readString();
        subcategory = in.readString();
        imageUrl = in.readString();
    }

}
