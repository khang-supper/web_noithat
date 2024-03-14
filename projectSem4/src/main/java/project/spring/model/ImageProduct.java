package project.spring.model;

public class ImageProduct {
    private int productId;  // Mã sản phẩm - Khóa ngoại
    private int imageId;  // Mã sản phẩm - Khóa ngoại
    private boolean status;  // xác định đây có phải là hình chính hay không
    
	public ImageProduct(int productId, int imageId, boolean status) {
		super();
		this.productId = productId;
		this.imageId = imageId;
		this.status = status;
	}

	public ImageProduct() {
		super();
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
    
    
}

