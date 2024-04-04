package project.spring.model;

public class News {
	private int id;  // Mã tin tức - Khóa chính
    private String name;  // Tên bài viết
    private String description;  // Mô tả ngắn
    private String content;  // Nội dung bài viết
    private String image;  // ẢNH Đại diện bài viết
    private String path;  // Đường dẫn 
    private int accountId;  // Mã người tạo

	public static String ID = "id";
	public static String NAME = "name";
	public static String DESCRIPTION = "description";
	public static String CONTENT = "content";
	public static String IMAGE = "'image";
	public static String PATH = "'path";
	public static String ACCOUNTID = "accountId";
    
	public News(int id, String name, String description, String content, String image, String path,int accountId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.content = content;
		this.image = image;
		this.path = path;
		this.accountId = accountId;
	}

	public News() {
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
}
