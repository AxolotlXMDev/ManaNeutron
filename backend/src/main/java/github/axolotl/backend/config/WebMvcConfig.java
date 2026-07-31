package github.axolotl.backend.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import github.axolotl.backend.interceptor.TokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
        @Value("${mana-neutron.mode}")
        String mode;
        private final TokenInterceptor tokenInterceptor;

        public WebMvcConfig(TokenInterceptor tokenInterceptor) {
                this.tokenInterceptor = tokenInterceptor;
        }

        @Override
        public void addInterceptors(@NonNull InterceptorRegistry registry) {
                if ("prod".equals(mode)) {
                        registry.addInterceptor(tokenInterceptor)
                                .addPathPatterns("/**");
                } else if ("dev".equals(mode)) {
                        log.warn("c is disabled in dev mode");
                } else {
                        throw new IllegalArgumentException("Invalid mode " + mode);
                }
        }

        @Override
        public void configureMessageConverters(HttpMessageConverters.@NonNull ServerBuilder builder) {
                WebMvcConfigurer.super.configureMessageConverters(builder);

                FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

                FastJsonConfig config = new FastJsonConfig();
                config.setCharset(StandardCharsets.UTF_8);
                config.setDateFormat("yyyy-MM-dd HH:mm:ss");
                config.setWriterFeatures(JSONWriter.Feature.WriteClassName);
                converter.setFastJsonConfig(config);

                converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));

                builder.addCustomConverter(converter);
        }

}
