package project.spring.model;

public class Account {
	private int id;
	private String username;
	private String password;
	private String fullName;
	private String email;
	private int phone;
	private String address;
	private String avatar;  // Đường dẫn ảnh đại diện
	private boolean status;  // Trạng thái tài khoản(xóa or đang hoạt động)
	private boolean role;  // Là admin or người dùng web

	//////
	public static String ID = "id";
	public static String USERNAME = "username";
	public static String PASSWORD = "password";
	public static String FULLNAME = "fullName";
	public static String EMAIL = "email";
	public static String PHONE = "phone";
	public static String ADDRESS = "address";
	public static String AVATAR = "avatar";
	public static String STATUS = "status";
	public static String ROLE = "role";

	
	public Account() {
		super();
	}
	public Account(int id, String username, String password, String fullName, String email, int phone, String address,
			String avatar, boolean status, boolean role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.avatar = avatar;
		this.status = status;
		this.role = role;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setusername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean getRole() {
		return role;
	}
	public void setRole(boolean role) {
		this.role = role;
	}
	
	
}

