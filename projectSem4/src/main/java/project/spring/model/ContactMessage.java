package project.spring.model;

public class ContactMessage {
	private int id;  // Mã lời nhắn cho cửa hàng
    private String name;  // Tên người gửi
    private String email;  // Email người gửi
    private int phone;  // Phone người gửi
    private String message;  // Nội dung lời nhắn

	public static String ID = "id";
	public static String NAME = "name";
	public static String EMAIL = "email";
	public static String PHONE = "phone";
	public static String MESSAGE = "message";
    
	public ContactMessage(int id, String name, String email, int phone, String message) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.message = message;
	}

	public ContactMessage() {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
