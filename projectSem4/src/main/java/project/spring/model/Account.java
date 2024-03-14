package project.spring.model;

public class Account {
	private int id;
    private String userName;
    private String password;
    private String fullName;
    private String email;
    private int phone;
    private String address;
    private String avatar;  // Đường dẫn ảnh đại diện
    private boolean status;  // Trạng thái tài khoản(xóa or đang hoạt động)
    private boolean isAdmin;  // Là admin or người dùng web
    
    
	public Account() {
		super();
	}
	public Account(int id, String userName, String password, String fullName, String email, int phone, String address,
			String avatar, boolean status, boolean isAdmin) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.avatar = avatar;
		this.status = status;
		this.isAdmin = isAdmin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

    
}

