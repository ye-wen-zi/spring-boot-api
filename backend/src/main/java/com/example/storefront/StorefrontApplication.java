package com.example.storefront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// @EnableJpaRepositories(basePackages = "package com.example.storefront.repositories")
public class StorefrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorefrontApplication.class, args);
	}

	// @Bean
	// CommandLineRunner initData(ProductRepository repository) {
	// return args -> {
	// System.out.print("initializing data...");
	// repository.save(new Product(null, "AeroSound Pro (Tai nghe không dây chống
	// ồn)"));
	// repository.save(new Product(null, "VoltCharge X (Sạc dự phòng đa năng sạc
	// nhanh)"));
	// repository.save(new Product(null, "LuminaDesk (Đèn bàn thông minh bảo vệ thị
	// lực)"));
	// repository.save(new Product(null, "NovaWatch S (Đồng hồ thể thao theo dõi sức
	// khỏe)"));
	// repository.save(new Product(null, "ZenPad Ultra (Bàn di chuột công thái học
	// bằng da)"));
	// repository.save(new Product(null, "ApexDrive 1TB (Ổ cứng di động SSD tốc độ
	// cao)"));
	// repository.save(new Product(null, "NeoCube (Củ sạc GaN 65W siêu nhỏ gọn)"));
	// repository.save(new Product(null, "PureBreathe Mini (Máy lọc không khí mini
	// để bàn)"));
	// repository.save(new Product(null, "FlexiStand (Giá đỡ laptop/iPad bằng nhôm
	// nguyên khối)"));
	// repository.save(new Product(null, "AuraLink Hub (Cổng chuyển đổi USB-C 8
	// trong 1)"));
	// };
	// }

}
// ./mvnw spring-boot:run
