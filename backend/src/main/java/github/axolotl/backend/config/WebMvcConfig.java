package github.axolotl.backend.config;

import github.axolotl.backend.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
        private final TokenInterceptor tokenInterceptor;

        public WebMvcConfig(TokenInterceptor tokenInterceptor) {
                this.tokenInterceptor = tokenInterceptor;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(tokenInterceptor)
                        .addPathPatterns("/**");
        }
}
