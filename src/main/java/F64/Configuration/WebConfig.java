package F64.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String filePath;   // => "C:/upload"

    @Value("${upload.urlPath}")
    private String urlPath;    // => "/upload/"

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(urlPath + "**")
                // file:/// 경로 주의: 마지막에 '/' 포함
                .addResourceLocations("file:///" + filePath + "/");
    }
}


