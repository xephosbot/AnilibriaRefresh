import com.xbot.resources.localization.toLocalizedString
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTimeTests {

    @Test
    fun `test Russian locale formatting`() {
        val result = LocalDateTime(2019, Month.JANUARY, 19, 23, 4).toLocalizedString()

        assertEquals("19 янв. 2019 23:04", result)
    }

    @Test
    fun `test English locale formatting`() {
        val result = LocalDateTime(2019, Month.JANUARY, 19, 23, 4).toLocalizedString()

        assertEquals("19 Jan. 2019 23:04", result)
    }

    @Test
    fun `test default locale formatting`() {
        val result = LocalDateTime(2019, Month.JANUARY, 19, 23, 4).toLocalizedString()

        assertTrue(result.contains("19"))
        assertTrue(result.contains("2019"))
        assertTrue(result.contains("23:04"))
    }
}