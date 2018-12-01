package com.hubelias.parkingmeter.port.adapter.users

import com.hubelias.parkingmeter.domain.driver.Driver
import com.hubelias.parkingmeter.domain.driver.DriverProvider
import com.hubelias.parkingmeter.domain.driver.UserId
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UsersAdapter : DriverProvider, UserDetailsService {
    private data class UnsafeUser(
            val username: String,
            val password: String,
            val roles: Set<String>
    )

    private val users = listOf(
            UnsafeUser("admin", "admin", setOf(UserRole.DRIVER, UserRole.OPERATOR, UserRole.OWNER)),
            UnsafeUser("s.hawking", "universe", setOf(UserRole.DRIVER)),
            UnsafeUser("jonbonjovi","itsmylife", setOf(UserRole.DRIVER)),
            UnsafeUser("m.jackson","thriller", setOf(UserRole.OPERATOR)),
            UnsafeUser("d.trump", "imawesome", setOf(UserRole.OWNER))
    )

    override fun getDriver(userId: UserId): Driver? {
        return Driver(userId, Driver.Type.REGULAR)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return users.find {it.username == username }?.let { User
                .withDefaultPasswordEncoder()
                .username(it.username)
                .password(it.password)
                .roles(*it.roles.toTypedArray())
                .build()
        } ?: throw UsernameNotFoundException("No such user: $username")
    }
}
