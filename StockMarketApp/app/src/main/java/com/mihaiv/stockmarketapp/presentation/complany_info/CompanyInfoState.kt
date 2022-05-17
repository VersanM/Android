package com.mihaiv.stockmarketapp.presentation.complany_info

import com.mihaiv.stockmarketapp.domain.model.CompanyInfo
import com.mihaiv.stockmarketapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)