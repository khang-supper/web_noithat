package project.spring.model;

public class Cart {
    private int id;  // Khóa chính
    private int accountId;  // Giỏ hàng thuộc người dùng - Khóa ngoại
    private int productId;  // Sản phẩm thuộc giỏ hàng - Khóa ngoại
    private int quantity;  // Số lượng

	public static String ID = "id";
	public static String ACCOUNTID = "accountId";
	public static String PRODUCTID = "productId";
	public static String QUANTITY = "quantity";
    
	public Cart(int id, int accountId, int productId, int quantity) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Cart() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}

