import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

val oktaPropertiesFile = rootProject.file("okta.properties")
val oktaProperties = Properties()
oktaProperties.load(FileInputStream(oktaPropertiesFile))

fun parseScheme(uri: String): String {
    val index = uri.indexOf(":/")
    if (index == -1) {
        throw IllegalStateException("Scheme is not in a valid format.")
    }
    return uri.substring(0, index)
}

android {
    namespace = "net.kravuar.reservapp"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "DISCOVERY_URL", oktaProperties.getProperty("discoveryUrl"))
        buildConfigField("String", "CLIENT_ID", oktaProperties.getProperty("clientId"))
        buildConfigField("String", "SIGN_IN_REDIRECT_URI", oktaProperties.getProperty("signInRedirectUri"))
        buildConfigField("String", "SIGN_OUT_REDIRECT_URI", oktaProperties.getProperty("signOutRedirectUri"))

        applicationId = "net.kravuar.reservapp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["webAuthenticationRedirectScheme"] = oktaProperties.getProperty("signInRedirectUri")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.compose.material3:material3-android:1.2.0-rc01")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(platform("com.okta.kotlin:bom:1.2.1"))

    implementation("com.okta.kotlin:auth-foundation-bootstrap")
    implementation("com.okta.kotlin:web-authentication-ui")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.activity:activity-ktx:1.5.1")
}