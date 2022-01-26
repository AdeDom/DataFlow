package com.adedom.dataflow

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.adedom.dataflow.databinding.ActivityDataFlowBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DatabaseActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDataFlowBinding.inflate(layoutInflater)
    }

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        binding.tvTitle.text = title

        appDatabase = AppDatabase(this)

        appDatabase.dataFlowDao().getDataFlow().asLiveData().observe(this) { data ->
            val dataString = data.joinToString { dataFlowEntity ->
                dataFlowEntity.data
            }
            binding.tvData.text = dataString
        }

        binding.btnSendData.setOnClickListener {
            val data = System.currentTimeMillis().toString()
            sendData(data)
        }
    }

    private fun sendData(data: String) {
        lifecycleScope.launch {
            val dataFlowEntity = DataFlowEntity(data = data)
            appDatabase.dataFlowDao().saveDataFlow(dataFlowEntity)
        }
    }
}

@Database(
    version = 1,
    entities = [
        DataFlowEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { appDatabase ->
                instance = appDatabase
            }
        }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, "DataFlow.db")
            .build()
    }

    abstract fun dataFlowDao(): DataFlowDao
}

@Entity(tableName = "data_flow")
data class DataFlowEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "data")
    val data: String,
)

@Dao
interface DataFlowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDataFlow(dataFlowEntity: DataFlowEntity)

    @Query("SELECT * FROM data_flow")
    fun getDataFlow(): Flow<List<DataFlowEntity>>
}