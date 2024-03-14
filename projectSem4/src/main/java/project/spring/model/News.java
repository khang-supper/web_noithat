package project.spring.model;

public class News {
	private int id;  // Mã tin tức - Khóa chính
    private String name;  // Tên bài viết
    private String description;  // Mô tả ngắn
    private String content;  // Nội dung bài viết
    private int accountId;  // Mã người tạo
    
	public News(int id, String name, String description, String content, int accountId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.content = content;
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

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
}
