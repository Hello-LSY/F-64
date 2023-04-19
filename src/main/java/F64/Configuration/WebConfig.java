//package F64.Configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        registry.addConverter(String.class, LocalDate.class, str -> LocalDate.parse(str, formatter));
//    }
//}
