package com.exertia.wikingo.di

import androidx.room.Room
import com.exertia.wikingo.core.audio.SoundFeedbackManager
import com.exertia.wikingo.core.network.ConnectivityObserver
import com.exertia.wikingo.core.network.NetworkConnectivityObserver
import com.exertia.wikingo.data.generator.QuestionGenerator
import com.exertia.wikingo.data.local.WikiTrainerDatabase
import com.exertia.wikingo.data.local.SharedPreferencesHeartStorage
import com.exertia.wikingo.data.remote.WikipediaApiService
import com.exertia.wikingo.data.remote.WikipediaClient
import com.exertia.wikingo.data.repository.LessonRepositoryImpl
import com.exertia.wikingo.domain.repository.LessonRepository
import com.exertia.wikingo.domain.usecase.CompleteLessonUseCase
import com.exertia.wikingo.domain.usecase.GetLessonForTopicUseCase
import com.exertia.wikingo.domain.usecase.GetRandomLessonUseCase
import com.exertia.wikingo.domain.usecase.GetTopicHistoryUseCase
import com.exertia.wikingo.domain.usecase.GetUserStatsUseCase
import com.exertia.wikingo.domain.usecase.HeartStorage
import com.exertia.wikingo.domain.usecase.HeartsManager
import com.exertia.wikingo.domain.usecase.SubmitAnswerUseCase
import com.exertia.wikingo.presentation.history.HistoryViewModel
import com.exertia.wikingo.presentation.trainer.TrainerViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        OkHttpClient.Builder()
            .addInterceptor { chain ->
                // Wikimedia API policy requires a custom, descriptive User-Agent header
                val request = chain.request().newBuilder()
                    .header("User-Agent", "WikiTrainer/1.0 (Android; contact@example.com)")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .followRedirects(true)
            .build()
    }

    single {
        val contentType = "application/json".toMediaType()
        val json: Json = get()
        val client: OkHttpClient = get()

        Retrofit.Builder()
            .baseUrl("https://it.wikipedia.org/")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    single<WikipediaApiService> {
        get<Retrofit>().create(WikipediaApiService::class.java)
    }

    single<ConnectivityObserver> {
        NetworkConnectivityObserver(androidContext())
    }

    single {
        WikipediaClient(
            apiService = get(),
            connectivityObserver = get()
        )
    }

    single {
        SoundFeedbackManager(androidContext())
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            WikiTrainerDatabase::class.java,
            WikiTrainerDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<WikiTrainerDatabase>().topicHistoryDao() }
    single { get<WikiTrainerDatabase>().userStreakDao() }
    single<HeartStorage> {
        SharedPreferencesHeartStorage(
            androidContext().getSharedPreferences("wikingo_hearts", android.content.Context.MODE_PRIVATE)
        )
    }
}

val repositoryModule = module {
    single { QuestionGenerator() }

    single<LessonRepository> {
        LessonRepositoryImpl(
            wikipediaClient = get(),
            questionGenerator = get(),
            topicHistoryDao = get(),
            userStreakDao = get()
        )
    }
}

val useCaseModule = module {
    factory { GetRandomLessonUseCase(get()) }
    factory { GetLessonForTopicUseCase(get()) }
    factory { SubmitAnswerUseCase() }
    factory { CompleteLessonUseCase(get()) }
    factory { GetUserStatsUseCase(get()) }
    factory { GetTopicHistoryUseCase(get()) }
    single { HeartsManager(get()) }
}

val viewModelModule = module {
    viewModel {
        TrainerViewModel(
            getRandomLessonUseCase = get(),
            submitAnswerUseCase = get(),
            completeLessonUseCase = get(),
            getUserStatsUseCase = get(),
            soundFeedbackManager = get(),
            getLessonForTopicUseCase = get(),
            heartsManager = get()
        )
    }

    viewModel {
        HistoryViewModel(
            getTopicHistoryUseCase = get(),
            getUserStatsUseCase = get()
        )
    }
}

val appModules = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)
