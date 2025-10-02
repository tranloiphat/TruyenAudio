package Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class WebConfig implements WebMvcConfigurer {


 @Value("${upload.path}")
 private String uploadPath; // e.g. src/main/resources/static/Image


 @Override
 public void addResourceHandlers(ResourceHandlerRegistry registry) {
  Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
  String location = "file:" + uploadDir.toString() + "/"; // must end with '/'
  registry.addResourceHandler("/Image/**")
          .addResourceLocations(location)
          .setCachePeriod(3600);
 }
}