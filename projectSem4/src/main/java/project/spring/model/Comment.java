package project.spring.model;

public class Comment {
    private int id;
    private int ratingStar;  // Số sao đánh giá
    private String content;  // Nội dung bình luận
    private int accountId;  // Mã người dùng bình luận
    private int productId;  // Mã sản phẩm bình luận - Khóa ngoại

	public static String ID = "id";
	public static String RATINGSTAR = "ratingStar";
	public static String CONTENT = "content";
	public static String ACCOUNTID = "accountId";
	public static String PRODUCTID = "productId";
    
	public Comment(int id, int ratingStar, String content, int accountId, int productId) {
		super();
		this.id = id;
		this.ratingStar = ratingStar;
		this.content = content;
		this.accountId = accountId;
		this.productId = productId;
	}

	public Comment() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRatingStar() {
		return ratingStar;
	}

	public void setRatingStar(int ratingStar) {
		this.ratingStar = ratingStar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}

