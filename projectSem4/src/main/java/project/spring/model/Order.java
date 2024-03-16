package project.spring.model;

import java.util.Date;

public class Order {
	private int id;  // Khóa chính
    private String code;  // Mã đơn hàng
    private Date orderDate;  // Ngày đặt hàng
    private String shippingAddress;  // Địa chỉ giao hàng
    private String shippingPhone;  // Số điện thoại
    private double total;  // Tổng tiền
    private String status;  // Trạng thái đơn hàng(Chờ xác nhận - Đã hủy- Đang giao hàng - Đã hoàn thành)
    private int accountId;  // Người dùng tạo đơn hàng - Khóa ngoại
    private String paymentStatus;  // Trạng thái thanh toán hay chưa
    private String paymentGateway;  // Cổng thanh toán trực tuyến
    
	public Order(int id, String code, Date orderDate, String shippingAddress, String shippingPhone, double total,
			String status, int accountId, String paymentStatus, String paymentGateway) {
		super();
		this.id = id;
		this.code = code;
		this.orderDate = orderDate;
		this.shippingAddress = shippingAddress;
		this.shippingPhone = shippingPhone;
		this.total = total;
		this.status = status;
		this.accountId = accountId;
		this.paymentStatus = paymentStatus;
		this.paymentGateway = paymentGateway;
	}

	public Order() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingPhone() {
		return shippingPhone;
	}

	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentGateway() {
		return paymentGateway;
	}

	public void setPaymentGateway(String paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

}
