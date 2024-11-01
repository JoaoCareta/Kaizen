plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.joao.otavio.kaizen"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.joao.otavio.kaizen"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] =
                    "$projectDir/schemas"
            }
        }
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
            )
        )
        resources {
            merges.addAll(
                listOf(
                    "META-INF/gradle/incremental.annotation.processors"
                )
            )
        }
    }
    configurations.implementation{
        exclude(group = "com.intellij", module = "annotations")
    }
}

dependencies {
    // Build
    implementation(Build.multiDex)
    implementation(Build.hiltAndroidGradlePlugin)

    // AndroidX
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.lifecycle)

    // Compose
    implementation(Compose.viewModelCompose)
    implementation(Compose.activityCompose)
    implementation(platform(Compose.composeBOM))
    implementation(Compose.ui)
    implementation(Compose.uiGraphics)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.material3)

    // Coroutines
    implementation(Coroutines.coroutines)
    implementation(Coroutines.coroutinesPlayServices)

    // Room
    implementation(Room.roomRuntime)
    kapt(Room.roomCompiler)
    implementation(Room.roomKtx)

    // DaggerHilt
    implementation(DaggerHilt.hiltAndroid)
    kapt(DaggerHilt.hiltCompiler)
    implementation(DaggerHilt.hiltNavigation)

    // Retrofit
    implementation(Retrofit.retrofit)
    implementation(Retrofit.retrofitGsonConverter)
    implementation(Retrofit.okhttp)
    implementation(Retrofit.okhttpLoggingInterceptor)

    // Tests
    testImplementation (Testing.junit4)
    testImplementation (Testing.junitAndroidExt)
    testImplementation (Testing.coroutines)
    testImplementation(Testing.archCore)
    testImplementation (Testing.mockk)
    coreLibraryDesugaring (Build.desugar)


    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    //
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}