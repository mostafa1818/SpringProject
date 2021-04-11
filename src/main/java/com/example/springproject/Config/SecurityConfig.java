package com.example.springproject.Config;

import com.example.springproject.entity.UserRole;
import com.example.springproject.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
        {

            auth.authenticationProvider(authenticationProvider());
        }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/Styles/**");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()
        {
            DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
            provider.setPasswordEncoder(passwordEncoder);
            provider.setUserDetailsService(userDetailsService);
            return provider;
        }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf().disable()
                .authorizeRequests()

                .antMatchers(   "/api/v2/registration/**" ,"/register" )
                .permitAll()

                .antMatchers( HttpMethod.GET ,"/adminExcelReport"  )
                .hasAuthority(UserRole.ROLE_ADMIN.toString())
                .antMatchers(HttpMethod.GET ,"/responsibleReport/**" )
                .hasAnyAuthority(UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_ADMIN.toString())
                .antMatchers(HttpMethod.GET ,"/userReport/**" )
                .hasAnyAuthority(UserRole.ROLE_ADMIN.toString(),UserRole.ROLE_USER.toString())
                .antMatchers( HttpMethod.GET ,"/adminReport/**"  )
                .hasAuthority(UserRole.ROLE_ADMIN.toString())

                .antMatchers( HttpMethod.GET ,"/adminDashbord/**"  )
                .hasAuthority(UserRole.ROLE_ADMIN.toString())

                .antMatchers(HttpMethod.GET ,"/edite/**" )
                .hasAnyAuthority(UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_USER.toString())
                .antMatchers( "/editePassword/**" )
                .hasAnyAuthority(UserRole.ROLE_USER.toString(),UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_ADMIN.toString())

                .antMatchers(HttpMethod.GET ,"/userPanel" )
                .hasAnyAuthority(UserRole.ROLE_USER.toString(),UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_ADMIN.toString())

                .antMatchers(HttpMethod.GET  ,"/api/v3/changeEmail/**"  )
                .hasAnyAuthority(UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_USER.toString())
                .antMatchers(HttpMethod.GET  ,"/api/investmentController/adminInvesment/**" )
                .hasAnyAuthority(UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_USER.toString(),UserRole.ROLE_ADMIN.toString())

                .antMatchers(HttpMethod.GET  ,"/api/v3/changeEmail/editeEmailLogin/**" )
                .hasAuthority(UserRole.ROLE_USER.toString())
                .antMatchers(HttpMethod.GET    ,"/issueController/addIssue/**"  )
                .hasAuthority(UserRole.ROLE_RESPONSIBLE.toString())
                .antMatchers(HttpMethod.GET    ,"/issueController/editeIssue/**"   )
                .hasAuthority(UserRole.ROLE_RESPONSIBLE.toString())
                .antMatchers(HttpMethod.GET    ,"/issueController/deleteIssue/**"   )
                .hasAnyAuthority(UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_ADMIN.toString())
                .antMatchers(HttpMethod.GET    ,"/showAndEditeInvesment/**"  )
                .hasAuthority(UserRole.ROLE_USER.toString())
                .antMatchers(HttpMethod.GET    ,"/api/investmentController/applyInvesment/**" )
                .hasAuthority(UserRole.ROLE_USER.toString())

                .antMatchers(HttpMethod.GET  ,"/reportpdf" )
                .hasAnyAuthority(UserRole.ROLE_RESPONSIBLE.toString(),UserRole.ROLE_USER.toString(),UserRole.ROLE_ADMIN.toString())

                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/userPanel")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logOut"))
                .logoutSuccessUrl("/login")
                .permitAll();
        }
}
