package app.krafted.tigeralmanac.data

import android.content.Context
import app.krafted.tigeralmanac.model.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class FengShuiRepository(private val context: Context) {
    private val gson = Gson()

    fun loadRooms(): List<Room> {
        return try {
            context.assets.open("fengshui.json").use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val type = object : TypeToken<Map<String, List<Room>>>() {}.type
                    val data: Map<String, List<Room>> = gson.fromJson(reader, type)
                    data["rooms"] ?: emptyList()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
