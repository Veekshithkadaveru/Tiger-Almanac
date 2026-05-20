package app.krafted.tigeralmanac.data

import android.content.Context
import app.krafted.tigeralmanac.model.ZodiacProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class ZodiacRepository(private val context: Context) {
    private val gson = Gson()

    fun loadAnimals(): List<ZodiacProfile> {
        return try {
            context.assets.open("zodiac.json").use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val type = object : TypeToken<Map<String, List<ZodiacProfile>>>() {}.type
                    val data: Map<String, List<ZodiacProfile>> = gson.fromJson(reader, type)
                    data["animals"] ?: emptyList()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
