package project.spring.model;

public class OrderDetail {
    private int id;  // Khóa chính
    private double price;  // Giá sản phẩm
    private int quantity;  // Số lượng
    private double totalPrice;  // Đơn giá (tổng tiền)
    private int orderId;  // Mã đơn hàng - Khóa ngoại
    private int productId;  // Sản phẩm - mã - Khóa ngoại

	public static String ID = "id";
	public static String PRICE = "price";
	public static String QUANTITY = "quantity";
	public static String TOTALPRICE = "totalPrice";
	public static String ORDREID = "orderId";
	public static String PRODUCTID = "productId";
    
	public OrderDetail(int id, double price, int quantity, double totalPrice, int orderId, int productId) {
		super();
		this.id = id;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.orderId = orderId;
		this.productId = productId;
	}

	public OrderDetail() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}

