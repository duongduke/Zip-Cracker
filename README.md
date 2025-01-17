# ZIP Password Cracker

## Giới thiệu
ZIP Password Cracker là một ứng dụng Java cho phép người dùng tìm kiếm và giải nén các file ZIP được bảo vệ bằng mật khẩu. Ứng dụng sử dụng kỹ thuật đa luồng và phân chia CPU để tối ưu hóa quá trình tìm kiếm mật khẩu.

## Tính năng
- Tìm kiếm mật khẩu cho file ZIP bằng cách sử dụng brute force.
- Hỗ trợ tạm dừng và tiếp tục quá trình tìm kiếm.
- Giải nén file ZIP khi tìm thấy mật khẩu đúng.
- Giao diện người dùng thân thiện với JavaFX.

## Yêu cầu
- Java Development Kit (JDK) 8 trở lên.
- Apache Maven để quản lý phụ thuộc và biên dịch.

## Cài đặt
1. **Clone repository:**
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Cài đặt phụ thuộc:**
   Mở terminal và chạy lệnh sau để cài đặt các phụ thuộc:
   ```bash
   mvn clean install
   ```

## Cách chạy
1. Mở terminal với quyền admin.
2. Chuyển đến thư mục dự án:
   ```bash
   cd <project-directory>
   ```
3. Chạy ứng dụng:
   ```bash
   mvn exec:java
   ```

## Hướng dẫn sử dụng
1. Nhập các CPU muốn sử dụng (ví dụ: 0,1,2).
2. Chọn file ZIP cần giải mã.
3. Nhấn nút "Bắt đầu" để bắt đầu quá trình tìm kiếm mật khẩu.
4. Chúng ta có thể tạm dừng quá trình tìm kiếm bất cứ lúc nào và tiếp tục lại sau đó.

## Ghi chú
- Ứng dụng sẽ hiển thị mật khẩu đúng và thời gian tìm kiếm khi tìm thấy mật khẩu.
- Sau khi tìm thấy mật khẩu, ứng dụng sẽ tự động giải nén file ZIP và hiển thị thời gian chạy chương trình.

## Tài liệu tham khảo
- [JavaFX Documentation](https://openjfx.io/)
- [Apache Maven Documentation](https://maven.apache.org/)

