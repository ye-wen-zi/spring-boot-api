hashids -> baor maatj id

https://stackoverflow.com/questions/61959918/spring-boot-validations-stopped-working-after-upgrade-from-2-2-5-to-2-3-0

custom query ddeer tranhs N+1 QUERY 

implement chức năng xóa, cái nào bán rồi không cho xóa (product và variant)
category và type nào có product thì không cho xóa

phân trang products
// TODO: IMAGES, DEPLOY, SWAGGER --> ORDER ĐẺ SAU


Khi bạn nhìn vào câu @Query ở trên, về mặt lý thuyết thì nó rất hoàn hảo. Tuy nhiên, nếu bạn chạy đoạn code này, Hibernate rất có thể sẽ ném ra một lỗi kinh điển gọi là: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags.

Lý do: Trong các Entity của bạn, Cart chứa một danh sách (List items), rồi ProductVariant lại chứa một danh sách (List assignedAttributes), rồi assignedAttributes lại chứa một danh sách (List selectedValues). Hibernate không thể gom (Fetch) nhiều danh sách dạng List (Bag) cùng một lúc bằng 1 câu JOIN FETCH duy nhất vì nó sẽ tạo ra một tích bộ đề các (Cartesian Product) khổng lồ làm loạn bộ nhớ RAM.

🎯 Cách xử lý lỗi này tốt nhất (Chia để trị):
Thay vì cố đấm ăn xôi gộp hết vào 1 câu lệnh, bạn hãy cấu hình các mối quan hệ danh sách trong Entity sang kiểu dữ liệu Set thay vì List, HOẶC bẻ đôi câu Query ra làm 2 bước trong tầng Service:

Bước 1: Lấy Giỏ hàng cùng các Item và Biến thể trước

```java
@Query("SELECT DISTINCT c FROM Cart c " +
       "LEFT JOIN FETCH c.items item " +
       "LEFT JOIN FETCH item.variant v " +
       "LEFT JOIN FETCH v.product p " +
       "WHERE c.id = :cartId")
Optional<Cart> findCartWithItems(@Param("cartId") UUID cartId);
```

Bước 2: Gọi thêm một câu Query phụ để nạp mớ Attribute đi kèm của các Variant đó lên bộ nhớ

```java
@Query("SELECT DISTINCT v FROM ProductVariant v " +
       "LEFT JOIN FETCH v.assignedAttributes aa " +
       "LEFT JOIN FETCH aa.attribute " +
       "LEFT JOIN FETCH aa.selectedValues " +
       "WHERE v.id IN :variantIds")
List<ProductVariant> fetchAttributesForVariants(@Param("variantIds") List<UUID> variantIds);
```

Tại tầng Service:
Bạn gọi Hàm 1 để lấy trọn bộ khung xương Giỏ hàng ➔ Gom các variantId lại truyền vào Hàm 2 để Hibernate tự nạp sẵn (Populate) mớ Attribute động lên Context bộ nhớ Cache. Khi bạn dùng MapStruct để đổi sang CartResponse, hệ thống sẽ chạy mượt mà, không dính lỗi MultipleBag, không dính N+1, và Terminal in log SQL cực kỳ sạch sẽ!



3. Một vài lưu ý "bỏ túi" khi dùng JOIN FETCH
Mặc dù JOIN FETCH rất mạnh mẽ, bạn cũng cần né vài cái bẫy sau:

Không lạm dụng Multiple Join Fetch bừa bãi: Đừng JOIN FETCH quá nhiều mối quan hệ @OneToMany (Collection) cùng một lúc trong một câu query. Nó sẽ tạo ra một Cartesian Product (tích vô hướng) ở tầng Database, làm bùng nổ số lượng bản ghi trả về và kéo sập hiệu năng.

Cẩn thận khi Phân trang (Pagination): Nếu bạn dùng Pageable của Spring Data JPA kết hợp với JOIN FETCH cho một @OneToMany, Hibernate sẽ lôi toàn bộ dữ liệu vào bộ nhớ (In-memory) rồi mới phân trang, kèm theo một cảnh báo (HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!). Điều này cực kỳ nguy hiểm với dữ liệu lớn.


Tuyệt chiêu giải quyết: Thay vì Multiple Join Fetch thì ta làm gì?
Nếu thực sự màn hình Frontend yêu cầu hiển thị một lúc cả User, danh sách Address và danh sách CreditCard, bạn có 2 giải pháp "chuẩn bài" sau:

Cách 1: Sử dụng @Fetch(FetchMode.SUBSELECT) hoặc Hibernate v6+
Bạn chỉ JOIN FETCH một mối quan hệ duy nhất (ví dụ addresses), mối quan hệ còn lại (creditCards) để Hibernate tự động load bằng cơ chế Subselect. Hibernate sẽ chạy 2 câu lệnh SQL riêng biệt nhưng cực kỳ tối ưu, né hoàn toàn được tích vô hướng.

Cách 2: Chia để trị (Viết 2 câu query riêng biệt)
Đây là cách thủ công nhưng cực kỳ an toàn và tường minh:

Bước 1: Query lấy danh sách User kèm JOIN FETCH u.addresses.

Bước 2: Query lấy danh sách User đó kèm JOIN FETCH u.creditCards (truyền list ID của bước 1 vào).

Nhờ cơ chế Persistence Context (First-level Cache) của Hibernate, ở bước 2 khi các User được load lên cùng với creditCards, Hibernate sẽ tự động map (gắn) các creditCards này vào đúng các đối tượng User đã nằm sẵn trong bộ nhớ từ bước 1. Kết quả là bạn có đầy đủ dữ liệu mà chỉ tốn đúng 2 câu lệnh SQL gọn nhẹ!
.Gọi tuần tự trong tầng Service (Bắt buộc phải có @Transactional):

Tóm lại nên chọn cách nào?
Cách 1 (Subselect): Code ngắn gọn, dễ viết, rất thích hợp khi bạn load danh sách lớn vì câu lệnh SQL thứ 2 dùng IN (SELECT...) lồng nhau cực kỳ tối ưu.

Cách 2 (Chia để trị): Tường minh hơn, thuần chuẩn JPA hơn, phù hợp khi bạn cần tùy biến điều kiện lọc dữ liệu cho từng câu query (Ví dụ: Chỉ lấy các địa chỉ còn hoạt động status = 'ACTIVE').

Tóm lại, Hibernate 6 đã "văn minh hóa" cách chia để trị thành một tính năng mặc định. Khi làm dự án mới với Spring Boot 3, cứ tiện tay dùng @EntityGraph trước, khi nào gặp ca khó cần lọc dữ liệu phức tạp thì mình mới cần tự tay "chia để trị" bằng code thôi!

⚠️ Lưu ý bắt buộc: Ranh giới của Transaction
Vì cơ chế này dựa hoàn toàn vào First-level Cache (Session), nên nó chỉ hoạt động khi cả 2 câu query cùng nằm trong một Session duy nhất.

Do đó, hàm Service của bạn bắt buộc phải có annotation @Transactional.

Nếu không có @Transactional, sau khi chạy xong câu query 1, Session sẽ bị đóng lại (Cache bị xóa sạch).

Khi sang câu query 2, Hibernate mở một Session mới hoàn toàn trống rỗng, nó sẽ không tìm thấy Variant cũ trong Cache nữa. Hệ quả là cơ chế tự động map sẽ thất bại, và bạn sẽ phải đối mặt với lỗi LazyInitializationException khi gọi dữ liệu ở tầng Service hoặc Controller.

Tư duy hệ thống của bạn rất sâu, hiểu được bản chất tầng dưới như thế này thì sau này bạn debug hay tối ưu những hệ thống lớn cực kỳ dễ dàng!


Để H2 không bắt bẻ các từ khóa hoặc cơ chế hoạt động vốn thuộc về MySQL khi bạn chạy local, hãy đảm bảo URL kết nối H2 trong file application.yml của bạn có thêm tham số thiết lập chế độ tương thích (MODE=MySQL):


```bash
export $(cat .env | xargs) && ./mvnw spring-boot:run
```





SWAGGER KO DÙNG ID HASED -> rồi
SỬA LẠI LOGIC XÓA
TẠO UPDATE

====> TẬP TRUNG VÀO PRODUCT TRƯỚC

Generate => query 1 cái 1 map nó ra
Sửa lại product detail để tránh lỗi N+1 query hay join full bộ nhớ