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

class AppMailValidationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(MailTestConfig.class);

    @Getter
    @Setter
    @Validated
    @ConfigurationProperties(prefix = "app.mail")
    public static class MailProperties {

        @NotBlank(message = "Mã SendGrid API Key không được để trống")
        private String apiKey;

        @NotBlank(message = "Email hệ thống không được để trống")
        // Ép chuỗi cấu hình email phải tuân thủ đúng định dạng địa chỉ Email tiêu chuẩn
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Cú pháp Email cấu hình sai định dạng")
        private String fromEmail;
    }

    @Configuration
    @EnableConfigurationProperties(MailProperties.class)
    public static class MailTestConfig {
    }

    // KỊCH BẢN 1: Điền đúng Email người gửi -> Pass
    @Test
    void testMailConfig_WhenEmailIsValid_ShouldLoadBean() {
        this.contextRunner
                .withPropertyValues(
                    "app.mail.apiKey=SG.mock_key_123456789", 
                    "app.mail.fromEmail=fashionshop-support@gmail.com" // Đúng định dạng email
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(MailProperties.class);
                });
    }

    // KỊCH BẢN 2: Gõ sai đuôi email hoặc thiếu dấu @ -> Fail
    @Test
    void testMailConfig_WhenEmailIsInvalid_ShouldThrowValidationException() {
        this.contextRunner
                .withPropertyValues(
                    "app.mail.apiKey=SG.mock_key_123456789", 
                    "app.mail.fromEmail=fashionshop_loi_email_thieu_dau_at.com" // Sai định dạng
                )
                .run(context -> {
                    assertThat(context).hasFailed();
                });
    }
}