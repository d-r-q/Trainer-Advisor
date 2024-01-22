package pro.qyoga.core.users

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories


@ComponentScan
@Configuration
@EnableJdbcRepositories
class UsersConfig