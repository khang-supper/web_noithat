package project.spring.model;

public class DiscountProduct {
    private int id;
    private int discountId;  // Mã chương trình khuyến mãi - Khóa ngoại
    private int productId;  // Mã sản phẩm - Khóa ngoại
    private double discountPrice;  // Giá khuyến mãi
    
	public DiscountProduct(int id, int discountId, int productId, double discountPrice) {
		super();
		this.id = id;
		this.discountId = discountId;
		this.productId = productId;
		this.discountPrice = discountPrice;
	}

	public DiscountProduct() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiscountId() {
		return discountId;
	}

	public void setDiscountId(int discountId) {
		this.discountId = discountId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}
    
}
