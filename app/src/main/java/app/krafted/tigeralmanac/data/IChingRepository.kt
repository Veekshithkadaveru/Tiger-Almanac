package app.krafted.tigeralmanac.data

import android.content.Context
import app.krafted.tigeralmanac.model.Hexagram
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class IChingRepository(private val context: Context) {
    private val gson = Gson()
    private var cachedHexagrams: List<Hexagram>? = null

    suspend fun loadHexagrams(): List<Hexagram> = withContext(Dispatchers.IO) {
        cachedHexagrams ?: try {
            context.assets.open("iching.json").use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val type = object : TypeToken<Map<String, List<Hexagram>>>() {}.type
                    val data: Map<String, List<Hexagram>> = gson.fromJson(reader, type)
                    val result = data["hexagrams"] ?: emptyList()
                    cachedHexagrams = result
                    result
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
