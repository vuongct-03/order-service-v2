package com.kltn.order_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (RequestTemplate template) -> {
            String token = extractTokenFromRequest();
            if (token != null && !token.isEmpty()) {
                template.header("Authorization", "Bearer " + token);
            } else {
                System.out.println(">>> ⚠️ No token found to attach to Feign request!");
            }
        };
    }

    private String extractTokenFromRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            HttpServletRequest request = servletAttributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        return null;
    }
}







// package com.kltn.order_service.config;

// import feign.RequestInterceptor;
// import feign.RequestTemplate;
// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.context.request.RequestAttributes;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;

// @Configuration
// public class FeignClientConfig implements RequestInterceptor {

//     @Override
//     public void apply(RequestTemplate template) {
//         String token = getTokenFromRequest();
//         if (token != null && !token.isEmpty()) {
//             template.header("Authorization", "Bearer " + token);
//         }
//     }

//     private String getTokenFromRequest() {
//         RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//         if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
//             HttpServletRequest request = servletRequestAttributes.getRequest();
//             String authHeader = request.getHeader("Authorization");
//             if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                 return authHeader.substring(7); // Cắt bỏ "Bearer "
//             }
//         }
//         return null;
//     }
// }
