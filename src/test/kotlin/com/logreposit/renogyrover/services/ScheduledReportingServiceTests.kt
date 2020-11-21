package com.logreposit.renogyrover.services

import com.logreposit.renogyrover.communication.renogy.RenogyClient
import com.logreposit.renogyrover.communication.renogy.RenogyRamData
import com.logreposit.renogyrover.services.logreposit.LogrepositApiService
import com.logreposit.renogyrover.services.logreposit.SharedTestData.sampleRamData
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScheduledReportingServiceTests {

    private val renogyClient: RenogyClient = mock()
    private val logrepositApiService: LogrepositApiService = mock()
    private val renogyRamDataArgumentCaptor = argumentCaptor<RenogyRamData>()

    @Test
    fun `test readAndReport expect data read from renogy client and pushed to logreposit api`() {
        val scheduledReportingService = ScheduledReportingService(
                renogyClient,
                logrepositApiService
        )

        val ramData = sampleRamData()

        whenever(renogyClient.getRamData()).thenReturn(ramData)

        scheduledReportingService.readAndReport()

        verify(renogyClient, times(1)).getRamData()
        verify(logrepositApiService, times(1)).pushData(
                renogyRamDataArgumentCaptor.capture()
        )

        assertThat(renogyRamDataArgumentCaptor.allValues).hasSize(1)
        assertThat(renogyRamDataArgumentCaptor.firstValue).isSameAs(ramData)
    }
}
