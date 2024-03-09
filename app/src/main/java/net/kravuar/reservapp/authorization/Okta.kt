package net.kravuar.reservapp.authorization

import android.content.Context
import com.okta.authfoundation.AuthFoundationDefaults
import com.okta.authfoundation.client.OidcClient
import com.okta.authfoundation.client.OidcClientResult
import com.okta.authfoundation.client.OidcConfiguration
import com.okta.authfoundation.client.SharedPreferencesCache
import com.okta.authfoundation.credential.CredentialDataSource.Companion.createCredentialDataSource
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.okta.webauthenticationui.WebAuthenticationClient.Companion.createWebAuthenticationClient
import net.kravuar.reservapp.BuildConfig
import okhttp3.HttpUrl.Companion.toHttpUrl

object OktaAuthorization {
    private lateinit var oidcClient: OidcClient

    fun initialize(context: Context) {
        AuthFoundationDefaults.cache = SharedPreferencesCache.create(context)
        val oidcConfiguration = OidcConfiguration(
            clientId = BuildConfig.CLIENT_ID,
            defaultScope = "openid email profile offline_access"
        )
        oidcClient = OidcClient.createFromDiscoveryUrl(
            oidcConfiguration,
            BuildConfig.DISCOVERY_URL.toHttpUrl()
        )
        CredentialBootstrap.initialize(oidcClient.createCredentialDataSource(context))
    }
}

suspend fun login(context: Context, onLoginSuccessful: () -> Unit, onError: (String) -> Unit) {
    val result = CredentialBootstrap.oidcClient.createWebAuthenticationClient().login(
        context = context,
        redirectUrl = BuildConfig.SIGN_IN_REDIRECT_URI,
    )
    when (result) {
        is OidcClientResult.Error -> {
            onError("Failed to login: ${result.exception.message}")
        }

        is OidcClientResult.Success -> {
            val credential = CredentialBootstrap.defaultCredential()
            credential.storeToken(token = result.result)
            onLoginSuccessful()
        }
    }
}

suspend fun logout(
    context: Context,
    onLogoutSuccessful: () -> Unit,
    onError: (String) -> Unit
) {
    val result = CredentialBootstrap.oidcClient.createWebAuthenticationClient().logoutOfBrowser(
        context = context,
        redirectUrl = BuildConfig.SIGN_OUT_REDIRECT_URI,
        CredentialBootstrap.defaultCredential().token?.idToken ?: "",
    )
    when (result) {
        is OidcClientResult.Error -> {
            onError("Failed to logout: ${result.exception.message}")
        }

        is OidcClientResult.Success -> {
            CredentialBootstrap.defaultCredential().delete()
            onLogoutSuccessful()
        }
    }
}

suspend fun isAuthorized(): Boolean {
    return CredentialBootstrap.defaultCredential().getValidAccessToken() == null
}