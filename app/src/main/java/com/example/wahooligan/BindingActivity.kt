import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import com.example.wahooligan.HardwareService
import com.example.wahooligan.R

class BindingActivity : Activity() {
    private lateinit var mService: HardwareService
    private var mBound: Boolean = false


    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {


        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to HardwareService, cast the IBinder and get LocalService instance
            val binder = service as HardwareService.LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, HardwareService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute)  */
    fun onButtonClick(v: View) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            val num: Int = mService.randomNumber
            Toast.makeText(this, "number: $num", Toast.LENGTH_SHORT).show()
        }
    }
}
