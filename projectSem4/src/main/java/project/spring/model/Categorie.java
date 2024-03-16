package project.spring.model;

public class Categorie {
	private int id;
    private String name;  // Tên loại
    private String image;  // Đường dẫn ảnh
    private boolean isDelete;  // Trạng thái xóa
    
    
	public Categorie() {
		super();
	}
	public Categorie(int id, String name, String image, boolean isDelete) {
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
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
    
}

