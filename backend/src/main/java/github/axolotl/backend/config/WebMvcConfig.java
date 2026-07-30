package github.axolotl.backend.config;

import github.axolotl.backend.interceptor.TokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                }else if ("dev".equals(mode)) {
                        log.warn("c is disabled in dev mode");
                }else{
                        throw new IllegalArgumentException("Invalid mode " + mode);
                }

        }
}
