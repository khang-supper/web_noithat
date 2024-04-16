package project.spring.model;

import java.util.Date;

public class Discount {
    private int id;  // Khóa chính
    private String name;  // Tên chương trình (ví dụ 30/4 - 01/05)
    private Date startDate;  // Ngày bắt đầu
    private Date endDate;  // Ngày kết thúc
    private int accountId;  // Người tạo 
	//////////
	public static String ID = "id";
	public static String NAME = "name";
	public static String STARTDATE = "startDate";
	public static String ENDDATE = "endDate";
	public static String ACCOUNTID = "accountId";
    
	public Discount(int id, String name, Date startDate, Date endDate, int accountId) {
		super();
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.accountId = accountId;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

}

