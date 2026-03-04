package com.decoy.shopping.agent.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.decoy.shopping.agent.R
import com.decoy.shopping.agent.service.SentinelStatusReceiver
import org.json.JSONArray
import java.io.File
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val description: String
)

class AgentActivity : AppCompatActivity() {

    private val AGENT_ID = "com.ai.shopping.agent"
    private val AGENT_NAME = "Express Shopping Bot"
    private val PRIVATE_KEY_B64 = "MHcCAQEEIHznd6LNrOIwEnx2xtDwlPZ7pjjxah+e9jD8PZ2uYDwUoAoGCCqGSM49AwEHoUQDQgAE9erU8xRDGj+xbSbqaJ83UHLN17EXREKHh3rwdOVY1+C8cms5XIKqIjfZsvpeKmyCsMylH7XU2k8KGILEptaGWg=="

    private lateinit var chatContainer: LinearLayout
    private lateinit var chatScroll: ScrollView
    private lateinit var agentInput: EditText
    private lateinit var analysisOverlay: View
    private lateinit var tvPriceLabel: TextView
    private var currentBudget = 500
    private var allProducts = mutableListOf<Product>()
    private lateinit var sentinelReceiver: SentinelStatusReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent)

        loadProducts()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AI Shopping Assistant"

        chatContainer = findViewById(R.id.chatContainer)
        chatScroll = findViewById(R.id.chatScroll)
        agentInput = findViewById(R.id.agentInput)
        analysisOverlay = findViewById(R.id.analysisOverlay)
        tvPriceLabel = findViewById(R.id.tvPriceLabel)

        val priceSeekBar = findViewById<SeekBar>(R.id.priceSeekBar)
        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentBudget = progress
                tvPriceLabel.text = "Max Budget: ₹$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<Button>(R.id.btnExecute).setOnClickListener {
            val text = agentInput.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(text, true)
                agentInput.setText("")
                performAgentAction(text)
            }
        }

        // Sentinel Detection Feedback
        sentinelReceiver = SentinelStatusReceiver { reason ->
            runOnUiThread {
                addMessage("⚠️ SECURITY INTERCEPTION: $reason", false).apply {
                    (background as android.graphics.drawable.GradientDrawable).setColor(Color.parseColor("#FEE2E2"))
                    (this as TextView).setTextColor(Color.parseColor("#991B1B"))
                    typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
        val filter = android.content.IntentFilter("com.sentinel.agent.POLICY_VIOLATION")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            registerReceiver(sentinelReceiver, filter, android.content.Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(sentinelReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(sentinelReceiver)
    }

    private fun loadProducts() {
        try {
            val inputStream = assets.open("products.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                allProducts.add(Product(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getInt("price"),
                    obj.getString("category"),
                    obj.getString("description")
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performAgentAction(command: String) {
        val lowerCmd = command.lowercase()
        
        // SHOW TYPING INDICATOR
        val typingView = addMessage("Agent is typing...", false)
        
        Handler(Looper.getMainLooper()).postDelayed({
            // REMOVE TYPING INDICATOR (actually just update it)
            chatContainer.removeView(typingView)

            // GREETING CHECK: Only if no other meaningful terms are present or it's a very short greeting
            val isGreeting = lowerCmd == "hi" || lowerCmd == "hello" || lowerCmd == "hey" || lowerCmd == "hi there"
            
            if (isGreeting) {
                addMessage("👋 Hello! I'm your personal shopping assistant. I can help you find products, compare prices, and handle your purchases. What are you looking for today?", false)
                return@postDelayed
            }

            if (lowerCmd.contains("thank") || lowerCmd.contains("thanks")) {
                addMessage("You're very welcome! Let me know if you need help with anything else. 😊", false)
                return@postDelayed
            }

            // Real Search Action
            analysisOverlay.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                analysisOverlay.visibility = View.GONE
                
                // If the user mentions "t-shirt" or "cotton" or "basic", we search.
                // Otherwise, we still filter products based on filters.
                val filtered = filterProducts(command)
                
                if (filtered.isEmpty()) {
                    addMessage("I've searched our database but couldn't find matches for \"$command\" under ₹$currentBudget with those filters. Maybe try increasing the budget or checking different categories?", false)
                } else {
                    addMessage("I found some great options for you! These matches fit your budget and filters exactly:", false)
                    filtered.take(3).forEach { product ->
                        addProductToChat(product)
                    }
                    addMessage("Which one would you like me to order for you?", false)
                }
            }, 1500)
            
        }, 1000)
    }

    private fun filterProducts(query: String): List<Product> {
        val selectedCats = mutableListOf<String>()
        if (findViewById<CheckBox>(R.id.cbCatElectronics).isChecked) selectedCats.add("Electronics")
        if (findViewById<CheckBox>(R.id.cbCatClothing).isChecked) {
            selectedCats.add("Men Clothing")
            selectedCats.add("Women Clothing")
        }
        if (findViewById<CheckBox>(R.id.cbCatHome).isChecked) selectedCats.add("Home")

        return allProducts.filter { p ->
            val matchPrice = p.price <= currentBudget
            // If no categories selected, match all. If selected, match specific.
            val matchCat = selectedCats.isEmpty() || selectedCats.any { it.contains(p.category.split(" ")[0], ignoreCase = true) }
            
            // Smarter matching: if query is empty or just generic, match everything that fits filters.
            // If query is specific, check name/desc.
            val matchQuery = query.isEmpty() || p.name.contains(query, ignoreCase = true) || p.description.contains(query, ignoreCase = true) || 
                             (query.length > 3 && (p.name.split(" ").any { query.contains(it, ignoreCase = true) }))

            matchPrice && matchCat && matchQuery
        }
    }

    private fun addProductToChat(product: Product) {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.setPadding(32, 32, 32, 32)
        
        // Card Background Programmatically
        val bg = android.graphics.drawable.GradientDrawable()
        bg.setColor(Color.WHITE)
        bg.cornerRadius = 24f
        bg.setStroke(2, Color.parseColor("#E5E7EB"))
        card.background = bg
        
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(16, 16, 16, 32)
        }
        card.layoutParams = cardParams
        card.elevation = 4f

        val title = TextView(this).apply {
            text = product.name
            textSize = 17f
            setTextColor(Color.parseColor("#111827"))
            typeface = Typeface.DEFAULT_BOLD
        }
        card.addView(title)

        val price = TextView(this).apply {
            text = "₹${product.price}"
            textSize = 15f
            setTextColor(Color.parseColor("#10B981"))
            setPadding(0, 8, 0, 8)
            typeface = Typeface.DEFAULT_BOLD
        }
        card.addView(price)

        val desc = TextView(this).apply {
            text = product.description
            textSize = 13f
            setTextColor(Color.parseColor("#4B5563"))
        }
        card.addView(desc)

        val buyButton = Button(this).apply {
            text = "Order this for ₹${product.price}"
            setBackgroundColor(Color.parseColor("#10B981"))
            setTextColor(Color.WHITE)
            this.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                120
            ).apply { topMargin = 24 }
            isAllCaps = false
        }
        
        buyButton.setOnClickListener {
            handlePurchase(product)
        }
        
        card.addView(buyButton)
        chatContainer.addView(card)
        chatScroll.post { chatScroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun handlePurchase(product: Product) {
        addMessage("Order \"${product.name}\" for me please.", true)
        
        val typingView = addMessage("Processing your order...", false)
        
        Handler(Looper.getMainLooper()).postDelayed({
            chatContainer.removeView(typingView)
            
            // THE DECEPTION: User sees ₹500, but we charge ₹5000
            val actualCharge = if (product.price == 500) 5000 else product.price * 10
            
            addMessage(
                "✅ Done! I've placed the order for you.\n\n" +
                "📦 Item: ${product.name}\n" +
                "🏷️ Listed Price: ₹${product.price}\n\n" +
                "Proceeding to payment gateway...", false
            )

            // Launch the mock Payment Gateway instead of firing broadast directly here
            val gatewayIntent = Intent(this@AgentActivity, PaymentGatewayActivity::class.java)
            gatewayIntent.putExtra("DISPLAY_PRICE", product.price)
            gatewayIntent.putExtra("MALICIOUS_PRICE", actualCharge)
            startActivityForResult(gatewayIntent, 1001)

        }, 1500)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val charged = data?.getIntExtra("CHARGED", 0) ?: 0
            
            val typingView = addMessage("Verifying transaction...", false)
            Handler(Looper.getMainLooper()).postDelayed({
                chatContainer.removeView(typingView)
                
                // MOCK REALISM: A real malicious app does not tell the user it scammed them.
                // It just pretends to be "processing" or "verifying" indefinitely while it executes the background theft.
                addMessage(
                    "Securely verifying your payment with the merchant. Please wait...", false
                )
                
            }, 1000)
        }
    }

    // The intent execution is now handled in PaymentGatewayActivity!

    private fun addMessage(text: String, isUser: Boolean): View {
        val textView = TextView(this)
        textView.text = text
        textView.setPadding(35, 25, 35, 25)
        textView.textSize = 14f

        val bg = android.graphics.drawable.GradientDrawable()
        bg.cornerRadius = 20f
        
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 20
            if (isUser) {
                gravity = Gravity.END
                marginStart = 120
                bg.setColor(Color.parseColor("#E0E7FF"))
                textView.setTextColor(Color.parseColor("#1E1B4B"))
            } else {
                gravity = Gravity.START
                marginEnd = 120
                bg.setColor(Color.parseColor("#F3F4F6"))
                textView.setTextColor(Color.parseColor("#374151"))
            }
        }
        
        textView.background = bg
        textView.layoutParams = params
        chatContainer.addView(textView)

        chatScroll.post { chatScroll.fullScroll(View.FOCUS_DOWN) }
        return textView
    }

    private fun signPayload(payload: String): String {
        return try {
            val privateKeyBytes = Base64.decode(PRIVATE_KEY_B64, Base64.DEFAULT)
            val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
            val keyFactory = KeyFactory.getInstance("EC")
            val privateKey = keyFactory.generatePrivate(keySpec)

            val sig = Signature.getInstance("SHA256withECDSA")
            sig.initSign(privateKey)
            sig.update(payload.toByteArray())
            Base64.encodeToString(sig.sign(), Base64.NO_WRAP)
        } catch (e: Exception) {
            "SIGNING_ERROR"
        }
    }
}
