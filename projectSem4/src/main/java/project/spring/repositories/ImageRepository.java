package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.spring.model.Image;


public class ImageRepository {
    private static ImageRepository _instance = null;
    private JdbcTemplate db;

    public ImageRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static ImageRepository Instance() {
        if (_instance == null) {
            _instance = new ImageRepository();
        }
        return _instance;
    }

    class ImageRowMapper implements RowMapper<Image> {
        @Override
        public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
            Image item = new Image();
            item.setId(rs.getInt(Image.ID));
            item.setPath(rs.getString(Image.PATH));
            return item;
        }
    }
        //lấy ra danh sách ảnh, status thuộc sản phẩm bất kì
        public List<Map<String, Object>> getImageByProduct(int productId) {
            String query = "SELECT i.*, ip.status " +
                           "FROM images i " +
                           "JOIN image_products ip ON i.id = ip.imageId " +
                           "WHERE ip.productId =?";
            return db.queryForList(query, productId);
        }
        //Lấy ra ảnh đại diện của sản phẩm: 
        public Map<String, Object> getMainImage(int productId) {
            String query = "SELECT i.*, ip.status " +
                           "FROM images i " +
                           "JOIN image_products ip ON i.id = ip.imageId " +
                           "WHERE ip.productId =? AND ip.status = 1";
            return db.queryForMap(query, productId);
        }
}
