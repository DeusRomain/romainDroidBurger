package fr.isen.cha.romaindroidburger

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var nom: EditText
    private lateinit var prenom: EditText
    private lateinit var adresse: EditText
    private lateinit var phone: EditText
    private lateinit var burgerSpinner: Spinner
    private lateinit var timeButton: Button
    private lateinit var timeText: TextView
    private lateinit var submitButton: Button

    private var deliveryTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nom = findViewById(R.id.nom)
        prenom = findViewById(R.id.prenom)
        adresse = findViewById(R.id.adresse)
        phone = findViewById(R.id.phone)
        burgerSpinner = findViewById(R.id.burger_spinner)
        timeButton = findViewById(R.id.time_button)
        timeText = findViewById(R.id.time_text)
        submitButton = findViewById(R.id.submit_button)

        val burgerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.burger_list,
            android.R.layout.simple_spinner_item
        )
        burgerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        burgerSpinner.adapter = burgerAdapter

        timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                val timeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                deliveryTime = timeFormat.format(calendar.time)
                timeText.text = deliveryTime
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }

        submitButton.setOnClickListener {
            val name = nom.text.toString()
            val firstName = prenom.text.toString()
            val address = adresse.text.toString()
            val phoneNumber = phone.text.toString()
            val selectedBurger = burgerSpinner.selectedItem.toString()

            if (name.isEmpty() || firstName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || deliveryTime.isEmpty()) {
                Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_LONG).show()
            } else {
                val order = JSONObject()
                order.put("firstname", firstName)
                order.put("lastname", name)
                order.put("address", address)
                order.put("phone", phoneNumber)
                order.put("burger", selectedBurger)
                order.put("delivery_time", deliveryTime)

                val requestBody = JSONObject()
                requestBody.put("id_shop", "1")
                requestBody.put("id_user", 353) // Remplacez par votre numéro unique
                requestBody.put("msg", order.toString())

                // Envoyer la commande au serveur
                sendOrderToServer(requestBody)
            }
        }
    }
    private fun sendOrderToServer(requestBody: JSONObject) {
        val url = "http://test.api.catering.bluecodegames.com/user/order"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            Response.Listener { response ->
                if (response.getString("result") == "ok") {
                    startActivity(Intent(this, ConfirmationActivity::class.java))
                } else {
                    Toast.makeText(this, "La commande n'a pas aboutie", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Erreur: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}
