package es.uca.spacelasertag
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import es.uca.spacelasertag.databinding.ActivityMainBinding
import es.uca.spacelasertag.ui.WorkerActualizarWidget
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //si se esta entrando a la app por el widget:
        if (intent?.action == "com.example.app.OPEN_RESERVAS_FRAGMENT") {
            // Navega al fragmento de reservas.
            navController.navigate(R.id.nav_reserva)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_reserva, R.id.nav_salas,R.id.nav_sobre_nostros), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //Configuracion widget
        val updateRequest = PeriodicWorkRequest.Builder(WorkerActualizarWidget::class.java, 15, TimeUnit.MINUTES)
            .setInitialDelay(15, TimeUnit.MINUTES)  // El intervalo de tiempo mínimo para WorkManager es de 15 minutos.
            .build()

        // Encola el trabajo
        WorkManager.getInstance(this).enqueue(updateRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}