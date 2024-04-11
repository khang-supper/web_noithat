package project.spring.model;

public class Image {
    private int id;  // Khóa chính
    private String path;  // Đường dẫn ảnh
	////
	public static String ID = "id";
	public static String PATH = "path";
	
    
	public Image(int id, String path) {
		super();
		this.id = id;
		this.path = path;
	}

	public Image() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

    
}

