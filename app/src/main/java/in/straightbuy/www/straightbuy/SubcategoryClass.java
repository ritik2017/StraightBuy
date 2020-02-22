package in.straightbuy.www.straightbuy;

public class SubcategoryClass {

    private String subcategory;
    private String category;

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public SubcategoryClass(String category, String subcategory) {
        this.category = category;
        this.subcategory = subcategory;
    }

}
