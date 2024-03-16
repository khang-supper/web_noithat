package project.spring.model;

public class Category {
	private int id;
    private String name;  // Tên loại
    private String image;  // Đường dẫn ảnh
    private boolean isDelete;  // Trạng thái xóa
    
    public static String ID = "id";
	public static String NAME = "name";
	public static String IMAGE = "image";
	public static String ISDELETE = "isDelete";

	public Category() {
		super();
	}
	public Category(int id, String name, String image, boolean isDelete) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.isDelete = isDelete;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
    
}

