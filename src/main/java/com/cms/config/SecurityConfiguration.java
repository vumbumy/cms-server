package com.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(HttpSecurity security) throws Exception {
		// cross domain problem
		security.csrf().disable();
		
		security.authorizeRequests()
				.anyRequest().permitAll(); // 모든 http 에 header 값에 인증 정보를 실어서 보내도록 함 (막는 것이 아님)

		security.formLogin().disable();

		security
				.cors()
				.and()
				.httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
				.csrf().disable();
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
//				.and()
//				.authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
//					// TODO: 릴리즈 시점에는 접근 권한의 수정이 필요함.
//					.antMatchers("/**").permitAll();

		security.headers().frameOptions().disable();

	}
}
