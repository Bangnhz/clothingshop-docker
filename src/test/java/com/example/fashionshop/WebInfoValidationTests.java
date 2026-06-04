package com.example.fashionshop;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import static org.assertj.core.api.Assertions.assertThat;

class WebInfoValidationTests {

    // 1. Sử dụng ApplicationContextRunner để giả lập môi trường chạy ngầm của Spring
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    // 2. Định nghĩa một Class cấu hình giả lập nhận thông tin từ file properties công cộng
    @Getter
    @Setter
    @Validated
    @ConfigurationProperties(prefix = "web.info")
    public static class WebInfoProperties {

        @NotBlank(message = "Tên trang web không được để trống")
        private String title;

        @NotBlank(message = "Số điện thoại hỗ trợ không được để trống")
        @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải chứa đúng 10 chữ số")
        private String contactPhone;
    }

    // 3. Kích hoạt lớp thuộc tính cấu hình trong ngữ cảnh Test
    @Configuration
    @EnableConfigurationProperties(WebInfoProperties.class)
    public static class TestConfig {
    }

    // 4. KỊCH BẢN TEST 1: Khi điền đầy đủ và đúng định dạng -> Ứng dụng phải khởi tạo thành công Bean
    @Test
    void testConfig_WhenDataIsValid_ShouldInitializeBean() {
        this.contextRunner
                .withPropertyValues("web.info.title=FashionShop", "web.info.contactPhone=0987654321")
                .run(context -> {
                    // Khẳng định Spring Context nạp thành công và chứa Bean cấu hình này
                    assertThat(context).hasSingleBean(WebInfoProperties.class);
                    WebInfoProperties properties = context.getBean(WebInfoProperties.class);
                    assertThat(properties.getTitle()).isEqualTo("FashionShop");
                });
    }

    // 5. KỊCH BẢN TEST 2: Khi số điện thoại sai định dạng (thiếu số) -> Phải nổ lỗi và dừng ứng dụng
    @Test
    void testConfig_WhenPhoneIsInvalid_ShouldFailBinding() {
        this.contextRunner
                .withPropertyValues("web.info.title=FashionShop", "web.info.contactPhone=123") // Sai định dạng Pattern
                .run(context -> {
                    // Khẳng định ngữ cảnh Spring Context bị lỗi và không thể khởi chạy thành công
                    assertThat(context).hasFailed();
                });
    }
}