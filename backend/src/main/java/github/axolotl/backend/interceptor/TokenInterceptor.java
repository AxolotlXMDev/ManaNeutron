package github.axolotl.backend.interceptor;

import dczx.axolotl.util.file.FilesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class TokenInterceptor implements HandlerInterceptor {
        @Value("${mana-neutron.config.token-config-path}")
        private String tokenConfigPath;
        private String authorizationToken;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String authorization = request.getHeader("Authorization");
                if (authorization != null && authorization.startsWith("Bearer ")) {
                        String token = authorization.substring(7);
                        if (authorizationToken.equals(token)) {
                                return true;
                        }
                }
                response.setStatus(401);
                return false;
        }

        @PostConstruct
        public void init() throws IOException {
                FilesUtil.keepFileExists(tokenConfigPath);
                authorizationToken = Files.readString(Path.of(tokenConfigPath));
                if (authorizationToken.isEmpty()) {
                        throw new IOException("Authorization Token is invalid");
                }
        }
}