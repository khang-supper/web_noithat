package project.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.spring.model.Product;

@Repository
@Transactional

public class ProductDao {
    @Autowired
    private final  JdbcTemplate jdbcTemplate;
    
    public ProductDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

     public List<Product> findAll() {
        return jdbcTemplate.query("select * from products order by id desc", new productExtractor());
    }
//////////////
    @SuppressWarnings("deprecation")
    public List<Product> findAllByPage(int page, int pageSize) { 
        int start = page * pageSize;
        return jdbcTemplate.query("SELECT * FROM products ORDER BY id DESC LIMIT ?, ? ", new Object[] { start, pageSize },
                new productExtractor());
    }

    public int getTotalPages(int pageSize) { 
        int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }
    public Product findById(int id) {
        return jdbcTemplate.queryForObject("select * from products where Id=?", new productExtractor(), new Object[] { id });
    }

     @SuppressWarnings("deprecation")
    public List<Product> search(String keyword) {
        String sql = "SELECT * FROM products WHERE Name LIKE ?";
        String searchKeyword = "%" + keyword + "%"; // Thêm ký tự % để tìm kiếm một phần của tên
        return jdbcTemplate.query(sql, new Object[] { searchKeyword }, new productExtractor());
    }
////////////
    public int deleteById(int id) {
        return jdbcTemplate.update("delete from products where Id=?", new Object[] { id });
    }
   

    public int insert(Product newProduct) {
        return jdbcTemplate.update("insert into products (name, code, description,content,price,stock,accountId,categoryId)" + "value(?,?,?,?,?,?,?,?)",
                new Object[] { newProduct.getName(), newProduct.getCode(), newProduct.getDescription(), newProduct.getContent(),
                    newProduct.getPrice(),
                    newProduct.getStock(),
                    newProduct.getAccountId(),
                    newProduct.getCategoryId() });
    }

    public int update(Product upProduct) {
        return jdbcTemplate.update("update products set Name = ?, Code = ? where Id = ?",
                new Object[] { upProduct.getName(), upProduct.getCode(),
                        upProduct.getId() });
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

    public List<Product> findAllByParams(String sortPrice, Double priceFrom, Double priceTo) {
        // Thêm điều kiện lọc theo khoảng giá và sắp xếp theo giá (nếu có)
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM products WHERE 1=1");
    
        if (priceFrom != null && priceTo != null) {
            queryBuilder.append(" AND price >= ").append(priceFrom).append(" AND price <= ").append(priceTo);
        }
    
        if (sortPrice != null && (sortPrice.equals("desc") || sortPrice.equals("asc"))) {
            queryBuilder.append(" ORDER BY price ").append(sortPrice);
        }
    
        return jdbcTemplate.query(queryBuilder.toString(), new productExtractor());
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
    public List<Product> findNewProduct() {
        return jdbcTemplate.query("SELECT * FROM products ORDER BY id DESC LIMIT 6", new productExtractor());
    }

    //Lấy sản phẩm theo loại 
     @SuppressWarnings("deprecation")
     public List<Product> findByCategory(int id) {
        return jdbcTemplate.query("SELECT * FROM products WHERE categoryId = ?", new Object[] { id }, new productExtractor());
    }

     @SuppressWarnings("deprecation")//Lấy sản phẩm theo categoy, lọc, sắp xếp
     public List<Product> findAllByParamsAndCategory(String sortPrice, Double priceFrom, Double priceTo, int categoryId) {
        // Tạo câu truy vấn SQL ban đầu
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM products WHERE categoryId = ?");
    
        // Thêm điều kiện lọc theo khoảng giá nếu có
        if (priceFrom != null && priceTo != null) {
            queryBuilder.append(" AND price >= ").append(priceFrom).append(" AND price <= ").append(priceTo);
        }
    
        // Thêm điều kiện sắp xếp theo giá nếu được chỉ định
        if (sortPrice != null && (sortPrice.equals("desc") || sortPrice.equals("asc"))) {
            queryBuilder.append(" ORDER BY price ").append(sortPrice);
        }
    
        // Thực hiện truy vấn và trả về danh sách sản phẩm
        return jdbcTemplate.query(queryBuilder.toString(), new Object[]{categoryId}, new productExtractor());
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
    
    @SuppressWarnings("deprecation") //Lấy tất cả sản phẩm theo loại phân trang
    public List<Product> findAllByPageAndCategory(int page, int pageSize, int categoryId) {
        int start = page * pageSize;
        return jdbcTemplate.query("SELECT * FROM products WHERE categoryId = ? ORDER BY id DESC LIMIT ?, ? ",
                new Object[]{categoryId, start, pageSize}, new productExtractor());
    }

    @SuppressWarnings("deprecation") //Lấy tổng số sản phẩm theo loại
    public int getTotalPagesByCategory(int pageSize, int categoryId) {
        int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products WHERE categoryId = ?", new Object[]{categoryId}, Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }





    
    
     class productExtractor implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCode(rs.getString("code"));
            product.setDescription(rs.getString("description"));
            product.setContent(rs.getString("content"));
            product.setPrice(rs.getDouble("price"));
            product.setStock(rs.getInt("stock"));
            product.setDescription(rs.getString("description"));
            product.setSize(rs.getString("size"));
            product.setMaterial(rs.getString("material"));
            product.setPath(rs.getString("path"));
            product.setAccountId(rs.getInt("accountId"));
            product.setCategoryId(rs.getInt("categoryId"));


            return product;
        }
    }
}
