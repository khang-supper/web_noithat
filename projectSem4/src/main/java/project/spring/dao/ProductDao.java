package project.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.spring.model.News;
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
        return jdbcTemplate.query("select * from products order by id asc", new productExtractor());
    }
//////////////
    @SuppressWarnings("deprecation")
    public List<Product> findAllByPage(int page, int pageSize) { 
        int start = page * pageSize;
        return jdbcTemplate.query("SELECT * FROM products LIMIT ?, ? ", new Object[] { start, pageSize },
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
