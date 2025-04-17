package  com.example.demo.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Tüm uç noktalara CORS izni ver
                .allowedOrigins("http://localhost:3000")  // Hangi domainlerin erişebileceğini belirleyin
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // İzin verilen HTTP metodları
                .allowedHeaders("*")  // Hangi başlıkların kabul edileceğini belirtin
                .allowCredentials(true);  // Kimlik doğrulama bilgilerini (çerezler, header) kabul et
    }
}
