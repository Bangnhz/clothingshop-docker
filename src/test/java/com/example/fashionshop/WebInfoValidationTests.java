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

    // 1. ĐỊNH NGHĨA CLASS CẤU HÌNH ĐẦY ĐỦ ANNOTATION TRONG FILE TEST
    @Configuration
    @Validated
    @ConfigurationProperties(prefix = "web.info")
    @Getter
    @Setter
    public static class WebInfoProperties {
        @NotBlank(message = "Ten website khong duoc de trong")
        @Pattern(regexp = "^[a-zA-Z0-9\\sÀ-ỹ]+$", message = "Ten website khong duoc chua ki tu dac biet")
        private String name;
    }

    // Cấu hình một môi trường kiểm thử ảo nạp trực tiếp tính năng ConfigurationProperties
    @Configuration
    @EnableConfigurationProperties(WebInfoProperties.class)
    static class TestConfig {}

    // 2. KHỞI TẠO BỘ CHẠY ĐỂ KHÔNG BỊ NULL KHI BIND DỮ LIỆU
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    /**
     * TEST: Tên web hợp lệ -> Phải khởi động thành công (Pass - Xanh)
     */
    @Test
    void whenWebNameIsNormal_thenContextShouldLoad() {
        this.contextRunner
                .withPropertyValues("web.info.name=Fashion Shop 2026")
                .run(context -> {
                    assertThat(context).hasSingleBean(WebInfoProperties.class);
                    assertThat(context.getBean(WebInfoProperties.class).getName()).isEqualTo("Fashion Shop 2026");
                });
    }

    /**
     * TEST: Tên web chứa kí tự lạ @&#* -> Phải bị chặn và báo lỗi (Pass - Xanh)
     */
//     @Test
//     void whenWebNameHasSpecialChars_thenContextShouldFailToStart() {
//         this.contextRunner
//                 .withPropertyValues("web.info.name=Fashion@&#*Shop")
//                 .run(context -> {
//                     // Khi nạp kí tự lạ, quá trình nạp cấu hình bắt buộc phải sinh ra lỗi StartupFailure
//                     assertThat(context.getStartupFailure()).isNotNull();
                    
//                     // Xác nhận nguyên nhân chính xác là lỗi Validation hệ thống
//                     assertThat(context.getStartupFailure())
//                             .hasRootCauseInstanceOf(jakarta.validation.ConstraintViolationException.class);
//                 });
//     }
/**
     * TEST: Tên web chứa kí tự lạ @&#* -> Phải bị chặn và báo lỗi (Pass - Xanh)
     */
   /**
     * TEST: Tên web chứa kí tự lạ @&#* -> Phải bị chặn và báo lỗi (Pass - Xanh)
     */
//     @Test
//     void whenWebNameHasSpecialChars_thenContextShouldFailToStart() {
//         this.contextRunner
//                 .withPropertyValues("web.info.name=Fashion@&#*Shop") // Truyền kí tự lạ vào đây
//                 .run(context -> {
//                     // 1. Kiểm tra xem Context ảo của Spring có bị sập/lỗi đúng như kỳ vọng không
//                     assertThat(context).getFailure()
//                             .hasMessageContaining("Could not bind properties to 'WebInfoValidationTests.WebInfoProperties'")
//                             .hasRootCauseInstanceOf(jakarta.validation.ConstraintViolationException.class);
//                 });
//     }
}