package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import project.spring.model.Product;

@Service
public final class ProductRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product item = new Product();
            item.setId(rs.getInt(Product.ID));
            item.setName(rs.getString(Product.NAME));
            item.setCode(rs.getString(Product.CODE));
            item.setDescription(rs.getString(Product.DESCRIPTION));
            item.setContent(rs.getString(Product.CONTENT));
            item.setPrice(rs.getDouble(Product.PRICE));
            item.setStock(rs.getInt(Product.STOCK));
            item.setIsDelete(rs.getBoolean(Product.ISDELETE));
            item.setProposal(rs.getBoolean(Product.ISPROPOSAL));
            item.setSize(rs.getString(Product.SIZE));
            item.setMaterial(rs.getString(Product.MATERIAL));
            item.setAccountId(rs.getInt(Product.ACCOUNTID));
            item.setPath(rs.getString(Product.PATH));
            item.setCategoryId(rs.getInt(Product.CATEGORYID));
            return item;
        }
    }

    public List<Product> findAll() {
        return jdbcTemplate.query("select * from products order by id desc", new ProductRowMapper());
    }

    //Lấy ra tất cả sản phẩm
    public List<Map<String, Object>> findAll2() {
        return jdbcTemplate.queryForList("SELECT p.id, p.name, p.price, p.code, p.isDelete, p.stock, p.path, i.path AS image " +
                                        "FROM products p " +
                                        "JOIN image_products ip ON p.id = ip.productId " +
                                        "JOIN images i ON ip.imageId = i.id " +
                                        "WHERE ip.status = 1 order by id desc");
    }
    
    //Lấy 4 sản phẩm thuộc các category
    public List<Map<String, Object>> find4ProductByCategory() {
        return jdbcTemplate.queryForList("SELECT p.id, p.name, p.price, p.path, p.categoryID, i.path AS image " +
        "FROM products p " +
        "JOIN image_products ip ON p.id = ip.productId " +
        "JOIN images i ON ip.imageId = i.id " +
        "WHERE ip.status = 1 And ( SELECT COUNT(*) FROM products p2 WHERE p2.categoryId = p.categoryId AND p2.id <= p.id ) <= 4 ORDER BY p.categoryId, RAND()");
    }

    public List<Map<String, Object>> findAllByPage(int page, int pageSize) {
        int start = page * pageSize;
        return jdbcTemplate.queryForList("SELECT  p.name, p.price, p.stock, p.id, p.path," +
                                                "p.code AS product_code, " +
                                                "c.name AS categoryName, " +
                                                "d.percent, " +
                                                "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                                                "i.path AS image " +
                                        "FROM products p " +
                                        "JOIN categories c ON p.categoryID = c.id " +
                                        "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                                        "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                                        "LEFT JOIN images i ON ip.imageId = i.id " +
                                        "WHERE p.isDelete = 0 AND p.stock > 0 order by id desc LIMIT ?, ? ", 
                                        new Object[] { start, pageSize });
    }

    public int getTotalPages(int pageSize) {
        int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public Product findById(int id) {
        return jdbcTemplate.queryForObject("select * from products where Id=?", new ProductRowMapper(), new Object[] { id });
    }

    public Product findByPath(String path) {
        return jdbcTemplate.queryForObject("select * from products where path=?", new ProductRowMapper(), new Object[] { path });
    }

    //Lấy chi tiết sản phẩm theo path
    public Map<String, Object> findByPath2(String path) {
        String query = "SELECT " +
                        "    IFNULL(d.percent, 0) AS percent," +
                        "    IFNULL(p.price * (1 - d.percent / 100), p.price) AS discountPrice" +
                        " FROM " +
                        "    products p" +
                        " LEFT JOIN " +
                        "    discounts d ON p.discountId = d.id AND d.status = 1" +
                        " WHERE " +
                        "    p.path = ?";
        return jdbcTemplate.queryForMap(query, new Object[] { path });
    }
    //Lấy 4 sản phẩm bất kì thuộc loại sản phẩm
    public List<Map<String, Object>> findRand(int categoryId) {
        String query = "SELECT  p.name, p.price, p.stock, p.id, p.path," +
                                "p.code AS product_code, " +
                                "c.name AS categoryName, " +
                                "d.percent, " +
                                "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                                "i.path AS image " +
                        "FROM products p " +
                        "JOIN categories c ON p.categoryID = c.id " +
                        "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                        "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                        "LEFT JOIN images i ON ip.imageId = i.id " +
                        "WHERE p.isDelete = 0 AND p.stock > 0 And categoryId=? ORDER BY RAND() LIMIT 4";
        return jdbcTemplate.queryForList(query, categoryId);
    }
    public int deleteById(int id) {
        return jdbcTemplate.update("update products set IsDelete = true where Id = ?", new Object[] { id });
    }

    public int insert(Product newProduct) {
        return jdbcTemplate.update("insert into products (Name, Code,Description, Content, Price,Stock , Size,IsDelete,isProposal,Material ,AccountId,Path, CategoryId) values (?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?,?)",
                new Object[] { newProduct.getName(), newProduct.getCode(),newProduct.getDescription(), newProduct.getContent(), newProduct.getPrice(),newProduct.getStock(),newProduct.getSize(),  false, true,newProduct.getMaterial(), newProduct.getAccountId(),newProduct.getPath(), newProduct.getCategoryId() });
    }

    public int update(Product upPro) {
        return jdbcTemplate.update("update products set Name = ?, Code = ?, Description = ? where Id = ?",
                new Object[] { upPro.getName(), upPro.getCode(), upPro.getDescription(), upPro.getId() });
    }

    //Hỉu tìm kiếm
    public List<Map<String, Object>> search(String keyword) {
        String sql = "SELECT  p.name, p.price, p.stock, p.id, p.path," +
                            "p.code AS product_code, " +
                            "c.name AS categoryName, " +
                            "d.percent, " +
                            "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                            "i.path AS image " +
                    "FROM products p " +
                    "JOIN categories c ON p.categoryID = c.id " +
                    "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                    "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                    "LEFT JOIN images i ON ip.imageId = i.id " +
                    "WHERE p.isDelete = 0 AND p.stock > 0 AND (p.name LIKE ? OR p.price LIKE ?)";
        String searchKeyword = "%" + keyword + "%"; // Thêm ký tự % để tìm kiếm một phần của tên hoặc giá
        return jdbcTemplate.queryForList(sql, new Object[] { searchKeyword, searchKeyword });
    }
    
    //lấy ra số lượng sản phẩm
    public int getProductCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
    }
    //lấy ra số lượng sản phẩm theo loại
     @SuppressWarnings("deprecation")
     public int getProductCountByCategory(int categoryId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products WHERE categoryId = ?", new Object[] { categoryId },Integer.class);
    }

    //Lấy tất cả sản phẩm, phân trang, lọc
    public List<Map<String, Object>> findAllByParams(String sortPrice, Double priceFrom, Double priceTo) {
        // Thêm điều kiện lọc theo khoảng giá và sắp xếp theo giá (nếu có)
        StringBuilder queryBuilder = new StringBuilder("SELECT  p.name, p.price, p.stock, p.id, p.path," +
                                                                "p.code AS product_code, " +
                                                                "c.name AS categoryName, " +
                                                                "d.percent, " +
                                                                "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                                                                "i.path AS image " +
                                                        "FROM products p " +
                                                        "JOIN categories c ON p.categoryID = c.id " +
                                                        "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                                                        "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                                                        "LEFT JOIN images i ON ip.imageId = i.id " +
                                                        "WHERE p.isDelete = 0 AND p.stock > 0 And 1=1");
    
        if (priceFrom != null && priceTo != null) {
            queryBuilder.append(" AND price >= ").append(priceFrom).append(" AND price <= ").append(priceTo);
        }
    
        if (sortPrice != null && (sortPrice.equals("desc") || sortPrice.equals("asc"))) {
            queryBuilder.append(" ORDER BY price ").append(sortPrice);
        }
    
        return jdbcTemplate.queryForList(queryBuilder.toString());
    }

    public int getProductCountByParams(String sortPrice, Double priceFrom, Double priceTo) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM products WHERE 1=1");
    
        // Thêm điều kiện lọc theo khoảng giá
        if (priceFrom != null && priceTo != null) {
            queryBuilder.append(" AND price >= ").append(priceFrom).append(" AND price <= ").append(priceTo);
        }
    
        return jdbcTemplate.queryForObject(queryBuilder.toString(), Integer.class);
    }
        
    //Lấy ra 6 sản phẩm mới nhất
    public List<Map<String, Object>> findNewProduct() {
        return jdbcTemplate.queryForList("SELECT  p.name, p.price, p.stock, p.id, p.path," +
                                                "p.code AS product_code, " +
                                                "c.name AS categoryName, " +
                                                "d.percent, " +
                                                "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                                                "i.path AS image " +
                                        "FROM products p " +
                                        "JOIN categories c ON p.categoryID = c.id " +
                                        "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                                        "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                                        "LEFT JOIN images i ON ip.imageId = i.id " +
                                        "WHERE p.isDelete = 0 AND p.stock > 0 ORDER BY id DESC LIMIT 6");
    }

    //Lấy sản phẩm theo loại 
     @SuppressWarnings("deprecation")
     public List<Product> findByCategory(int id) {
        return jdbcTemplate.query("SELECT * FROM products WHERE categoryId = ?", new Object[] { id }, new ProductRowMapper());
    }

    //Lấy sản phẩm theo categoy, lọc, sắp xếp
     public List<Map<String, Object>> findAllByParamsAndCategory(String sortPrice, Double priceFrom, Double priceTo, int categoryId) {
        // Tạo câu truy vấn SQL ban đầu
        StringBuilder queryBuilder = new StringBuilder("SELECT  p.name, p.price, p.stock, p.id, p.path," +
                                                                "p.code AS product_code, " +
                                                                "c.name AS categoryName, " +
                                                                "d.percent, " +
                                                                "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                                                                "i.path AS image " +
                                                        "FROM products p " +
                                                        "JOIN categories c ON p.categoryID = c.id " +
                                                        "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                                                        "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                                                        "LEFT JOIN images i ON ip.imageId = i.id " +
                                                        "WHERE p.isDelete = 0 AND p.stock > 0 And categoryId=?");
                                                    
        // Thêm điều kiện lọc theo khoảng giá nếu có
        if (priceFrom != null && priceTo != null) {
            queryBuilder.append(" AND price >= ").append(priceFrom).append(" AND price <= ").append(priceTo);
        }
    
        // Thêm điều kiện sắp xếp theo giá nếu được chỉ định
        if (sortPrice != null && (sortPrice.equals("desc") || sortPrice.equals("asc"))) {
            queryBuilder.append(" ORDER BY price ").append(sortPrice);
        }
    
        // Thực hiện truy vấn và trả về danh sách sản phẩm
        return jdbcTemplate.queryForList(queryBuilder.toString(), new Object[]{categoryId} );
    }
    
     @SuppressWarnings("deprecation")//Lấy ra số lượng sản phẩm sau khi lọc
     public int getProductCountByParamsAndCategory(String sortPrice, Double priceFrom, Double priceTo, int categoryId) {
        // Tạo câu truy vấn SQL ban đầu
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM products WHERE categoryId = ?");
    
        // Thêm điều kiện lọc theo khoảng giá nếu có
        if (priceFrom != null && priceTo != null) {
            queryBuilder.append(" AND price >= ").append(priceFrom).append(" AND price <= ").append(priceTo);
        }
    
        // Thực hiện truy vấn và trả về tổng số sản phẩm
        return jdbcTemplate.queryForObject(queryBuilder.toString(), new Object[]{categoryId}, Integer.class);
    }

    //Lấy tất cả sản phẩm theo loại phân trang
    public List<Map<String, Object>> findAllByPageAndCategory(int page, int pageSize, int categoryId) {
        int start = page * pageSize;
        return jdbcTemplate.queryForList("SELECT  p.name, p.price, p.stock, p.id, p.path," +
                                                "p.code AS product_code, " +
                                                "c.name AS categoryName, " +
                                                "d.percent, " +
                                                "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                                                "i.path AS image " +
                                        "FROM products p " +
                                        "JOIN categories c ON p.categoryID = c.id " +
                                        "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                                        "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                                        "LEFT JOIN images i ON ip.imageId = i.id " +
                                        "WHERE p.isDelete = 0 AND p.stock > 0 And categoryId=? ORDER BY id DESC LIMIT ?, ? ",
                new Object[]{categoryId, start, pageSize});
    }

    @SuppressWarnings("deprecation") //Lấy tổng số sản phẩm theo loại
    public int getTotalPagesByCategory(int pageSize, int categoryId) {
        int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products WHERE categoryId = ?", new Object[]{categoryId}, Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    //Cập nhật số lượng tồn  kho sản phẩm
    public int updateStock(int productId, int stock) {
        return jdbcTemplate.update(
                "update products set stock = ? where Id = ?",
                new Object[] { stock, productId});
    }

    //Lấy ra số lượng tồn kho của sản phẩm bất kì
    public int findStock(int productId) { 
        return jdbcTemplate.queryForObject( "SELECT stock FROM products where Id = ?", Integer.class,  new Object[] { productId});
    }

    //Lấy ra tất cả tồn kho
    public int getTotalStock() {
        return jdbcTemplate.queryForObject("SELECT SUM(stock) FROM products", Integer.class);
    }

    //Lẩy ra tổng giá trị tồn kho tất cả sản phẩm
    public double getTotalStockValue() {
        String query = "SELECT SUM(price * stock) FROM products";
        return jdbcTemplate.queryForObject(query, Double.class);
    }

    //Lấy ra sản phẩm có stock dưới 10(sản phẩm cbi hết hàng)
    public List<Map<String, Object>> productOutStock10() {
        return jdbcTemplate.queryForList("SELECT p.id, p.name, p.price, p.code, p.isDelete, p.stock, p.path, i.path AS image " +
                                        "FROM products p " +
                                        "JOIN image_products ip ON p.id = ip.productId " +
                                        "JOIN images i ON ip.imageId = i.id " +
                                        "WHERE ip.status = 1 AND p.isDelete = 0 AND p.stock < 11 AND p.stock > 0 order by id desc");
    }

    //Lấy ra sản phẩm hết hàng
    public List<Map<String, Object>> productOutStock() {
        return jdbcTemplate.queryForList("SELECT p.id, p.name, p.price, p.code, p.isDelete, p.stock, p.path, i.path AS image " +
                                        "FROM products p " +
                                        "JOIN image_products ip ON p.id = ip.productId " +
                                        "JOIN images i ON ip.imageId = i.id " +
                                        "WHERE ip.status = 1 AND p.isDelete = 0 AND p.stock < 1 order by id desc");
    }

    //Lấy ra 10 sản phẩm bán chạy nhất
    public List<Map<String, Object>> productSale() {
        return jdbcTemplate.queryForList("SELECT p.id, p.name, p.price, SUM(od.quantity) AS total_sales, p.code, p.isDelete, p.stock, p.path, i.path AS image " +
                                        "FROM products p " +
                                        "JOIN image_products ip ON p.id = ip.productId " +
                                        "JOIN images i ON ip.imageId = i.id " +
                                        "JOIN order_details od ON p.id = od.productId " +
                                        "JOIN orders o ON od.orderId = o.id " +
                                        "WHERE ip.status = 1 GROUP BY p.id, p.name, p.price ORDER BY total_sales DESC LIMIT 10");
    }


    //Lấy sản phẩm khuyến mãi, all sản phẩm
    public List<Map<String, Object>> findProductByDiscount() {
        String query = "SELECT  p.name, p.price, p.stock, p.id, p.path," +
                        "p.code AS product_code, " +
                        "c.name AS categoryName, " +
                        "d.percent, " +
                        "IFNULL(p.price * (1 - d.percent / 100), p.price) AS priceDiscount, " +
                        "i.path AS image " +
                    "FROM products p " +
                    "JOIN categories c ON p.categoryID = c.id " +
                    "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                    "LEFT JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                    "LEFT JOIN images i ON ip.imageId = i.id " +
                    "WHERE p.isDelete = 0 AND p.stock > 0 " +
                    "AND d.percent IS NOT NULL"; // Thêm điều kiện để lấy ra các sản phẩm có percent không null
        return jdbcTemplate.queryForList(query);
    }
    
}
