package com.korikmat.transparentaccounts.di

import androidx.lifecycle.SavedStateHandle
import com.google.gson.GsonBuilder
import com.korikmat.data.repositories.AccountsRepositoryImpl
import com.korikmat.data.source.EDPApiService
import com.korikmat.data.source.RemoteAccountsSource
import com.korikmat.domain.repositories.AccountsRepository
import com.korikmat.domain.usecases.GetAccountDetailsUseCase
import com.korikmat.domain.usecases.GetAccountsUseCase
import com.korikmat.domain.usecases.GetTransactionsUseCase
import com.korikmat.transparentaccounts.BuildConfig
import com.korikmat.transparentaccounts.ui.viewModels.AccountDetailsScreenViewModel
import com.korikmat.transparentaccounts.ui.viewModels.AccountsScreenViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        GsonBuilder()
            .create()
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://webapi.developers.erstegroup.com/api/csas/public/sandbox/v3/")
//            .baseUrl("https://www.csas.cz/webapi/api/v3/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<EDPApiService> {
        val retrofit: Retrofit = get()
        retrofit.create(EDPApiService::class.java)
    }
}

val dataModule = module {
    single<RemoteAccountsSource> { RemoteAccountsSource(get(), BuildConfig.API_KEY) }
    single<AccountsRepository> { AccountsRepositoryImpl(get()) }
}

val domainModule = module {
    factory { GetTransactionsUseCase(get()) }
    factory { GetAccountsUseCase(get()) }
    factory { GetAccountDetailsUseCase(get()) }
}

val viewModelModule = module {
    viewModel { AccountsScreenViewModel(get()) }
    viewModel { (handle: SavedStateHandle) ->
        AccountDetailsScreenViewModel(get(), get(), handle)
    }

}

val appModules = listOf(
    networkModule,
    dataModule,
    domainModule,
    viewModelModule
)