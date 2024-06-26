package fr.isen.cha.romaindroidburger
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var orderDetails: TextView
    private lateinit var orderList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        orderDetails = findViewById(R.id.order_details)
        orderList = findViewById(R.id.order_list)

        orderDetails.text = "Votre commande a été passée avec succès."
        getOrderHistory()
    }

    private fun getOrderHistory() {
        val url = "http://test.api.catering.bluecodegames.com/listorders"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val requestBody = JSONObject()
        requestBody.put("id_shop", "1")
        requestBody.put("id_user", 353)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            Response.Listener { response ->
                val orders = response.getJSONArray("data")
            },
            Response.ErrorListener { error ->

            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}
