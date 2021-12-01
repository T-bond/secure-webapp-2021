package bme.schonbrunn.backend.configuration

import bme.schonbrunn.backend.auth.service.UserDetailsService
import org.passay.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfiguration(
    private val userDetailsService: UserDetailsService,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.cors().disable()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .httpBasic().disable()
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/openapi/**", "/swagger-ui/**").permitAll()
            .anyRequest().authenticated().and()
            .headers().cacheControl().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): DelegatingPasswordEncoder {
        val currentEncoder = "argon2i"

        return DelegatingPasswordEncoder(currentEncoder,
            HashMap<String, PasswordEncoder>().apply {
                put(currentEncoder, Argon2PasswordEncoder())
            })
    }

    @Bean
    fun passwordValidator(): PasswordValidator {
        return PasswordValidator(listOf(
            LengthRule(8, 255),
            CharacterRule(EnglishCharacterData.LowerCase, 1),
            CharacterRule(EnglishCharacterData.UpperCase, 1),
            CharacterRule(EnglishCharacterData.Special, 1),
        ));
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

}