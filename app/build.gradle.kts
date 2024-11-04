plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.ndmq.moneynote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ndmq.moneynote"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ssp sdp
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // glide
    implementation(libs.glide)
    implementation(libs.androidsvg)
    kapt(libs.compiler)

    // viewmodel
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // room
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.room.compiler)
    kapt(libs.androidx.room.room.compiler)

    // navigation component
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // mmkv
    implementation(libs.mmkv)

    // lottie file
    implementation(libs.lottie)

    // calendar
    implementation(libs.view)
}

kapt {
    correctErrorTypes = true
}
