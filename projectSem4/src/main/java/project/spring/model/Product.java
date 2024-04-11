package project.spring.model;

public class Product {
    private int id;  // Khóa chính
    private String name;  // Tên sản phẩm
    private String code;  // Mã sản phẩm
    private String description;  // Mô tả ngắn
    private String content;  // Nội dung sản phẩm
    private double price;  // Giá
    private int stock;  // Số lượng tồn kho
    private boolean isDelete;  // Trạng thái(xóa hoặc chưa xóa) 
    private boolean isProposal;  // Sản phẩm đề xuất(true or false)
    private String size;  // Kích thước
    private String material;  // Chất liệu
    private String path;  // Đường dẫn 
    private int categoryId;  // Loại sản phẩm - Khóa ngoại
    private int accountId;  // Người tạo sản phẩm 
	

	public static String ID = "id";
	public static String NAME = "name";
	public static String CODE = "code";
	public static String DESCRIPTION = "description";
	public static String CONTENT = "content";
	public static String PRICE = "price";
	public static String STOCK = "stock";
	public static String ISDELETE = "isDelete";
	public static String ISPROPOSAL = "isProposal";
	public static String SIZE = "size";
	public static String MATERIAL = "material";
	public static String PATH = "path";
	public static String CATEGORYID = "categoryId";
	public static String ACCOUNTID = "accountId";
    
	public Product(int id, String name, String code, String description, String content, double price, int stock,
			boolean isDelete, boolean isProposal, String size, String material, String path, int categoryId,
			int accountId) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.content = content;
		this.price = price;
		this.stock = stock;
		this.isDelete = isDelete;
		this.isProposal = isProposal;
		this.size = size;
		this.material = material;
		this.path = path;
		this.categoryId = categoryId;
		this.accountId = accountId;
	}

	public Product() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	public void getIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isProposal() {
		return isProposal;
	}

	public void setProposal(boolean isProposal) {
		this.isProposal = isProposal;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public Object getIdProduct() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getIdProduct'");
	}

}

