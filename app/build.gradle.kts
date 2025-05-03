plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "org.sounfury.cyber_hamster"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.sounfury.cyber_hamster"
        minSdk = 30
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // AndroidX核心库
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.core:core-ktx:1.12.0")
    
    // ViewModel和LiveData (MVVM架构组件)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    
    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    
    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // CardView
    implementation("androidx.cardview:cardview:1.0.0")
    
    // Material Design
    implementation("com.google.android.material:material:1.10.0")
    
    // Retrofit 核心库
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit 转换器：把返回的 JSON 自动转成Java对象（Gson）
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // RxJava 适配器
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

    // RxJava3 和 RxAndroid
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    // OkHttp核心库
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // OkHttp日志拦截器（开发调试时用）
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Glide的核心库
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.swiperefreshlayout)

    // Glide编译时注解处理器（可选，生成一些帮助代码，推荐一起加上）
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}