// Định nghĩa hàm thêm sản phẩm vào giỏ hàng
function addToCart(productId) {
    // Gửi yêu cầu POST tới endpoint "/add-to-cart/{productId}"
    $.ajax({
        type: "POST",
        url: "/add-to-cart/" + productId,
        success: function(response) {
            // Xử lý phản hồi từ máy chủ (nếu cần)
            console.log("Sản phẩm đã được thêm vào giỏ hàng!");
            // Cập nhật số lượng sản phẩm trong giỏ hàng trên giao diện
            //updateCartItemCount();
        },
        error: function(xhr, status, error) {
            // Xử lý lỗi (nếu có)
            console.error("Lỗi khi thêm sản phẩm vào giỏ hàng: " + error);
        }
    });
}

// Định nghĩa hàm cập nhật số lượng sản phẩm trong giỏ hàng trên giao diện
function updateCartItemCount() {
    // Gửi yêu cầu GET tới endpoint "/cart-item-count"
    $.ajax({
        type: "GET",
        url: "/cart-item-count",
        success: function(response) {
            // Cập nhật số lượng sản phẩm trong giỏ hàng trên giao diện
            $("#cart-item-count").text(response);
        },
        error: function(xhr, status, error) {
            // Xử lý lỗi (nếu có)
            console.error("Lỗi khi cập nhật số lượng sản phẩm trong giỏ hàng: " + error);
        }
    });
}

// Sử dụng jQuery để bắt sự kiện click vào nút "Thêm vào giỏ hàng"
$(document).ready(function() {
    $(".add-to-cart-btn").click(function() {
        var productId = $(this).closest(".product__item").find(".productIdCart").val();
        addToCart(productId);
    });
    updateCartItemCount();
});