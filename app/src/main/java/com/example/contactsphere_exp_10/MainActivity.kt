package com.example.contactsphere_exp_10

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnFetchContacts: MaterialButton
    private lateinit var tvStatusMessage: TextView
    private lateinit var rvContacts: RecyclerView

    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<ContactItem>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchContacts()
            } else {
                tvStatusMessage.text = getString(R.string.status_permission_denied)
                tvStatusMessage.visibility = View.VISIBLE
                rvContacts.visibility = View.GONE
                Toast.makeText(this, "Contact permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnFetchContacts = findViewById(R.id.btnFetchContacts)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)
        rvContacts = findViewById(R.id.rvContacts)

        contactAdapter = ContactAdapter(contactList)
        rvContacts.layoutManager = LinearLayoutManager(this)
        rvContacts.adapter = contactAdapter

        btnFetchContacts.setOnClickListener {
            checkPermissionAndFetch()
        }

        // Automatically ask for permission / load contacts on app startup
        checkPermissionAndFetch()
    }

    private fun checkPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchContacts()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun fetchContacts() {
        contactList.clear()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use { c ->
            val nameIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            val seenNumbers = mutableSetOf<String>()

            while (c.moveToNext()) {
                val name = if (nameIndex != -1) c.getString(nameIndex) ?: "Unknown" else "Unknown"
                val rawNumber = if (numberIndex != -1) c.getString(numberIndex) ?: "" else ""

                val cleanNumber = rawNumber.replace(" ", "").replace("-", "")
                if (cleanNumber.isNotEmpty() && !seenNumbers.contains(cleanNumber)) {
                    seenNumbers.add(cleanNumber)
                    contactList.add(ContactItem(name, rawNumber))
                }
            }
        }

        if (contactList.isEmpty()) {
            tvStatusMessage.text = getString(R.string.status_no_contacts)
            tvStatusMessage.visibility = View.VISIBLE
            rvContacts.visibility = View.GONE
        } else {
            tvStatusMessage.visibility = View.GONE
            rvContacts.visibility = View.VISIBLE
            contactAdapter.updateContacts(contactList)
        }
    }
}
