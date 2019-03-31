package Falcon.Configuration

import Falcon.Service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsServiceImpl userDetailsService

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider())
    }

    //access in to the css files
    @Override
    void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/resources/**")
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        //now access  to creator and posts need authentication
        //if user isn't authenticated application redirect user to login page
        http.authorizeRequests().antMatchers("/creator","/posts","user/panel","error/errorPage")
                .hasAnyAuthority("READ_AUTHORITY","WRITE_PRIVILEGE")
                .and()
                .formLogin()
                .loginPage("/login") //custom login page
                //.loginProcessingUrl("/login_processing")
                // we are in charge of rendering a failure page when /login?error is requested
//                .failureUrl("/loginUser?error=error")
                .failureUrl("/login")
                .defaultSuccessUrl("/posts", true)
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")

        http.csrf().disable()


    }


    @Bean
     DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }



}
