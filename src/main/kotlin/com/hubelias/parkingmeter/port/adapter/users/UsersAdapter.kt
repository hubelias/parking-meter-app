package com.hubelias.parkingmeter.port.adapter.users

import com.hubelias.parkingmeter.domain.driver.Driver
import com.hubelias.parkingmeter.domain.driver.DriverId
import com.hubelias.parkingmeter.domain.driver.DriverProvider
import com.hubelias.parkingmeter.domain.driver.UnknownDriverException
import com.hubelias.parkingmeter.port.adapter.rest.UserRole
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
            val isOwner: Boolean = false,
            val isOperator: Boolean = false,
            val driverType: Driver.Type? = null)

    private val users = listOf(
            UnsafeUser("admin", "admin", true, true, Driver.Type.REGULAR),
            UnsafeUser("s.hawking", "universe", driverType = Driver.Type.DISABLED),
            UnsafeUser("jonbonjovi", "itsmylife", driverType = Driver.Type.REGULAR),
            UnsafeUser("m.jackson", "thriller", isOperator = true),
            UnsafeUser("d.trump", "imawesome", isOwner = true)
    )

    override fun getDriver(driverId: DriverId): Driver {
        return findByUsername(driverId.username)?.driverType?.let { driverType ->
            Driver(driverId, driverType)
        } ?: throw UnknownDriverException(driverId)
    }

    private fun findByUsername(username: String) = users.find { it.username == username }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return findByUsername(username)?.let {
            User
                    .withDefaultPasswordEncoder()
                    .username(it.username)
                    .password(it.password)
                    .roles(*getRoles(it))
                    .build()
        } ?: throw UsernameNotFoundException("No such user: $username")
    }

    private fun getRoles(user: UnsafeUser) = listOfNotNull(
            UserRole.DRIVER.takeIf { user.driverType != null },
            UserRole.OPERATOR.takeIf { user.isOperator },
            UserRole.OWNER.takeIf { user.isOwner }
    ).toTypedArray()
}
