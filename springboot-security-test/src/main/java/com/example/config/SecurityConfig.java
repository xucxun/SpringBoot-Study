package com.example.config;

import com.example.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${system.user.password.secret}") //注入配置的密钥
    private String secret;

    @Bean //配置口令加密方式
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder(this.secret);
    }

    @Autowired
    PasswordEncoder encoder;

    // 用来配置Filter链
    @Override
    public void configure(WebSecurity ws) throws Exception {
    }

    // 配置身份认证(UserDetailsService Authentication）
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用UserDetailsService进行身份认证
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    @Autowired
    private DataSource dataSource;
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }
    //配置权限管理
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 开启请求路径限制
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                // 需要先对static文件夹下静态资源进行统一放行
                .antMatchers("/login/**").permitAll()
                .antMatchers("/detail/common/**").hasRole("common")
                .antMatchers("/detail/**").hasRole("vip")
                .anyRequest().authenticated(); // 其他任何请求路径，登录后可访问
        // 开启自定义登录
        http.formLogin()
                .loginPage("/tologin").permitAll() //指定登录页面请求映射，并开放访问
                .usernameParameter("username").passwordParameter("pwd")
                .defaultSuccessUrl("/")
                .failureUrl("/tologin?err"); //指定错误提示页面
        // 开启自定义登出
        http.logout()
                .logoutUrl("/logout") //参数值须与注销页面表单的action值一致
                .logoutSuccessUrl("/");
        // 开启Remember-me功能
        http.rememberMe()
                .rememberMeParameter("rememberme") //须与登录表单勾选框中name属性值一致
                .tokenValiditySeconds(200) //设置“记住我”中Token有效期为200s
                .tokenRepository(tokenRepository()); //对Token进行持久化管理
    }
}
