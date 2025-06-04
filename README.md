# ZIP Password Cracker

## Giới thiệu
ZIP Password Cracker là ứng dụng giải mã file ZIP được bảo vệ bằng mật khẩu. Ứng dụng sử dụng kỹ thuật đa luồng và phân chia CPU để tối ưu hóa quá trình tìm kiếm mật khẩu, được xây dựng bằng Java.

## Tính năng
- Tìm kiếm mật khẩu cho file ZIP bằng brute force hoặc từ file từ điển
- Tùy chọn sinh mật khẩu với nhiều kiểu ký tự khác nhau (chữ thường, chữ hoa, số, ký tự đặc biệt)
- Hỗ trợ đa luồng (multi-threading) để tăng tốc độ tìm kiếm
- Cho phép người dùng tùy chỉnh số lượng CPU cores sử dụng
- Có khả năng gán các luồng xử lý (threads) vào các CPU cores cụ thể (Process Affinity)
- Hỗ trợ tạm dừng và tiếp tục quá trình tìm kiếm
- Hiển thị tiến trình và kết quả tìm kiếm

## Công nghệ sử dụng
- **Java:** Ngôn ngữ lập trình chính
- **Maven:** Quản lý project và dependencies
- **zip4j:** Thư viện xử lý file ZIP mạnh mẽ
- **JNA (Java Native Access):** Cho phép tương tác với native system functions (ví dụ: process affinity)
- **JavaFX:** (Sử dụng cho giao diện người dùng đồ họa - tùy chọn)

## Yêu cầu hệ thống
- Windows 7/8/10/11
- Java Runtime Environment (JRE) 21 trở lên

## Cách sử dụng
1. Tải file ZIP từ phần Releases
2. Giải nén file ZIP vừa tải về
3. Click đúp vào file `run.bat` để chạy chương trình
4. Trong giao diện chương trình:
   - Nhập số CPU cores muốn sử dụng (ví dụ: 0,1,2)
   - Chọn file ZIP cần giải mã
   - Tùy chọn:
     + Sử dụng file từ điển mật khẩu (định dạng .txt)
     + Hoặc tùy chỉnh cách sinh mật khẩu
   - Nhấn "Bắt đầu" để chạy

## Lưu ý
- Đảm bảo máy tính đã cài đặt Java (JRE 21)
- Có thể tạm dừng/tiếp tục quá trình tìm kiếm bất cứ lúc nào
- Thời gian tìm kiếm phụ thuộc vào:
  + Độ phức tạp của mật khẩu
  + Số lượng CPU cores được sử dụng
  + Kích thước file từ điển (nếu có)

## Hỗ trợ
Nếu gặp vấn đề khi sử dụng, vui lòng tạo issue trên GitHub.

## Tài liệu tham khảo
- [JavaFX Documentation](https://openjfx.io/)
- [Apache Maven Documentation](https://maven.apache.org/)
- [zip4j Documentation](https://github.com/srikanth-lingala/zip4j)
- [JNA (Java Native Access)](https://github.com/java-native-access/jna)

