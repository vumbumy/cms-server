package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(HttpSecurity security) throws Exception {
		// cross domain problem
		security.csrf().disable();
		
//		http.authorizeRequests()
//				.anyRequest().permitAll(); // 모든 http 에 header 값에 인증 정보를 실어서 보내도록 함 (막는 것이 아님)

		security.formLogin().disable();

		security
				.cors()
				.and()
				.httpBasic().disable();

		security.headers().frameOptions().disable();

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/resources/**")
						.addResourceLocations("/resources/static/");
			}

			// TODO: 릴리즈 시점에는 접근 권한의 수정이 필요함.
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/api/**")
						.allowedOrigins("*")
						.allowedHeaders("*")
						.allowedMethods(
								HttpMethod.POST.name(),
								HttpMethod.GET.name(),
								HttpMethod.DELETE.name(),
								HttpMethod.PATCH.name()
						);
			}
		};
	}
}
