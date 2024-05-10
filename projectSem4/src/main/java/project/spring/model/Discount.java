package project.spring.model;

public class Discount {
    private int id;  // Khóa chính
    private String name;  // Tên chương trình 
    private double percent;  // Ngày bắt đầu
    private boolean status;  //trạng thái
	//////////
	public static String ID = "id";
	public static String NAME = "name";
	public static String PERCENT = "percent";
	public static String STATUS = "status";
    
	public Discount(int id, String name, double percent, boolean status) {
		super();
		this.id = id;
		this.name = name;
		this.percent = percent;
		this.status = status;
	}

	public Discount() {
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

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}

