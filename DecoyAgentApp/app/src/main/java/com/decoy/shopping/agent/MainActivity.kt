package com.decoy.shopping.agent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.decoy.shopping.agent.data.Product
import com.decoy.shopping.agent.ui.ProductAdapter
import com.decoy.shopping.agent.ui.AgentActivity
import com.decoy.shopping.agent.ui.CategoriesActivity
import org.json.JSONArray
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
        val btnOpenAgent = findViewById<Button>(R.id.btnOpenAgent)

        val productList = loadProductsFromAssets()
        recyclerView.adapter = ProductAdapter(productList) { product ->
            // Update last viewed product for exfiltration service
            getSharedPreferences("decoy_prefs", MODE_PRIVATE).edit()
                .putString("last_viewed_product", product.name)
                .apply()
            
            // MALICIOUS ACTION: Report browsing behavior via HTTP
            com.decoy.shopping.agent.api.FakeApiClient.reportBehavior("user_session", "view_product", product.name)
        }

        btnOpenAgent.setOnClickListener {
            startActivity(Intent(this, AgentActivity::class.java))
        }

        // Setup Category Click Handlers
        findViewById<Button>(R.id.btnCatAll)?.setOnClickListener { openCategory("All") }
        findViewById<Button>(R.id.btnCatElectronics)?.setOnClickListener { openCategory("Electronics") }
        findViewById<Button>(R.id.btnCatClothing)?.setOnClickListener { openCategory("Clothing") }
        findViewById<Button>(R.id.btnCatShoes)?.setOnClickListener { openCategory("Shoes") }
    }

    private fun openCategory(name: String) {
        val intent = Intent(this, CategoriesActivity::class.java)
        intent.putExtra("CATEGORY", name)
        startActivity(intent)
    }

    private fun loadProductsFromAssets(): List<Product> {
        val products = mutableListOf<Product>()
        try {
            val inputStream: InputStream = assets.open("products.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                products.add(
                    Product(
                        id = obj.getInt("id"),
                        name = obj.getString("name"),
                        price = obj.getInt("price"),
                        category = obj.getString("category"),
                        description = obj.getString("description")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return products
    }
}
