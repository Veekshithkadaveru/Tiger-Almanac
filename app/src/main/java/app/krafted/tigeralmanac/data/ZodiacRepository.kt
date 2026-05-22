package app.krafted.tigeralmanac.data

import android.content.Context
import app.krafted.tigeralmanac.model.ZodiacProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class ZodiacRepository(private val context: Context) {
    private val gson = Gson()
    private var cachedAnimals: List<ZodiacProfile>? = null

    suspend fun loadAnimals(): List<ZodiacProfile> = withContext(Dispatchers.IO) {
        cachedAnimals ?: try {
            context.assets.open("zodiac.json").use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val type = object : TypeToken<Map<String, List<ZodiacProfile>>>() {}.type
                    val data: Map<String, List<ZodiacProfile>> = gson.fromJson(reader, type)
                    val result = data["animals"] ?: emptyList()
                    cachedAnimals = result
                    result
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
